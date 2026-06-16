package com.cargosyabonos.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.application.port.in.EstatusProspectoAbogadoUseCase;
import com.cargosyabonos.domain.EstatusProspectoAbogadoEntity;

import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/estatus-prospecto-abogado")
@RestController
public class EstatusProspectoAbogadoController {

    @Autowired
	private EstatusProspectoAbogadoUseCase estProsAboService;

    @GetMapping()
	public List<EstatusProspectoAbogadoEntity> obtenerEstadosUsuario() { 
		return estProsAboService.obtenerEstatusProspectosAbogado();
	}

}
