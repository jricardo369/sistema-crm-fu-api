package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.ConfiguracionEntity;

@Repository
public interface ConfiguracionJpa extends CrudRepository<ConfiguracionEntity, Serializable> {

	public List<ConfiguracionEntity> findAll();
	
	@Query(value = "SELECT * FROM configuracion WHERE codigo = ?1", nativeQuery = true)
	public ConfiguracionEntity obtenerConfiguracionPorCodigo(String codigo);

}
