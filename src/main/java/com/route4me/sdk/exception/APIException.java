package com.route4me.sdk.exception;

public class APIException extends Route4MeException {

	private static final long serialVersionUID = 1L;

    public APIException(String message) {
        super(message);
    }

        
	public APIException(String message, Throwable e) {
		super(message, e);
	}

}
