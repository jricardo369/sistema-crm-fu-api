package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.TipoPagoEntity;

@Repository
public interface TipoPagoJpa extends CrudRepository<TipoPagoEntity, Serializable> {

	public List<TipoPagoEntity> findAll();
	
	@Query(value = "SELECT * FROM tipo_pago WHERE id_tipo_pago = ?1", nativeQuery = true)
	public TipoPagoEntity obtenerTipoPagoPorId(int idTipoPago);

}
