package com.cargosyabonos.application.port.in;
 
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.domain.DisponibilidadTodoDeUsuario;
import com.cargosyabonos.domain.DisponibilidadUsuario;
import com.cargosyabonos.domain.DisponibilidadUsuarioEntity;

public interface DisponibilidadUsuarioUseCase {
	
	public List<DisponibilidadUsuarioEntity> obtenerDisponibilidadUsuario(int idUsuario); 
	public List<DisponibilidadUsuario> obtenerDisponibilidadUsuarioPorFecha(String fecha,int rol,boolean fechaAnterior,int idSolicitud,boolean clinician); 
	public void crearDisponibilidadUsuario(DisponibilidadUsuario es);
	public void actualizarDisponibilidadUsuario(DisponibilidadUsuarioEntity es);
	public void eliminarDisponibilidadUsuario(int idDisponibilidad);
	public String cargarExcel(MultipartFile archivo,int idUsuario);
	public List<DisponibilidadTodoDeUsuario> obtenerDisponibilidadTodoDeUsuario(int idUsuario);
	
}