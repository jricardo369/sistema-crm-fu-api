package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.PagoVocEntity;

@Repository
public interface PagoVocJpa extends CrudRepository<PagoVocEntity, Serializable> {

	public List<PagoVocEntity> findAll();

	@Query(value = "SELECT * FROM pagos_voc WHERE id_pago = ?1", nativeQuery = true)
	public PagoVocEntity obtenerPagoPorId(int idPago);

	@Query(value = "SELECT * FROM pagos_voc WHERE id_cita = ?1", nativeQuery = true)
	public List<PagoVocEntity> obtenerPagosPorIdCita(int idCita);

}
