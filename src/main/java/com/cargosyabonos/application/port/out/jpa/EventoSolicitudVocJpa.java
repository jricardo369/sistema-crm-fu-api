package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.EventoSolicitudVocEntity;

@Repository
public interface EventoSolicitudVocJpa extends CrudRepository<EventoSolicitudVocEntity, Serializable> {

	public List<EventoSolicitudVocEntity> findAll();
	
	@Query(value = "SELECT * FROM evento_solicitud_voc WHERE id_solicitud = ?1 ORDER BY fecha DESC", nativeQuery = true)
	public List<EventoSolicitudVocEntity> obtenerEventosDesolicitud(int idSolicitud);
	
	@Query(value = "SELECT * FROM evento_solicitud_voc WHERE evento NOT LIKE '%Pay%' AND id_solicitud = ?1 ORDER BY fecha DESC", nativeQuery = true)
	public List<EventoSolicitudVocEntity> obtenerEventosDesolicitudSinPagos(int idSolicitud);
	
	@Query(value = "SELECT * FROM evento_solicitud_voc WHERE tipo = 'Schedule'  AND fecha LIKE %?1% AND fecha_schedule IS NULL ORDER BY id_evento", nativeQuery = true)
		public List<EventoSolicitudVocEntity> obtenerEventosSchedules(String fecha);
	
	@Query(value = "SELECT e.id_evento,e.fecha,e.evento,e.descripcion,e.tipo,e.id_usuario,e.id_solicitud "
			+ "FROM evento_solicitud_voc e "
			+ "JOIN usuario u on u.id_usuario = e.id_usuario WHERE id_solicitud = ?1", nativeQuery = true)
	public List<EventoSolicitud> obtenerEventosDeSolicitudIdSol(int idSolicitud);
	
	@Query(value = "SELECT count(*) FROM evento_solicitud_voc WHERE id_solicitud = ?1 AND evento = ?2", nativeQuery = true)
	public int tieneEventoLaSolicitud(int idSolicitud,String evento);
	
	@Query(value = "SELECT count(*) FROM evento_solicitud_voc WHERE id_solicitud = ?1 AND tipo = ?2", nativeQuery = true)
	public int tieneTipoEventoLaSolicitud(int idSolicitud,String tipoEvento);
	
	@Query(value = "select id_solicitud from evento_solicitud_voc where tipo = ?1", nativeQuery = true)
	public List<Integer> solicitudesConTipoImportant(String tipoEvento);
	
	@Query(value = "select count(id_solicitud) from evento_solicitud_voc where tipo = ?1 and id_solicitud = ?2", nativeQuery = true)
	public int solicitudesSiTieneAlgunEventoDeSolicitud(String tipoEvento,int idSo);

	@Query(value = "SELECT id_evento,fecha,evento, "+
				"SUBSTRING_INDEX(descripcion, ',', 1) AS descripcion,tipo, usuario,id_solicitud " +
				"FROM evento_solicitud_voc " + 
				"WHERE id_solicitud = ?1 AND evento = 'Session Adjustment' " + 
				"ORDER BY fecha ASC;", nativeQuery = true)
	public List<EventoSolicitudVocEntity> obtenerHistorialNumSesionesDeSolicitud(int idSolicitud);

	@Transactional
	@Query(value = "UPDATE evento_solicitud_voc SET tipo = ?1,descripcion=?2 WHERE id_evento = ?3", nativeQuery = true)
	@Modifying
	public void actualizarEstatusEvento(String tipo,String descripcion,int idEvento);

}
