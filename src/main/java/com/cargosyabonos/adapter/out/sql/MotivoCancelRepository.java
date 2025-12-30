package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.MotivoCancelPort;
import com.cargosyabonos.application.port.out.jpa.MotivoCancelJpa;
import com.cargosyabonos.domain.MotivoCancelEntity;

@Service
public class MotivoCancelRepository implements MotivoCancelPort{

	@Autowired
	MotivoCancelJpa mcJpa;

	@Override
	public List<MotivoCancelEntity> obtenerMotivosPorTipo(String tipo,String rol) {
		return mcJpa.obtenerMotivosPorTipo(tipo,rol);
	}	

}
