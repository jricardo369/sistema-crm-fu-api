package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.cargosyabonos.domain.NotaProspectoAbogadoEntity;

@Repository
public interface NotaProspectoAbogadoJpa extends CrudRepository<NotaProspectoAbogadoEntity, Serializable>{

    public List<NotaProspectoAbogadoEntity> findAll();

	@Query(value = "SELECT * FROM nota_prospecto_abogado WHERE id_nota = ?1", nativeQuery = true)
	public NotaProspectoAbogadoEntity obtenerNotaPorId(int idNota);

	@Query(value = "SELECT * FROM nota_prospecto_abogado WHERE id_prospecto_abogado = ?1 ORDER BY id_nota DESC", nativeQuery = true)
	public List<NotaProspectoAbogadoEntity> obtenerNotasDeProspectoAbogado(int idProspectoAbogado);

}
