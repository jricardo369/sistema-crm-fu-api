package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.MotivoCancelEntity;

public interface MotivoCancelUseCase {
	
	public List<MotivoCancelEntity> obtenerMotivosPorTipo(String tipo,int idUsuario);

}
