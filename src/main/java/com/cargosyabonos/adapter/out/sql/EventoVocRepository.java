package com.cargosyabonos.adapter.out.sql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.EventoSolicitudVocPort;
import com.cargosyabonos.application.port.out.jpa.EventoSolicitudVocJpa;
import com.cargosyabonos.domain.EventoSolicitudVocEntity;
import com.cargosyabonos.domain.SolicitudVocEntity;

@Service
public class EventoVocRepository implements EventoSolicitudVocPort {

	@Autowired
	EventoSolicitudVocJpa eSolJpa;

	@Override
	public List<EventoSolicitudVocEntity> obtenerEventosSolicitudes(int idSolicitud) {
		return eSolJpa.obtenerEventosDesolicitud(idSolicitud);
	}
	
	@Override
	public List<EventoSolicitudVocEntity> obtenerEventosSolicitudesSinPagos(int idSolicitud) {
		return eSolJpa.obtenerEventosDesolicitudSinPagos(idSolicitud);
	}

	@Override
	public EventoSolicitudVocEntity crearEventoSolicitud(EventoSolicitudVocEntity es) {
		return eSolJpa.save(es);
	}

	@Override
	public void eliminarEventoSolicitud(int idEvento) {
		eSolJpa.deleteById(idEvento);
	}

	@Override
	public boolean tieneEventoLaSolicitud(int idSolicitud,String evento){
		boolean tieneEvento = false;
		int tieneEventoInt = eSolJpa.tieneEventoLaSolicitud(idSolicitud, evento);
		if(tieneEventoInt != 0){
			tieneEvento = true;
		}
		return tieneEvento;
	}
	
	@Override
	public boolean tieneTipoEventoLaSolicitud(int idSolicitud,String evento){
		boolean tieneEvento = false;
		int tieneEventoInt = eSolJpa.tieneTipoEventoLaSolicitud(idSolicitud, evento);
		if(tieneEventoInt != 0){
			tieneEvento = true;
		}
		return tieneEvento;
	}
	
	@Override
	public void ingresarEventoDeSolicitud(String evento,String descripcion,String tipo,String usuario,SolicitudVocEntity solicitud){
		EventoSolicitudVocEntity e = new EventoSolicitudVocEntity();
		e.setEvento(evento);
		e.setDescripcion(descripcion);
		e.setFecha(new Date());
		e.setTipo(tipo);
		e.setUsuario(usuario);
		e.setSolicitud(solicitud);
		crearEventoSolicitud(e);
	}

	@Override
	public List<Integer> solicitudesConTipoImportant(String tipoEvento) {
		return eSolJpa.solicitudesConTipoImportant(tipoEvento);
	}

	@Override
	public void actualizarEventoSolicitud(EventoSolicitudVocEntity es) {
		eSolJpa.save(es);
	}

	@Override
	public int obtenerSolicitudesSiTieneAlgunEventoDeSolicitud(String tipoEvento, int idSolicitud) {
		return eSolJpa.solicitudesSiTieneAlgunEventoDeSolicitud(tipoEvento, idSolicitud);
	}

	@Override
	public List<EventoSolicitudVocEntity> obtenerEventosSchedules(String fecha) {
		return eSolJpa.obtenerEventosSchedules(fecha);
	}


}
