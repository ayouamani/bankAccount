package model;


import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
	private Account targetAccount;
	private BigDecimal amount;
	private Date date;
	private EnumTransactionType transactionType;
	private BigDecimal newBalance;
	
	public Transaction(Account targetAccount, BigDecimal amount, EnumTransactionType transactionType) throws InvalidTransactionException {
		super();
		if(targetAccount == null || amount == null || transactionType == null || amount.compareTo(BigDecimal.ZERO) <0) {
			throw new InvalidTransactionException("Invalid operation");
		}
		this.targetAccount = targetAccount;
		this.amount = amount;
		this.transactionType = transactionType;
	}

	public Account targetAccount() {
		return targetAccount;
	}
	
	public EnumTransactionType type() {
		return transactionType;
	}
	
	public BigDecimal amount() {
		return amount;
	}
	
	public Date date() {
		return date;
	}
	
	public BigDecimal newBalance() {
		return newBalance;
	}
	
	public void execute() throws InvalidAmountException, InvalidAmountException {
		BigDecimal nextBalance = transactionType.equals(EnumTransactionType.DEPOSIT) 
				? this.targetAccount().balance().add(this.amount)
				: this.targetAccount().balance().subtract(this.amount);
		if(!Boolean.TRUE.equals(nextBalance.compareTo(BigDecimal.ZERO) > 0)) {
			throw new InvalidAmountException(
					String.format(
							"withdraw %s but balance is %s",
							amount.toString(),
							this.targetAccount().balance()
						)
					);
		}
		if(transactionType.equals(EnumTransactionType.DEPOSIT)) {
			this.targetAccount().increaseBalance(this.amount);
		}
		else {
			this.targetAccount().decreaseBalance(this.amount);
		}
		this.newBalance = nextBalance;
		this.date = new Date();
		this.targetAccount().transactions().add(this);
	}
	
	public String toString() {
		if(date == null) {
			return String.format(
					"NOT YET EXECUTED | %s | %s |",
					transactionType.toString(),
					amount.toString()
				);
		}
		return String.format(
				"%s | %s | %s | %s",
				date.toString(),
				transactionType.toString(),
				amount.toString(),
				newBalance.toString()
			);
	}
}
