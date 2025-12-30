package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.PermisoUsuarioEntity;

public interface PermisosUsuariosUseCase {
	
	public List<PermisoUsuarioEntity> obtenerPermisosUsuarios(); 
	public List<PermisoUsuarioEntity> obtenerPermisosDeUsuario(int idUsuario); 
	public void insertarPermisoUsuario(int idPermiso,int idUsuario);
	public void eliminarPermisosDeUsuario(int idUsuario);

}
