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

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.RolUseCase;
import com.cargosyabonos.application.port.in.UsuariosUseCase;
import com.cargosyabonos.domain.RolEntity;
import com.cargosyabonos.domain.UsuarioEntity;
import com.cargosyabonos.domain.UsuarioObj;

@RequestMapping("/usuarios")
@RestController
public class UsuariosController {

	Logger log = LoggerFactory.getLogger(UsuariosController.class);

	@Autowired
	UsuariosUseCase usrUseCase;
	
	@Autowired
	RolUseCase rolUseCase;

	@GetMapping
	public List<UsuarioEntity> obtenerUsuarios() {
		return usrUseCase.obtenerUsuarios();
	}

	@GetMapping("por-usuario")
	public UsuarioEntity buscarPorUsuario(String usuario) {
		return usrUseCase.buscarPorUsuario(usuario);
	}
	
	@GetMapping("{idUsuario}")
	public UsuarioEntity buscarPorUsuario(@PathVariable("idUsuario") int idUsuario) {
		return usrUseCase.buscarPorId(idUsuario);
	}
	
	@GetMapping("usuario-obj/{idUsuario}")
	public UsuarioObj buscarPorUsuarioObj(@PathVariable("idUsuario") int idUsuario) {
		return usrUseCase.buscarPorIdObj(idUsuario);
	}
	
	@GetMapping("por-rol/{idRol}")
	public List<UsuarioEntity> buscarPorUsuariosPorRol(@PathVariable("idRol") int idRol,@RequestParam(required = false) String revisor) {
		UtilidadesAdapter.pintarLog("Revisor:"+revisor);
		int rev = 0;
		if(revisor != null){
			rev = Integer.valueOf(revisor);
		}
		return usrUseCase.obtenerUsuariosDeRol(idRol,rev);
	}
	
	@GetMapping("assigned-clinicians")
	public List<UsuarioEntity> obtenerUsuariosAssignedClinician() {
		return usrUseCase.obtenerUsuariosParaAssignedClinician();
	}
	
	@GetMapping("para-schedules")
	public List<UsuarioEntity> buscarUsuariosParaSchedules(@RequestParam("idUsuario") int idUsuario) {
		UtilidadesAdapter.pintarLog("idUsuario:"+idUsuario);
		return usrUseCase.obtenerUsuariosParaSchedules(idUsuario);
	}
	
	@GetMapping("para-dash")
	public List<UsuarioEntity> buscarUsuariosParaDash(@RequestParam("idUsuario") int idUsuario) {
		UtilidadesAdapter.pintarLog("idUsuario:"+idUsuario);
		return usrUseCase.obtenerUsuariosParaDash(idUsuario);
	}

	@PostMapping
	public void crearUsuario(@RequestBody UsuarioEntity usuario) {
		usrUseCase.crearUsuario(usuario);
	}

	@PutMapping
	public void actulizarUsuario(@RequestBody UsuarioEntity usuario) {
		usrUseCase.actualizarUsuario(usuario);
	}
	
	@PutMapping("desbloquear/{idUsuario}")
	public void desbloquearUsuario(@PathVariable("idUsuario") int idUsuario) {
		usrUseCase.desbloquearUsuario(idUsuario);
	}

	@DeleteMapping("{idUsuario}")
	public void eliminarUsuario(@PathVariable("idUsuario") int idUsuario) {
		usrUseCase.eliminarUsuario(idUsuario);
	}
	
	@PostMapping("actualizar-imagen/{idUsuario}")
	public void cargarImagen(@PathVariable("idUsuario") int idUsuario,@RequestParam(value = "archivo", required = false) MultipartFile archivo) {
		usrUseCase.cargarImagen(archivo, idUsuario);
	}
	
	@GetMapping("roles")
	public List<RolEntity> obtenerRoles() {
		return rolUseCase.obtenerRoles();
	}

	@GetMapping("roles-citas-dispo")
	public List<RolEntity> obtenerRolesCitasDispo() {
		return rolUseCase.obtenerRolesCitasDispo();
	}

}
