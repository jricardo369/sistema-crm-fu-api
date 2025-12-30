package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.TipoSolicitudEntity;

@Repository
public interface TipoSolicitudJpa extends CrudRepository<TipoSolicitudEntity, Serializable> {

	public List<TipoSolicitudEntity> findAll();

	@Query(value = "SELECT * FROM tipo_solicitud WHERE id_tipo_solicitud = ?1", nativeQuery = true)
	public TipoSolicitudEntity obtenerTipoSolicitudPorId(int idTipoSolicitud);
	
}
