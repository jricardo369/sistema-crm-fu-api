package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.NotaCitaEntity;

public interface NotaCitaUseCase {
	
	public List<NotaCitaEntity> obtenerNotasCitas(int idCita); 
	public NotaCitaEntity obtenerNotaDeCita(int idCita); 
	public NotaCitaEntity obtenerNota(int idNota);
	public void crearNotaCita(NotaCitaEntity a);
	public void actualizarNotaCita(NotaCitaEntity a);
	public void eliminarNotaCita(int idNota,int idUsuario);

}
