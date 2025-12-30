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
import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.application.port.in.DisponibilidadUsuarioUseCase;
import com.cargosyabonos.application.port.out.ArchivosPort;
import com.cargosyabonos.domain.DisponibilidadTodoDeUsuario;
import com.cargosyabonos.domain.DisponibilidadUsuario;
import com.cargosyabonos.domain.DisponibilidadUsuarioEntity;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/disponibilidad-usuarios")
@RestController
public class DisponibilidadUsuariosController {
	
	Logger log = LoggerFactory.getLogger(DisponibilidadUsuariosController.class);
	
	@Autowired
	private DisponibilidadUsuarioUseCase dCase;
	
	@Autowired
	private ArchivosPort aPort;
	
	@GetMapping("{idUsuario}")
	public List<DisponibilidadUsuarioEntity> obtenerDisponibilidadDeUsuario(@PathVariable("idUsuario") int idUsuario) { 
		return dCase.obtenerDisponibilidadUsuario(idUsuario);
	}
	
	@GetMapping("/de-dia/{fecha}")
	public List<DisponibilidadUsuario> obtenerDisponibilidadDeUsuariosPorFecha(@PathVariable("fecha") String fecha,
			@RequestParam("rol") int rol,@RequestParam(required = false) boolean fechaAnterior,@RequestParam(required = false) String idSolicitud,
			@RequestParam(required = false) boolean clinician) { 
		int idSol = idSolicitud == null ? 0 : Integer.valueOf(idSolicitud);
		return dCase.obtenerDisponibilidadUsuarioPorFecha(fecha,rol,fechaAnterior,idSol,clinician);
	}
	
	@GetMapping("/todo-usuario/{idUsuario}")
	public List<DisponibilidadTodoDeUsuario> obtenerDisponibilidadTodoDeUsuario(@PathVariable("idUsuario") int idUsuario) { 
		return dCase.obtenerDisponibilidadTodoDeUsuario(idUsuario);
	}
	
	@PostMapping()
	public void crearDisponibilidad (@RequestBody DisponibilidadUsuario d) { 
		 dCase.crearDisponibilidadUsuario(d);
	}
	
	@PutMapping()
	public void actualizarTipoPago(@RequestBody DisponibilidadUsuarioEntity d) { 
		dCase.actualizarDisponibilidadUsuario(d);
	}
	
	@DeleteMapping("{idDisponibilidad}")
	public void eliminarDisponibilidad(@PathVariable int idDisponibilidad) { 
		dCase.eliminarDisponibilidadUsuario(idDisponibilidad);
	}
	
	@PostMapping("/excel/{idUsuario}")
	public void cargarExcel(@RequestParam(value = "archivo", required = false) MultipartFile archivo,@PathVariable("idUsuario") int idUsuario) {
		dCase.cargarExcel(archivo,idUsuario);
	}
	
	@GetMapping("/excel/obtener-layout")
	public byte[] obtenerExcel() {
		return aPort.obtenerArchivo("LayoutAvailability.xlsx");
	}

}
