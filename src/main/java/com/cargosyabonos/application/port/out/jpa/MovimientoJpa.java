package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.AdeudoSolicitudes;
import com.cargosyabonos.domain.MovimientoEntity;
import com.cargosyabonos.domain.ReporteMovimientosSolicitudes;

@Repository
public interface MovimientoJpa extends CrudRepository<MovimientoEntity, Serializable> {

	public List<MovimientoEntity> findAll();

	@Query(value = "SELECT * FROM movimiento WHERE id_solicitud = ?1 AND fecha BETWEEN ?2 AND ?3", nativeQuery = true)
	public List<MovimientoEntity> obtenerMovimientoDeClientePorFecha(int idCliente, Date fechai, Date fechaf);

	@Query(value = "SELECT * FROM movimiento WHERE fecha BETWEEN ?1 AND ?2", nativeQuery = true)
	public List<MovimientoEntity> obtenerMovimientoPorFecha(Date fechai, Date fechaf);
	
	@Query(value = "SELECT * FROM movimiento WHERE id_movimiento = ?1", nativeQuery = true)
	public MovimientoEntity obtenerMovimientoPorId(int idMovimiento);

	@Query(value = "SELECT m.id_movimiento,m.folio,s.cliente,tp.nombre as tipo_pago,tp.id_tipo_pago,m.tipo,m.fecha, m.monto,m.descuento,m.descripcion,s.id_solicitud "
			+ "FROM movimiento m " + "JOIN solicitud s ON s.id_solicitud = m.id_solicitud "
			+ "JOIN tipo_pago tp ON tp.id_tipo_pago = m.id_tipo_pago "
			+ "WHERE fecha BETWEEN ?1 AND ?2 ORDER BY fecha ASC", nativeQuery = true)
	public List<ReporteMovimientosSolicitudes> obteneReporterMovimientoPorFecha(Date fechai, Date fechaf,
			int estatusPago);

	@Query(value = "SELECT m.id_movimiento,m.folio,s.cliente,tp.nombre as tipo_pago,tp.id_tipo_pago,m.tipo,m.fecha, m.monto,m.descuento,m.descripcion,s.id_solicitud "
			+ "FROM movimiento m " + "JOIN solicitud s ON s.id_solicitud = m.id_solicitud "
			+ "JOIN tipo_pago tp ON tp.id_tipo_pago = m.id_tipo_pago "
			+ "WHERE fecha BETWEEN ?2 AND ?3 AND cliente = ?1 ORDER BY fecha ASC ", nativeQuery = true)
	public List<ReporteMovimientosSolicitudes> obteneReporterMovimientoPorFechaYClt(String cliente, Date fechai,
			Date fechaf, int estatusPago);

	@Query(value = "SELECT * FROM movimiento WHERE id_solicitud = ?1", nativeQuery = true)
	public List<MovimientoEntity> obtenerMovimientoDeSolicitud(int idSolicitud);

	@Query(value = "SELECT IFNULL( (SELECT folio FROM request_evaluation.movimiento where id_solicitud = ?1 order by folio desc limit 1) ,0)", nativeQuery = true)
	public int obtenerUltimoFolio(int idSolicitud);

	@Query(value = "SELECT " + " COALESCE(SUM(case when id_tipo_movimiento = 1 then monto else 0 end) - "
			+ "SUM(case when id_tipo_movimiento = 2 then monto else 0 end),0) as adeudo "
			+ "FROM movimiento WHERE id_paciente = ?1", nativeQuery = true)
	public BigDecimal obtenerAdeudo(int idPaciente);

	@Query(value = "SELECT sum(monto) FROM movimiento WHERE id_tipo_movimiento = ?1", nativeQuery = true)
	public BigDecimal obtenerSumaTipoMovimiento(int tipo);

	@Query(value = "SELECT COALESCE(sum(monto) ,0) FROM movimiento WHERE tipo in( 'Payment') AND id_solicitud = ?1", nativeQuery = true)
	public BigDecimal obtenerSumaMovimientosPorIdSolicitud(int idSolicitud);

	@Query(value = "SELECT s.id_solicitud,s.cliente,s.email,s.telefono,s.amount,ts.nombre as tipo, "
			+"(amount-(SELECT IFNULL( (SELECT sum(monto) FROM request_evaluation.movimiento where id_solicitud = s.id_solicitud ) ,0))) as deuda "
			+"FROM solicitud s "
			+"LEFT JOIN tipo_solicitud ts ON ts.id_tipo_solicitud = s.id_tipo_solicitud "
			+"LEFT JOIN movimiento m on m.id_solicitud = s.id_solicitud "
			+"WHERE  s.amount > 0  AND s.id_estatus_pago NOT IN(2) "
			+"AND id_estatus_solicitud NOT IN(11) AND amount-(SELECT IFNULL( (SELECT sum(monto) "
			+"FROM request_evaluation.movimiento where id_solicitud = s.id_solicitud ) ,0)) > 0 " 
			+"AND (s.email != '' OR s.telefono != '') "
			+"AND UPPER(LEFT(TRIM(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(s.cliente,'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')), 1))  "
			+"BETWEEN ?1 AND ?2 "
			+"GROUP BY s.id_solicitud ORDER BY cliente asc", nativeQuery = true)
	public List<AdeudoSolicitudes> obtenerDeudasSolicitudes(String letraInicio,String letrafin);

	@Query(value = "SELECT s.cliente,s.id_solicitud,email,amount,"
			+ "(amount-(SELECT IFNULL( (SELECT sum(monto) FROM request_evaluation.movimiento where id_solicitud = s.id_solicitud ) ,0))) as deuda "
			+ "FROM solicitud s " + "LEFT JOIN movimiento m on m.id_solicitud = s.id_solicitud "
			+ "WHERE s.id_solicitud = ?1 GROUP BY s.id_solicitud", nativeQuery = true)
	public AdeudoSolicitudes obtenerDeudasSolicitud(int idSolicitud);
	
	@Query(value = "SELECT COUNT(*) FROM movimiento WHERE id_solicitud = ?1 AND id_tipo_pago IN(4,7)", nativeQuery = true)
	public int tieneProbono(int idSolicitud);

}
