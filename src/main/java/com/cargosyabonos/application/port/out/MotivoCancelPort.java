package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.MotivoCancelEntity;

public interface MotivoCancelPort {
	
	public List<MotivoCancelEntity> obtenerMotivosPorTipo(String tipo,String rol);

}
