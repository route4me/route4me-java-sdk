package com.route4me.sdk.exception;

public class ParamException extends Route4MeException {

	private static final long serialVersionUID = 1L;
        private Object param;

	public ParamException(String message) {
		super(message);
	}

	public ParamException(String message, Object param, Throwable e) {
		super(message, e);
                this.param = param;
	}

    /**
     * @return the param
     */
    public Object getParam() {
        return param;
    }

    /**
     * @param param the param to set
     */
    public void setParam(Object param) {
        this.param = param;
    }

}
