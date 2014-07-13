package com.route4me.sdk.exception;

public class InvalidRequestException extends Route4MeException {

	private static final long serialVersionUID = 1L;

	private final String param;

	public InvalidRequestException(String message, String param, Throwable e) {
		super(message, e);
		this.param = param;
	}

	public String getParam() {
		return param;
	}

}
