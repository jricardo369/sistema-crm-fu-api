package com.cargosyabonos.application.port.in;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.domain.DetalleSolsPorFecha;
import com.cargosyabonos.domain.FilesFirmaAbogadoObj;
import com.cargosyabonos.domain.NumFilesAbogados;
import com.cargosyabonos.domain.ReporteComparacionAnios;
import com.cargosyabonos.domain.ReporteContador;
import com.cargosyabonos.domain.ReporteDash;
import com.cargosyabonos.domain.ReporteSolsDeUsuarioObj;
import com.cargosyabonos.domain.Solicitud;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.TelefonosSolicitudes;

public interface SolicitudUseCase {

	public List<ReporteContador> obtenerReporteSolicitudesDeUsuarios(int idUsuario,Date fechai,Date fechaf);
	public List<ReporteSolsDeUsuarioObj> obtenerReporteSolicitudesDeUsuario(Date fechai, Date fechaf,int idUsuario);
	public List<ReporteContador> obtenerReporteMailsAbogados(String fechai,String fechaf);
	public List<Solicitud> obtenerSolicitudesV2(int idUsuario,int estatus,String fechai,String fechaf,String ordenarPor,String orden,String campo,String valor, 
			boolean myFiles,String cerradas, boolean primeraVez,int usuario);
	public SolicitudEntity obtenerSolicitud(int idRequest);
	public Solicitud obtenerSolicitudObjV2(int idRequest,int idUsuario);
	public void crearSolicitud(Solicitud r,int usuario);
	public void actualizarSolicitud(Solicitud r,String estatus,int idUsuario);
	public void eliminarSolicitud(int r);
	public int obtenerRevisorConMenorSolicitudes();
	public void actualizarEstatusSolicitud(int idSolicitud, int idEstatus,int idUsuario,boolean closed,String motivo);
	public void envioSiguienteProceso(int idSolicitud,int idUsuarioCambio,int idDisponibilidad,boolean fechaAnterior);
	public void envioInterviewerCaseManager(int idSolicitud, int idUsuarioCambio, int idDisponibilidad,boolean fechaAnterior);
	public void envioInterviewerScales(int idSolicitud, int idUsuarioCambio, int idDisponibilidad,boolean fechaAnterior);
	public void envioInterviewerClinician(int idSolicitud, int idUsuarioCambio, int idDisponibilidad,boolean fechaAnterior);
	public void envioFinEntrevistaCaseManager(int idSolicitud,int idUsuarioCambio);
	public void envioFinEntrevistaClinician(int idSolicitud,int idUsuarioCambio);

	public void envioTemplate(int idUsuario,int idSolicitud,int idUsuarioEnvio);

	public void parcheFinEntrevistaClinician(int idSolicitud,int idUsuarioCambio);
	public void envioFinEntrevistaScales(int idSolicitud,int idUsuarioCambio);
	public void envioReadyOnDraft(int idSolicitud,int idUsuario);
	public String cargarExcel(MultipartFile archivo);
	public void rejectSolicitud(int idUsuario,String motivo,int idSolicitud,int idUsuarioEnvio);
	public void noShow(String motivo,int idSolicitud,int idUsuarioEnvio);
	public void cancelTemplate(int idUsuario,String motivo,int idSolicitud,int idUsuarioEnvio);
	public List<TelefonosSolicitudes> obtenerSolicitudesDeTelefono(String telefono);
	public ReporteComparacionAnios reporteDeAnio(int anio, String valorFiltro);
	public ReporteDash reporteDash(String fechai, String fechaf,int usuario);
	public List<String> obtenerTextosOrdenarPor(int idUsuario);
	public List<String> obtenerTextosTipoParaFiltros(int idUsuario);
	public List<NumFilesAbogados> obtenerFirmasAbogadosyNumFiles(String fechai, String fechaf);
	public List<FilesFirmaAbogadoObj> obtenerSolsDeFirmaYFechas(String fechai, String fechaf,String firmaAbogado,int idAbogado,boolean all);
	public List<Solicitud> obtenerSolicitudesDetalleReporteDash(int dash,String fechai, String fechaf,int usuario);
	public List<DetalleSolsPorFecha> reporteDetalleSolsFecha(String fechai, String fechaf,int usuario);
	public void actualizarSolicitudConCupon(int idSolicitud,int idUsuario);
	public void actualizarEmailAbo(String emailsAbogado,int idSolicitud);

}
