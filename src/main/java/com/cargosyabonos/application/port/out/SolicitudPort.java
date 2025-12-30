package com.cargosyabonos.application.port.out;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.cargosyabonos.domain.CorreosFirmas;
import com.cargosyabonos.domain.DetalleSolsPorFecha;
import com.cargosyabonos.domain.FilesFirmaAbogadoObj;
import com.cargosyabonos.domain.NumFilesAbogados;
import com.cargosyabonos.domain.ReporteAnios;
import com.cargosyabonos.domain.ReporteDash;
import com.cargosyabonos.domain.ReporteSolsDeUsuario;
import com.cargosyabonos.domain.ReporteSolsUsuario;
import com.cargosyabonos.domain.SchedulersSolicitudes;
import com.cargosyabonos.domain.Solicitud;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.TelefonosSolicitudes;
import com.cargosyabonos.domain.UsuarioEntity;

public interface SolicitudPort {
	
	public List<SolicitudEntity> obtenerSolicitudesPorFecha(Date fechai, Date fechaf);
	public List<SolicitudEntity> obtenerSolicitudesPorFiltros(String valor);
	public int obtenerReporteMailsAbogados(String firma);
	public SolicitudEntity obtenerSolicitud(int idRequest);
	public SolicitudEntity crearSolicitud(SolicitudEntity r,int idUsuario,boolean conEvento);
	public SolicitudEntity crearSolicitudMasivo(SolicitudEntity r);
	public void actualizarSolicitud(SolicitudEntity r);
	public void eliminarSolicitud(SolicitudEntity r);
	public List<Solicitud> obtenerSolicitudesDeUsuarioPorQueryV2(UsuarioEntity us, int estatus,int idSolicitud,String fechai,
			String fechaf,String ordenarPor,String orden,String campo,String valor, boolean myFiles,String cerradas,boolean primeraVez, int usuario);
	public void actualizarEstatusSolicitud(int idSolicitud, int idEstatus);
	public int obtenerRevisorConMenorSolicitudes(int revisor);
	public int obtenerUsuarioRevisandoPorRolCoMenosSols(int idRol);
	public List<SchedulersSolicitudes> obtenerSchedulersSolicitudes();
	public List<SchedulersSolicitudes> obtenerSchedulersSolicitud(int idSolicitud);
	public List<SolicitudEntity> obtenerSolicitudesEstatusEnProcesos();
	public List<ReporteSolsUsuario> obtenerNumeroSolicitudesAtendidasPorRevisores(Date fechai, Date fechaf);
	public List<ReporteSolsDeUsuario> obtenerSolicitudesAtendidasPorRevisor(Date fechai, Date fechaf,int idUsuario);
	public List<ReporteSolsUsuario> obtenerNumeroSolicitudesAtendidasPorRevisorAdicional(Date fechai, Date fechaf);
	public List<ReporteSolsUsuario> obtenerSolicitudesClinicians(Date fechai, Date fechaf);
	public List<ReporteSolsDeUsuario> obtenerSolicitudesClinician(Date fechai, Date fechaf,int idUsuario);
	public List<ReporteSolsDeUsuario> obtenerSolicitudesAtendidasPorRevisorAdicional(Date fechai, Date fechaf,int idUsuario);
	public List<ReporteSolsUsuario> obtenerNumeroSolicitudesAtendidasPorTemplates(String fechai, String fechaf);
	public List<ReporteSolsDeUsuario> obteneSolicitudesAtendidasPorTemplate(Date fechai, Date fechaf,int idUsuario);
	public void actualizarUsuarioRevisando(int idUsuarioRevisando,int idSolicitud);
	public void actualizarEstatusPago(int idEstatusPago, int idSolicitud);
	public BigDecimal obtenerSumaAmountPorFechayCliente(Date fechai, Date fechaf,String cliente);
	public List<String> obtenerFirmasAbogados(String fechai, String fechaf);
	public List<NumFilesAbogados> obtenerFirmasAbogadosyNumFiles(String fechai, String fechaf);
	public List<FilesFirmaAbogadoObj> obtenerSolsDeFirmaYFechas(String fechai, String fechaf,String firmaAbogado,int idAbogado, boolean all);
	public int obtenerUsuarioSiEsRevisor(int idRol,int revisor);
	public void actualizarUsuariosRevisores(int idUsuario,int idSolicitud);
	public void actualizarAsignacionTemplate(int asignacionTemplate,int idUsuarioTemp,int idSolicitud);
	public void actualizarAsignacionIntSc(int asignacionIntSc,int idUsuario,int idSolicitud);
	public void actualizarAsignacionClnc(int idUsuario,int idSolicitud);
	public void actualizarAsignacionIntScReset(int idSolicitud);
	public void actualizarAsignacionClncReset(int idSolicitud);
	public void actualizarNumeroEntrevistas(int num,int idSolicitud);
	public void actualizarImportante(String importante,int idSolicitud);
	public void actualizarFinIntSc(String valor,int idSolicitud);
	public void actualizarFinAsgClnc(String valor, int idSolicitud);
	public List<TelefonosSolicitudes> obtenerSolicitudesDeTelefono(String telefono);
	public List<CorreosFirmas> obtenerCorreosAbogados(String firmas);
	public List<ReporteAnios> reporteDeAnio(String anios,String valorFiltro);
	public ReporteDash reporteDash(String fechai, String fechaf,int usuario);
	public void actualizarInterviewMaster(String valor,int idSolicitud);
	public void actualizarIntScalesSolicitud(int idSolicitud);
	public void actualizarInterview(int idUsuario,int idSolicitud);
	public void actualizarAsignacionIntReset(int idSolicitud);
	public void actualizarFinInterview(int finIntIni,int idSolicitud);
	public void actualizarAssignedClinician(int assignedClinician,int idSolicitud);
	public void actualizarFinAsgTmp(String valor, int idSolicitud);
	public List<Solicitud> obtenerSolicitudesDetalleReporteDash(int dash,String fechai, String fechaf,int usuario);
	public List<DetalleSolsPorFecha> reporteDetalleSolsFecha(String fechai, String fechaf,int usuario);
	public void actualizarSolicitudConCupon(int idSolicitud);
	public void actualizarEmailAbo(String emailsAbogado,int idSolicitud);
	public int esFinEntrevistas(int idSolicitud);
	public int esFinEntrevistasWithoutClinician(int idSolicitud);

}
