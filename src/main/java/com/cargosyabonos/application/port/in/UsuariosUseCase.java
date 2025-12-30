package com.cargosyabonos.application.port.in;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.domain.UsuarioEntity;
import com.cargosyabonos.domain.UsuarioObj;

public interface UsuariosUseCase {
	
	public List<UsuarioEntity> obtenerUsuarios(); 
	public List<UsuarioEntity> obtenerUsuariosDeRol(int idRol,int revisor); 
	public UsuarioEntity buscarPorUsuario(String usuario);
	public UsuarioEntity buscarPorId(int idUsuario);
	public UsuarioObj buscarPorIdObj(int idUsuario);
	public void  crearUsuario(UsuarioEntity usuario);
	public void actualizarUsuario(UsuarioEntity usuario);
	public void eliminarUsuario(int idUsuario);
	public UsuarioEntity buscarPorCorreo(String correo);
	public List<UsuarioEntity> obtenerUsuariosParaSchedules(int idUsuario);
	public List<UsuarioEntity> obtenerUsuariosParaDash(int idUsuario);
	public void cargarImagen(MultipartFile archivo,int idUsuario);
	public List<UsuarioEntity> obtenerUsuariosParaAssignedClinician();
	public void desbloquearUsuario(int idUsuario);
	
}
