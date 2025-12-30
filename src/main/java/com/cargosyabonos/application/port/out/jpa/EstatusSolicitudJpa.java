package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.EstatusSolicitudEntity;

@Repository
public interface EstatusSolicitudJpa extends CrudRepository<EstatusSolicitudEntity, Serializable> {

	public List<EstatusSolicitudEntity> findAll();
	
	@Query(value = "SELECT * FROM estatus_solicitud WHERE id_estatus_solicitud = ?1", nativeQuery = true)
	public EstatusSolicitudEntity obtenerEstatusSolicitudPorId(int idEstatusSolicitud);
	
	@Query(value = "SELECT * FROM estatus_solicitud WHERE id_estatus_solicitud IN(1,6,11,13,14)", nativeQuery = true)
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitudesVoc();
	
	@Query(value = "SELECT * FROM estatus_solicitud WHERE id_estatus_solicitud NOT IN(6,12,13,14)", nativeQuery = true)
	public List<EstatusSolicitudEntity> obtenerEstatusSolicitudes();
	
	@Query(value = "SELECT * FROM estatus_solicitud WHERE descripcion = ?1", nativeQuery = true)
	public EstatusSolicitudEntity obtenerEstatusSolicitudPorDescripcion(String desc);

}
