package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.PermisosUsuariosPort;
import com.cargosyabonos.application.port.out.jpa.PermisoJpa;
import com.cargosyabonos.domain.PermisoUsuarioEntity;

@Service
public class PermisoUsuarioRepository implements PermisosUsuariosPort {

	@Autowired
	PermisoJpa jpa;

	@Override
	public List<PermisoUsuarioEntity> obtenerPermisosUsuarios() {
		return jpa.findAll();
	}

	@Override
	public void insertarPermisoUsuario(int idPermiso, int idUsuario) {
		jpa.insertarPermiso(idPermiso, idUsuario);
		
	}
	
	@Override
	public List<PermisoUsuarioEntity> obtenerPermisosDeUsuario(int idUsuario){
		return jpa.obtenerPermisosDeUsuario(idUsuario);
	}

	@Override
	public void eliminarPermisosDeUsuario(int idUsuario){
		jpa.eliminarPermisosDeUsuario(idUsuario);
	}

}
