package com.csmtech.sjta.exception;

public class ExistingRecordCheckException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExistingRecordCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExistingRecordCheckException(String message) {
		super(message);
	}
}