package com.cargosyabonos.application.port.in;

import java.util.Map;

import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.SolicitudEntity;

public interface CorreoElectronicoUseCase {

	public void enviarCorreoRecuperacion(String usuario);
	public void enviarCorreoMovimiento(int idCliente);
	public void enviarSaldoVencido(String usuario);
	public void enviarCorreoNuevaSolicitudCliente(int idUsuario,SolicitudEntity solicitud,boolean isMensaje,boolean isMail);
	public void enviarCorreoNotifNuevaSolAAbogado(int idUsuario,SolicitudEntity solicitud,boolean isMensaje,boolean isMail);
	public void enviarCorreoCita(String nombre,String fecha,String hora,String tipo,String email,int idSolicitud,String formatoFecha);
	public void enviarCorreoCitaVoc(String nombre,String fecha,String hora,String tipo,String email,int idSolicitud);
	public void enviarCorreoSaldoVencido(SolicitudEntity solicitud,boolean isMensaje,boolean isMail);
	public void enviarCorreoAAbogadoPorFinSolicitud(SolicitudEntity solicitud);
	public void enviarCorreoAMasterPorFinSolicitud(SolicitudEntity solicitud);
	public void enviarCorreoNoShow(String emailRevisor,String usuario,SolicitudEntity solicitud,EventoSolicitudEntity e);
	public void enviarCorreoSuicidio(SolicitudEntity solicitud,String email,String nombreUsuario);
	public void enviarCorreoRecordatorio(String usuario,String email,String fecha,String hora,EventoSolicitudEntity evento,SolicitudEntity solicitud);
	public void enviarCorreoFileVOCEndingSessions(String usuario,String email,Map<String, Object> params);
	public void enviarCorreoPrimeraCita(String nombre,String fecha,String hora,String email,SolicitudEntity solicitud);
	public void enviarCorreoInvoice(String email,SolicitudEntity solicitud);
	public void enviarCorreoAsignacion(SolicitudEntity solicitud,String nombre,String email);
	public void enviarCorreoNotificacionVoc(String nombreUsuario,String titulo,String cuerpo,String email);
	
}
