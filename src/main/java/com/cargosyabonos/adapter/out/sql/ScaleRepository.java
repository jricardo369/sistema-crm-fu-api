package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.ScalePort;
import com.cargosyabonos.application.port.out.jpa.ScaleJpa;
import com.cargosyabonos.domain.ScaleEntity;

@Service
public class ScaleRepository implements ScalePort {

	@Autowired
	ScaleJpa scaleJpa;

	@Override
	public List<ScaleEntity> obtenerScalesSolicitudes(int idSolicitud) {
		return scaleJpa.obtenerScalesDesolicitud(idSolicitud);
	}

	@Override
	public void crearScale(ScaleEntity es) {
		scaleJpa.save(es);
	}

	@Override
	public void actualizarScale(ScaleEntity es) {
		scaleJpa.save(es);
	}

	@Override
	public void eliminarScale(ScaleEntity es) {
		scaleJpa.delete(es);
	}

	@Override
	public ScaleEntity obtenerScale(int idScale) {
		return scaleJpa.obtenerScale(idScale);
	}

	

}
