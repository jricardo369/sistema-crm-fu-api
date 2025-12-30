package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.ArchivoEntity;

public interface AdjuntoPort {
	
	public List<ArchivoEntity> obtenerAdjuntos(); 
	public List<ArchivoEntity> buscarPorIdSolicitud(int idSolicitud);
	public ArchivoEntity buscarPorId(int id);
	public void crearAdjunto(ArchivoEntity a);
	public void actualizarAdjunto(ArchivoEntity a);
	public void eliminarAdjunto(ArchivoEntity a);

}
