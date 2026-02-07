package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.CargosCitasVoc;
import com.cargosyabonos.domain.Cita;
import com.cargosyabonos.domain.CitaEntity;

public interface CitaUseCase {
	
	public List<Cita> obtenerTodasLasCitasSemana(String fecha);
	public List<Cita> obtenerTodasCitasPorFecha(String fecha);
	public List<Cita> obtenerCitasPorSolicitud(int idSolicitud); 
	public List<Cita> obtenerCitasDeUsuarioPorFecha(int idUsuario,String fecha);
	public List<Cita> obtenerCitasDeUsuarioPorSemana(int idUsuario,String fecha,String filtro,boolean disponibilidad,String idRol,String estatusCita,String estado);
	public void crearCita(CitaEntity a);
	public void actualizarCita(CitaEntity a);
	public void actualizarNoShow(int idCita,int idUsuario,String motivo);
	public void eliminarCita(int idCita,int idUsuario);
	public List<CargosCitasVoc> obtenerCargosPendientes(String fechai, String fechaf, int idUsuario, String campo, String valor,
			String tipo);
	public void actualizarPagado(boolean pagado,int idCita,int idUsuario);
	
	
	public List<Cita> obtenerCitasDeUsuarioPorSemanaV2(String fecha,int idUsuario,int noShow);

	public boolean envioRecordatorio(int idEvento);
	public boolean envioRecordatorios(String fecha,int idUsuario);

}
