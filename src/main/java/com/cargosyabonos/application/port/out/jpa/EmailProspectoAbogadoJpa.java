package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargosyabonos.domain.EmailProspectoAbogadoEntity;

@Repository
public interface EmailProspectoAbogadoJpa extends CrudRepository<EmailProspectoAbogadoEntity, Serializable> {

	public List<EmailProspectoAbogadoEntity> findAll();
	
	@Query(value = "SELECT * FROM email_prospecto_abogado WHERE id_pros_abogado = ?1", nativeQuery = true)
	public List<EmailProspectoAbogadoEntity> obtenerEmailsProspectoAbogado(int idProspectoAbogado);
	
	@Query(value = "SELECT * FROM email_prospecto_abogado WHERE id_email_pros_abo = ?1", nativeQuery = true)
	public EmailProspectoAbogadoEntity findByIdEmailProsAbo(int idEmailProspectoAbogado);
	
	@Query(value = "SELECT * FROM email_prospecto_abogado WHERE email = ?1", nativeQuery = true)
	public EmailProspectoAbogadoEntity obtenerEmailProspectoAbogado(String email);
	
	@Transactional
	@Query(value = "DELETE FROM email_prospecto_abogado  WHERE id_pros_abogado = ?1", nativeQuery = true)
	@Modifying
	public void eliminarEmailByIdProspectoAbogado(int idProspectoAbogado);
    
}
