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

import com.cargosyabonos.application.port.in.EstatusPagoUseCase;
import com.cargosyabonos.domain.EstatusPagoEntity;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/estatus-pago")
@RestController
public class EstatusPagoController {
	
	Logger log = LoggerFactory.getLogger(EstatusPagoController.class);
	
	@Autowired
	private EstatusPagoUseCase eCase;
	
	@GetMapping()
	public List<EstatusPagoEntity> obtenerEstatusPago() { 
		return eCase.obtenerEstatusPagos();
	}
	
	@PostMapping()
	public void crearTipoSolicitud(@RequestBody EstatusPagoEntity o) { 
		eCase.crearEstatusPago(o);
	}
	
	@PutMapping()
	public void actualizarTipoSolicitud(@RequestBody EstatusPagoEntity o) { 
		eCase.actualizarEstatusPago(o);
	}
	
	@DeleteMapping("{idEstatusPago}")
	public void eliminarEstatusPago(@PathVariable("idEstatusPago") int idEstatusPago) { 
		eCase.eliminarEstatusPago(null);
	}

}
