package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.Scale;
import com.cargosyabonos.domain.ScaleEntity;

public interface ScaleUseCase {
	
	public List<ScaleEntity> obtenerScalesSolicitudes(int idSolicitud); 
	public void crearScale(Scale es,int idUsuario);
	public void actualizarScale(ScaleEntity es);
	public void eliminarScale(int idScale);

}