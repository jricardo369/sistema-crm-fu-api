package com.cargosyabonos.application.port.in;

import java.util.Date;
import java.util.List;

import com.cargosyabonos.domain.CitasDeUsuario;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.SchedulersActivasDeSolicitud;

public interface EventoSolicitudUseCase {
	
	public List<EventoSolicitudEntity> obtenerEventosSolicitudes(int idSolicitud,int idUsuario); 
	public void crearEventoSolicitud(EventoSolicitud es,int idUsuarioSchedule);
	public void eliminarEventoSolicitud(int idEvento);
	public List<CitasDeUsuario> citasDeUsuarioEntrevistador(int idUsuario,Date fi,Date ff);
	public List<Integer> solicitudesConTipoImportant(String tipoEvento);
	public void actualizarEstatusSchedule(int idEvento,String estatusSchedule,int idUsuario,String motivo);
	public List<SchedulersActivasDeSolicitud> obtenerSchedulesActivasDeSolicitud(int idSolicitud,int idUsuario);
	public List<EventoSolicitud> obtenerEventosDash(String fechai, String fechaf, int tileDash,int idUsuario,int usuario);

}