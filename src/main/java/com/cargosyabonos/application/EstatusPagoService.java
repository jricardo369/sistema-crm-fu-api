package com.cargosyabonos.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.in.EstatusPagoUseCase;
import com.cargosyabonos.application.port.out.EstatusPagoPort;
import com.cargosyabonos.domain.EstatusPagoEntity;

@Service
public class EstatusPagoService implements EstatusPagoUseCase {

	Logger log = LoggerFactory.getLogger(EstatusPagoService.class);

	@Autowired
	private EstatusPagoPort estPagoPort;

	@Override
	public List<EstatusPagoEntity> obtenerEstatusPagos() {
		return estPagoPort.obtenerEstatusPagos();
	}

	@Override
	public void crearEstatusPago(EstatusPagoEntity es) {
		estPagoPort.crearEstatusPago(es);
	}

	@Override
	public void actualizarEstatusPago(EstatusPagoEntity es) {
		estPagoPort.actualizarEstatusPago(es);
	}

	@Override
	public void eliminarEstatusPago(EstatusPagoEntity es) {
		
	}

	

}
