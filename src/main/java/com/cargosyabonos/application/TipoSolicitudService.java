package com.cargosyabonos.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.in.TipoSolicitudUseCase;
import com.cargosyabonos.application.port.out.TipoSolicitudPort;
import com.cargosyabonos.domain.TipoSolicitudEntity;

@Service
public class TipoSolicitudService implements TipoSolicitudUseCase {

	Logger log = LoggerFactory.getLogger(TipoSolicitudService.class);

	@Autowired
	private TipoSolicitudPort tsPort;

	@Override
	public List<TipoSolicitudEntity> obtenerTiposSolicitudes() {
		return tsPort.obtenerTiposSolicitudes();
	}

	@Override
	public void crearTipoSolicitud(TipoSolicitudEntity es) {
		tsPort.crearTipoSolicitud(es);
	}

	@Override
	public void actualizarTipoSolicitud(TipoSolicitudEntity es) {
		tsPort.actualizarTipoSolicitud(es);
	}

	@Override
	public void eliminarTipoSolicitud(TipoSolicitudEntity es) {
		
	}


	

}
