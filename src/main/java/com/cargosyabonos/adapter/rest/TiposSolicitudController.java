package com.cargosyabonos.adapter.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.application.port.in.TipoSolicitudUseCase;
import com.cargosyabonos.domain.TipoSolicitudEntity;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/tipos-solicitud")
@RestController
public class TiposSolicitudController {
	
	Logger log = LoggerFactory.getLogger(TiposSolicitudController.class);
	
	@Autowired
	private TipoSolicitudUseCase tpCase;
	
	@GetMapping()
	public List<TipoSolicitudEntity> obtenerTiposSolicitud() { 
		return tpCase.obtenerTiposSolicitudes();
	}
	
	@PostMapping()
	public void crearTipoSolicitud(@RequestBody TipoSolicitudEntity ts) { 
		 tpCase.crearTipoSolicitud(ts);
	}
	
	@PutMapping()
	public void actualizarTipoSolicitud(@RequestBody TipoSolicitudEntity ts) { 
		tpCase.actualizarTipoSolicitud(ts);
	}
	
	@DeleteMapping()
	public void eliminarTipoSolicitud(@RequestBody TipoSolicitudEntity ts) { 
		tpCase.eliminarTipoSolicitud(ts);
	}

}
