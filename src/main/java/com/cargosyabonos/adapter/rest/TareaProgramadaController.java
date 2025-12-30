package com.cargosyabonos.adapter.rest;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.TareaProgramadaUseCase;
import com.cargosyabonos.domain.TareaProgramadaEntity;

@RequestMapping("/tareas-programadas")
@RestController
public class TareaProgramadaController {

	Logger log = LoggerFactory.getLogger(TareaProgramadaController.class);

	@Autowired
	TareaProgramadaUseCase tpUseCase;
	
	@GetMapping
	public List<TareaProgramadaEntity> obtenerTareasProgramadas() {
		return tpUseCase.obtenerTareasProgramadas();
	}

	@GetMapping("{codigo}")
	public TareaProgramadaEntity obtenerConfig(@PathVariable("codigo") String codigo) {
		return tpUseCase.obtenerPorCodigo(codigo);
	}
	
	@PutMapping
	public void actTareaProg(@RequestBody  TareaProgramadaEntity tp) {
		tpUseCase.actualizarTareaProgramada(tp);
	}
	
	@GetMapping("ejecutar-tarea/{codigoTarea}")
	public void obtenerTareasProgramadas(@PathVariable("codigoTarea") String codigoTarea,
			@RequestParam(required = false) Date fechai, @RequestParam(required = false) Date fechaf) {
		UtilidadesAdapter.pintarLog("Ejecutando tarea:" + codigoTarea + "|fechai:" + fechai + "|fechaf:" + fechaf);
		tpUseCase.ejecutarTareaProgramada(codigoTarea, fechai, fechaf, 0);
	}

}
