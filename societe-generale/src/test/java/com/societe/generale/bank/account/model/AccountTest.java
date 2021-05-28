package com.societe.generale.bank.account.model;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import model.Account;
import model.Client;
import model.EnumTransactionType;
import model.InvalidAmountException;
import model.InvalidTransactionException;
import model.Transaction;

public class AccountTest {

	private Client client = new Client();

	@Test
	public void shouldHaveCorrectClientWhenInstanciated() throws InvalidAmountException {
		Account account  = new Account(client);
		
		Assert.assertEquals(client, account.client());
	}
	
	
	@Test
	public void shouldFailWhenDeposeNullAmount() throws InvalidAmountException {
		Account account  = new Account(client);
		
		assertThatThrownBy(() -> {
			new Transaction(account, null, EnumTransactionType.DEPOSIT);
		})
			.isInstanceOf(InvalidTransactionException.class)
			.hasMessage("Invalid operation");
		Assert.assertEquals(BigDecimal.ZERO, account.balance());
		Assert.assertTrue(account.transactions().isEmpty());
	}
	
	@Test
	public void shouldFailWhenTryingToDepositNegativeAmount() throws InvalidAmountException {
		Account account  = new Account(client);
		
		assertThatThrownBy(() -> {
			new Transaction(account, new BigDecimal(-1), EnumTransactionType.DEPOSIT);
		})
			.isInstanceOf(InvalidTransactionException.class)
			.hasMessage("Invalid operation");

		Assert.assertEquals(BigDecimal.ZERO, account.balance());
		Assert.assertTrue(account.transactions().isEmpty());
		
	}
	
	@Test
	public void shouldUpdateBalanceAndCreateOperationWhenDepositValidAmount() throws  InvalidTransactionException, InvalidAmountException {
		Account account  = new Account(client);
		
		account.deposit(new BigDecimal(500));
		
		Assert.assertEquals(new BigDecimal(500), account.balance());
		Assert.assertEquals(1, account.transactions().size());		
	}
	
	
	@Test
	public void shouldFailWhenTryingToWithdrawNullAmount() throws  InvalidTransactionException, InvalidAmountException {
		Account account  = new Account(client);
		account.deposit(new BigDecimal(500));
		
		assertThatThrownBy(() -> {
			account.withdraw(null);
		})
			.isInstanceOf(InvalidTransactionException.class)
			.hasMessage("Invalid operation");
		Assert.assertEquals(new BigDecimal(500f), account.balance());
		Assert.assertEquals(1, account.transactions().size());
		
	}
	
	@Test
	public void shouldFailWhenTryingToWithdrawInvalidAmount() throws  InvalidTransactionException, InvalidAmountException {
		Account account  = new Account(client);
		account.deposit(new BigDecimal(500));
		
		assertThatThrownBy(() -> {
			account.withdraw(new BigDecimal(-1));
		})
			.isInstanceOf(InvalidTransactionException.class)
			.hasMessage("Invalid operation");
		Assert.assertEquals(new BigDecimal(500), account.balance());
		Assert.assertEquals(1, account.transactions().size());
		
	}
	
	@Test
	public void ShouldFailToWithdrawWhenInsufficientFunds() throws InvalidAmountException, InvalidTransactionException {
		Account account  = new Account(client);
		account.deposit(new BigDecimal(500));
		
		assertThatThrownBy(() -> {
			account.withdraw(new BigDecimal(1200));
		})
			.isInstanceOf(InvalidAmountException.class)
			.hasMessage("withdraw 1200 but balance is 500");
		Assert.assertEquals(new BigDecimal(500), account.balance());
		Assert.assertEquals(1, account.transactions().size());
	}
	
	@Test
	public void shouldUpdateBalanceAndCreateOperaionWithdrawingValidAmount() throws  InvalidTransactionException, InvalidAmountException {
		Account account  = new Account(client);
		account.deposit(new BigDecimal(500));
		account.withdraw(new BigDecimal(200));
		Assert.assertEquals(new BigDecimal(300), account.balance());
		Assert.assertEquals(2, account.transactions().size());
	}
	
	@Test
	public void shouldPrintHistoryWhenDepositAndWithdraw() throws InvalidTransactionException, InvalidAmountException {
		Account account  = new Account(client);
		account.deposit(new BigDecimal(7500));
		account.withdraw(new BigDecimal(2000));
		
		String dateString = new Date().toString();
		String expectedString = new StringBuilder(dateString)
				.append(" | DEPOSIT | 7500 | 7500\n")
				.append(dateString)
				.append(" | WITHDRAWAL | 2000 | 5500")
				.toString();
		
		Assert.assertEquals(expectedString, account.getOperationHistoryAsString());
	}
	
	@Test
	public void shouldUpddateBalanceAndCreateOperaionWithdrawingValidAmount() throws InvalidAmountException, InvalidTransactionException  {
		//Given
		Account account  = new Account(client);
		//When
		account.deposit(new BigDecimal(1000));
		account.withdraw(new BigDecimal(200));
		//then
		String dateString = new Date().toString();
		String expectedString = new StringBuilder(dateString)
				.append(" | DEPOSIT | 1000 | 1000\n")
				.append(dateString)
				.append(" | WITHDRAWAL | 200 | 800")
				.toString();
		
		Assert.assertEquals(expectedString, account.getOperationHistoryAsString());
	}
	
	
	
	

}
