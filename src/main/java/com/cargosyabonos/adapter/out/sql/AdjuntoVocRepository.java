package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.AdjuntoVocPort;
import com.cargosyabonos.application.port.out.jpa.AdjuntoVocJpa;
import com.cargosyabonos.domain.ArchivoVocEntity;

@Service
public class AdjuntoVocRepository implements AdjuntoVocPort{

	@Autowired
	AdjuntoVocJpa imagenJpa;

	@Override
	public List<ArchivoVocEntity> buscarPorIdSolicitud(int idSolicitud) {
		return imagenJpa.encontrarAdjuntosdeRequest(idSolicitud);
	}

	@Override
	public void crearAdjunto(ArchivoVocEntity a) {
		imagenJpa.save(a);
	}

	@Override
	public void actualizarAdjunto(ArchivoVocEntity a) {
		imagenJpa.save(a);
	}

	@Override
	public void eliminarAdjunto(ArchivoVocEntity a) {
		imagenJpa.delete(a);
	}

	@Override
	public List<ArchivoVocEntity> obtenerAdjuntos() {
		return null;
	}

	@Override
	public ArchivoVocEntity buscarPorId(int idImagen) {
		return imagenJpa.findByIdImagen(idImagen);
	}

	

}
