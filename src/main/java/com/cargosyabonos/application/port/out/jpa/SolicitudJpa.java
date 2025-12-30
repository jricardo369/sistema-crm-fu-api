package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargosyabonos.domain.CorreosFirmas;
import com.cargosyabonos.domain.NumFilesAbogados;
import com.cargosyabonos.domain.ReporteSolsDeUsuario;
import com.cargosyabonos.domain.ReporteSolsUsuario;
import com.cargosyabonos.domain.SchedulersSolicitudes;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.TelefonosSolicitudes;
import com.cargosyabonos.domain.UltimoUsuarioRevisor;

@Repository
public interface SolicitudJpa extends CrudRepository<SolicitudEntity, Serializable> {

	public List<SolicitudEntity> findAll();

	@Query(value = "SELECT * FROM solicitud WHERE id_estatus_solicitud IN(:estatusSols) ORDER BY fecha_inicio ASC ", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesPorEstatusFile(@Param("estatusSols") List<String> estatusSols);

	@Query(value = "SELECT * FROM solicitud WHERE id_estatus_pago IN(:estatusPago) ORDER BY fecha_inicio ASC ", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesPorEstatusPago(@Param("estatusPago") List<String> estatusPago);

	@Query(value = "SELECT * FROM solicitud WHERE id_estatus_pago IN(1) AND id_estatus_solicitud = 10 ORDER BY fecha_inicio ASC ", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesPendientesPagoYReady();

	@Query(value = "SELECT * FROM solicitud WHERE id_solicitud LIKE %?1% OR cliente LIKE %?1% OR apellidos LIKE %?1% OR telefono lIKE %?1%", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesPorFiltros(String valor);

	@Query(value = "SELECT * FROM solicitud WHERE id_solicitud LIKE %?1% OR cliente LIKE %?2% OR apellidos LIKE %?2% OR telefono lIKE %?3% OR email lIKE %?4% ", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesPorFiltro(String file, String cliente, String telefono,
			String email);

	@Query(value = "SELECT * FROM solicitud WHERE id_solicitud = ?1", nativeQuery = true)
	public SolicitudEntity findByIdSolicitud(int idSolicitud);

	@Query(value = "SELECT * FROM solicitud WHERE id_estatus_solicitud not in(10,11,12,6)", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesEstatusEnProcesos();

	@Query(value = "SELECT * FROM solicitud WHERE fecha_inicio BETWEEN ?1 AND ?2 ORDER BY fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesPorFecha(Date fechai, Date fechaf);

	@Query(value = "SELECT * FROM solicitud WHERE fecha_inicio BETWEEN ?1 AND ?2 ORDER BY fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesPorFechaString(String fechai, String fechaf);

	@Query(value = "SELECT * FROM solicitud WHERE fecha_inicio BETWEEN ?1 AND ?2 and id_estatus_solicitud = ?3", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesPorFechayEstatus(Date fechai, Date fechaf, String estatus);

	@Query(value = "SELECT * FROM solicitud WHERE fecha_inicio BETWEEN ?1 AND ?2 and id_estatus_solicitud = ?3 and usuario_revisor = ?4", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesPorFechayEstatusYUsuario(Date fechai, Date fechaf, String estatus,
			int idUsuario);

	@Query(value = "SELECT * FROM solicitud WHERE usuario_revisor = ?1 AND id_estatus_solicitud NOT IN(11) ORDER BY fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesDeUsuarioRevisor(int idUsuario);

	@Query(value = "SELECT * FROM solicitud WHERE usuario_revisando IN(:usuarios) AND id_estatus_solicitud NOT IN(11) ORDER BY fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesDeUsuariosRevisando(@Param("usuarios") List<Integer> usuarios);

	@Query(value = "SELECT * FROM solicitud WHERE waiver = ?1 order by fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesDeWaiver(boolean waiver);

	@Query(value = "SELECT * FROM solicitud WHERE usuario_int_sc = ?1 AND id_estatus_solicitud NOT IN(11) AND fin_int_sc = 0 ORDER BY fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesDeUsuarioIntScl(int idUsuario);

	@Query(value = "SELECT * FROM solicitud WHERE usuario_int_sc = ?1 AND id_estatus_solicitud = ?2 AND fin_int_sc = 0 ORDER BY fecha_inicio ASC", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesDeUsuarioIntSclCerradas(int idUsuario, int estatus);

	@Query(value = "SELECT s.id_solicitud as solicitud,s.fecha_inicio as fecha,s.telefono,s.cliente,s.email,t.nombre as tipo FROM solicitud s JOIN tipo_solicitud t ON t.id_tipo_solicitud = s.id_tipo_solicitud WHERE s.telefono = ?1 ", nativeQuery = true)
	public List<TelefonosSolicitudes> obtenerSolicitudesDeTelefono(String telefono);

	@Query(value = "SELECT id_usuario as usuario,(SELECT count(*) FROM solicitud e where e.usuario_revisor = u.id_usuario ) as numero  FROM usuario u "
			+ "WHERE u.id_rol = 4 AND u.ausencia = 0 AND estatus NOT IN(2,4) AND u.revisor = ?1 ORDER BY numero LIMIT 1", nativeQuery = true)
	public UltimoUsuarioRevisor obtenerRevisor(int revisor);

	@Query(value = "SELECT id_usuario as usuario,(SELECT count(*) FROM solicitud e where e.usuario_revisando = u.id_usuario ) as numero  "
			+ "FROM usuario u " + "WHERE u.id_rol = ?1 order by numero LIMIT 1", nativeQuery = true)
	public UltimoUsuarioRevisor obtenerUltimoUsuarioRevisandoPorRol(int idRol);

	@Query(value = "SELECT id_usuario as usuario,(SELECT count(*) FROM solicitud e where e.usuario_revisando = u.id_usuario ) as numero  "
			+ "FROM usuario u " + "WHERE u.id_rol = ?1 AND u.revisor = ?2 order by numero LIMIT 1", nativeQuery = true)
	public UltimoUsuarioRevisor obtenerUsuarioSiEsRevisor(int idRol, int revisor);

	@Transactional
	@Query(value = "UPDATE solicitud SET id_estatus_solicitud  = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarEstatusSolicitud(int idEstatus, int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET id_estatus_pago  = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarEstatusPago(int idEstatusPago, int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET usuario_revisando = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarUsuarioRevisando(int idUsuarioRevisando, int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET usuario_revisando = ?1,usuario_revisor = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarUsuariosRevisores(int idUsuario, int idSolicitud);

	@Query(value = "select id_solicitud,fecha_schedule,hora_schedule,tipo_schedule,descripcion "
			+ "from evento_solicitud where tipo = 'Schedule' and estatus_schedule = 1 and id_solicitud  = ?1 order by id_solicitud, fecha desc ", nativeQuery = true)
	public List<SchedulersSolicitudes> obtenerSchedulersSolicitud(int idSolicitud);

	@Query(value = "SELECT s.* FROM solicitud s RIGHT JOIN evento_solicitud e ON e.id_solicitud = s.id_solicitud "
			+ "WHERE e.evento LIKE %?1% GROUP BY id_solicitud", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesLikePorEventoEnEvento(String desc);

	@Query(value = "SELECT s.* FROM solicitud s RIGHT JOIN evento_solicitud e ON e.id_solicitud = s.id_solicitud "
			+ "WHERE e.descripcion LIKE %?1% GROUP BY id_solicitud", nativeQuery = true)
	public List<SolicitudEntity> obtenerSolicitudesLikeDescEvento(String desc);

	@Query(value = "SELECT count(*) FROM evento_solicitud e " + "JOIN solicitud s ON e.id_solicitud = s.id_solicitud "
			+ "WHERE s.firma_de_abogados = ?1 AND e.evento = 'Email to lawyer sent' ", nativeQuery = true)
	public int obtenerReporteMailsAbogados(String firma);

	@Query(value = "SELECT u.color,u.image,usuario_revisor as id_usuario,CONCAT(u.nombre,' (',r.nombre,')') as usuario,COUNT(usuario_revisor) as numero FROM solicitud s "
			+ "JOIN usuario u ON u.id_usuario = s.usuario_revisor " + "JOIN rol r ON r.id_rol = u.id_rol "
			+ "WHERE fecha_inicio BETWEEN ? AND ? " + "GROUP BY usuario_revisor ", nativeQuery = true)
	public List<ReporteSolsUsuario> obtenerSolicitudesRevisores(Date fechai, Date fechaf);
	
	@Query(value = "SELECT s.id_solicitud,s.fecha_inicio as fecha FROM solicitud s "
			+"JOIN usuario u ON u.id_usuario = s.usuario_revisor  JOIN rol r ON r.id_rol = u.id_rol "
			+"WHERE fecha_inicio BETWEEN ?1 AND ?2 AND usuario_revisor = ?3 ", nativeQuery = true)
	public List<ReporteSolsDeUsuario> obtenerSolicitudesRevisor(Date fechai, Date fechaf,int idUsuario);

	@Query(value = "SELECT u.color,u.image,u.id_usuario,CONCAT(u.nombre,' (',r.nombre,')') as usuario,(SELECT count(*) from evento_solicitud e "
				+"WHERE   descripcion = 'It was sent to the next process Ready on draft' "
				+"AND e.fecha BETWEEN ?1 AND ?2 AND usuario = u.usuario) as numero "
				+"FROM usuario u "
				+"JOIN rol r ON r.id_rol = u.id_rol "
				+"WHERE u.id_rol = 7", nativeQuery = true)
	public List<ReporteSolsUsuario> obtenerSolicitudesTemplates(String fechai, String fechaf);
	
	@Query(value = "SELECT e.id_solicitud,e.fecha FROM evento_solicitud e "
				+"JOIN usuario u ON u.usuario = e.usuario "
				+"WHERE   descripcion = 'It was sent to the next process Ready on draft' "
				+"AND e.fecha BETWEEN ?1 AND ?2  AND u.id_usuario = ?3 ORDER BY fecha DESC", nativeQuery = true)
public List<ReporteSolsDeUsuario> obtenerSolicitudesDeUsuarioTemplate(Date fechai, Date fechaf,int idUsuario);

	@Query(value = "SELECT u.color,u.image,usuario_revisando AS id_usuario,CONCAT(u.nombre,' (',r.nombre,')') as usuario,COUNT(usuario_revisando) AS numero FROM solicitud s "
			+ "JOIN usuario u ON u.id_usuario = s.usuario_revisando " + "JOIN rol r ON r.id_rol = u.id_rol "
			+ "WHERE fecha_inicio BETWEEN ?1 AND ?2 AND u.revisor = 1 "
			+ "GROUP BY usuario_revisando ", nativeQuery = true)
	public List<ReporteSolsUsuario> obtenerSolicitudesRevisoresAd(Date fechai, Date fechaf);
	
	@Query(value = "SELECT u.color,u.image,assigned_clinician AS id_usuario,CONCAT(u.nombre,' (',r.nombre,')') as usuario,COUNT(assigned_clinician) AS numero " 
			+"FROM solicitud s "
			+"JOIN usuario u ON u.id_usuario = s.assigned_clinician "
			+"JOIN rol r ON r.id_rol = u.id_rol "
			+"WHERE fecha_inicio BETWEEN ?1 AND ?2 "
			+"GROUP BY s.assigned_clinician ", nativeQuery = true)
	public List<ReporteSolsUsuario> obtenerSolicitudesClinicians(Date fechai, Date fechaf);
	
	@Query(value = "SELECT s.id_solicitud,s.fecha_inicio as fecha FROM solicitud s  "
			+"WHERE fecha_inicio BETWEEN ?1 AND ?2 AND s.assigned_clinician = ?3 ", nativeQuery = true)
	public List<ReporteSolsDeUsuario> obtenerSolicitudesClinician(Date fechai, Date fechaf,int idUsuario);
	
	@Query(value = "SELECT s.id_solicitud, s.fecha_inicio as fecha FROM solicitud s "
			+ "JOIN usuario u ON u.id_usuario = s.usuario_revisando " + "JOIN rol r ON r.id_rol = u.id_rol "
			+ "WHERE fecha_inicio BETWEEN ?1 AND ?2 AND u.revisor = 1 AND id_usuario = ?3 "
			+ "GROUP BY s.id_solicitud ", nativeQuery = true)
	public List<ReporteSolsDeUsuario> obtenerSolicitudesRevisorAd(Date fechai, Date fechaf,int idUsuario);

	@Query(value = "SELECT COALESCE(SUM(amount) ,0)  FROM solicitud WHERE fecha_inicio BETWEEN ?1 AND ?2 ", nativeQuery = true)
	public BigDecimal obtenerSumaAmountSolicitudesPorFecha(Date fechai, Date fechaf);

	@Query(value = "SELECT COALESCE(SUM(amount) ,0) FROM solicitud WHERE fecha_inicio BETWEEN ?1 AND ?2 AND cliente = ?3", nativeQuery = true)
	public BigDecimal obtenerSumaAmountSolicitudesPorFechaYClt(Date fechai, Date fechaf, String cliente);

	@Query(value = "SELECT firma_de_abogados FROM solicitud WHERE fecha_inicio BETWEEN ?1 AND ?2 GROUP BY firma_de_abogados ", nativeQuery = true)
	public List<String> obtenerFirmasAbogados(String fechai, String fechaf);
	
	@Query(value = "SELECT firma_de_abogados as firma,count(*) as numero FROM solicitud WHERE fecha_inicio BETWEEN ?1 AND ?2 GROUP BY firma_de_abogados ", nativeQuery = true)
	public List<NumFilesAbogados> obtenerFirmasAbogadosyNumFiles(String fechai, String fechaf);

	@Query(value = "SELECT email,firma_de_abogados from solicitud WHERE firma_de_abogados IN(:correos)  ", nativeQuery = true)
	public List<CorreosFirmas> obtenerCorreosFirmas(@Param("correos") List<String> correos);

	@Query(value = "SELECT CASE " + 
				"WHEN fin_int_ini = 1 AND fin_int_sc = 1 AND fin_asg_clnc = 1 THEN 1 " + 
				"ELSE 0  " + 
				"END AS todos_finalizados " +
				"FROM " + 
				"solicitud s " + 
				"WHERE id_solicitud in (?1) ", nativeQuery = true)
	public int esFinEntrevistas(int idSolicitud);

	@Query(value = "SELECT CASE " + 
				"WHEN fin_int_ini = 1 AND fin_int_sc = 1 THEN 1 " + 
				"ELSE 0  " + 
				"END AS todos_finalizados " +
				"FROM " + 
				"solicitud s " + 
				"WHERE id_solicitud in (?1) ", nativeQuery = true)
	public int esFinEntrevistasWithoutClinician(int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET asignacion_template = ?1,usuario_template = ?2 WHERE id_solicitud = ?3", nativeQuery = true)
	@Modifying
	public void actualizarAsignacionTemplate(int asignacionTemplate, int idUsuarioTemp, int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET asignacion_int_sc = ?1,usuario_int_sc = ?2 WHERE id_solicitud = ?3", nativeQuery = true)
	@Modifying
	public void actualizarAsignacionIntSc(int asignacionIntSc, int idUsuario, int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud SET assigned_clinician = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarAsignacionClnc(int idUsuario, int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET asignacion_int_sc = 0,usuario_int_sc = 0,fin_int_sc = 0 WHERE id_solicitud = ?1", nativeQuery = true)
	@Modifying
	public void actualizarAsignacionIntScReset(int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud SET asignacion_clnc = 0,usuario_int_sc = 0,fin_int_sc = 0 WHERE id_solicitud = ?1", nativeQuery = true)
	@Modifying
	public void actualizarAsignacionClncReset(int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET numero_entrevistas = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarNumeroEntrevistas(int num, int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET importante = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarImportante(String importante, int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET fin_int_sc = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarFinIntSc(String valor, int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud SET fin_asg_tmp = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarFinAsgTmp(String valor, int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud SET fin_asg_clnc = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarFinAsgClnc(String valor, int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET usuario_interview = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarInterview(int idUsuario, int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud SET fin_int_ini = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarFinInterview(int finIntIni,int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud SET usuario_interview = 0 WHERE id_solicitud = ?1", nativeQuery = true)
	@Modifying
	public void actualizarAsignacionIntReset(int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET interview_master = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarInterviewMaster(String valor, int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET asignacion_int_sc = 0,usuario_int_sc = 0 WHERE id_solicitud = ?1", nativeQuery = true)
	@Modifying
	public void actualizarIntSc(int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud SET assigned_clinician = ?1  WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarAssignedClinician(int assignedClinician,int idSolicitud);

	@Transactional
	@Query(value = "UPDATE solicitud SET con_cupon = 1, fecha_cupon = CURDATE()  WHERE id_solicitud = ?1", nativeQuery = true)
	@Modifying
	public void actualizarSolicitudConCupon(int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE solicitud SET email_abo_sel = ?1 WHERE id_solicitud = ?2", nativeQuery = true)
	@Modifying
	public void actualizarEmailAbo(String emailsAbogado,int idSolicitud);
	
	
}
