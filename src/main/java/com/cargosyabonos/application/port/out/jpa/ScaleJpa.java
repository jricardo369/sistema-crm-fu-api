package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.ScaleEntity;

@Repository
public interface ScaleJpa extends CrudRepository<ScaleEntity, Serializable> {

	public List<ScaleEntity> findAll();
	
	@Query(value = "SELECT * FROM scale WHERE id_solicitud = ?1", nativeQuery = true)
	public List<ScaleEntity> obtenerScalesDesolicitud(int idSolicitud);
	
	@Query(value = "SELECT * FROM scale WHERE id_scale = ?1", nativeQuery = true)
	public ScaleEntity obtenerScale(int idScale);

}
