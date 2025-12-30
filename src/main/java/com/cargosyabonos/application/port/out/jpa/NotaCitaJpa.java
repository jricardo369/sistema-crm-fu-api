package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.NotaCitaEntity;

@Repository
public interface NotaCitaJpa extends CrudRepository<NotaCitaEntity, Serializable> {

	public List<NotaCitaEntity> findAll();
	
	@Query(value = "SELECT * FROM nota_cita WHERE id_nota = ?1", nativeQuery = true)
	public NotaCitaEntity obtenerPorId(int idNota);
	
	@Query(value = "SELECT * FROM nota_cita WHERE id_cita = ?1 ORDER BY id_nota DESC", nativeQuery = true)
	public List<NotaCitaEntity> obtenerNotasCitas(int idCita);
	
	@Query(value = "SELECT * FROM nota_cita WHERE id_cita = ?1 ORDER BY id_nota DESC", nativeQuery = true)
	public NotaCitaEntity obtenerNotaDeCita(int idCita);
	
	@Query(value = "SELECT s.id_solicitud FROM nota_cita nc "
			+ "LEFT JOIN cita c ON c.id_cita = nc.id_cita "
			+ "LEFT JOIN solicitud_voc s ON s.id_solicitud = c.id_solicitud "
			+ "WHERE nc.id_nota = ?1", nativeQuery = true)
	public int obtenerIdSolByIdNota(int idNota);

}
