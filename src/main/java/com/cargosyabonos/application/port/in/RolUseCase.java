package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.RolEntity;

public interface RolUseCase {
	
	public List<RolEntity> obtenerRoles(); 
	public List<RolEntity> obtenerRolesCitasDispo();

	
}