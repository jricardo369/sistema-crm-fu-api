package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.UsuarioEntity;

public interface UsuariosPort {
	
	public List<UsuarioEntity> obtenerUsuarios(); 
	public List<UsuarioEntity> obtenerUsuariosDeRol(int idRol,int revisor); 
	public UsuarioEntity buscarPorUsuario(String usuario);
	public UsuarioEntity buscarPorNombre(String nombre);
	public UsuarioEntity encontrarPorNombre(String nombre);
	public List<Integer> encontrarUsuariosPorNombre(String nombre);
	public List<UsuarioEntity> obtenerPorUsuarioPorSociedad(int idSociedad);
	public UsuarioEntity buscarSoloPorUsuario(String usuario);
	public UsuarioEntity buscarPorId(int idUsuario);
	public UsuarioEntity crearUsuario(UsuarioEntity usuario);
	public void actualizarUsuario(UsuarioEntity usuario);
	public void eliminarUsuario(UsuarioEntity usuario);
	public UsuarioEntity buscarPorCorreo(String correo);
	public UsuarioEntity obtenerUsuarioPorRol(int idRol);
	public List<UsuarioEntity> obtenerUsuariosDeRoles(List<Integer> roles); 
	public List<UsuarioEntity> obtenerUsuariosDeRolesConDesc(List<Integer> roles); 
	public void actualizarEstatusUsuario(int estatus,int idUsuario);
	public void actualizarImageUsuario(String image,int idUsuario);
	public List<UsuarioEntity> obtenerUsuariosParaAssignedClinician();
	public void desbloquearUsuario(int idUsuario);

}
