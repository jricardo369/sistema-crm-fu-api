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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.application.port.in.ScaleUseCase;
import com.cargosyabonos.domain.Scale;
import com.cargosyabonos.domain.ScaleEntity;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/scales")
@RestController
public class ScalesController {
	
	Logger log = LoggerFactory.getLogger(ScalesController.class);
	
	@Autowired
	private ScaleUseCase sUC;
	
	@GetMapping("{idSolicitud}")
	public List<ScaleEntity> obtenerScaleSolicitudes(@PathVariable("idSolicitud") int idSolicitud) { 
		return sUC.obtenerScalesSolicitudes(idSolicitud);
	}
	
	@PostMapping()
	public void crearScale(@RequestBody Scale es,@RequestParam("idUsuario") int idUsuario) { 
		sUC.crearScale(es,idUsuario);
	}
	
	@PutMapping()
	public void actualizarScale(@RequestBody ScaleEntity es) { 
		sUC.actualizarScale(es);
	}
	
	@DeleteMapping("{idScale}")
	public void eliminarScale(@PathVariable("idScale") int idScale) { 
		sUC.eliminarScale(idScale);
	}

}
