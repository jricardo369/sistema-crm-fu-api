package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.EstatusPagoEntity;

@Repository
public interface EstatusPagoJpa extends CrudRepository<EstatusPagoEntity, Serializable> {

	public List<EstatusPagoEntity> findAll();
	
	@Query(value = "SELECT * FROM estatus_pago WHERE id_estatus_pago = ?1", nativeQuery = true)
	public EstatusPagoEntity obtenerEstatusPagoPorId(int idEstatusPago);
	
	@Query(value = "SELECT * FROM estatus_pago WHERE descripcion = ?1", nativeQuery = true)
	public EstatusPagoEntity obtenerEstatusPagoPorDescripcion(String descripcion);

}
