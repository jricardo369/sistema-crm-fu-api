package com.cargosyabonos.application.port.out;

import java.util.Map;

public interface EnviarCorreoPort {
	
	public void enviarCorreoRecuperacion(String email, Map<String, Object> params);
	public void enviarCorreoMovimiento(String email, Map<String, Object> params);
	public void enviarCorreoSaldoVencido(String email, Map<String, Object> params);
	public void enviarCorreoRetrasoSolicitudes(String email, Map<String, Object> params,int idSolicitudes);
	public void enviarCorreoNuevaCita(String email, Map<String, Object> params);
	public void enviarCorreoCalendario(String email, Map<String, Object> params);
	public void enviarResetPassword(String email, Map<String, Object> params);
	public void enviarCorreoNotificacion(String email,String titulo,Map<String, Object> params,byte[] archivo,String extension,boolean ingles);
	public void enviarCorreoInvoice(String email,Map<String, Object> params,int idSolicitud);
	public void enviarCorreoDeLayout(String email,String titulo,Map<String, Object> params,String layout);
	public void enviarCorreoDeLayoutCalendar(String email,String titulo,Map<String, Object> params,String layout,String formatoFecha);
	public void enviarCorreoDeLayoutCalendarWithInvite(String email,String titulo,Map<String, Object> params,String layout);
	public void enviarCorreoSincronizacionCitas(String layout,String fecha,int idUsuario,String formatoFecha);
	public void testMail(String mail,String tipo,String fecha,String hora,String tipoH,String estado,String zonaHoraria);

}
