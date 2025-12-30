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

import com.cargosyabonos.application.port.in.TipoPagoUseCase;
import com.cargosyabonos.domain.TipoPagoEntity;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/tipos-pagos")
@RestController
public class TiposPagosController {
	
	Logger log = LoggerFactory.getLogger(TiposPagosController.class);
	
	@Autowired
	private TipoPagoUseCase tpCase;
	
	@GetMapping()
	public List<TipoPagoEntity> obtenerTiposPAgo() { 
		return tpCase.obtenerTiposPagos();
	}
	
	@PostMapping()
	public void crearTipoPago (@RequestBody TipoPagoEntity ts) { 
		 tpCase.crearTipoPago(ts);
	}
	
	@PutMapping()
	public void actualizarTipoPago(@RequestBody TipoPagoEntity ts) { 
		tpCase.actualizarTipoPago(ts);
	}
	
	@DeleteMapping()
	public void eliminarTipoPago(@RequestBody TipoPagoEntity ts) { 
		tpCase.eliminarTipoPago(ts);
	}

}
