package model;

public class InvalidTransactionException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7754906094616599994L;

	public InvalidTransactionException(String message) {
		super(message);
	}
}
