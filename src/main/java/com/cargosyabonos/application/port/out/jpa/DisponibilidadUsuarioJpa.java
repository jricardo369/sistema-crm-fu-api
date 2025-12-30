package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.DatosDisponibilidad;
import com.cargosyabonos.domain.DisponibilidadTodoDeUsuario;
import com.cargosyabonos.domain.DisponibilidadUsuarioEntity;

@Repository
public interface DisponibilidadUsuarioJpa extends CrudRepository<DisponibilidadUsuarioEntity, Serializable> {

	public List<DisponibilidadUsuarioEntity> findAll();
	
	public DisponibilidadUsuarioEntity findByIdDisponibilidad(int idDisponibilidad);

	@Query(value = "SELECT * FROM disponibilidad_usuario WHERE id_usuario = ?1 AND fecha >= curdate() ORDER BY fecha asc, tipo asc, hora asc", nativeQuery = true)
	public List<DisponibilidadUsuarioEntity> obtenerDisponibilidadDeUsuario(int idUsuario);
	
	@Query(value = "SELECT * FROM disponibilidad_usuario WHERE id_disponibilidad = ?1", nativeQuery = true)
	public DisponibilidadUsuarioEntity obtenerDisponibilidadPorId(int idDisponiblidad);
	
	@Query(value = "SELECT count(*) FROM request_evaluation.disponibilidad_usuario where fecha = ?1 and hora = ?2 and tipo = ?3 and id_usuario = ?4", nativeQuery = true)
	public int obtenerDisponibilidadPorTodo(String fecha,String hora,String tipo,int idUsuario);

	
	@Query(value = "SELECT fecha,hora,tipo,nombre,resumen FROM disponibilidad_usuario d JOIN usuario u on u.id_usuario = d.id_usuario WHERE fecha = ?1 ", nativeQuery = true)
	public List<DatosDisponibilidad> obtenerDisponibilidadFecha(String fecha);
	
	@Query(value = "SELECT d.fecha, d.hora,d.tipo,CONCAT(d.fecha,' ',d.hora,' ',d.tipo) AS fechaConcatenada, "
				 +"(SELECT id_solicitud FROM evento_solicitud "
				 +"WHERE usuario_schedule = d.id_usuario and fecha_schedule = d.fecha AND hora_schedule = d.hora AND tipo_schedule = d.tipo AND estatus_schedule = 1) AS solicitud "
				 +"FROM disponibilidad_usuario d "
				 +"WHERE d.id_usuario = ?1 AND d.fecha >= curdate() "
				 +"GROUP BY d.fecha,d.hora,d.tipo "
				 +"ORDER BY d.fecha asc, d.tipo asc, d.hora ASC;  ", nativeQuery = true)
	public List<DisponibilidadTodoDeUsuario> obtenerDisponibilidadTodoDeUsuario(int idUsuario);
	
	/*@Query(value = "SELECT d.fecha, d.hora, d.tipo, u.id_usuario as solicitud, u.id_usuario as usuario, u.nombre as nombreUsuario, u.color "
				+"FROM disponibilidad_usuario d "
				+"JOIN usuario u ON u.id_usuario = d.id_usuario "
				+"LEFT JOIN evento_solicitud e ON e.fecha_schedule = d.fecha "
				+"    AND e.hora_schedule = d.hora "
				+"    AND e.tipo_schedule = d.tipo "
				+"    AND e.usuario_schedule = u.id_usuario "
				+"    AND e.estatus_schedule = 1 "
				+"    AND YEARWEEK(e.fecha_schedule, 1) = YEARWEEK(?1, 1) "
				+"WHERE YEARWEEK(d.fecha, 1) = YEARWEEK(?1, 1) "
				+"    AND u.estatus != 4 "
				+"    AND e.id_evento IS NULL", nativeQuery = true)
	public List<CitaEntrevista> obtenerDisponibilidadesTodosUsuarios(String fecha);
	
	@Query(value = "SELECT d.fecha, d.hora, d.tipo, u.id_usuario as solicitud, u.id_usuario as usuario, u.nombre as nombreUsuario, u.color "
				+"FROM disponibilidad_usuario d "
				+"JOIN usuario u ON u.id_usuario = d.id_usuario "
				+"LEFT JOIN evento_solicitud e ON e.fecha_schedule = d.fecha "
				+"    AND e.hora_schedule = d.hora "
				+"    AND e.tipo_schedule = d.tipo "
				+"    AND e.usuario_schedule = u.id_usuario "
				+"    AND e.estatus_schedule = 1 "
				+"    AND YEARWEEK(e.fecha_schedule, 1) = YEARWEEK(?1, 1) "
				+"WHERE YEARWEEK(d.fecha, 1) = YEARWEEK(?1, 1) AND d.id_usuario = ?2"
				+"    AND u.estatus != 4 "
				+"    AND e.id_evento IS NULL", nativeQuery = true)
public List<CitaEntrevista> obtenerDisponibilidCitasUsuario(String fecha, int idUsuario);*/
	
}
