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

import com.cargosyabonos.application.port.in.EmailAbogadoUseCase;
import com.cargosyabonos.domain.EmailAbogadoEntity;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/email-abogado")
@RestController
public class EmailAbogadoController {
	
	Logger log = LoggerFactory.getLogger(EmailAbogadoController.class);
	
	@Autowired
	private EmailAbogadoUseCase emAbUC;
	
	@GetMapping("{idAbogado}")
	public List<EmailAbogadoEntity> obtenerEmailsAbogado(@PathVariable("idAbogado") int idAbogado) { 
		return emAbUC.obtenerEmailsDeAbogado(idAbogado);
	}
	
	@PostMapping()
	public void crearEmailAbogado(@RequestBody EmailAbogadoEntity ab,@RequestParam("idUsuario") int idUsuario) { 
		emAbUC.crearEmailAbogado(ab);
	}
	
	@PutMapping()
	public void actualizarEmailAbogado(@RequestBody EmailAbogadoEntity ab) { 
		emAbUC.actualizarEmailAbogado(ab);
	}
	
	@DeleteMapping("{idEmailAbogado}")
	public void eliminarEmailAbogado(@PathVariable("idEmailAbogado") int idEmailAbogado) { 
		emAbUC.eliminarEmailAbogado(idEmailAbogado);
	}

}
