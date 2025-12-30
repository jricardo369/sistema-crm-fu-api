package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.EstatusSolicitudEntity;

public interface EstatusSolicitudPort {
	
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitudes(); 
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitudesVoc(); 
	public EstatusSolicitudEntity obtenerEstatusSolicitudPorId(int idEstatusSolicitud);
	public EstatusSolicitudEntity obtenerEstatusSolicitudPorDescripcion(String desc); 
	public void crearEstatusSolicitud(EstatusSolicitudEntity es);
	public void actualizarEstatusSolicitud(EstatusSolicitudEntity es);
	public void eliminarEstatusSolicitud(EstatusSolicitudEntity es);

}