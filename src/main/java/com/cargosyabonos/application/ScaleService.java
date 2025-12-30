package com.cargosyabonos.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.in.ScaleUseCase;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.ScalePort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.Scale;
import com.cargosyabonos.domain.ScaleEntity;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
public class ScaleService implements ScaleUseCase {

	Logger log = LoggerFactory.getLogger(ScaleService.class);

	@Autowired
	private ScalePort scPort;

	@Autowired
	private SolicitudPort solPort;
	
	@Autowired
	private EventoSolicitudPort evPort;
	
	@Autowired
	private UsuariosPort usPort;
	
	@Override
	public List<ScaleEntity> obtenerScalesSolicitudes(int idSolicitud) {
		return scPort.obtenerScalesSolicitudes(idSolicitud);
	}

	@Override
	public void crearScale(Scale es,int idUsuario) {
	
		ScaleEntity se = new ScaleEntity();
		se.setScale(es.getScale());
		SolicitudEntity s = solPort.obtenerSolicitud(es.getIdSolicitud());
		se.setSolicitud(s);
		
		scPort.crearScale(se);
		
		UsuarioEntity u = usPort.buscarPorId(idUsuario);
		evPort.ingresarEventoDeSolicitud("Info", "Scale added to request: "+es.getScale(), "Info", u.getUsuario(), s);
	}

	@Override
	public void actualizarScale(ScaleEntity es) {
		scPort.actualizarScale(es);
	}

	@Override
	public void eliminarScale(int idScale) {
		ScaleEntity es = scPort.obtenerScale(idScale);
		scPort.eliminarScale(es);
	}

	
	
}
