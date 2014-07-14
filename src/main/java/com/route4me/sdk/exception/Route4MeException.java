package com.route4me.sdk.exception;

/**
 *
 * @author juan
 */

public abstract class Route4MeException extends Exception {

	public Route4MeException(String message) {
		super(message, null);
	}

	public Route4MeException(String message, Throwable e) {
		super(message, e);
	}

	private static final long serialVersionUID = 1L;

}


