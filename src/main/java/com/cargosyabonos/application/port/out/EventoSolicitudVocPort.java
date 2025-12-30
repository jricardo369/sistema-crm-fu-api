package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.EventoSolicitudVocEntity;
import com.cargosyabonos.domain.SolicitudVocEntity;

public interface EventoSolicitudVocPort {
	
	public List<EventoSolicitudVocEntity> obtenerEventosSolicitudes(int idSolicitud);
	public List<EventoSolicitudVocEntity> obtenerEventosSolicitudesSinPagos(int idSolicitud);
	public EventoSolicitudVocEntity crearEventoSolicitud(EventoSolicitudVocEntity es);
	public void eliminarEventoSolicitud(int idEvento);
	public void actualizarEventoSolicitud(EventoSolicitudVocEntity es);
	public boolean tieneEventoLaSolicitud(int idSolicitud,String evento);
	public boolean tieneTipoEventoLaSolicitud(int idSolicitud,String evento);
	public List<Integer> solicitudesConTipoImportant(String tipoEvento);
	public int obtenerSolicitudesSiTieneAlgunEventoDeSolicitud(String tipoEvento,int idSolicitud);
	public void ingresarEventoDeSolicitud(String envento,String descripcion,String tipo,String usuario,SolicitudVocEntity solicitud);
	public List<EventoSolicitudVocEntity> obtenerEventosSchedules(String fecha);

}