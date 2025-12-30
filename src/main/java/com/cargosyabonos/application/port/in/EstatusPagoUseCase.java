package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.EstatusPagoEntity;

public interface EstatusPagoUseCase {
	
	public List<EstatusPagoEntity> obtenerEstatusPagos(); 
	public void crearEstatusPago(EstatusPagoEntity es);
	public void actualizarEstatusPago(EstatusPagoEntity es);
	public void eliminarEstatusPago(EstatusPagoEntity es);

}