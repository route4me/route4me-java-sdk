/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.route4me.sdk.exception;

/**
 *
 * @author khepri
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


