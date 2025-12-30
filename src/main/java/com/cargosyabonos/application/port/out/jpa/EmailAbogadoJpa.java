package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargosyabonos.domain.EmailAbogadoEntity;

@Repository
public interface EmailAbogadoJpa extends CrudRepository<EmailAbogadoEntity, Serializable> {

	public List<EmailAbogadoEntity> findAll();
	
	@Query(value = "SELECT * FROM email_abogado WHERE id_abogado = ?1", nativeQuery = true)
	public List<EmailAbogadoEntity> obtenerEmailsAbogado(int idAbogado);
	
	@Query(value = "SELECT * FROM email_abogado WHERE id_email_abo = ?1", nativeQuery = true)
	public EmailAbogadoEntity findByIdEmailAbo(int idEmailAbogado);
	
	@Query(value = "SELECT * FROM email_abogado WHERE email = ?1", nativeQuery = true)
	public EmailAbogadoEntity obtenerEmailAbogado(String email);
	
	@Transactional
	@Query(value = "DELETE FROM email_abogado  WHERE id_abogado = ?1", nativeQuery = true)
	@Modifying
	public void eliminarEmailByIdAbogado(int idAbogado);

}
