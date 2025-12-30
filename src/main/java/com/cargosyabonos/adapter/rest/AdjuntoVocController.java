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
import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.application.port.in.AdjuntoVocUseCase;
import com.cargosyabonos.domain.ArchivoVocEntity;

@RequestMapping("/adjuntos-voc")
@RestController
public class AdjuntoVocController {

	Logger log = LoggerFactory.getLogger(AdjuntoVocController.class);

	@Autowired
	AdjuntoVocUseCase adjUseCase;

	@GetMapping
	public List<ArchivoVocEntity> obtenerAdjuntos() {
		return adjUseCase.obtenerAdjuntos();
	}
	
	@GetMapping("{idSolicitud}")
	public List<ArchivoVocEntity> obtenerAdjuntosDeFecha(@PathVariable("idSolicitud") int idSolicitud) {
		return adjUseCase.buscarPorIdSolicitud(idSolicitud);
	}

	@PostMapping("{idSolicitud}/adjunto")
	public void crearAdjunto(@PathVariable("idSolicitud") int idSolicitud,@RequestParam(value = "archivo", required = false) MultipartFile archivo,@RequestParam("idUsuario") int idUsuario) {
		adjUseCase.crearAdjunto(idSolicitud, archivo,idUsuario);
	}
	
	@PutMapping
	public void actualizarAdjunto(@RequestBody ArchivoVocEntity Adjunto) {
		adjUseCase.actualizarAdjunto(Adjunto);
	}
	
	@DeleteMapping(("{idImagen}"))
	public void eliminarAdjunto(@PathVariable("idImagen") int idImagen,@RequestParam("idUsuario") int idUsuario) {
		adjUseCase.eliminarAdjunto(idImagen,idUsuario);
	}

}
