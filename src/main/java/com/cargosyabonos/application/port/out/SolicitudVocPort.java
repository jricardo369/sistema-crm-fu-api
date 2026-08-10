package com.cargosyabonos.application.port.out;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.cargosyabonos.domain.NumeroCasosSolicitudes;
import com.cargosyabonos.domain.ReporteHorasMensualVoc;
import com.cargosyabonos.domain.ReporteHorasMesTerapeuta;
import com.cargosyabonos.domain.ReporteNotasCitasTerapeutasDetalleVoc;
import com.cargosyabonos.domain.ReporteNotasCitasTerapeutasVoc;
import com.cargosyabonos.domain.ReporteNotasCitasVoc;
import com.cargosyabonos.domain.ReporteSolsUsuario;
import com.cargosyabonos.domain.SolicitudVoc;
import com.cargosyabonos.domain.SolicitudVocEndingSessions;
import com.cargosyabonos.domain.SolicitudVocEntity;
import com.cargosyabonos.domain.UltimoUsuarioRevisor;
import com.cargosyabonos.domain.UsuarioEntity;

public interface SolicitudVocPort {

	public List<SolicitudVocEntity> obtenerSolicitudesDeUsuario(int idUsuario,int estatus);
	public SolicitudVocEntity obtenerSolicitud(int idRequest);
	public SolicitudVocEntity obtenerSolicitudByNumeroDeCaso(String numeroCaso);
	public SolicitudVocEntity crearSolicitud(SolicitudVocEntity r,int idUsuario,boolean conEvento);
	public void actualizarSolicitud(SolicitudVocEntity r);
	public void eliminarSolicitud(SolicitudVocEntity r);
	public List<SolicitudVocEntity> obtenerTodasLasSolicitudes(int estatus);
	public List<SolicitudVoc> obtenerTodasLasSolicitudesV2(UsuarioEntity us, int estatus,int idSolicitud,String fechai,String fechaf,String ordenarPor,String orden,String campo,String valor,boolean myFiles,String cerradas);
	public List<SolicitudVocEndingSessions> obtenerSolicitudesVOCEndingSessions();
	public List<SolicitudVocEntity> obtenerSolicitudesTerapueta(int idTerapeuta);
	public List<SolicitudVocEntity> obtenerSolicitudesActivasTerapueta(int idTerapeuta);
	public void actualizarEstatusSolicitud(int idSolicitud, int idEstatus);
	public void actualizarUsuarioTerapeuta(int idUsuario,int idSolicitud);
	public void actualizarEstatusPago(int idEstatusPago, int idSolicitud);
	public BigDecimal obtenerSumaAmountPorFechayCliente(Date fechai, Date fechaf,String cliente);
	public void actualizarImportante(String importante,int idSolicitud);
	public UltimoUsuarioRevisor obtenerUsuarioConMenosRevisiones(int idRol);
	public void actualizarNumSesiones(int numSesiones,int numSesPend, int idSolicitud);
	public List<ReporteSolsUsuario> obtenerNumeroSolicitudesDeTemperatura(Date fi,Date ff);
	public List<NumeroCasosSolicitudes> obtenerNumerosCaso(String numeroCaso);
	public int obtenerSesionesDeSolicitud(int idSolicitud);
	public List<SolicitudVoc> obtenerSolsVocPendTratmentPlan();
	public List<ReporteHorasMesTerapeuta> obtenerReporteHorasMesTerapeuta(int idTerapeuta, int anio, int mes);
	public List<ReporteHorasMensualVoc> obtenerReporteHorasMensualVoc(int idTerapeuta, int anio, int mes);
	public List<ReporteNotasCitasVoc> obtenerReporteNotasCitasRangoFechas(String fechai, String fechaf);
	public List<ReporteNotasCitasTerapeutasVoc> obtenerReporteNotasCitasTerapeutasRangoFechas(String fechai, String fechaf, Integer idUsuario);
	public List<ReporteNotasCitasTerapeutasDetalleVoc> obtenerReporteNotasCitasTerapeutasDetalleRangoFechas(String fechai, String fechaf, int idUsuario);
	
	
}
