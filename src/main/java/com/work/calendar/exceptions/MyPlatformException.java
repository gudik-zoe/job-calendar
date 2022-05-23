package com.work.calendar.exceptions;


public class MyPlatformException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int errorCode;
	
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public MyPlatformException() {
	}

	public MyPlatformException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public MyPlatformException(String message, Throwable cause, int errorCode) {
		super(message, cause);
		this.errorCode=errorCode;
	}


	public MyPlatformException(String message) {
		super(message);
	}
	
	public MyPlatformException(String message, int errorCode) {
		super(message);
		this.errorCode=errorCode;
	}


	public MyPlatformException(Throwable cause) {
		super(cause);
	}
	
	public MyPlatformException(Throwable cause, int errorCode) {
		super(cause);
		this.errorCode=errorCode;
	}
}
