package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargosyabonos.domain.CitaEntity;
import com.cargosyabonos.domain.CitaSql;

@Repository
public interface CitaJpa extends CrudRepository<CitaEntity, Serializable> {

	public List<CitaEntity> findAll();
	
	@Query(value = "SELECT * FROM cita WHERE id_cita = ?1", nativeQuery = true)
	public CitaEntity obtenerPorId(int idCita);
	
	@Query(value = " SELECT  c.id_cita as idCita,c.comentario,c.fecha,c.hora,c.tipo,c.dos_citas as dosCitas,c.id_usuario as idUsuario,c.id_solicitud as idSolicitud, " + 
				"   c.no_show as noShow,c.amount,c.pagado,c.fecha_pagado as fechaPagado, " + 
				"    CASE WHEN nc.id_cita IS NOT NULL THEN 1 ELSE 0 END AS tieneNota, " + 
				"    c.fecha_creacion AS fechaCreacion, " + 
				"    c.codigo_recurrencia AS codigoRecurrencia, " + 
				"    CASE " + 
				"        WHEN nc.usuario_firma IS NOT NULL " + 
				"             AND nc.usuario_firma <> '' " + 
				"        THEN 1 " + 
				"        ELSE 0 " + 
				"    END AS tieneFirma, " + 
				"  u.supervisor,nc.rechazada as rechazada,nc.fecha_aprobacion as fechaaprobacion,nc.hora as horanota,nc.fecha_creacion as fechacreacionnota " + 
				"FROM cita c " + 
				"LEFT JOIN nota_cita nc ON nc.id_cita = c.id_cita " +
				"LEFT JOIN usuario u ON u.id_usuario = c.id_usuario " + 
				"WHERE c.id_solicitud = ?1 " +
				"ORDER BY c.fecha DESC;", nativeQuery = true)
	public List<CitaSql> obtenerCitasDeSolicitud(int idSolicitud);
	
	@Query(value = " SELECT c.id_cita as idCita,c.comentario,c.fecha,c.hora,c.tipo,c.dos_citas as dosCitas,c.id_usuario as idUsuario,c.id_solicitud as idSolicitud, " 
			 +"c.no_show as noShow,c.amount,c.pagado,c.fecha_pagado as fechaPagado,(SELECT COUNT(*)  FROM nota_cita WHERE id_cita = c.id_cita ) as tieneNota "
			 +"FROM cita c "
			 +"WHERE id_solicitud = ?1 AND no_show = 0 "
			 +"ORDER BY fecha DESC", nativeQuery = true)
public List<CitaSql> obtenerCitasDeSolicitudSinNoshow(int idSolicitud);
	
	@Query(value = "SELECT * FROM cita WHERE id_usuario = ?1 AND fecha = ?2 ORDER BY fecha DESC", nativeQuery = true)
	public List<CitaEntity> obtenerCitasDeUsuarioPorFecha(int idUsuario,String fecha);
	
	@Query(value = "SELECT * FROM cita WHERE fecha = ?1 ORDER BY fecha DESC", nativeQuery = true)
	public List<CitaEntity> obtenerTodasCitasPorFecha(String fecha);
	
	@Query(value = "SELECT * FROM cita WHERE yearweek(`fecha`) = yearweek(?1) AND id_usuario = ?2 AND no_show = ?3", nativeQuery = true)
	public List<CitaEntity> obtenerCitasDeUsuarioPorSemana(String fecha,int idUsuario,int noShow);
	
	@Query(value = "SELECT * FROM cita WHERE yearweek(`fecha`) = yearweek(?1) AND no_show = ?2", nativeQuery = true)
	public List<CitaEntity> obtenerTodasCitasPorSemana(String fecha, int noShow);

	@Query(value = "SELECT * FROM cita WHERE  codigo_recurrencia = ?1 AND id_cita = ?2", nativeQuery = true)
	public CitaEntity obtenerCitaPorRecurrenciaYIdCita(String codigoRecurrencia,int idCita);

	@Query(value = "SELECT * FROM cita WHERE codigo_recurrencia = ?1 AND fecha >= ?2", nativeQuery = true)
	public List<CitaEntity> obtenerCitaPorRecurrenciaYFecha(String codigoRecurrencia,String fecha);
	
	@Query(value = " SELECT c.id_cita as idCita,c.comentario,c.fecha,c.hora,c.tipo,c.dos_citas as dosCitas,c.id_usuario as idUsuario,c.id_solicitud as idSolicitud, " + 
				"c.no_show as noShow,c.amount,c.pagado,c.fecha_pagado as fechaPagado," + 
				" CASE WHEN nc.id_cita IS NOT NULL THEN 1 ELSE 0 END AS tieneNota," + 
				"u.color, s.numero_de_caso as casenumber,s.cliente,u.nombre as nombreUsuario,c.codigo_recurrencia as codigoRecurrencia," + 
				"CASE " + 
				"        WHEN nc.usuario_firma IS NOT NULL " + 
				"             AND nc.usuario_firma <> '' " + 
				"        THEN 1 " + 
				"        ELSE 0 " + 
				"    END AS tieneFirma, " + 
				"  u.supervisor,nc.rechazada as rechazada,nc.fecha_aprobacion as fechaaprobacion,nc.hora as horanota,nc.fecha_creacion as fechacreacionnota " + 
				"FROM cita c " + 
				"LEFT JOIN solicitud_voc s ON s.id_solicitud= c.id_solicitud " + 
				"LEFT JOIN usuario u ON u.id_usuario = c.id_usuario " + 
				"LEFT JOIN nota_cita nc ON nc.id_cita = c.id_cita "
			 +"WHERE fecha >= ?1 AND  fecha < ?2 "
			 +"AND c.id_usuario = ?3 AND no_show = ?4", nativeQuery = true)
	public List<CitaSql> obtenerCitasDeUsuarioPorSemanaTerapeuta(LocalDate inicioSemana, LocalDate finSemana,int idUsuario,int noShow);

	@Query(value = " SELECT c.id_cita as idCita,c.comentario,c.fecha,c.hora,c.tipo,c.dos_citas as dosCitas,c.id_usuario as idUsuario,c.id_solicitud as idSolicitud, " + 
				"c.no_show as noShow,c.amount,c.pagado,c.fecha_pagado as fechaPagado," + 
				" CASE WHEN nc.id_cita IS NOT NULL THEN 1 ELSE 0 END AS tieneNota," + 
				"u.color, s.numero_de_caso as casenumber,s.cliente,u.nombre as nombreUsuario,c.codigo_recurrencia as codigoRecurrencia," + 
				"CASE " + 
				"        WHEN nc.usuario_firma IS NOT NULL " + 
				"             AND nc.usuario_firma <> '' " + 
				"        THEN 1 " + 
				"        ELSE 0 " + 
				"    END AS tieneFirma, " +
				"  u.supervisor,nc.rechazada as rechazada,nc.fecha_aprobacion as fechaaprobacion,nc.hora as horanota,nc.fecha_creacion as fechacreacionnota " + 
				"FROM cita c " + 
				"LEFT JOIN solicitud_voc s ON s.id_solicitud= c.id_solicitud " + 
				"LEFT JOIN usuario u ON u.id_usuario = c.id_usuario " + 
				"LEFT JOIN nota_cita nc ON nc.id_cita = c.id_cita "
			 +"WHERE fecha >= ?1 AND  fecha < ?2 "
			 +"AND no_show = ?3", nativeQuery = true)
	public List<CitaSql> obtenerCitasDeUsuarioPorSemanaVoc(LocalDate inicioSemana, LocalDate finSemana,int noShow);

	@Query(value = "SELECT CONCAT(u.nombre, ',', COUNT(*),' schedules') AS usuarioYSesiones " + 
				"FROM cita c " +
				"JOIN nota_cita n ON n.id_cita = c.id_cita " + 
				"JOIN usuario u ON u.id_usuario = c.id_usuario " + 
				"WHERE id_solicitud = ?1 " + 
				"GROUP BY u.nombre", nativeQuery = true)
	public List<String> obtenerNumeroCitasTerapeutasPorSolicitud(int idSolicitud);

	@Query(value = "SELECT  s.id_solicitud,s.cliente,s.numero_de_caso as casenumber,c.id_cita as idCita,c.comentario,c.fecha,c.hora,c.tipo,c.dos_citas as dosCitas,c.id_usuario as idUsuario,c.id_solicitud as idSolicitud, " + 
				"c.no_show as noShow,c.amount,c.pagado,c.fecha_pagado as fechaPagado, " +
				"CASE WHEN nc.id_cita IS NOT NULL THEN 1 ELSE 0 END AS tieneNota,  " + 
				"c.fecha_creacion AS fechaCreacion, " + 
				"c.codigo_recurrencia AS codigoRecurrencia, " + 
				"u.supervisor,nc.rechazada as rechazada,nc.fecha_aprobacion as fechaaprobacion,nc.hora as horanota, nc.fecha_creacion as fechaCreacionNota, "+
				"u.correo_electronico as emailterapeuta,u.nombre as nombreterapeuta " + 
				"FROM cita c " + 
				"LEFT JOIN nota_cita nc ON nc.id_cita = c.id_cita " + 
				"LEFT JOIN solicitud s ON s.id_solicitud = c.id_solicitud " + 
				"LEFT JOIN usuario u ON u.id_usuario = c.id_usuario " + 
				"WHERE fecha = DATE_SUB(CURDATE(), INTERVAL 1 DAY) AND no_show = 0 " + 
				"HAVING tieneNota = 0 " + 
				"ORDER BY c.fecha DESC;", nativeQuery = true)
	public List<CitaSql> obtenerCitasSinNotaDiaAnterior();
	
	@Transactional
	@Query(value = "UPDATE cita SET no_show  = ?1 WHERE id_cita = ?2", nativeQuery = true)
	@Modifying
	public void actualizarNoShow(boolean noShow,int idCita);
	
	@Transactional
	@Query(value = "UPDATE cita SET pagado  = ?1,fecha_pagado = ?2 WHERE id_cita = ?3", nativeQuery = true)
	@Modifying
	public void actualizarPagado(boolean pagado,String fechPagado,int idCita);

	@Transactional
	@Query(value = "DELETE  FROM cita WHERE codigo_recurrencia = ?1 AND fecha >= ?2", nativeQuery = true)
	@Modifying
	public void eliminarCitasRecurrentes(String codigoRecurrencia,String fecha);

}
