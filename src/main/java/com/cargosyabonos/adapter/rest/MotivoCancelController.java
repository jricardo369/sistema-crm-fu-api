package com.cargosyabonos.adapter.rest;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.application.port.in.MotivoCancelUseCase;
import com.cargosyabonos.domain.MotivoCancelEntity;

@RequestMapping("/motivos-cancel")
@RestController
public class MotivoCancelController {

	Logger log = LoggerFactory.getLogger(MotivoCancelController.class);

	@Autowired
	MotivoCancelUseCase mcUseCase;

	@GetMapping("/{tipo}")
	public List<MotivoCancelEntity> obtenerMotivosPorTipo(@PathVariable("tipo") String tipo,@RequestParam("idUsuario") int idUsuario) {
		return mcUseCase.obtenerMotivosPorTipo(tipo,idUsuario);
	}
	
	
 
}
