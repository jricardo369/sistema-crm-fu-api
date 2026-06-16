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

import com.cargosyabonos.application.port.in.EventoSolicitudVocUseCase;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.EventoSolicitudVocEntity;

@RequestMapping("/eventos-solicitud-voc")
@RestController
public class EventosSolicitudVocController {
	
	Logger log = LoggerFactory.getLogger(EventosSolicitudVocController.class);
	
	@Autowired
	private EventoSolicitudVocUseCase oCase;
	
	@GetMapping("/{idSolicitud}")
	public List<EventoSolicitudVocEntity> obtenerEventosSolicitudes(@PathVariable("idSolicitud") int idSolicitud,@RequestParam("idUsuario") int idUsuario) { 
		return oCase.obtenerEventosSolicitudes(idSolicitud,idUsuario);
	}
	
	@PostMapping()
	public void crearEventoSolicitud(@RequestBody EventoSolicitud o) { 
		oCase.crearEventoSolicitud(o);
	}

	@PutMapping("ajusteSesionesVOC/{idSolicitud}")
	public void ajusteSesionesVOC(@PathVariable("idSolicitud") int idSolicitud,@RequestParam("numSesiones") int numSesiones,
	@RequestParam("motivo") String motivo,@RequestParam("idUsuarioEnvio") int idUsuarioEnvio,@RequestParam("tipo") String tipo) { 
		oCase.ajusteSesionesVOC(idSolicitud, numSesiones, motivo, idUsuarioEnvio,tipo);
	}

	@PutMapping("actualizar-tipo-evento/{idEvento}")
	public void actualizarTipoEvento(@PathVariable("idEvento") int idEvento,@RequestParam("tipoEvento") String tipoEvento) { 
		oCase.actualizarTipoEvento(idEvento, tipoEvento);
	}
	
	@DeleteMapping("{idSolicitud}")
	public void eliminarEventoSolicitud(@PathVariable("idSolicitud") int idSolicitud) { 
		oCase.eliminarEventoSolicitud(idSolicitud);
	}

	@GetMapping("obtener-historial-nsesiones/{idSolicitud}")
	public List<EventoSolicitudVocEntity> obtenerHistorialNumSesionesDeSolicitud(@PathVariable("idSolicitud") int idSolicitud) { 
		return oCase.obtenerHistorialNumSesionesDeSolicitud(idSolicitud);
	}

}
