package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.CargosCitasVoc;
import com.cargosyabonos.domain.CitaEntity;
import com.cargosyabonos.domain.CitaSql;

public interface CitaPort {
	
	public List<CitaEntity> obtenerTodasLasCitasSemana(String fecha, int noShow);
	public List<CitaEntity> obtenerTodasCitasPorFecha(String fecha);
	public List<CitaSql> obtenerCitasPorSolicitud(int idSolicitud); 
	public List<CitaSql> obtenerCitasPorSolicitudSinNoShow(int idSolicitud); 
	public List<CitaEntity> obtenerCitasDeUsuarioPorFecha(int idUsuario,String fecha);
	public List<CitaEntity> obtenerCitasDeUsuarioPorSemana(int idUsuario,String fecha, int noShow);
	public CitaEntity obtenerCita(int idCita);
	public void crearCita(CitaEntity a);
	public void actualizarCita(CitaEntity a);
	public void eliminarCita(CitaEntity a);
	public void actualizarNoShow(boolean noShow,int idCita);
	public List<CargosCitasVoc> obtenerCargosPendientesFiltro(String fechai, String fechaf, int idUsuario, String campo, String valor,
			String tipo, String rol);
	public void actualizarPagado(boolean pagado,String fechPagado,int idCita);
	
	public List<CitaSql> obtenerCitasDeUsuarioPorSemana(String fecha,int idUsuario,int noShow);

}
