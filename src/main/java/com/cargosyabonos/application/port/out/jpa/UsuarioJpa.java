package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargosyabonos.domain.UsuarioEntity;

@Repository
public interface UsuarioJpa extends CrudRepository<UsuarioEntity, Serializable> {

	public List<UsuarioEntity> findAll();
	
	public UsuarioEntity findByIdUsuario(int idUsuario);
	
	public UsuarioEntity findByUsuario(String usuario);
	
	public UsuarioEntity findByNombre(String nombre);
	
	public UsuarioEntity findBycorreoElectronico(String correo);
	
	@Query(value = "SELECT * FROM usuario WHERE estatus NOT IN(4)", nativeQuery = true)
	public List<UsuarioEntity> obtenerTodosUsuariosActivos();
	
	@Query(value = "SELECT * FROM usuario WHERE usuario = ?1", nativeQuery = true)
	public UsuarioEntity encontrarUsuario(String usuario);
	
	@Query(value = "SELECT * FROM usuario WHERE nombre = ?1", nativeQuery = true)
	public UsuarioEntity encontrarPorNombre(String nombre);
	
	@Query(value = "SELECT id_usuario FROM usuario WHERE nombre like %?1%", nativeQuery = true)
	public List<Integer> encontrarUsuariosPorNombre(String nombre);

	@Query(value = "SELECT * FROM usuario WHERE usuario = ?1 AND contrasenia = ?2", nativeQuery = true)
	public UsuarioEntity autenticar(String usuario, String passwd);
	
	@Query(value = "SELECT * FROM usuario WHERE id_sociedad = ?1 AND estatus NOT IN(4)", nativeQuery = true)
	public List<UsuarioEntity> obtenerUsuariosPorSociedad(int idSociedad);
	
	@Query(value = "SELECT * FROM usuario WHERE id_rol = ?1 AND estatus NOT IN(4) LIMIT 1", nativeQuery = true)
	public UsuarioEntity obtenerUsuarioPorRol(int idRol);
	
	@Query(value = "SELECT * FROM usuario WHERE id_rol = ?1 AND revisor = ?2 AND estatus NOT IN(4)", nativeQuery = true)
	public List<UsuarioEntity> obtenerUsuariosPorRol(int idRol,int revisor);
	
	@Query(value = "SELECT * FROM usuario WHERE id_rol = 11 AND estatus NOT IN(4) OR usuario = 'amanda' ", nativeQuery = true)
	public List<UsuarioEntity> obtenerUsuariosParaAssignedClinician();
	
	@Query(value = "SELECT * FROM usuario WHERE id_rol IN(:roles) AND estatus NOT IN(4)", nativeQuery = true)
	public List<UsuarioEntity> obtenerUsuarioPorRoles(@Param("roles") List<Integer> roles);
	
	@Query(value = "SELECT u.id_usuario,u.usuario,u.contrasenia,CONCAT(u.nombre ,' (',r.nombre,')') as nombre,u.sexo,u.correo_electronico,u.telefono,u.direccion,u.pais,u.intentos,u.ciudad,u.estatus, "
		+"u.edad,u.id_rol,u.fecha_creacion,u.resumen,u.ausencia,u.revisor,u.licencia,u.licencia_valida,u.disponibilidad,u.color,u.rate,u.image,u.with_supervision,u.supervisor,u.unpaid_voc_flag "
		+"FROM usuario u "
		+"JOIN rol r ON r.id_rol = u.id_rol WHERE u.id_rol IN(:roles) AND u.estatus NOT IN(4) ORDER BY u.id_rol ASC", nativeQuery = true)
	public List<UsuarioEntity> obtenerUsuarioPorRolesConDesc(@Param("roles") List<Integer> roles);
	
	@Transactional
	@Query(value = "UPDATE usuario SET estatus = ?1 where id_usuario = ?2", nativeQuery = true)
	@Modifying
	public void actualizarEstatusUsuario(int estatus,int idUsuario);
	
	@Transactional
	@Query(value = "UPDATE usuario SET estatus = 1, intentos = 0 WHERE id_usuario = ?1", nativeQuery = true)
	@Modifying
	public void desbloquearUsuario(int idUsuario);
	
	@Transactional
	@Query(value = "UPDATE usuario SET image = ?1 where id_usuario = ?2", nativeQuery = true)
	@Modifying
	public void actualizarImageUsuario(String image,int idUsuario);

}
