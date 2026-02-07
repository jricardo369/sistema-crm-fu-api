package com.cargosyabonos.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.application.port.in.EstadoUsuarioUseCase;
import com.cargosyabonos.domain.EstadoUsuarioEntity;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/estado-usuario")
@RestController
public class EstadoUsuarioController {

    @Autowired
	private EstadoUsuarioUseCase esUsUC;

    @GetMapping("{idUsuario}")
	public List<EstadoUsuarioEntity> obtenerEstadosUsuario(@PathVariable("idUsuario") int idUsuario) { 
		return esUsUC.obtenerEstadosUsuario(idUsuario);
	}

    @PostMapping()
	public void crearEstadoUsuario(@RequestBody EstadoUsuarioEntity es,@RequestParam("idUsuarioEnvio") int idUsuario) { 
		System.out.println("noLicencia: "+es.getNoLicencia());
		esUsUC.crearEstadoUsuario(es);
	}
	
	@PutMapping()
	public void actualizarEstadoUsuario(@RequestBody EstadoUsuarioEntity es) { 
		esUsUC.actualizarEstadoUsuario(es);
	}
	
	@DeleteMapping("{idEstadoUsuario}")
	public void eliminarEstadoUsuario(@PathVariable("idEstadoUsuario") int idEstadoUsuario) { 
		esUsUC.eliminarEstadoUsuario(idEstadoUsuario);
	}

}
