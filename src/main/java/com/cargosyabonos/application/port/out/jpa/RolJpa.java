package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.cargosyabonos.domain.RolEntity;

@Repository
public interface RolJpa extends CrudRepository<RolEntity, Serializable> {

	public List<RolEntity> findAll();

	@Query(value = "SELECT * FROM rol WHERE id_rol IN(:roles)", nativeQuery = true)
	public List<RolEntity> obtenerRolesCitasDispo(@Param("roles") List<Integer> roles);

}
