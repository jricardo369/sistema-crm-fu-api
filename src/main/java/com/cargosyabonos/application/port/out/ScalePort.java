package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.ScaleEntity;

public interface ScalePort {
	
	public List<ScaleEntity> obtenerScalesSolicitudes(int idSolicitud); 
	public ScaleEntity obtenerScale(int idScale); 
	public void crearScale(ScaleEntity es);
	public void actualizarScale(ScaleEntity es);
	public void eliminarScale(ScaleEntity es);

}