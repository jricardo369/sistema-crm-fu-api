package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.AdjuntoPort;
import com.cargosyabonos.application.port.out.jpa.AdjuntoJpa;
import com.cargosyabonos.domain.ArchivoEntity;

@Service
public class AdjuntoRepository implements AdjuntoPort{

	@Autowired
	AdjuntoJpa imagenJpa;

	@Override
	public List<ArchivoEntity> buscarPorIdSolicitud(int idSolicitud) {
		return imagenJpa.encontrarAdjuntosdeRequest(idSolicitud);
	}

	@Override
	public void crearAdjunto(ArchivoEntity a) {
		imagenJpa.save(a);
		
	}

	@Override
	public void actualizarAdjunto(ArchivoEntity a) {
		imagenJpa.save(a);
		
	}

	@Override
	public void eliminarAdjunto(ArchivoEntity a) {
		imagenJpa.delete(a);
		
	}

	@Override
	public List<ArchivoEntity> obtenerAdjuntos() {
		return null;
	}

	@Override
	public ArchivoEntity buscarPorId(int idImagen) {
		return imagenJpa.findByIdImagen(idImagen);
	}

	

}
