package com.cargosyabonos;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ApiException(int status, Object entity) {
		super(""+entity);
	}
	
	
	

	

}
