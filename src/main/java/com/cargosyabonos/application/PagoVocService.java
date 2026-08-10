package com.cargosyabonos.application;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.PagoVocUseCase;
import com.cargosyabonos.application.port.out.PagoVocPort;
import com.cargosyabonos.domain.PagoVoc;
import com.cargosyabonos.domain.PagoVocEntity;

@Service
public class PagoVocService implements PagoVocUseCase {

	Logger log = LoggerFactory.getLogger(PagoVocService.class);

	@Autowired
	private PagoVocPort pagoVocPort;

	@Override
	public List<PagoVoc> obtenerPagos() {
		List<PagoVocEntity> entities = pagoVocPort.obtenerPagos();
		List<PagoVoc> result = new ArrayList<>();
		for (PagoVocEntity e : entities) {
			result.add(convertirAPagoVoc(e));
		}
		return result;
	}

	@Override
	public PagoVoc obtenerPagoPorId(int idPago) {
		PagoVocEntity e = pagoVocPort.obtenerPagoPorId(idPago);
		if (e == null) {
			return null;
		}
		return convertirAPagoVoc(e);
	}

	@Override
	public List<PagoVoc> obtenerPagosPorIdCita(int idCita) {
		List<PagoVocEntity> entities = pagoVocPort.obtenerPagosPorIdCita(idCita);
		List<PagoVoc> result = new ArrayList<>();
		for (PagoVocEntity e : entities) {
			result.add(convertirAPagoVoc(e));
		}
		return result;
	}

	@Override
	public void crearPago(PagoVoc pago) {
		pago.setFecha(UtilidadesAdapter.obtenerFechaActualPST());
		PagoVocEntity entity = convertirAEntity(pago);
		pagoVocPort.crearPago(entity);
	}

	@Override
	public void actualizarPago(PagoVoc pago) {
		PagoVocEntity entity = convertirAEntity(pago);
		pagoVocPort.actualizarPago(entity);
	}

	@Override
	public void eliminarPago(int idPago) {
		PagoVocEntity entity = pagoVocPort.obtenerPagoPorId(idPago);
		if (entity != null) {
			pagoVocPort.eliminarPago(entity);
		}
	}

	private PagoVoc convertirAPagoVoc(PagoVocEntity entity) {
		PagoVoc dto = new PagoVoc();
		dto.setIdPago(entity.getIdPago());
		dto.setFecha(entity.getFecha());
		dto.setMonto(entity.getMonto());
		dto.setDescripcion(entity.getDescripcion());
		dto.setFolio(entity.getFolio());
		dto.setTipoPago(entity.getTipoPago());
		dto.setIdCita(entity.getIdCita());
		dto.setDescuento(entity.getDescuento());
		return dto;
	}

	private PagoVocEntity convertirAEntity(PagoVoc dto) {
		PagoVocEntity entity = new PagoVocEntity();
		entity.setIdPago(dto.getIdPago());
		entity.setFecha(dto.getFecha());
		entity.setMonto(dto.getMonto());
		entity.setDescripcion(dto.getDescripcion());
		entity.setFolio(dto.getFolio());
		entity.setTipoPago(dto.getTipoPago());
		entity.setIdCita(dto.getIdCita());
		entity.setDescuento(dto.getDescuento());
		entity.setIdUsuario(dto.getIdUsuario());
		return entity;
	}

}
