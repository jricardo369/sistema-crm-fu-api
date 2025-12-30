package com.cargosyabonos.application.port.in;

import com.cargosyabonos.domain.CambiosUsuario;

public interface SeguridadUseCase {

	public void cambioPassword(CambiosUsuario cambiosUsuario);
	public void cambioEmail(CambiosUsuario cambiosUsuario);
	public void enviarCorreoResetPassword(String email);
	public String obtenerFormReset(String uuid,int idUsuario);
	public String enviarForRest(String uuid, String password,String passwordepeat,int idUsuario);
	
}
