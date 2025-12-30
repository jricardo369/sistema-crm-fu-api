package com.cargosyabonos.application.port.in;

import java.util.List;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.EventoSolicitudVocEntity;

public interface EventoSolicitudVocUseCase {
	
	public List<EventoSolicitudVocEntity> obtenerEventosSolicitudes(int idSolicitud,int idUsuario); 
	public void crearEventoSolicitud(EventoSolicitud es);
	public void eliminarEventoSolicitud(int idEvento);
	public List<Integer> solicitudesConTipoImportant(String tipoEvento);

}