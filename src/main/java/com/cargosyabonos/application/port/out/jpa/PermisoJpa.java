package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.PermisoUsuarioEntity;
@Repository
public interface PermisoJpa extends CrudRepository<PermisoUsuarioEntity, Serializable> {

	public List<PermisoUsuarioEntity> findAll();

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO permiso_has_usuario(permiso_id,usuario_id) VALUES(?1,?2)", nativeQuery = true)
	public void insertarPermiso(int idPermiso,int idUsuario);
	
	@Query(value = "SELECT pe.id_permiso as id,pe.nombre as nombre "
				 + "FROM permiso_has_usuario p JOIN  permiso pe on pe.id_permiso = p.permiso_id "
				 + "WHERE p.usuario_id = ?1", nativeQuery = true)
	public List<PermisoUsuarioEntity> obtenerPermisosDeUsuario(int idUsuario);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM permiso_has_usuario WHERE usuario_id = ?1", nativeQuery = true)
	public void eliminarPermisosDeUsuario(int idUsuario);

}
