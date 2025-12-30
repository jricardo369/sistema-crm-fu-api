package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.NotaCitaEntity;

public interface NotaCitaPort {
	
	public List<NotaCitaEntity> obtenerNotasCitas(int idCita); 
	public NotaCitaEntity obtenerNotaCita(int idNota);
	public void crearNotaCita(NotaCitaEntity a);
	public void actualizarNotaCita(NotaCitaEntity a);
	public void eliminarNotaCita(NotaCitaEntity a);
	public NotaCitaEntity obtenerNotaDeCita(int idCita); 
	public int obtenerIdSolByIdNota(int idNota);

}
