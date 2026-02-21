package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargosyabonos.domain.CitasUsuario;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.ReporteSolsDeUsuario;
import com.cargosyabonos.domain.ReporteSolsUsuario;
import com.cargosyabonos.domain.SchedulersActivasDeSolicitud;

@Repository
public interface EventoSolicitudJpa extends CrudRepository<EventoSolicitudEntity, Serializable> {

	public List<EventoSolicitudEntity> findAll();

	@Query(value = "SELECT * FROM evento_solicitud WHERE id_evento = ?1", nativeQuery = true)
	public EventoSolicitudEntity obtenerEventoPorId(int idEvento);

	@Query(value = "SELECT * FROM evento_solicitud WHERE id_solicitud = ?1 ORDER BY id_evento DESC", nativeQuery = true)
	public List<EventoSolicitudEntity> obtenerEventosDesolicitud(int idSolicitud);

	@Query(value = "SELECT * FROM evento_solicitud WHERE evento NOT LIKE '%Payment registration%' AND id_solicitud = ?1 ORDER BY fecha DESC", nativeQuery = true)
	public List<EventoSolicitudEntity> obtenerEventosDesolicitudSinPagos(int idSolicitud);

	@Query(value = "SELECT * FROM evento_solicitud where tipo = 'Schedule' "
			+ "AND id_solicitud = ?1 AND estatus_schedule = ?2 ORDER BY id_evento AND descripcion LIKE '%Scheduled appointment%' DESC LIMIT 1", nativeQuery = true)
	public EventoSolicitudEntity obtenerUltimoScheduleInt(int idSolicitud, String estatus);
	
	@Query(value = "SELECT * FROM evento_solicitud WHERE tipo = 'Schedule' "
			+"AND id_solicitud = ?1 AND estatus_schedule = ?2 AND descripcion LIKE '%Scheduled appointment%' AND usuario_schedule = ?3", nativeQuery = true)
	public EventoSolicitudEntity obtenerUltimoScheduleInterviewByUser(int idSolicitud, String estatus,int idUsuaroSchedule);

	@Query(value = "SELECT * FROM evento_solicitud WHERE tipo = 'Schedule' "
			+"AND id_solicitud = ?1 AND estatus_schedule = ?2 AND descripcion LIKE '%Scheduled clinician appointment%' AND usuario_schedule = ?3", nativeQuery = true)
	public EventoSolicitudEntity obtenerUltimoScheduleInterviewClinicianByUser(int idSolicitud, String estatus,int idUsuaroSchedule);
	
	@Query(value = "SELECT GROUP_CONCAT(u.nombre SEPARATOR ', ') AS nombres "
			+ "FROM evento_solicitud e "
			+ "JOIN usuario u ON u.id_usuario = e.usuario_schedule "
			+ "WHERE e.id_solicitud = ?1 AND e.evento = 'Schedule' AND e.estatus_schedule = 1 AND e.descripcion LIKE '%Scheduled appointment%' ", nativeQuery = true)
	public String saberQueSchedulesInterviewSeTienen(int idSolicitud);

	@Query(value = "SELECT * FROM evento_solicitud where tipo = 'Schedule' "
			+ "AND id_solicitud = ?1 AND estatus_schedule = ?2 ORDER BY id_evento AND descripcion LIKE '%Scheduled scales%' DESC LIMIT 1", nativeQuery = true)
	public EventoSolicitudEntity obtenerUltimoScheduleScale(int idSolicitud, String estatus);
	
	@Query(value = "SELECT * FROM evento_solicitud where tipo = 'Schedule' "
			+ "AND id_solicitud = ?1 AND estatus_schedule = ?2 ORDER BY id_evento AND descripcion LIKE '%Scheduled clinician%' DESC LIMIT 1", nativeQuery = true)
	public EventoSolicitudEntity obtenerUltimoScheduleClinician(int idSolicitud, String estatus);

	@Query(value = "SELECT * FROM evento_solicitud " + "WHERE tipo = 'Schedule'  "
			+ "AND (fecha LIKE %?1% AND fecha_schedule IS NULL) "
			+ "OR ( fecha_schedule LIKE %?1%  AND estatus_schedule = 1) " + "ORDER BY id_evento", nativeQuery = true)
	public List<EventoSolicitudEntity> obtenerSchedulesPendientesPorFecha(Date fecha);

	@Query(value = "SELECT * FROM evento_solicitud WHERE tipo = 'Schedule' AND  fecha_schedule = ?1 AND hora_schedule = ?2 AND tipo_schedule = ?3 AND estatus_schedule = 1 AND usuario_schedule = ?4", nativeQuery = true)
	public List<EventoSolicitudEntity> obtenerEventosScheduleDeFechaPorUsuario(String fecha,String hora,String tipo,int idUsuario);

	@Query(value = "SELECT count(*) FROM evento_solicitud WHERE tipo = 'Schedule' AND fecha_schedule = ?1 AND hora_schedule = ?2 "
			+ "AND tipo_schedule = ?3 AND usuario_schedule = ?4 AND estatus_schedule = ?5 ", nativeQuery = true)
	public int existeEventoSchedule(String fecha, String hora, String tipoSchedule, int idUsuario,
			String estatusSchedule);

	@Query(value = "SELECT e.id_evento,e.fecha,e.evento,e.descripcion,e.tipo,e.id_usuario,e.id_solicitud "
			+ "FROM evento_solicitud e "
			+ "JOIN usuario u on u.id_usuario = e.id_usuario WHERE id_solicitud = ?1", nativeQuery = true)
	public List<EventoSolicitud> obtenerEventosDeSolicitudIdSol(int idSolicitud);

	@Query(value = "SELECT count(*) FROM evento_solicitud WHERE id_solicitud = ?1 AND evento = ?2", nativeQuery = true)
	public int tieneEventoLaSolicitud(int idSolicitud, String evento);

	@Query(value = "SELECT count(*) FROM evento_solicitud WHERE id_solicitud = ?1 AND tipo = ?2", nativeQuery = true)
	public int tieneTipoEventoLaSolicitud(int idSolicitud, String tipoEvento);

	@Query(value = "SELECT u.color,u.image,u.id_usuario,u.nombre as usuario,count(*) as numero, r.nombre as rol "
			+  "FROM evento_solicitud e "
			+ "JOIN usuario u ON u.id_usuario = e.usuario_schedule " + "JOIN rol r ON r.id_rol = u.id_rol "
			+ "WHERE  tipo = ?1 " + "AND estatus_schedule = ?2  AND fecha_schedule BETWEEN ?3 AND ?4 AND e.fin_schedule = 1 "
			+ "GROUP BY usuario_schedule", nativeQuery = true)
	public List<ReporteSolsUsuario> obtenerNumeroSolicitudesAtendidasPorEntrevistadores(String tipoEvento,
			String estatusSchedule, Date fi, Date ff);
	
	@Query(value = "SELECT e.id_solicitud,e.fecha "
				+"FROM evento_solicitud e "
				+"JOIN usuario u ON u.id_usuario = e.usuario_schedule JOIN rol r ON r.id_rol = u.id_rol "
				+"WHERE  tipo = 'Schedule' AND estatus_schedule = 1 AND fecha_schedule BETWEEN ?1 AND ?2 AND e.fin_schedule = 1 AND id_usuario = ?3 "
				+"GROUP BY e.id_solicitud,e.fecha", nativeQuery = true)
	public List<ReporteSolsDeUsuario> obtenerSolicitudesAtendidasPorEntrevistador(Date fi, Date ff,int idUsuario);

	@Query(value = "select id_solicitud from evento_solicitud where tipo = ?1", nativeQuery = true)
	public List<Integer> solicitudesConTipoImportant(String tipoEvento);

	@Query(value = "select count(id_solicitud) from evento_solicitud where tipo = ?1 and id_solicitud = ?2", nativeQuery = true)
	public int solicitudesSiTieneAlgunEventoDeSolicitud(String tipoEvento, int idSo);

	@Query(value = "SELECT e.fecha,e.hora_schedule,e.tipo_schedule,s.cliente,s.telefono FROM evento_solicitud e "
			+ "JOIN solicitud s ON s.id_solicitud = e.id_solicitud "
			+ "WHERE e.tipo = 'Schedule' AND e.usuario_schedule = ?1 "
			+ "AND e.estatus_schedule = 1 AND fecha_schedule BETWEEN ?2 AND ?3 ", nativeQuery = true)
	public List<CitasUsuario> citasDeUsuario(int idUsuario, Date fi, Date ff);

	@Query(value = "SELECT e.id_evento as idEvento,e.usuario_schedule as idUsuario, u.nombre as nombreUsuario,r.nombre as rol,CONCAT(e.fecha_schedule,' ',e.hora_schedule,' ',e.tipo_schedule) AS fecha "
			+ "FROM evento_solicitud e " + "JOIN usuario u on u.id_usuario = e.usuario_schedule "
			+ "JOIN rol r on r.id_rol = u.id_rol "
			+ "WHERE e.tipo = 'Schedule' AND e.estatus_schedule = 1 AND e.id_solicitud = ?1 AND e.fin_schedule = 0 "
			+ "ORDER BY  id_solicitud,fecha DESC ", nativeQuery = true)
	public List<SchedulersActivasDeSolicitud> obtenerSchedulesActivasDeSolicitud(int idSolicitud);
	
	@Query(value = "SELECT e.id_evento as idEvento,e.usuario_schedule as idUsuario, u.nombre as nombreUsuario,r.nombre as rol,CONCAT(e.fecha_schedule,' ',e.hora_schedule,' ',e.tipo_schedule) AS fecha "
			+ "FROM evento_solicitud e " + "JOIN usuario u on u.id_usuario = e.usuario_schedule "
			+ "JOIN rol r on r.id_rol = u.id_rol "
			+ "WHERE e.tipo = 'Schedule' AND e.estatus_schedule = 1 AND e.usuario_schedule = ?1 AND e.id_solicitud = ?2 ORDER BY  id_solicitud,fecha DESC ", nativeQuery = true)
	public List<SchedulersActivasDeSolicitud> obtenerSchedulesActivasDeUsuario(int idUsuario,int idSolicitud);
	
	@Transactional
	@Query(value = "UPDATE evento_solicitud SET fin_schedule = ?1 WHERE id_evento = ?2", nativeQuery = true)
	@Modifying
	public void actualizarFinSchedule(String finSchedule,int idEvento);
	
	@Transactional
	@Query(value = "UPDATE evento_solicitud SET fin_schedule = 1 WHERE id_solicitud = ?1 AND estatus_schedule = 1 AND tipo = 'Schedule'", nativeQuery = true)
	@Modifying
	public void actualizarFinScheduleDeSolicitud(int idSolicitud);


}
