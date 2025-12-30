package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.TipoSolicitudEntity;

public interface TipoSolicitudPort {
	
	public List<TipoSolicitudEntity> obtenerTiposSolicitudes(); 
	public TipoSolicitudEntity obtenerTipoSolicitudPorId(int idTipoSolicitud); 
	public void crearTipoSolicitud(TipoSolicitudEntity es);
	public void actualizarTipoSolicitud(TipoSolicitudEntity es);
	public void eliminarTipoSolicitud(TipoSolicitudEntity es);

}