package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.TipoSolicitudPort;
import com.cargosyabonos.application.port.out.jpa.TipoSolicitudJpa;
import com.cargosyabonos.domain.TipoSolicitudEntity;

@Service
public class TipoSolicitudRepository implements TipoSolicitudPort {

	@Autowired
	TipoSolicitudJpa tipoSolJpa;

	@Override
	public List<TipoSolicitudEntity> obtenerTiposSolicitudes() {
		return tipoSolJpa.findAll();
	}

	@Override
	public void crearTipoSolicitud(TipoSolicitudEntity es) {
		tipoSolJpa.save(es);
	}

	@Override
	public void actualizarTipoSolicitud(TipoSolicitudEntity es) {
		tipoSolJpa.save(es);
	}

	@Override
	public void eliminarTipoSolicitud(TipoSolicitudEntity es) {
		tipoSolJpa.delete(es);
	}

	@Override
	public TipoSolicitudEntity obtenerTipoSolicitudPorId(int idTipoSolicitud) {
		return tipoSolJpa.obtenerTipoSolicitudPorId(idTipoSolicitud);
	}

	

}
