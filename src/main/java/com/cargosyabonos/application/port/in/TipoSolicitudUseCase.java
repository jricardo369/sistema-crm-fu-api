package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.TipoSolicitudEntity;

public interface TipoSolicitudUseCase {
	
	public List<TipoSolicitudEntity> obtenerTiposSolicitudes(); 
	public void crearTipoSolicitud(TipoSolicitudEntity es);
	public void actualizarTipoSolicitud(TipoSolicitudEntity es);
	public void eliminarTipoSolicitud(TipoSolicitudEntity es);

}