package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.PagoVoc;

public interface PagoVocUseCase {

	public List<PagoVoc> obtenerPagos();

	public PagoVoc obtenerPagoPorId(int idPago);

	public List<PagoVoc> obtenerPagosPorIdCita(int idCita);

	public void crearPago(PagoVoc pago);

	public void actualizarPago(PagoVoc pago);

	public void eliminarPago(int idPago);

}
