package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.EstatusPagoEntity;

public interface EstatusPagoPort {
	
	public List<EstatusPagoEntity> obtenerEstatusPagos(); 
	public EstatusPagoEntity obtenerEstatusPagoPorId(int idEstatusPago); 
	public void crearEstatusPago(EstatusPagoEntity es);
	public void actualizarEstatusPago(EstatusPagoEntity es);
	public void eliminarEstatusPago(EstatusPagoEntity es);
	public EstatusPagoEntity obtenerEstatusPagoPorDescripcion(String desc);

}