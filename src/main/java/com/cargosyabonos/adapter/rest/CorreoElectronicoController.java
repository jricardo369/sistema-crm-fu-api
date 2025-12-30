package com.cargosyabonos.adapter.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.ApiException;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;


@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/correo-electronico")
@RestController
public class CorreoElectronicoController {
	
	Logger log = LoggerFactory.getLogger(CorreoElectronicoController.class);
	
	@Autowired
	private CorreoElectronicoUseCase envioCorreoUseCase;
	
	@GetMapping("rec-pass/{usuario}")
	public void envioCorreoElectronico(@PathVariable("usuario") String usuario) throws ApiException { 
		envioCorreoUseCase.enviarCorreoRecuperacion(usuario);
		
	}
	
	@GetMapping("abono/{idCliente}")
	public void envioCorreoAbono(@PathVariable("idCliente") int idCliente) throws ApiException { 
		envioCorreoUseCase.enviarCorreoMovimiento(idCliente);
		
	}
	
	@GetMapping("saldo-vencido/{usuario}")
	public void envioCorreoSaldoVencido(@PathVariable("usuario") String usuario) throws ApiException { 
		envioCorreoUseCase.enviarSaldoVencido(usuario);
		
	}
	
	

}
