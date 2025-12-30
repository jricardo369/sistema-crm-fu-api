package com.cargosyabonos.adapter.out.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.application.port.out.jpa.EventoSolicitudJpa;
import com.cargosyabonos.domain.Cita;
import com.cargosyabonos.domain.CitasUsuario;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.ReporteSolsDeUsuario;
import com.cargosyabonos.domain.ReporteSolsUsuario;
import com.cargosyabonos.domain.SchedulersActivasDeSolicitud;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
public class EventoRepository implements EventoSolicitudPort {

	@Autowired
	EventoSolicitudJpa eSolJpa;
	
	@Autowired
	UsuariosPort uPort;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<EventoSolicitudEntity> obtenerEventosSolicitudes(int idSolicitud) {
		return eSolJpa.obtenerEventosDesolicitud(idSolicitud);
	}
	
	@Override
	public List<EventoSolicitudEntity> obtenerEventosSolicitudesSinPagos(int idSolicitud) {
		return eSolJpa.obtenerEventosDesolicitudSinPagos(idSolicitud);
	}

	@Override
	public EventoSolicitudEntity crearEventoSolicitud(EventoSolicitudEntity es) {
		return eSolJpa.save(es);
	}

	@Override
	public void eliminarEventoSolicitud(int idEvento) {
		eSolJpa.deleteById(idEvento);
	}

	@Override
	public boolean tieneEventoLaSolicitud(int idSolicitud,String evento){
		boolean tieneEvento = false;
		int tieneEventoInt = eSolJpa.tieneEventoLaSolicitud(idSolicitud, evento);
		if(tieneEventoInt != 0){
			tieneEvento = true;
		}
		return tieneEvento;
	}
	
	@Override
	public boolean tieneTipoEventoLaSolicitud(int idSolicitud,String evento){
		boolean tieneEvento = false;
		int tieneEventoInt = eSolJpa.tieneTipoEventoLaSolicitud(idSolicitud, evento);
		if(tieneEventoInt != 0){
			tieneEvento = true;
		}
		return tieneEvento;
	}
	
	@Override
	public void ingresarEventoDeSolicitud(String evento,String descripcion,String tipo,String usuario,SolicitudEntity solicitud){
		EventoSolicitudEntity e = new EventoSolicitudEntity();
		e.setEvento(evento);
		e.setDescripcion(descripcion);
		e.setFecha(new Date());
		e.setTipo(tipo);
		e.setUsuario(usuario);
		e.setSolicitud(solicitud);
		crearEventoSolicitud(e);
	}

	@Override
	public List<Integer> solicitudesConTipoImportant(String tipoEvento) {
		return eSolJpa.solicitudesConTipoImportant(tipoEvento);
	}

	@Override
	public EventoSolicitudEntity ingresarEventoScheduleDeSolicitud(String envento, String descripcion, String tipo, String usuario,
			Date fechaSchedule, String horaSchedule, String tipoSchedule, String usuarioSchedule,
			SolicitudEntity solicitud,String zonaHoraria) {
		EventoSolicitudEntity e = new EventoSolicitudEntity();
		e.setEvento(envento);
		e.setDescripcion(descripcion);
		e.setFecha(new Date());
		e.setTipo(tipo);
		e.setUsuario(usuario);
		e.setSolicitud(solicitud);
		e.setFechaSchedule(fechaSchedule);
		e.setHoraSchedule(horaSchedule);
		e.setTipoSchedule(tipoSchedule);
		e.setUsuarioSchedule(usuarioSchedule);
		e.setEstatusSchedule("1");
		e.setTimeZoneSchedule(zonaHoraria);
		return crearEventoSolicitud(e);
		
	}
	
	@Override
	public EventoSolicitudEntity obtenerUltimoScheduleInt(int idSolicitud,String estatus){
		return eSolJpa.obtenerUltimoScheduleInt(idSolicitud, estatus);
	}
	
	@Override
	public EventoSolicitudEntity obtenerUltimoScheduleInterviewByUser(int idSolicitud,String estatus,int idUsuaroSchedule){
		return eSolJpa.obtenerUltimoScheduleInterviewByUser(idSolicitud, estatus,idUsuaroSchedule);
	}

	@Override
	public EventoSolicitudEntity obtenerUltimoScheduleInterviewClinicianByUser(int idSolicitud,String estatus,int idUsuaroSchedule){
		return eSolJpa.obtenerUltimoScheduleInterviewClinicianByUser(idSolicitud, estatus,idUsuaroSchedule);
	}
	
	@Override
	public EventoSolicitudEntity obtenerUltimoScheduleScales(int idSolicitud,String estatus){
		return eSolJpa.obtenerUltimoScheduleScale(idSolicitud, estatus);
	}
	
	@Override
	public EventoSolicitudEntity obtenerUltimoScheduleClinician(int idSolicitud,String estatus){
		return eSolJpa.obtenerUltimoScheduleClinician(idSolicitud, estatus);
	}
	
	@Override
	public String saberQueSchedulesInterviewSeTienen(int idSolicitud){
		return eSolJpa.saberQueSchedulesInterviewSeTienen(idSolicitud);
	}

	@Override
	public void actualizarEventoSolicitud(EventoSolicitudEntity es) {
		eSolJpa.save(es);
	}

	@Override
	public List<ReporteSolsUsuario> obtenerNumeroSolicitudesAtendidasPorEntrevistadores(String tipoEvento,
			String estatusSchedule, Date fi, Date ff) {
		return eSolJpa.obtenerNumeroSolicitudesAtendidasPorEntrevistadores(tipoEvento, estatusSchedule, fi, ff);
	}
	
	@Override
	public List<ReporteSolsDeUsuario> obtenerSolicitudesAtendidasPorEntrevistador(Date fi, Date ff,int idUsuario){
		return eSolJpa.obtenerSolicitudesAtendidasPorEntrevistador(fi, ff, idUsuario);
	}

	@Override
	public List<CitasUsuario> citasDeUsuarioEntrevistador(int idUsuario,Date fi,Date ff) {
		return eSolJpa.citasDeUsuario(idUsuario,fi,ff);
	}

	@Override
	public List<EventoSolicitudEntity> obtenerSchedulesPendientesPorFecha(Date fecha) {
		return eSolJpa.obtenerSchedulesPendientesPorFecha(fecha);
	}
	
	@Override
	public List<EventoSolicitudEntity> obtenerEventosScheduleDeFechaPorUsuario(String fecha,String hora,String tipo,int idUsuario){
		return eSolJpa.obtenerEventosScheduleDeFechaPorUsuario(fecha, hora,tipo,idUsuario);
	}

	@Override
	public int obtenerSolicitudesSiTieneAlgunEventoDeSolicitud(String tipoEvento, int idSolicitud) {
		return eSolJpa.solicitudesSiTieneAlgunEventoDeSolicitud(tipoEvento, idSolicitud);
	}

	@Override
	public int existeEventoSchedule(String fecha, String hora, String tipoSchedule, int idUsuario,
			String estatusSchedule) {
		return eSolJpa.existeEventoSchedule(fecha, hora, tipoSchedule, idUsuario,estatusSchedule);
	}

	@Override
	public EventoSolicitudEntity findByIdEvento(int idEvento) {
		return eSolJpa.obtenerEventoPorId(idEvento);
	}

	@Override
	public List<SchedulersActivasDeSolicitud> obtenerSchedulesActivasDeSolicitud(int idSolicitud,int idUsuario) {
		if(idUsuario != 0){
			return eSolJpa.obtenerSchedulesActivasDeUsuario(idUsuario,idSolicitud);
		}else{
			return eSolJpa.obtenerSchedulesActivasDeSolicitud(idSolicitud);
		}
		
	}
	
	@Override
	public List<Cita> obtenerCitasInterviewer(String fecha,int idUsuario,String estatusCita) {

		List<Cita> result = null;
		List<Object[]> rows = obtenerCitasInterviewers(fecha,idUsuario,estatusCita);
		result = new ArrayList<>(rows.size());

		for (Object[] row : rows) {
			Cita du = convertirACitaEntrevista(row);
			result.add(du);
		}

		return result;
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
		o.setFinSchedule(Integer.valueOf(row[7].toString()) == 0 ? false : true);
		o.setDescEst((String) row[8]);
		o.setIdEstaSol((Integer) row[9]);
		o.setImportante((String) row[10]);
		
		return o;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerCitasInterviewers(String fecha, int idUsuario,String estatusCita) {

		StringBuilder sb = new StringBuilder();

		sb.append("SELECT  DATE_FORMAT(e.fecha_schedule,'%Y-%m-%d') as fecha, e.hora_schedule as hora, e.tipo_schedule as tipo, e.id_solicitud as solicitud,e.usuario_schedule as usuario, u.nombre as nombreUsuario,u.color as color,"+"\n"
			+ "e.fin_schedule as finSchedule,es.descripcion  as descEst,es.id_estatus_solicitud as estSol,s.importante  "+"\n"
			+ "FROM evento_solicitud e " +"\n"
			+ "JOIN usuario u ON u.id_usuario = usuario_schedule "+"\n"
			+ "LEFT JOIN solicitud s ON s.id_solicitud = e.id_solicitud "+"\n"
			+ "LEFT JOIN estatus_solicitud es ON es.id_estatus_solicitud = s.id_estatus_solicitud "+"\n"
			+ "WHERE e.tipo = 'Schedule' and YEARWEEK(`fecha_schedule`,1) = YEARWEEK(:fecha,1) "+"\n"
			+ "AND e.estatus_schedule = 1  "+"\n");

		if (idUsuario != 0) {
			sb.append(" AND id_usuario  = " + idUsuario +"\n" );
		}

		if (!estatusCita.equals("")) {
			if(estatusCita.equals("Attended")){
				sb.append(" AND e.fin_schedule  = 1" + "\n" );
			}
			if(estatusCita.equals("Not Attended")){
				sb.append(" AND e.fin_schedule   = 0" + "\n" );
			}
			
		}

		sb.append("ORDER BY e.fecha_schedule asc, e.tipo_schedule asc, e.hora_schedule ASC "+"\n");

		UtilidadesAdapter.pintarLog("query:"+"\n"  + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

	    query.setParameter("fecha", fecha);
	    
	    List<Object[]> rows = query.getResultList();
		return rows;

	}

	@Override
	public void actualizarFinSchedule(String finSchedule,int idEvento) {
		eSolJpa.actualizarFinSchedule(finSchedule,idEvento);
	}
	
	@Override
	public void actualizarFinScheduleDeSolicitud(int idSolicitud) {
		eSolJpa.actualizarFinScheduleDeSolicitud(idSolicitud);
	}
	
	private EventoSolicitud convertirAEventosSolicitud(Object[] row) {
		EventoSolicitud s = new EventoSolicitud();
		s.setFecha((String) row[0]);
		s.setUsuario((String) row[1]);
		s.setIdSolicitud((Integer) row[2]);
		s.setDescripcion((String) row[3]);
		s.setUsuarioCreacion((String) row[4]);
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerEventosDashBD(String fechai, String fechaf, int tileDash,int usuario) {
		
		fechai = fechai + " 00:00:00";
		fechaf = fechaf + " 23:59:59";

		UtilidadesAdapter.pintarLog("ejecutando query anios");
		StringBuilder sb = new StringBuilder();
		StringBuilder sbW = new StringBuilder();
		
		String byUsuario = "";
		
		if(usuario > 0){
			
			UsuarioEntity usE = uPort.buscarPorId(usuario);
			if(usE.getRol().equals("4")){
				byUsuario = " u.id_usuario = "+usuario+" ";
			}
			if(usE.getRol().equals("5")||usE.getRol().equals("11")){
				byUsuario = " u.id_usuario = "+usuario+" ";
			}
			if(usE.getRol().equals("7")){
				byUsuario = " u.id_usuario = "+usuario+" ";
			}
			if(usE.getRol().equals("8")){
				byUsuario = " u.id_usuario = "+usuario+" ";
			}
			
		}

		sb.append("SELECT DATE_FORMAT(e.fecha, '%Y-%m-%d') AS fecha,u.usuario,e.id_solicitud,e.descripcion,ec.usuario AS usuario_creacion "
				+ "FROM evento_solicitud e "
				+ "LEFT JOIN usuario u on u.usuario = e.usuario "
				+ "LEFT JOIN evento_solicitud ec "
			    + "ON ec.id_solicitud = e.id_solicitud "
			    + "AND ec.evento = 'Request creation' ");
		
		sb.append("\n "); 

		if (sbW.length() != 0)
			sbW.append(" AND ");
			sbW.append(" e.fecha BETWEEN :fechai AND :fechaf \n");

		if (tileDash == 4) {
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append("  e.descripcion like '%No show%' AND (e.descripcion NOT LIKE '%re-scheduled%' AND e.descripcion NOT LIKE '%was changed%') \n");

		}

		if (tileDash == 5) {
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(" e.evento = 'Reject file' \n");

		}

		if (tileDash == 6) {
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(" e.evento = 'Update' AND e.tipo = 'Reject file' AND e.descripcion NOT LIKE '%re-scheduled%' AND e.descripcion NOT LIKE '%was changed%' AND e.descripcion NOT LIKE '%is changed%' \n");

		}
		
		if(usuario > 0){
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(" "+byUsuario+" ");
		}

		if (sbW.length() != 0) {
			sb.append(" WHERE " + sbW.toString());
		}

		sb.append("\n");

		UtilidadesAdapter.pintarLog("query:\n" + sb.toString().replace(":fechai", "'"+fechai+"'").replace(":fechaf", "'"+fechaf+"'"));

		Query query = entityManager.createNativeQuery(sb.toString());
		
		query.setParameter("fechai", fechai);
		query.setParameter("fechaf", fechaf);

		List<Object[]> rows = query.getResultList();
		return rows;
	}

	@Override
	public List<EventoSolicitud> obtenerEventosDash(String fechai, String fechaf, int tileDash,int idUsuario,int usuario) {
		List<Object[]> rows = null;	
		rows = obtenerEventosDashBD(fechai, fechaf, tileDash,usuario);			
		List<EventoSolicitud> result = null;

		result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirAEventosSolicitud(row));
		}
		
		return result;
	}


}
