package com.cargosyabonos.application.port.out;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.cargosyabonos.domain.Adeudo;
import com.cargosyabonos.domain.AdeudoSolicitudes;
import com.cargosyabonos.domain.MovimientoEntity;
import com.cargosyabonos.domain.ReporteMovimientosSolicitudes;

public interface MovimientosPort {
	
	public List<MovimientoEntity> obtenerMovimientos(int idSolicitud); 
	public List<MovimientoEntity> obtenerMovimientosDeClientePorFecha(int idCliente,Date fechai,Date fechaf);
	public List<Adeudo> obtenerAdeudos(String fechai,String fechaf,int idUsuario,String campo,String valor,String tipo,String rol);
	public List<ReporteMovimientosSolicitudes> obtenerMovimientosSolsDeClientePorFecha(String cliente,Date fechai,Date fechaf,int estatusPago);
	public List<ReporteMovimientosSolicitudes> obtenerMovimientosSolsPorFecha(Date fechai,Date fechaf,int estatusPago);
	public List<MovimientoEntity> obtenerMovimientosPorFecha(Date fechai,Date fechaf);
	public MovimientoEntity obtenerMovimiento(int idMovimiento);
	public void crearMovimiento(MovimientoEntity mov);
	public void actualizarMovimiento(MovimientoEntity mov);
	public void eliminarMovimiento(MovimientoEntity mov);
	public BigDecimal obtenerAdeudo(int idCliente);
	public BigDecimal obtenerSumaTipoMovimiento(int tipo);
	public int obtenerUltimoFolio(int idSolicitud);
	public BigDecimal obtenerSumaMovimientosSolicitud(int idSolicitud);
	public List<AdeudoSolicitudes> obtenerDeudasSolicitudes(Date fechai,Date fechaf,int tipoPayment);
	public AdeudoSolicitudes obtenerDeudasSolicitud(int idSolicitud);
	public BigDecimal obtenerSumaMovimientosPorIdSolicitud(int idSolicitud);
	public int tieneProbono(int idSolicitud);

}
