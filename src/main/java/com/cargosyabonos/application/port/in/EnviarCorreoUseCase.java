package com.cargosyabonos.application.port.in;

import java.util.Map;

public interface EnviarCorreoUseCase {
	
	public void enviarCorreoRecuperacion(String email, Map<String, Object> params);
	public void enviarCorreoMovimiento(String email, Map<String, Object> params);
	public void enviarCorreoSaldoVencido(String email, Map<String, Object> params);
	public void enviarCorreoNuevaCita(String email, Map<String, Object> params);
	public void enviarCorreoCalendario(String email, Map<String, Object> params);
	public void enviarResetPassword(String email, Map<String, Object> params);
	public void testMail();
	

}
