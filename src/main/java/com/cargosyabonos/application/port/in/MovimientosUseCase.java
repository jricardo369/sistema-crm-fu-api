package com.cargosyabonos.application.port.in;

import java.util.Date;
import java.util.List;

import com.cargosyabonos.domain.Adeudo;
import com.cargosyabonos.domain.DetalleMovimientos;
import com.cargosyabonos.domain.Movimiento;
import com.cargosyabonos.domain.MovimientosReporte;

public interface MovimientosUseCase {
	
	public List<Movimiento> obtenerMovimientos(int idSolicitud); 
	public MovimientosReporte obtenerMovimientosDeClientePorFecha(String cliente,Date fechai,Date fechaf);
	public MovimientosReporte obtenerAdeudosDeClientePorFecha(String fechai,String fechaf,int idUsuario,String campo,String valor,String tipo);
	public void crearMovimiento(Movimiento mov, boolean envioNotificacion,int idUsuario);
	public void actualizarMovimiento(Movimiento mov);
	public void eliminarMovimiento(int idMov,int idUsuario);
	public Adeudo obtenerAdeudo(int idCliente);
	public DetalleMovimientos obtenerDetalleMovimientos(int idSolicitud);

}
