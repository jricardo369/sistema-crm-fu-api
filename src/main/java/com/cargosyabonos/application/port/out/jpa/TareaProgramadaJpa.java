package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargosyabonos.domain.TareaProgramadaEntity;

@Repository
public interface TareaProgramadaJpa extends CrudRepository<TareaProgramadaEntity, Serializable> {

	public List<TareaProgramadaEntity> findAll();
	
	public TareaProgramadaEntity findByCodigo(String codigo);
	
	@Transactional
	@Query(value = "DELETE FROM disponibilidad_usuario WHERE fecha < DATE_SUB(NOW(), INTERVAL 11 DAY)", nativeQuery = true)
	@Modifying
	public void eliminarDisponibilidadesAnteriores();

}
