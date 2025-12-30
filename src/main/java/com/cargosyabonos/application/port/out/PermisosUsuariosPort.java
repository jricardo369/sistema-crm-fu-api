package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.PermisoUsuarioEntity;

public interface PermisosUsuariosPort {
	
	public List<PermisoUsuarioEntity> obtenerPermisosUsuarios(); 
	public List<PermisoUsuarioEntity> obtenerPermisosDeUsuario(int idUsuario); 
	public void insertarPermisoUsuario(int idPermiso,int idUsuario);
	public void eliminarPermisosDeUsuario(int idUsuario);

}
