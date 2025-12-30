package com.cargosyabonos.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.in.TipoPagoUseCase;
import com.cargosyabonos.application.port.out.TipoPagoPort;
import com.cargosyabonos.domain.TipoPagoEntity;

@Service
public class TipoPagoService implements TipoPagoUseCase {

	Logger log = LoggerFactory.getLogger(TipoPagoService.class);

	@Autowired
	private TipoPagoPort tpPort;

	@Override
	public List<TipoPagoEntity> obtenerTiposPagos() {
		return tpPort.obtenerTiposPagos();
	}

	@Override
	public void crearTipoPago(TipoPagoEntity es) {
		tpPort.crearTipoPago(es);
	}

	@Override
	public void actualizarTipoPago(TipoPagoEntity es) {
		tpPort.actualizarTipoPago(es);
	}

	@Override
	public void eliminarTipoPago(TipoPagoEntity es) {
		
	}

	
}
