package com.cargosyabonos.adapter.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.application.port.in.NotaProspectoAbogadoUseCase;
import com.cargosyabonos.domain.NotaProspectoAbogado;
import com.cargosyabonos.domain.NotaProspectoAbogadoEntity;

@RequestMapping("/notas-prospecto-abogado")
@RestController
public class NotaProspectoAbogadoController {

    Logger log = LoggerFactory.getLogger(NotaProspectoAbogadoController.class);
	
	@Autowired
	private NotaProspectoAbogadoUseCase oCase;
	
	@GetMapping("/{idProspectoAbogado}")
	public List<NotaProspectoAbogado> obtenerEventosSolicitudes(@PathVariable("idProspectoAbogado") int idProspectoAbogado,@RequestParam("idUsuario") int idUsuario) { 
		return oCase.obtenerNotasProspectosAbogadosPorIdProspecto(idProspectoAbogado);
	}
	
	@PostMapping()
	public void crearEventoSolicitud(@RequestBody NotaProspectoAbogadoEntity o) { 
		oCase.crearNotaProspecto(o);
	}
	
	@DeleteMapping("{idNota}")
	public void eliminarNotaProspecto(@PathVariable("idNota") int idNota) { 
		oCase.eliminarNotaProspecto(idNota);
	}

}
