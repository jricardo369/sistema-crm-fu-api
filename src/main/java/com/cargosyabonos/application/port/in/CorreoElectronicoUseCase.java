package com.cargosyabonos.application.port.in;

import java.math.BigDecimal;
import java.util.Map;

import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.SolicitudEntity;

public interface CorreoElectronicoUseCase {

	public void enviarCorreoNuevaSolicitudCliente(int idUsuario,SolicitudEntity solicitud,boolean isMensaje,boolean isMail);
	public void enviarCorreoNotifNuevaSolAAbogado(int idUsuario,SolicitudEntity solicitud,boolean isMensaje,boolean isMail);
	public void enviarCorreoCita(String nombre,String fecha,String hora,String tipo,String email,int idSolicitud,String formatoFecha,String entrevistador,String uidSchedule);
	public void enviarCorreoCitaCancelacion(String nombre,String fecha,String hora,String tipo,String email,String estado,int idSolicitud,String formatoFecha,String zonaHoraria,String entrevistador,String uidSchedule, boolean isCliente);
	public void enviarCorreoCitaVoc(String nombre,String fecha,String hora,String tipo,String email,int idSolicitud,String uidSchedule);
	public void enviarCorreoSaldoVencido(SolicitudEntity solicitud,boolean isMensaje,boolean isMail);
	public void enviarCorreoAAbogadoPorFinSolicitud(SolicitudEntity solicitud);
	public void enviarCorreoAMasterPorFinSolicitud(SolicitudEntity solicitud);
	public void enviarCorreoNoShow(String emailRevisor,String emailEntrevisador,String nombreRevisor,String nombreEntrevistador,SolicitudEntity solicitud,EventoSolicitudEntity e,String motivo);
	public void enviarCorreoReject(String emailRevisor,String emailEntrevistador,String nombreRevisor,String nombreEntrevistado,SolicitudEntity solicitud,EventoSolicitudEntity e,String motivo);
	public void enviarCorreoSuicidio(SolicitudEntity solicitud,String email,String nombreUsuario);
	public void enviarCorreoRecordatorio(String usuario,String email,String fecha,String hora,EventoSolicitudEntity evento,SolicitudEntity solicitud);
	public void enviarCorreoFileVOCEndingSessions(String usuario,String email,Map<String, Object> params);
	public void enviarCorreoPrimeraCita(String nombre,String fecha,String hora,String email,SolicitudEntity solicitud);
	public void enviarCorreoInvoice(String email,SolicitudEntity solicitud);
	public void enviarCorreoAsignacion(SolicitudEntity solicitud,String nombre,String email);
	public void enviarCorreoNotificacion(String nombre, String cuerpo,String email, String titulo, byte[] archivo,String extension,boolean ingles);
	public void enviarCorreoNotificacionVoc(String nombreUsuario,String titulo,String cuerpo,String email);
	public void enviarResetPassword(String email,String url);
	public void enviarCorreoRetrasoSolicitudes(String email, int idSolicitud,String nombreUsuario,int dias);
	public void enviarCorreoMovimiento(String email,String usuario,BigDecimal monto,String tipo);
	
}
