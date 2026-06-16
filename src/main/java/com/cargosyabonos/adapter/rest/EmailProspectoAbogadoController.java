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

import com.cargosyabonos.application.port.in.EmailProspectoAbogadoUseCase;
import com.cargosyabonos.domain.EmailProspectoAbogadoEntity;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/email-prospecto-abogado")
@RestController
public class EmailProspectoAbogadoController {
	
	Logger log = LoggerFactory.getLogger(EmailProspectoAbogadoController.class);
	
	@Autowired
	private EmailProspectoAbogadoUseCase emAbUC;
	
	@GetMapping("{idProspectoAbogado}")
	public List<EmailProspectoAbogadoEntity> obtenerEmailsProspectoAbogado(@PathVariable("idProspectoAbogado") int idProspectoAbogado) { 
		return emAbUC.obtenerEmailsDeProspectoAbogado(idProspectoAbogado);
	}
	
	@PostMapping()
	public void crearEmailProspectoAbogado(@RequestBody EmailProspectoAbogadoEntity ab,@RequestParam("idUsuario") int idUsuario) { 
		emAbUC.crearEmailProspectoAbogado(ab);
	}
	
	@PutMapping()
	public void actualizarEmailProspectoAbogado(@RequestBody EmailProspectoAbogadoEntity ab) { 
		emAbUC.actualizarEmailProspectoAbogado(ab);
	}
	
	@DeleteMapping("{idEmailProspectoAbogado}")
	public void eliminarEmailProspectoAbogado(@PathVariable("idEmailProspectoAbogado") int idEmailProspectoAbogado) { 
		emAbUC.eliminarEmailProspectoAbogado(idEmailProspectoAbogado);
	}

}