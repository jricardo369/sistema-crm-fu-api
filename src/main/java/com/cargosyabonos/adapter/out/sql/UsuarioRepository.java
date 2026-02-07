package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.application.port.out.jpa.UsuarioJpa;
import com.cargosyabonos.domain.UsuarioEntity;

@Repository
public class UsuarioRepository implements UsuariosPort {

	@Autowired
	UsuarioJpa usJpa;

	@Override
	public List<UsuarioEntity> obtenerUsuarios() {
		return usJpa.obtenerTodosUsuariosActivos();
	}

	@Override
	public UsuarioEntity buscarPorUsuario(String usuario) {
		return usJpa.findByUsuario(usuario);
	}
	
	@Override
	public UsuarioEntity buscarPorNombre(String nombre){
		return usJpa.findByNombre(nombre);
	}
	
	@Override
	public UsuarioEntity encontrarPorNombre(String nombre){
		return usJpa.encontrarPorNombre(nombre);
	}
	
	@Override
	public UsuarioEntity buscarPorCorreo(String correo) {
		return usJpa.findBycorreoElectronico(correo);
	}

	@Override
	public UsuarioEntity crearUsuario(UsuarioEntity usuario) {
		UsuarioEntity u = usJpa.save(usuario);
		return u;
	}

	@Override
	public void actualizarUsuario(UsuarioEntity usuario) {
		usJpa.save(usuario);
	}

	@Override
	public void eliminarUsuario(UsuarioEntity usuario) {
		usJpa.delete(usuario);
	}

	@Override
	public UsuarioEntity buscarSoloPorUsuario(String usuario) {
		return usJpa.encontrarUsuario(usuario);
	}

	@Override
	public UsuarioEntity buscarPorId(int idUsuario) {
		return usJpa.findByIdUsuario(idUsuario);
	}
	
	@Override
	public List<UsuarioEntity> obtenerPorUsuarioPorSociedad(int idSociedad) {
		return usJpa.obtenerUsuariosPorSociedad(idSociedad);
	}

	@Override
	public UsuarioEntity obtenerUsuarioPorRol(int idRol) {
		return usJpa.obtenerUsuarioPorRol(idRol);
	}

	@Override
	public List<UsuarioEntity> obtenerUsuariosDeRol(int idRol,int revisor) {
		return usJpa.obtenerUsuariosPorRol(idRol,revisor);
	}

	@Override
	public List<Integer> encontrarUsuariosPorNombre(String nombre) {
		return usJpa.encontrarUsuariosPorNombre(nombre);
	}

	@Override
	public List<UsuarioEntity> obtenerUsuariosDeRoles(List<Integer> roles) {
		return usJpa.obtenerUsuarioPorRoles(roles);
	}
	
	@Override
	public List<UsuarioEntity> obtenerUsuariosDeRolesConDesc(List<Integer> roles) {
		return usJpa.obtenerUsuarioPorRolesConDesc(roles);
	}

	@Override
	public void actualizarEstatusUsuario(int estatus, int idUsuario) {
		usJpa.actualizarEstatusUsuario(estatus, idUsuario);
	}
	
	@Override
	public void actualizarImageUsuario(String image,int idUsuario){
		usJpa.actualizarImageUsuario(image, idUsuario);
	}
	
	@Override
	public List<UsuarioEntity> obtenerUsuariosParaAssignedClinician(){
		return usJpa.obtenerUsuariosParaAssignedClinician();
	}

	@Override
	public void desbloquearUsuario(int idUsuario) {
		usJpa.desbloquearUsuario(idUsuario);
	}

}
