package com.cargosyabonos.application.port.in;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.domain.NumeroCasosSolicitudes;
import com.cargosyabonos.domain.ReporteHorasMensualVoc;
import com.cargosyabonos.domain.ReporteHorasMesTerapeuta;
import com.cargosyabonos.domain.ReporteNotasCitasTerapeutasDetalleVoc;
import com.cargosyabonos.domain.ReporteNotasCitasTerapeutasVoc;
import com.cargosyabonos.domain.ReporteNotasCitasVoc;
import com.cargosyabonos.domain.SolicitudVoc;
import com.cargosyabonos.domain.SolicitudVocEntity;

public interface SolicitudVocUseCase {
	
	//public List<SolicitudVoc> obtenerSolicitudesDeUsuarioPorFechayEstatus(int idUsuario,Date fechai,Date fechaf,String estatus);	
	//public List<SolicitudVoc> obtenerSolicitudesFiltro(int idUsuario,String campo,String valor,String fecha1,String fecha2);
	public List<SolicitudVoc> obtenerSolicitudes(int idUsuario, int estatus,int idSolicitud,String fechai,String fechaf,String ordenarPor,String orden,String campo,String valor,boolean myFiles,String cerradas);
	public List<SolicitudVoc> obtenerSolicitudesActivasTerapeuta(int idUsuario);
	public SolicitudVocEntity obtenerSolicitud(int idRequest);
	public SolicitudVoc obtenerSolicitudObj(int idRequest,int idUsuario);
	public void crearSolicitud(SolicitudVoc r,int usuario);
	public SolicitudVocEntity crearSolicitudMasivo(SolicitudVoc r,int usuario);
	public void actualizarSolicitud(SolicitudVoc r);
	public void eliminarSolicitud(int r);
	public void actualizarEstatusSolicitud(int idSolicitud, int idEstatus,int idUsuario,boolean closed);
	public void reasginar(int idUsuario,String motivo,int idSolicitud,int idUsuarioEnvio);
	public void actualizarUsuarioTerapeuta(int idUsuario,int idSolicitud,int idUsuarioEntrada);
	public List<SolicitudVoc> obtenerSolicitudesFiltroV2(int idUsuario, String campo,String valor,String fecha1,String fecha2);
	public List<NumeroCasosSolicitudes> obtenerNumerosCaso(String numeroCaso);
	public void syncNumSesiones(int idSolicitud, int idUsuario);
	public List<ReporteHorasMesTerapeuta> obtenerReporteHorasMesTerapeuta(int idTerapeuta, int anio, int mes);
	public List<ReporteHorasMensualVoc> obtenerReporteHorasMensualVoc(int idTerapeuta, int anio, int mes);
	public List<ReporteNotasCitasVoc> obtenerReporteNotasCitasRangoFechas(String fechai, String fechaf);
	public List<ReporteNotasCitasTerapeutasVoc> obtenerReporteNotasCitasTerapeutasRangoFechas(String fechai, String fechaf, Integer idUsuario);
	public List<ReporteNotasCitasTerapeutasDetalleVoc> obtenerReporteNotasCitasTerapeutasDetalleRangoFechas(String fechai, String fechaf, int idUsuario);

}
