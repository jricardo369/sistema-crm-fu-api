package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.EstatusSolicitudEntity;

public interface EstatusSolicitudUseCase {
	
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitudes(); 
	public void crearEstatusSolicitud(EstatusSolicitudEntity es);
	public void actualizarEstatusSolicitud(EstatusSolicitudEntity es);
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitudFiltros(int idUsuario);
	public void eliminarEstatusSolicitud(int es);

}