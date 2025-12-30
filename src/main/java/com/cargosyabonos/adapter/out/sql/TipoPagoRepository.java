package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.TipoPagoPort;
import com.cargosyabonos.application.port.out.jpa.TipoPagoJpa;
import com.cargosyabonos.domain.TipoPagoEntity;

@Service
public class TipoPagoRepository implements TipoPagoPort {

	@Autowired
	TipoPagoJpa tipoPagoJpa;

	@Override
	public List<TipoPagoEntity> obtenerTiposPagos() {
		return tipoPagoJpa.findAll();
	}

	@Override
	public void crearTipoPago(TipoPagoEntity es) {
		tipoPagoJpa.save(es);
	}

	@Override
	public void actualizarTipoPago(TipoPagoEntity es) {
		tipoPagoJpa.save(es);
	}

	@Override
	public void eliminarTipoPago(TipoPagoEntity es) {
		tipoPagoJpa.delete(es);
	}

	@Override
	public TipoPagoEntity obtenerTiposPago(int idTipoPago) {
		return tipoPagoJpa.obtenerTipoPagoPorId(idTipoPago);
	}
	

}
