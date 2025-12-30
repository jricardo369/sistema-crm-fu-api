package com.cargosyabonos.application;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.in.EventoSolicitudVocUseCase;
import com.cargosyabonos.application.port.out.EventoSolicitudVocPort;
import com.cargosyabonos.application.port.out.SolicitudVocPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.EventoSolicitudVocEntity;
import com.cargosyabonos.domain.SolicitudVocEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
public class EventoSolicitudVocService implements EventoSolicitudVocUseCase {

	Logger log = LoggerFactory.getLogger(EventoSolicitudVocService.class);

	@Autowired
	private EventoSolicitudVocPort esPort;
	
	@Autowired
	private SolicitudVocPort solPort;
	
	@Autowired
	private UsuariosPort usPort;
	
	@Autowired
	private CorreoElectronicoUseCase correoUs;

	@Override
	public List<EventoSolicitudVocEntity> obtenerEventosSolicitudes(int idSolicitud,int idUsuario) {
		UsuarioEntity u = usPort.buscarPorId(idUsuario);
		if(u.getRol().equals("10")){
			return esPort.obtenerEventosSolicitudesSinPagos(idSolicitud);
		}else{
			return esPort.obtenerEventosSolicitudes(idSolicitud);
		}
	}

	@Override
	public void crearEventoSolicitud(EventoSolicitud es) {
		
		EventoSolicitudVocEntity e = new EventoSolicitudVocEntity();
		if(es.getFecha() == null){
			e.setFecha(new Date());
		}else{
			try {
				e.setFecha(UtilidadesAdapter.cadenaAFecha(es.getFecha()));
			} catch (ParseException e1) {
			}
		}
		e.setDescripcion(es.getDescripcion());
		String evento = es.getEvento();
		String tipoEvento = es.getTipo();
		
		//UtilidadesAdapter.pintarLog("evento:"+evento);
		
		if(evento == null){
			e.setEvento("");
		}else{
			e.setEvento(es.getEvento());
		}
		
		SolicitudVocEntity sol = solPort.obtenerSolicitud(es.getIdSolicitud());
		e.setSolicitud(sol);
		e.setTipo(es.getTipo());
		e.setUsuario(es.getUsuario());	
		
		if(tipoEvento.equals("Important")){
			solPort.actualizarImportante("Important", sol.getIdSolicitud());
		}
		
		EventoSolicitudVocEntity evSalida = esPort.crearEventoSolicitud(e);
		
		if(tipoEvento.equals("Schedule")){
			
			//Obtener fecha y hora
			//String fechaString = UtilidadesAdapter.formatearFecha(es.getFecha());
			String fechaString = UtilidadesAdapter.formatearFechaStringAtipoUS(es.getFecha());
			UtilidadesAdapter.pintarLog("fechaString:"+fechaString+"|horaString:"+es.getHora()+"|"+es.getTipoSchedule());
			e.setDescripcion(e.getDescripcion()+","+" Appointment for the day "+fechaString+" at "+es.getHora() + " " + es.getTipoSchedule());
			
			UsuarioEntity us = usPort.buscarPorNombre(evSalida.getUsuario());
			if(us != null){
				// Enviar notificacion
				correoUs.enviarCorreoCita(us.getNombre(), fechaString, es.getHora(),es.getTipo(),sol.getEmail(),es.getIdSolicitud(),null);
			}
			
		}
		
	}


	@Override
	public void eliminarEventoSolicitud(int idEvento) {
		esPort.eliminarEventoSolicitud(idEvento);
	}


	@Override
	public List<Integer> solicitudesConTipoImportant(String tipoEvento) {
		return esPort.solicitudesConTipoImportant(tipoEvento);
	}
	

}
