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
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.application.port.in.ConfiguracionUseCase;
import com.cargosyabonos.domain.ConfiguracionEntity;

@RequestMapping("/configuraciones")
@RestController
public class ConfiguracionController {

	Logger log = LoggerFactory.getLogger(ConfiguracionController.class);

	@Autowired
	ConfiguracionUseCase confUseCase;
	
	@GetMapping
	public List<ConfiguracionEntity> obtenerConfigs() {
		return confUseCase.obtenerConfiguraciones();
	}

	@GetMapping("{codigo}")
	public ConfiguracionEntity obtenerConfig(@PathVariable("codigo") String codigo) {
		return confUseCase.obtenerConfiguracionPorCodigo(codigo);
	}

	@PostMapping
	public void crearConf(@RequestBody ConfiguracionEntity conf) {
		confUseCase.crearConf(conf);
	}
	
	@PutMapping
	public void actulizarConf(@RequestBody ConfiguracionEntity conf) {
		confUseCase.actualizarConf(conf);
	}
	
	@DeleteMapping
	public void eliminarConf(@RequestBody ConfiguracionEntity conf) {
		confUseCase.eliminarConf(conf);
	}

}
