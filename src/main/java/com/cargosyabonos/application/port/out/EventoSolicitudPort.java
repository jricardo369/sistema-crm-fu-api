package com.cargosyabonos.application.port.out;

import java.util.Date;
import java.util.List;

import com.cargosyabonos.domain.Cita;
import com.cargosyabonos.domain.CitasUsuario;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.ReporteSolsDeUsuario;
import com.cargosyabonos.domain.ReporteSolsUsuario;
import com.cargosyabonos.domain.SchedulersActivasDeSolicitud;
import com.cargosyabonos.domain.SolicitudEntity;

public interface EventoSolicitudPort {
	
	public EventoSolicitudEntity findByIdEvento(int idEvento);
	public List<EventoSolicitudEntity> obtenerEventosSolicitudes(int idSolicitud);
	public List<EventoSolicitudEntity> obtenerEventosSolicitudesSinPagos(int idSolicitud);
	public EventoSolicitudEntity crearEventoSolicitud(EventoSolicitudEntity es);
	public void eliminarEventoSolicitud(int idEvento);
	public void actualizarEventoSolicitud(EventoSolicitudEntity es);
	public boolean tieneEventoLaSolicitud(int idSolicitud,String evento);
	public boolean tieneTipoEventoLaSolicitud(int idSolicitud,String evento);
	public List<Integer> solicitudesConTipoImportant(String tipoEvento);
	public int existeEventoSchedule(String fecha, String hora, String tipoSchedule, int idUsuario,String estatusSchedule);
	public int obtenerSolicitudesSiTieneAlgunEventoDeSolicitud(String tipoEvento,int idSolicitud);
	public void ingresarEventoDeSolicitud(String envento,String descripcion,String tipo,String usuario,SolicitudEntity solicitud);
	public EventoSolicitudEntity ingresarEventoScheduleDeSolicitud(String envento,String descripcion,String tipo,String usuario,Date fechaSchedule,
			String horaSchedule,String tipoSchedule,String usuarioSchedule,SolicitudEntity solicitud,String zonaHoraria);
	public EventoSolicitudEntity obtenerUltimoScheduleInt(int idSolicitud,String estatus);
	public EventoSolicitudEntity obtenerUltimoScheduleInterviewByUser(int idSolicitud,String estatus,int idUsuaroSchedule);
	public EventoSolicitudEntity obtenerUltimoScheduleInterviewClinicianByUser(int idSolicitud,String estatus,int idUsuaroSchedule);
	public EventoSolicitudEntity obtenerUltimoScheduleScales(int idSolicitud,String estatus);
	public EventoSolicitudEntity obtenerUltimoScheduleClinician(int idSolicitud,String estatus);
	public String saberQueSchedulesInterviewSeTienen(int idSolicitud);
	public List<ReporteSolsUsuario> obtenerNumeroSolicitudesAtendidasPorEntrevistadores(String tipoEvento, String estatusSchedule, Date fi,Date ff);
	public List<ReporteSolsDeUsuario> obtenerSolicitudesAtendidasPorEntrevistador(Date fi, Date ff,int idUsuario);
	public List<CitasUsuario> citasDeUsuarioEntrevistador(int idUsuario,Date fi,Date ff);
	public List<EventoSolicitudEntity> obtenerSchedulesPendientesPorFecha(Date fecha);
	public List<EventoSolicitudEntity> obtenerEventosScheduleDeFechaPorUsuario(String fecha,String hora,String tipo,int idUsuario);
	public List<SchedulersActivasDeSolicitud> obtenerSchedulesActivasDeSolicitud(int idSolicitud,int idUsuario);
	public List<Cita> obtenerCitasInterviewer(String fecha,int idUsuario, String estatusCita);
	public void actualizarFinSchedule(String finSchedule,int idEvento);
	public void actualizarFinScheduleDeSolicitud(int idSolicitud);	
	public List<EventoSolicitud> obtenerEventosDash(String fechai, String fechaf, int tileDash,int idUsuario,int usuario);

}