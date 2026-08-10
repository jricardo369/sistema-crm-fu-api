package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.PagoVocPort;
import com.cargosyabonos.application.port.out.jpa.PagoVocJpa;
import com.cargosyabonos.domain.PagoVocEntity;

@Service
public class PagoVocRepository implements PagoVocPort {

	@Autowired
	PagoVocJpa pagoVocJpa;

	@Override
	public List<PagoVocEntity> obtenerPagos() {
		return pagoVocJpa.findAll();
	}

	@Override
	public PagoVocEntity obtenerPagoPorId(int idPago) {
		return pagoVocJpa.obtenerPagoPorId(idPago);
	}

	@Override
	public List<PagoVocEntity> obtenerPagosPorIdCita(int idCita) {
		return pagoVocJpa.obtenerPagosPorIdCita(idCita);
	}

	@Override
	public void crearPago(PagoVocEntity pago) {
		pagoVocJpa.save(pago);
	}

	@Override
	public void actualizarPago(PagoVocEntity pago) {
		pagoVocJpa.save(pago);
	}

	@Override
	public void eliminarPago(PagoVocEntity pago) {
		pagoVocJpa.delete(pago);
	}

}
