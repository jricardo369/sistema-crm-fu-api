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
	
	@Query(value = " SELECT c.id_cita as idCita,c.comentario,c.fecha,c.hora,c.tipo,c.dos_citas as dosCitas,c.id_usuario as idUsuario,c.id_solicitud as idSolicitud, " 
				 +"c.no_show as noShow,c.amount,c.pagado,c.fecha_pagado as fechaPagado,(SELECT COUNT(*)  FROM nota_cita WHERE id_cita = c.id_cita ) as tieneNota,c.fecha_creacion as fechaCreacion "
				 +"FROM cita c "
				 +"WHERE id_solicitud = ?1 "
				 +"ORDER BY fecha DESC", nativeQuery = true)
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
	
	@Query(value = " SELECT c.id_cita as idCita,c.comentario,c.fecha,c.hora,c.tipo,c.dos_citas as dosCitas,c.id_usuario as idUsuario,c.id_solicitud as idSolicitud, " 
			 +"c.no_show as noShow,c.amount,c.pagado,c.fecha_pagado as fechaPagado,"
			 + "(SELECT COUNT(*)  FROM nota_cita WHERE id_cita = c.id_cita ) as tieneNota,u.color, s.numero_de_caso as casenumber,s.cliente,u.nombre as nombreUsuario   "
			 +"FROM cita c "
			 +"LEFT JOIN solicitud_voc s ON s.id_solicitud= c.id_solicitud "
			 +"LEFT JOIN usuario u ON u.id_usuario = c.id_usuario "
			 +"WHERE fecha >= ?1 AND  fecha < ?2 "
			 +"AND c.id_usuario = ?3 AND no_show = ?4", nativeQuery = true)
	public List<CitaSql> obtenerCitasDeUsuarioPorSemana(LocalDate inicioSemana, LocalDate finSemana,int idUsuario,int noShow);
	
	@Transactional
	@Query(value = "UPDATE cita SET no_show  = ?1 WHERE id_cita = ?2", nativeQuery = true)
	@Modifying
	public void actualizarNoShow(boolean noShow,int idCita);
	
	@Transactional
	@Query(value = "UPDATE cita SET pagado  = ?1,fecha_pagado = ?2 WHERE id_cita = ?3", nativeQuery = true)
	@Modifying
	public void actualizarPagado(boolean pagado,String fechPagado,int idCita);

}
