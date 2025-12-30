package com.cargosyabonos.application;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.in.EstatusSolicitudUseCase;
import com.cargosyabonos.application.port.out.EstatusSolicitudPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.EstatusSolicitudEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
public class EstatusSolicitudService implements EstatusSolicitudUseCase {
	
	@Autowired
	private UsuariosPort usPort;

	Logger log = LoggerFactory.getLogger(EstatusSolicitudService.class);

	@Autowired
	private EstatusSolicitudPort estPort;

	@Override
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitudes() {
		return estPort.obtenerEstatusSolicitudes();
	}

	@Override
	public void crearEstatusSolicitud(EstatusSolicitudEntity es) {
		estPort.actualizarEstatusSolicitud(es);
	}

	@Override
	public void actualizarEstatusSolicitud(EstatusSolicitudEntity es) {
		estPort.actualizarEstatusSolicitud(es);
	}

	@Override
	public void eliminarEstatusSolicitud(int es) {
	}

	@Override
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitudFiltros(int idUsuario) {

		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		String rol = us.getRol();
		List<EstatusSolicitudEntity> s = new ArrayList<>();
		if (rol.equals("2") || rol.equals("3") || rol.equals("4") || rol.equals("5") || rol.equals("7") || rol.equals("8") || rol.equals("9")) {

			s = estPort.obtenerEstatusSolicitudes();

		}
		if (rol.equals("6") || rol.equals("10")) {

			s = estPort.obtenerEstatusSolicitudesVoc();

		}

		return s;
	}
	

}
