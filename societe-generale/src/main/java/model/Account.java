package model;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Account {
	private Client client;
	private BigDecimal balance;
	private List<Transaction> transactions; 
	
	
	public Account(Client client) throws InvalidAmountException {
		super();
		this.client = client;
		this.balance = new BigDecimal(0);
		this.transactions = new ArrayList<>();
	}

	public Client client() {
		return client;
	}
	
	public BigDecimal balance() {
		return balance;
	}
	
	public List<Transaction> transactions() {
		return transactions;
	}
	
	public void increaseBalance(BigDecimal amount) throws InvalidAmountException {
		balance = balance.add(amount);
	}
	
	public void decreaseBalance(BigDecimal amount) throws InvalidAmountException {
		balance = balance.subtract(amount);
	}
	
	public void deposit(BigDecimal amount) throws  InvalidTransactionException, InvalidAmountException {
		new Transaction(this, amount, EnumTransactionType.DEPOSIT).execute();
	}
	
	public void withdraw(BigDecimal amount) throws  InvalidTransactionException, InvalidAmountException {
		new Transaction(this, amount, EnumTransactionType.WITHDRAWAL).execute();
	}
	
	public String getOperationHistoryAsString() {
		return transactions().stream().map(Transaction::toString).collect(Collectors.joining("\n"));
	}

	public void printHistory() {
		System.out.println(getOperationHistoryAsString());
	}
}
