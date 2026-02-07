package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.Cita;
import com.cargosyabonos.domain.DisponibilidadTodoDeUsuario;
import com.cargosyabonos.domain.DisponibilidadUsuario;
import com.cargosyabonos.domain.DisponibilidadUsuarioEntity;

public interface DisponibilidadUsuarioPort {
	
	public List<DisponibilidadUsuarioEntity> obtenerDisponibilidadUsuario(int idUsuario); 
	public List<DisponibilidadUsuario> obtenerDisponibilidadUsuarioPorFecha(String fecha,int rol,int idSolicitud,boolean fechaAnterior,String estado);
	//public List<DisponibilidadUsuario> obtenerDisponibilidadUsuarioPorFechaConAnteriores(String fecha,int rol,int idSolicitud);
	public DisponibilidadUsuarioEntity obtenerDisponibilidadPorId(int idDisponibilidad); 
	public void crearDisponibilidadUsuario(DisponibilidadUsuarioEntity es);
	public void actualizarDisponibilidadUsuario(DisponibilidadUsuarioEntity es);
	public void eliminarDisponibilidadUsuario(int idDisponibilidad);
	public int obtenerDisponibilidadPorTodo(String fecha,String hora,String tipo,int idUsuario);
	public List<DisponibilidadTodoDeUsuario> obtenerDisponibilidadTodoDeUsuario(int idUsuario);
	public List<Cita> obtenerDisponibilidadesTodosUsuarios(String fecha,int idUsuario, String idRol,String estado);

}