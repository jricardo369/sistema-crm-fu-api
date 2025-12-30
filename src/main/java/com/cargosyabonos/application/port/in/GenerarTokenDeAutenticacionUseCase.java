package com.cargosyabonos.application.port.in;

import com.cargosyabonos.ApiException;

public interface GenerarTokenDeAutenticacionUseCase {

	public String autenticarUsuario(String usuario,String passwd) throws ApiException;
	
}
