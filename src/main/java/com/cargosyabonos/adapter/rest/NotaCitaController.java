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

import com.cargosyabonos.application.port.in.NotaCitaUseCase;
import com.cargosyabonos.domain.NotaCitaEntity;

@RequestMapping("/notas-citas")
@RestController
public class NotaCitaController {

	Logger log = LoggerFactory.getLogger(NotaCitaController.class);

	@Autowired
	NotaCitaUseCase ncUseCase;
	
	@GetMapping("/{idCita}")
	public List<NotaCitaEntity> obtenerNotasDeSolicitud(@PathVariable("idCita") int idCita) {
		return ncUseCase.obtenerNotasCitas(idCita);
	}
	
	@PostMapping()
	public void crearNota(@RequestBody NotaCitaEntity c) {
		ncUseCase.crearNotaCita(c);
	}
	
	@PutMapping
	public void actualizarNota(@RequestBody NotaCitaEntity c) {
		ncUseCase.actualizarNotaCita(c);
	}
	
	@DeleteMapping("{idNota}")
	public void eliminarNota(@PathVariable("idNota") int idNota,@RequestParam("idUsuario") int idUsuario) {
		ncUseCase.eliminarNotaCita(idNota,idUsuario);
	}

}
