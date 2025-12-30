package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.RolEntity;

public interface RolPort {
	
	public List<RolEntity> obtenerRoles(); 
	public List<RolEntity> obtenerRolesCitasDispo(List<Integer> roles);
	
}