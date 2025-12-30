package com.cargosyabonos.application;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.in.RolUseCase;
import com.cargosyabonos.application.port.out.RolPort;
import com.cargosyabonos.domain.RolEntity;

@Service
public class RolService implements RolUseCase {

	Logger log = LoggerFactory.getLogger(RolService.class);

	@Autowired
	private RolPort rolPort;

	@Override
	public List<RolEntity> obtenerRoles() {
		return rolPort.obtenerRoles();
	}

	@Override
	public List<RolEntity> obtenerRolesCitasDispo() {

		List<RolEntity> salida = null;

		List<Integer> li = new ArrayList<>();
		li.add(5);
		li.add(8);
		li.add(11);

		salida = rolPort.obtenerRolesCitasDispo(li);

		return salida;
	}
	
	
	
}
