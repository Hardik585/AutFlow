package com.authflow.exception;

public class EmailAlreadyExist extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailAlreadyExist(String msg) {
		super(msg);
	}
}
