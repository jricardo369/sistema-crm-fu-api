package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.RolPort;
import com.cargosyabonos.application.port.out.jpa.RolJpa;
import com.cargosyabonos.domain.RolEntity;

@Service
public class RolRepository implements RolPort {

	@Autowired
	RolJpa rolJpa;

	@Override
	public List<RolEntity> obtenerRoles() {
		return rolJpa.findAll();
	}

	@Override
	public List<RolEntity> obtenerRolesCitasDispo(List<Integer> roles) {
		return rolJpa.obtenerRolesCitasDispo(roles);
	}

}
