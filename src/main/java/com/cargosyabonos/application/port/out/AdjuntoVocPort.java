package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.ArchivoVocEntity;

public interface AdjuntoVocPort {
	
	public List<ArchivoVocEntity> obtenerAdjuntos(); 
	public List<ArchivoVocEntity> buscarPorIdSolicitud(int idSolicitud);
	public ArchivoVocEntity buscarPorId(int id);
	public void crearAdjunto(ArchivoVocEntity a);
	public void actualizarAdjunto(ArchivoVocEntity a);
	public void eliminarAdjunto(ArchivoVocEntity a);

}
