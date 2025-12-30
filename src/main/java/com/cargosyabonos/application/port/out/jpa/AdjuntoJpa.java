package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.ArchivoEntity;

@Repository
public interface AdjuntoJpa extends CrudRepository<ArchivoEntity, Serializable> {

	public List<ArchivoEntity> findAll();
	
	public ArchivoEntity findByIdImagen(int idImagen);
	
	@Query(value = "SELECT * FROM adjuntos WHERE id_solicitud = ?1", nativeQuery = true)
	public List<ArchivoEntity> encontrarAdjuntosdeRequest(int idSoliictud);
	
}
