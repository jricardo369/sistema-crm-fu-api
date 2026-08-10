package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.PagoVocEntity;

public interface PagoVocPort {

	public List<PagoVocEntity> obtenerPagos();

	public PagoVocEntity obtenerPagoPorId(int idPago);

	public List<PagoVocEntity> obtenerPagosPorIdCita(int idCita);

	public void crearPago(PagoVocEntity pago);

	public void actualizarPago(PagoVocEntity pago);

	public void eliminarPago(PagoVocEntity pago);

}
