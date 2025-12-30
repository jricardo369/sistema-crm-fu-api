package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.EstatusPagoPort;
import com.cargosyabonos.application.port.out.jpa.EstatusPagoJpa;
import com.cargosyabonos.domain.EstatusPagoEntity;

@Service
public class EstatusPagoRepository implements EstatusPagoPort {

	@Autowired
	EstatusPagoJpa estPagoJpa;

	@Override
	public List<EstatusPagoEntity> obtenerEstatusPagos() {
		return estPagoJpa.findAll();
	}

	@Override
	public void crearEstatusPago(EstatusPagoEntity es) {
		estPagoJpa.save(es);
	}

	@Override
	public void actualizarEstatusPago(EstatusPagoEntity es) {
		estPagoJpa.save(es);
	}

	@Override
	public void eliminarEstatusPago(EstatusPagoEntity es) {
		estPagoJpa.delete(es);
	}

	@Override
	public EstatusPagoEntity obtenerEstatusPagoPorId(int idEstatusPago) {
		return estPagoJpa.obtenerEstatusPagoPorId(idEstatusPago);
	}

	@Override
	public EstatusPagoEntity obtenerEstatusPagoPorDescripcion(String desc) {
		return estPagoJpa.obtenerEstatusPagoPorDescripcion(desc);
	}
	

}
