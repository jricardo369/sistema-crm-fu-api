package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.TipoPagoEntity;

public interface TipoPagoPort {
	
	public List<TipoPagoEntity> obtenerTiposPagos(); 
	public TipoPagoEntity obtenerTiposPago(int idTipoPago); 
	public void crearTipoPago(TipoPagoEntity es);
	public void actualizarTipoPago(TipoPagoEntity es);
	public void eliminarTipoPago(TipoPagoEntity es);

}