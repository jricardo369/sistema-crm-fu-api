package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.TipoPagoEntity;

public interface TipoPagoUseCase {
	
	public List<TipoPagoEntity> obtenerTiposPagos(); 
	public void crearTipoPago(TipoPagoEntity es);
	public void actualizarTipoPago(TipoPagoEntity es);
	public void eliminarTipoPago(TipoPagoEntity es);

}