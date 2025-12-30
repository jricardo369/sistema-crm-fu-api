package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.ArchivoVocEntity;

@Repository
public interface AdjuntoVocJpa extends CrudRepository<ArchivoVocEntity, Serializable> {

	public List<ArchivoVocEntity> findAll();
	
	public ArchivoVocEntity findByIdImagen(int idImagen);
	
	@Query(value = "SELECT * FROM adjuntos_voc WHERE id_solicitud = ?1", nativeQuery = true)
	public List<ArchivoVocEntity> encontrarAdjuntosdeRequest(int idSoliictud);
	
}
