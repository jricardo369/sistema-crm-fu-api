package com.cargosyabonos.application.port.out;

import java.util.Map;

public interface CorreosPort {
	
	public void enviarCorreoMovimiento(String email, Map<String, Object> params);
	public void enviarCorreoSaldoVencido(String email, Map<String, Object> params);
	public void enviarCorreoRetrasoSolicitudes(String email, String subject,Map<String, Object> params);
	public void enviarResetPassword(String email, Map<String, Object> params);
	public void enviarCorreoNotificacionLayout(String email,String titulo,Map<String, Object> params,byte[] archivo,String extension,String layout);
	public void enviarCorreoInvoice(String email,Map<String, Object> params,int idSolicitud);
	public void enviarCorreoDeLayout(String email,String titulo,Map<String, Object> params,String layout);
	public void enviarCorreoDeLayoutCalendarWithInvite(String tipoInvitacion,String email,String subject,Map<String, Object> params,String layout,String uidSchedule);

}
