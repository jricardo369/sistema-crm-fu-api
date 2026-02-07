package com.cargosyabonos.adapter.out.sql;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.DisponibilidadUsuarioPort;
import com.cargosyabonos.application.port.out.jpa.DisponibilidadUsuarioJpa;
import com.cargosyabonos.application.port.out.jpa.SolicitudJpa;
import com.cargosyabonos.domain.Cita;
import com.cargosyabonos.domain.DisponibilidadTodoDeUsuario;
import com.cargosyabonos.domain.DisponibilidadUsuario;
import com.cargosyabonos.domain.DisponibilidadUsuarioEntity;
import com.cargosyabonos.domain.SolicitudEntity;

@Service
public class DisponibilidadUsuarioRepository implements DisponibilidadUsuarioPort {

	@Autowired
	DisponibilidadUsuarioJpa dispUsJpa;
	
	@Autowired
	SolicitudJpa reqJpa;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Value("classpath:/querys/queryDisponibilidadUsuariosPorRolYEstado.txt")
    private Resource queryDisponibilidadUsuariosPorRolYEstado;

	@Override
	public List<DisponibilidadUsuarioEntity> obtenerDisponibilidadUsuario(int idUsuario) {
		return dispUsJpa.obtenerDisponibilidadDeUsuario(idUsuario);
	}

	@Override
	public void crearDisponibilidadUsuario(DisponibilidadUsuarioEntity es) {
		dispUsJpa.save(es);
	}

	@Override
	public void actualizarDisponibilidadUsuario(DisponibilidadUsuarioEntity es) {
		dispUsJpa.save(es);
	}

	@Override
	public void eliminarDisponibilidadUsuario(int idDisponibilidad) {
		DisponibilidadUsuarioEntity o = dispUsJpa.findByIdDisponibilidad(idDisponibilidad);
		dispUsJpa.delete(o);
	}

	@Override
	public List<DisponibilidadUsuario> obtenerDisponibilidadUsuarioPorFecha(String fecha,int rol,int idSolicitud,boolean fechaAnterior,
		String estado) {
		
		SolicitudEntity sol = null;
		if(idSolicitud > 0){
			sol = reqJpa.findByIdSolicitud(idSolicitud);
		}
		java.util.Date fechaActual = null;
		try {
			fechaActual = UtilidadesAdapter.cadenaAFecha(UtilidadesAdapter.fechaActual());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<Object[]> rows = obtenerDisponbilidadRols(fecha, rol,estado);

		List<DisponibilidadUsuario> result = new ArrayList<>(rows.size());
		boolean hfc = false;
		for (Object[] row : rows) {
			
			DisponibilidadUsuario du = convertirADatosDisponibilidad(row);
			hfc = horarioFueraDeClase(du, fecha,fechaActual);
			
			if (!hfc) {
				if (idSolicitud > 0) {
					if(sol != null){
						if (sol.getEstado() != null) {
							String hora = UtilidadesAdapter.convertirAHoraEstado(
									UtilidadesAdapter.formatearFechaStringAtipoUS(du.getFecha()), du.getHora(),
									du.getTipo(), sol.getEstado(), du.getZonaHoraria());
							du.setTimeFile(hora);
						} else {
							du.setTimeFile(du.getHora() + " " + du.getTipo());
						}
					}else{
						du.setTimeFile(du.getHora() + " " + du.getTipo());
					}

				} else {
					du.setTimeFile("");
				}
				result.add(du);
			}

			if (fechaAnterior) {
				if (idSolicitud > 0) {
					if(sol != null){
						if (sol.getEstado() != null) {
							String hora = UtilidadesAdapter.convertirAHoraEstado(
									UtilidadesAdapter.formatearFechaStringAtipoUS(du.getFecha()), du.getHora(),
									du.getTipo(), sol.getEstado(), du.getZonaHoraria());
							du.setTimeFile(hora);
						} else {
							du.setTimeFile(du.getHora() + " " + du.getTipo());
						}
					}else{
						du.setTimeFile(du.getHora() + " " + du.getTipo());
					}

				} else {
					du.setTimeFile("");
				}
				result.add(du);
			}
			
			
		}
		return result;
	}

	@Override
	public DisponibilidadUsuarioEntity obtenerDisponibilidadPorId(int idDisponibilidad) {
		return dispUsJpa.obtenerDisponibilidadPorId(idDisponibilidad);
	}

	@Override
	public int obtenerDisponibilidadPorTodo(String fecha, String hora, String tipo, int idUsuario) {
		return dispUsJpa.obtenerDisponibilidadPorTodo(fecha, hora, tipo, idUsuario);
	}
	
	@Override
	public List<DisponibilidadTodoDeUsuario> obtenerDisponibilidadTodoDeUsuario(int idUsuario){
		return dispUsJpa.obtenerDisponibilidadTodoDeUsuario(idUsuario);
	}

	
public boolean horarioFueraDeClase(DisponibilidadUsuario d, String fecha,java.util.Date fechaActual) {

		
		boolean salida = false;
		
		try {		
			
			int n =  fechaActual.compareTo(UtilidadesAdapter.cadenaAFecha(fecha));
			if(n != 1){
			
				if(fechaActual.equals(UtilidadesAdapter.cadenaAFecha(fecha))){
				
					String horaActual = "";
					horaActual = UtilidadesAdapter.generarFecha(false, true, false, "", 0, fecha).substring(0, 2);
					
					d.getHora();
					d.getTipo();
					
					String horaClase =  d.getHora().substring(0,2);
					String tipo = d.getTipo();
		
					//UtilidadesAdapter.pintarLog("Tipo:" + tipo);
					//UtilidadesAdapter.pintarLog("Hora Atual:" + horaActual);
					//UtilidadesAdapter.pintarLog("Hora Clase:" + d.getHora().substring(0,2));
		
					if ("PM".equals(tipo)) {
						horaClase = UtilidadesAdapter.convertirHoraA24Hrs(horaClase);
					}
		
					//UtilidadesAdapter.pintarLog("Hora Ingresada despues formato:" + horaActual);
					//UtilidadesAdapter.pintarLog("Hora Clase despues de formato:" + horaClase);
		
					//UtilidadesAdapter.pintarLog(horaActual + " es mayor que " + horaClase);
					
					String horaClaseFinal = UtilidadesAdapter.limpiarCaracteres(horaClase);
					//UtilidadesAdapter.pintarLog("horaClaseFinal:" + horaClaseFinal);
		
					if (Integer.valueOf(horaActual) > Integer.valueOf(horaClaseFinal)) {
						salida = true;
					}
					
				}else{
					salida = false;
				}
			}else if(n == 1){
				salida = true;
			}

			//System.out.println("Clase fuera de horario?:" + salida);
		} catch (ParseException e) {
			e.printStackTrace();
		};
		

		return salida;
	}


	
	public static void main(String args[]){
		DisponibilidadUsuarioRepository a = new DisponibilidadUsuarioRepository();
		DisponibilidadUsuario d = new DisponibilidadUsuario();
		d.setHora("05:30");
		d.setTipo("PM");
		java.util.Date fa = null;
		try {
			fa = UtilidadesAdapter.cadenaAFecha("2024-11-20");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		a.horarioFueraDeClase(d, "2024-11-20",fa);
	}

	@Override
	public List<Cita> obtenerDisponibilidadesTodosUsuarios(String fecha,int idUsuario, String idRol,String estado) {

		List<Cita> result = null;
		List<Object[]> rows = obtenerDispoTodosUsuarios(fecha,idUsuario,idRol,estado);
		result = new ArrayList<>(rows.size());

		for (Object[] row : rows) {
			Cita du = convertirACitaEntrevista(row);
			du.setImportante("");
			result.add(du);
		}

		return result;
	}
	
	private DisponibilidadUsuario convertirADatosDisponibilidad(Object[] row){

		DisponibilidadUsuario d = new DisponibilidadUsuario();
		d.setIdDisponibilidad((Integer) row[0]);
		Date f = (Date) row[1];
		d.setFecha(UtilidadesAdapter.formatearFecha(f));
		d.setHora((String) row[2]);
		d.setTipo((String) row[3]);
		d.setIdUsuario((Integer) row[4]);
		d.setZonaHoraria((String) row[5]);
		d.setRol((String) row[6]);
		d.setResumen((String) row[7]);
		d.setNombreUsuario((String) row[8]);
		
		return d;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerDisponbilidadRols(String fecha, int idRol,String estado) {

		StringBuilder sb = new StringBuilder();
		String queryS = "";
		boolean mostrarSeccionEstado = false;

		mostrarSeccionEstado = idRol == 11;
		UtilidadesAdapter.pintarLog("mostrarSeccionEstado:"+mostrarSeccionEstado);

		try (Scanner scanner = new Scanner(queryDisponibilidadUsuariosPorRolYEstado.getInputStream(), StandardCharsets.UTF_8.name())) {
			queryS = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}

		if(mostrarSeccionEstado) {
			queryS = queryS.replace("$seccionestado", "eu.estado,");
			String seccionEstado = estado.isEmpty() ? "" : " JOIN estado_usuario eu ON eu.id_usuario = u.id_usuario "+"\n";
			queryS = queryS.replace("$joinestado", seccionEstado);

			if (!seccionEstado.isEmpty()) {
				queryS = queryS.replace("$andestado", " AND  eu.estado = " + "'" + estado + "' " );
			} else {
				queryS = queryS.replace("$andestado", "");
			}
		} else {
			queryS = queryS.replace("$seccionestado", "");
			queryS = queryS.replace("$joinestado", "");
			queryS = queryS.replace("$andestado", "");
		}

		if (idRol == 11) {
			queryS = queryS.replace("$androl", " AND u.id_rol = 11 ");
		}
		if (idRol == 5) {
			queryS = queryS.replace("$androl", " AND (u.id_rol = 5 OR u.id_rol = 11) ");
		}
		if (idRol == 8) {
			queryS = queryS.replace("$androl", " AND u.id_rol = 8 ");
		}

		sb.append(queryS);
		
		UtilidadesAdapter.pintarLog("query:"+"\n" + sb.toString().replace(":fecha", "'"+fecha+"'"));

		Query query = entityManager.createNativeQuery(sb.toString());

	    query.setParameter("fecha", fecha);
	    
	    List<Object[]> rows = query.getResultList();
		return rows;

	}	

	private Cita convertirACitaEntrevista(Object[] row){

		Cita o = new Cita();

		o.setFecha((String) row[0]);
		o.setHora((String) row[1]);
		o.setTipo((String) row[2]);
		o.setIdSolicitud((Integer) row[3]);
		o.setIdUsuario((Integer) row[4]);
		o.setNombreUsuario((String) row[5]);
		o.setColor((String) row[6]);
		o.setZonaHoraria((String) row[7]);
		
		return o;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerDispoTodosUsuarios(String fecha, int idUsuario,String idRol,String estado) {

		StringBuilder sb = new StringBuilder();

		String joinEstado = estado.isEmpty() ? "" : " JOIN estado_usuario eu ON eu.id_usuario = u.id_usuario "+"\n";

		sb.append("SELECT DATE_FORMAT(d.fecha,'%Y-%m-%d') as fecha, d.hora, d.tipo, u.id_usuario as solicitud, u.id_usuario as usuario, u.nombre as nombreUsuario, u.color , d.zona_horaria " +"\n" 
				+"FROM disponibilidad_usuario d "  +"\n" 
				+"JOIN usuario u ON u.id_usuario = d.id_usuario "+"\n" 
				+ joinEstado 
				+"LEFT JOIN evento_solicitud e ON e.fecha_schedule = d.fecha " +"\n" 
				+"AND e.hora_schedule = d.hora " +"\n" 
				+"AND e.tipo_schedule = d.tipo " +"\n" 
				+"AND e.usuario_schedule = u.id_usuario " +"\n" 
				+"AND e.estatus_schedule = 1 " +"\n" 
				+"AND YEARWEEK(e.fecha_schedule, 1) = YEARWEEK(:fecha, 1) " +"\n" 
				+"WHERE YEARWEEK(d.fecha, 1) = YEARWEEK(:fecha, 1) " +"\n" 
				+"AND u.estatus != 4 "+"\n" 
				+"AND e.id_evento IS NULL "+"\n" );

		if (idUsuario != 0) {
			sb.append(" AND d.id_usuario = " + idUsuario +"\n" );
		}else{
			sb.append(" AND u.id_rol IN (" + idRol + ") "+"\n" );	
		}

		if (!estado.isEmpty()) {
			sb.append(" AND eu.estado = '" + estado + "' "+"\n" );
		}

		sb.append("GROUP BY d.fecha,d.hora,d.tipo,u.id_usuario,u.nombre,u.color,d.zona_horaria ");

		UtilidadesAdapter.pintarLog("fecha:"+"\n"  + fecha);
		UtilidadesAdapter.pintarLog("query:"+"\n"  + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

	    query.setParameter("fecha", fecha);
	    
	    List<Object[]> rows = query.getResultList();
		return rows;

	}
	
	
}
