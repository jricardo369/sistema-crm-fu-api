package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.EstatusSolicitudPort;
import com.cargosyabonos.application.port.out.jpa.EstatusSolicitudJpa;
import com.cargosyabonos.domain.EstatusSolicitudEntity;

@Service
public class EstatusSolicitudRepository implements EstatusSolicitudPort {

	@Autowired
	EstatusSolicitudJpa estSolJpa;

	@Override
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitudes() {
		return estSolJpa.obtenerEstatusSolicitudes();
	}

	@Override
	public void crearEstatusSolicitud(EstatusSolicitudEntity es) {
		estSolJpa.save(es);
	}

	@Override
	public void actualizarEstatusSolicitud(EstatusSolicitudEntity es) {
		estSolJpa.save(es);
	}

	@Override
	public void eliminarEstatusSolicitud(EstatusSolicitudEntity es) {
		estSolJpa.delete(es);
	}

	@Override
	public EstatusSolicitudEntity obtenerEstatusSolicitudPorId(int idEstatusSolicitud) {
		return estSolJpa.obtenerEstatusSolicitudPorId(idEstatusSolicitud);
	}

	@Override
	public EstatusSolicitudEntity obtenerEstatusSolicitudPorDescripcion(String desc) {
		return estSolJpa.obtenerEstatusSolicitudPorDescripcion(desc);
	}

	@Override
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitudesVoc() {
		return estSolJpa.obtenerEstatusSolicitudesVoc();
	}

	

}
