package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargosyabonos.domain.NumeroCasosSolicitudes;
import com.cargosyabonos.domain.ReporteSolsUsuario;
import com.cargosyabonos.domain.SolicitudVocEntity;
import com.cargosyabonos.domain.TelefonosSolicitudes;
import com.cargosyabonos.domain.UltimoUsuarioRevisor;

@Repository
public interface SolicitudVocJpa extends CrudRepository<SolicitudVocEntity, Serializable> {

	public List<SolicitudVocEntity> findAll();
	
	@Query(value = "SELECT * FROM solicitud_voc WHERE id_estatus_solicitud NOT IN(11) ORDER BY fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerTodas();
	
	@Query(value = "SELECT * FROM solicitud_voc WHERE id_estatus_solicitud = ?1 ORDER BY fecha_inicio ASC ", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerTodasCerradas(int estatus);
	
	@Query(value = "SELECT * FROM solicitud_voc WHERE numero_de_caso = ?1", nativeQuery = true)
	public SolicitudVocEntity obtenerSolicitudByNumeroDeCaso(String numeroCaso);
	
	@Query(value = "SELECT * FROM solicitud_voc WHERE id_estatus_solicitud NOT IN(11) AND terapeuta = ?1 ORDER BY fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesTerapeuta(int idTerapeuta);

	@Query(value = "SELECT * FROM solicitud_voc WHERE id_estatus_solicitud NOT IN(11) AND terapeuta = ?1 AND sesiones_pendientes > 0 ORDER BY fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesConCitasTerapeuta(int idTerapeuta);
	
	/*@Query(value = "SELECT * FROM solicitud_voc WHERE id_estatus_solicitud IN(:estatusSols) ORDER BY fecha_inicio ASC ", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesPorEstatusFile(@Param("estatusSols") List<String> estatusSols);
	
	@Query(value = "SELECT * FROM solicitud_voc WHERE id_estatus_pago IN(:estatusPago) ORDER BY fecha_inicio ASC ", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesPorEstatusPago(@Param("estatusPago") List<String> estatusPago);
	
	@Query(value = "SELECT * FROM solicitud_voc WHERE id_solicitud LIKE %?1% OR cliente LIKE %?1% OR apellidos LIKE %?1% OR telefono lIKE %?1%", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesPorFiltros(String valor);
	
	@Query(value = "SELECT * FROM solicitud_voc WHERE id_solicitud LIKE %?1% OR cliente LIKE %?2% OR apellidos LIKE %?2% OR telefono lIKE %?3% OR email lIKE %?4% ", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesPorFiltro(String file,String cliente, String telefono, String email);	*/
	
	@Query(value = "SELECT * FROM solicitud_voc WHERE id_solicitud = ?1", nativeQuery = true)
	public SolicitudVocEntity findByIdSolicitud(int idSolicitud);

	/*@Query(value = "SELECT * FROM solicitud_voc WHERE fecha_inicio BETWEEN ?1 AND ?2 ORDER BY fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesPorFecha(Date fechai, Date fechaf);*/
	
	@Query(value = "SELECT * FROM solicitud_voc WHERE fecha_inicio BETWEEN ?1 AND ?2 ORDER BY fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesPorFechaString(String fechai, String fechaf);
	
	@Query(value = "SELECT * FROM solicitud_voc WHERE fecha_inicio BETWEEN ?1 AND ?2 and id_estatus_solicitud = ?3", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesPorFechayEstatus(Date fechai, Date fechaf, String estatus);
	
	@Query(value = "SELECT * FROM solicitud_voc WHERE fecha_inicio BETWEEN ?1 AND ?2 and id_estatus_solicitud = ?3 and usuario_revisor = ?4", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesPorFechayEstatusYUsuario(Date fechai, Date fechaf, String estatus,int idUsuario);
	
	@Query(value = "SELECT s.id_solicitud as solicitud,s.fecha_inicio as fecha,s.telefono,s.cliente,s.email,t.nombre as tipo FROM solicitud_voc s JOIN tipo_solicitud t ON t.id_tipo_solicitud = s.id_tipo_solicitud WHERE s.telefono = ?1 ", nativeQuery = true)
	public List<TelefonosSolicitudes> obtenerSolicitudesDeTelefono(String telefono);
	
	@Query(value = "SELECT id_usuario as usuario,(SELECT count(*) FROM solicitud_voc e where e.usuario_revisando = u.id_usuario ) as numero " 
		     +"FROM usuario u "
		     +"WHERE u.id_rol = ?1 order by numero LIMIT 1", nativeQuery = true) 
			public UltimoUsuarioRevisor obtenerUsuarioConMenosRevisiones(int idRol);
	
	@Query(value = "SELECT u.color,u.image,c.id_usuario,u.nombre as usuario, COUNT(*) AS numero, r.nombre as rol "
	        + "FROM cita c "
			+"JOIN usuario u ON u.id_usuario = c.id_usuario "
			+"JOIN rol r ON r.id_rol = u.id_rol "
			+"WHERE fecha BETWEEN ?1 AND ?2 GROUP BY id_usuario", nativeQuery = true)
	public List<ReporteSolsUsuario> obtenerNumeroCitasTerapueta(Date fi,Date ff);
	
	@Query(value = "SELECT s.id_solicitud as solicitud,s.numero_de_caso as numeroCaso,s.cliente,s.email, "
	+"t.nombre as tipo,s.fecha_inicio as fecha FROM solicitud_voc s JOIN tipo_solicitud t ON t.id_tipo_solicitud = s.id_tipo_solicitud "
	+"where numero_de_caso =  ?1 ", nativeQuery = true)
		public List<NumeroCasosSolicitudes> obtenerSolicitudesDeNumeroDecaso(String numeroCaso);
	
	@Transactional
	@Query(value = "UPDATE solicitud_voc SET id_estatus_solicitud  = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarEstatusSolicitud(int idEstatus, int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud_voc SET id_estatus_pago  = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarEstatusPago(int idEstatusPago, int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud_voc SET num_schedules  = ?1,sesiones_pendientes = ?2 WHERE id_solicitud = ?3", nativeQuery = true)
	@Modifying
	public void actualizarNumSesiones(int numSesiones, int numSesPend,int idSolicitud);
	
	@Query(value = "SELECT s.* FROM solicitud s RIGHT JOIN evento_solicitud e ON e.id_solicitud = s.id_solicitud "
			+ "WHERE e.evento LIKE %?1% GROUP BY id_solicitud", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesLikePorEventoEnEvento(String desc);
	
	@Query(value = "SELECT s.* FROM solicitud_voc s RIGHT JOIN evento_solicitud e ON e.id_solicitud = s.id_solicitud "
			+ "WHERE e.descripcion LIKE %?1% GROUP BY id_solicitud", nativeQuery = true)
	public List<SolicitudVocEntity> obtenerSolicitudesLikeDescEvento(String desc);
	
	@Query(value = "SELECT COALESCE(SUM(amount) ,0)  FROM solicitud_voc WHERE fecha_inicio BETWEEN ?1 AND ?2 ", nativeQuery = true)
	public BigDecimal obtenerSumaAmountSolicitudesPorFecha(Date fechai, Date fechaf);
	
	@Query(value = "SELECT COALESCE(SUM(amount) ,0) FROM solicitud_voc WHERE fecha_inicio BETWEEN ?1 AND ?2 AND cliente = ?3", nativeQuery = true)
	public BigDecimal obtenerSumaAmountSolicitudesPorFechaYClt(Date fechai, Date fechaf,String cliente);
	
	@Query(value = "SELECT COALESCE(SUM(CASE WHEN dos_citas = 1 THEN 2 ELSE 1 END), 0) AS conteo_ajustado  FROM request_evaluation.cita  WHERE id_solicitud = ?1 AND no_show = 0;", nativeQuery = true)
	public int obtenerSesionesDeSolicitud(int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud_voc SET importante = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarImportante(String importante,int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud_voc SET terapeuta = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarTerapeuta(int idUsuario,int idSolicitud);

}
