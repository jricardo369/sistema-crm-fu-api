package com.cargosyabonos.adapter.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.application.port.in.EventoSolicitudUseCase;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.SchedulersActivasDeSolicitud;

@RequestMapping("/eventos-solicitud")
@RestController
public class EventosSolicitudController {
	
	Logger log = LoggerFactory.getLogger(EventosSolicitudController.class);
	
	@Autowired
	private EventoSolicitudUseCase oCase;
	
	@GetMapping("/{idSolicitud}")
	public List<EventoSolicitudEntity> obtenerEventosSolicitudes(@PathVariable("idSolicitud") int idSolicitud,@RequestParam("idUsuario") int idUsuario) { 
		return oCase.obtenerEventosSolicitudes(idSolicitud,idUsuario);
	}
	
	@PostMapping()
	public void crearEventoSolicitud(@RequestBody EventoSolicitud o,@RequestParam(required = false) int idUsuarioSchedule) { 
		oCase.crearEventoSolicitud(o,idUsuarioSchedule);
	}
	
	@DeleteMapping("{idSolicitud}")
	public void eliminarEventoSolicitud(@PathVariable("idSolicitud") int idSolicitud) { 
		oCase.eliminarEventoSolicitud(idSolicitud);
	}
	
	@GetMapping("schedules-activas/{idSolicitud}")
	public List<SchedulersActivasDeSolicitud> obtenerSchedulesActivasDeSolicitud(@PathVariable("idSolicitud") int idSolicitud,@RequestParam(required = false) int idUsuario) {
		
		return oCase.obtenerSchedulesActivasDeSolicitud(idSolicitud,idUsuario);
	}
	
	
	@PutMapping("cancelar-schedule/{idEvento}")
	public void cancelarSchedule(@PathVariable("idEvento") int idEvento,@RequestParam("idUsuario") int idUsuario,@RequestParam("motivo") String motivo) {
		 oCase.actualizarEstatusSchedule(idEvento, "0",idUsuario,motivo);
	}

}
