package com.societe.generale.bank.account.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import model.Account;
import model.Client;
import model.EnumTransactionType;
import model.InvalidAmountException;
import model.InvalidTransactionException;
import model.Transaction;

public class TransactionTest {

	private Client client = new Client();
	

	@Test
	public void shouldFailWhenTryingToInstanciateWithNullType() throws InvalidAmountException {
		Account account = new Account(client);
		
		assertThatThrownBy(() -> {
			new Transaction(account, new BigDecimal(500), null);
		})
			.isInstanceOf(InvalidTransactionException.class)
			.hasMessage("Invalid operation");
	}
	
	@Test
	public void shouldFailWhenTryingToInstanciateWithNullAccount() throws InvalidAmountException {
		
		assertThatThrownBy(() -> {
			new Transaction(null, new BigDecimal(500), EnumTransactionType.DEPOSIT);
		})
			.isInstanceOf(InvalidTransactionException.class)
			.hasMessage("Invalid operation");
	}
	
	@Test
	public void shouldFailWhenTryingToInstanciateWithNullAmount() throws InvalidAmountException {
		Account account = new Account(client);
		
		assertThatThrownBy(() -> {
			new Transaction(account, null, EnumTransactionType.DEPOSIT);
		})
			.isInstanceOf(InvalidTransactionException.class)
			.hasMessage("Invalid operation");
	}
	
	@Test
	public void shouldFailWhenTryingToInstanciateWithNegativeAmount() throws InvalidAmountException {
		Account account = new Account(client);
		
		assertThatThrownBy(() -> {
			new Transaction(account,  new BigDecimal(-1), EnumTransactionType.DEPOSIT);
		})
			.isInstanceOf(InvalidTransactionException.class)
			.hasMessage("Invalid operation");
	}
	
	@Test
	public void shouldHaveCorrectAttributesWhenInstanciatedWhitValidArgs() throws InvalidTransactionException, InvalidAmountException {
		Account account = new Account(client);
		
		Transaction op = new Transaction(account,  new BigDecimal(20), EnumTransactionType.DEPOSIT);
		
		Assert.assertEquals(account, op.targetAccount());
		Assert.assertEquals(new BigDecimal(20), op.amount());
		Assert.assertEquals(EnumTransactionType.DEPOSIT, op.type());
	}
	
		
		@Test
		public void shouldUpdateAccountAndCompleteOwnAttributesWhenDepositIsExecuted() throws InvalidTransactionException, InvalidAmountException {
			Account account = Mockito.mock(Account.class);
			ArrayList<Transaction> history = new ArrayList<>();
			Mockito.when(account.balance())
				.thenReturn( new BigDecimal(500));
			Mockito.when(account.transactions())
				.thenReturn(history);

			Transaction op = new Transaction(account,  new BigDecimal(20), EnumTransactionType.DEPOSIT);
			op.execute();
			
			Assert.assertEquals(new BigDecimal(520), op.newBalance());
			Assert.assertEquals(history.get(0), op);
			Assert.assertNotNull(op.date());
			Mockito.verify(account).increaseBalance(new BigDecimal(20));
			
		}
		
		@Test
		public void shouldUpdateAccountAndCompleteOwnAttributesWhenWithdrawalIsExecuted() throws InvalidTransactionException, InvalidAmountException {
			Account account = Mockito.mock(Account.class);
			ArrayList<Transaction> history = new ArrayList<Transaction>();
			Mockito.when(account.balance())
				.thenReturn( new BigDecimal(500));
			Mockito.when(account.transactions())
				.thenReturn(history);

			Transaction op = new Transaction(account,  new BigDecimal(350), EnumTransactionType.WITHDRAWAL);
			op.execute();
			
			Assert.assertEquals(new BigDecimal(150), op.newBalance());
			
			Assert.assertEquals(history.get(0), op);
			Assert.assertNotNull(op.date());
			Mockito.verify(account).decreaseBalance(new BigDecimal(350));
		}
		
		@Test
		public void shouldFailWhenWithdrawalExecutionWithInsufficientFunds() throws InvalidTransactionException, InvalidAmountException {
			Account account = Mockito.mock(Account.class);
			ArrayList<Transaction> history = new ArrayList<>();
			Mockito.when(account.balance())
				.thenReturn( new BigDecimal(500));
			Mockito.when(account.transactions())
				.thenReturn(history);
			Transaction op = new Transaction(account,  new BigDecimal(550), EnumTransactionType.WITHDRAWAL);
			
			assertThatThrownBy(() -> {
				op.execute();
			})
				.isInstanceOf(InvalidAmountException.class)
				.hasMessage("withdraw 550 but balance is 500");
			Mockito.verify(account, never()).decreaseBalance(Mockito.any());
			Assert.assertTrue(history.isEmpty());
		}
		
		@Test
		public void shouldMatchExpectationsWhenUnexecutedOpConvertedToString() throws InvalidTransactionException, InvalidAmountException {
			Account account = Mockito.mock(Account.class);
			
			Transaction op = new Transaction(account,  new BigDecimal(550), EnumTransactionType.WITHDRAWAL);
			
			Assert.assertEquals("NOT YET EXECUTED | WITHDRAWAL | 550 |", op.toString());
		}
		
		@Test
		public void shouldMatchExpectationsWhenExecutedOpConvertedToString() throws InvalidTransactionException, InvalidAmountException {
			Account account = Mockito.mock(Account.class);
			ArrayList<Transaction> history = new ArrayList<>();
			Mockito.when(account.balance())
				.thenReturn(new BigDecimal(500));
			Mockito.when(account.transactions())
				.thenReturn(history);
			
			Transaction op = new Transaction(account,  new BigDecimal(550), EnumTransactionType.DEPOSIT);
			op.execute();
			
			Assert.assertEquals(
					String.format("%s | DEPOSIT | 550 | 1050", new Date().toString()),
					op.toString());
		}
			

}
