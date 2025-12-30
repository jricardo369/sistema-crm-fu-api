package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargosyabonos.domain.AbogadoEntity;

@Repository
public interface AbogadoJpa extends CrudRepository<AbogadoEntity, Serializable> {

	public List<AbogadoEntity> findAll();
	
	public AbogadoEntity findByIdAbogado(int idAbogado);
	
	public AbogadoEntity findByNombre(String nombre);
	
	@Query(value = "SELECT id_abogado,firma,nombre,email,telefono,sinonimos,SUBSTRING(fecha_creacion, 1, 10) as fecha_creacion,cupon,SUBSTRING(fecha_cupon, 1, 10) as fecha_cupon FROM abogado ", nativeQuery = true)
	public List<AbogadoEntity> obtenerTodosAbogadosActivos();
	
	@Query(value = "SELECT * FROM abogado WHERE nombre LIKE %?1% OR sinonimos LIKE %?1% OR email LIKE %?1%" , nativeQuery = true)
	public List<AbogadoEntity> obtenerAbogadosPorNombreYSinonimo(String nombre);
	
	@Query(value = "SELECT a.id_abogado,a.firma,a.nombre,a.telefono,a.sinonimos,a.fecha_creacion,a.cupon,a.fecha_cupon " +
			"FROM abogado a JOIN email_abogado e ON e.id_abogado = a.id_abogado WHERE e.email = ?1", nativeQuery = true)
	public AbogadoEntity obtenerAbogadoByEmail(String email);
	
	@Transactional
	@Query(value = "UPDATE abogado SET cupon = 1, fecha_cupon = CURDATE()  WHERE id_abogado = ?1", nativeQuery = true)
	@Modifying
	public void actualizarCuponAbogado(int idAbogado);
	

}
