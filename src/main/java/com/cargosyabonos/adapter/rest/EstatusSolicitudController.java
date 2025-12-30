package com.cargosyabonos.adapter.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.application.port.in.EstatusSolicitudUseCase;
import com.cargosyabonos.domain.EstatusSolicitudEntity;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/estatus-solicitud")
@RestController
public class EstatusSolicitudController {
	
	Logger log = LoggerFactory.getLogger(EstatusSolicitudController.class);
	
	@Autowired
	private EstatusSolicitudUseCase eCase;
	
	@GetMapping()
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitud() { 
		return eCase.obtenerEstatusSolicitudes();
	}
	
	@GetMapping("/{idUsuario}")
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitudFiltros(@PathVariable("idUsuario") int idUsuario) { 
		return eCase.obtenerEstatusSolicitudFiltros(idUsuario);
	}
	
	@PostMapping()
	public void crearTipoSolicitud(@RequestBody EstatusSolicitudEntity o) { 
		eCase.crearEstatusSolicitud(o);
	}
	
	@PutMapping()
	public void actualizarTipoSolicitud(@RequestBody EstatusSolicitudEntity o) { 
		eCase.actualizarEstatusSolicitud(o);
	}
	
	@DeleteMapping("{idTipoSolicitud}")
	public void eliminarTipoSolicitud(@PathVariable("idTipoSolicitud") int idTipoSolicitud) { 
		eCase.eliminarEstatusSolicitud(idTipoSolicitud);
	}

}
