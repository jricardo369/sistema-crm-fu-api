package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.MotivoCancelEntity;

@Repository
public interface MotivoCancelJpa extends CrudRepository<MotivoCancelEntity, Serializable> {

	public List<MotivoCancelEntity> findAll();
	
	@Query(value = "SELECT * FROM motivo_cancel WHERE tipo = ?1 AND rol = ?2", nativeQuery = true)
	public List<MotivoCancelEntity> obtenerMotivosPorTipo(String tipo,String rol);
	
}
