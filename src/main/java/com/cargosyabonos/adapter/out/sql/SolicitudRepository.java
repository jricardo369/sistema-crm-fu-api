package com.cargosyabonos.adapter.out.sql;

import com.cargosyabonos.adapter.out.file.PdfCita;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.EstatusPagoPort;
import com.cargosyabonos.application.port.out.EstatusSolicitudPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.application.port.out.jpa.MovimientoJpa;
import com.cargosyabonos.application.port.out.jpa.SolicitudJpa;
import com.cargosyabonos.domain.CorreosFirmas;
import com.cargosyabonos.domain.DetalleSolsPorFecha;
import com.cargosyabonos.domain.EstatusPagoEntity;
import com.cargosyabonos.domain.EstatusSolicitudEntity;
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
import com.cargosyabonos.domain.UltimoUsuarioRevisor;
import com.cargosyabonos.domain.UsuarioEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class SolicitudRepository implements SolicitudPort {

    private static final Logger logger = LoggerFactory.getLogger(SolicitudRepository.class);

	@Autowired
	SolicitudJpa reqJpa;

	@Autowired
	MovimientoJpa movJpa;

	@Autowired
	EventoSolicitudPort ePort;

	@Autowired
	private UsuariosPort uPort;

	@Autowired
	private EstatusSolicitudPort esPort;

	@Autowired
	private EstatusPagoPort esPPort;

	@PersistenceContext
	private EntityManager entityManager;

	@Value("classpath:/querys/querySolicitudes.txt")
    private Resource querySolicitudes;
	
	@Value("classpath:/querys/queryReporteAnios.txt")
    private Resource queryReporteAnios;
	
	@Value("classpath:/querys/queryReporteAniosByEvento.txt")
    private Resource queryReporteAniosByEvento;
	
	@Value("classpath:/querys/queryDetalleSolsAll.txt")
    private Resource queryDetalleSolsAll;
	
	@Value("classpath:/querys/queryNumFilesAbo.txt")
    private Resource queryNumFilesAbo;

	@Value("classpath:/querys/queryReporteDash.txt")
    private Resource queryReporteDash;

	@Value("classpath:/querys/queryDetallesDash.txt")
    private Resource queryDetallesDash;

	@Value("classpath:/querys/whereDash1.txt")
    private Resource whereDash1;

	@Value("classpath:/querys/whereDash2.txt")
    private Resource whereDash2;

	@Value("classpath:/querys/whereDash3.txt")
    private Resource whereDash3;

	@Override
	public List<SolicitudEntity> obtenerSolicitudesPorFecha(Date fechai, Date fechaf) {
		return reqJpa.obtenerSolicitudesPorFecha(fechai, fechaf);
	}

	@Override
	public SolicitudEntity obtenerSolicitud(int idRequest) {
		return reqJpa.findByIdSolicitud(idRequest);
	}

	@Override
	public SolicitudEntity crearSolicitud(SolicitudEntity r, int idUsuario, boolean conEvento) {
		SolicitudEntity e = reqJpa.save(r);
		UsuarioEntity u = uPort.buscarPorId(idUsuario);
		if (idUsuario != 0) {
			if (conEvento) {
				ePort.ingresarEventoDeSolicitud("Request creation", "The request was created", "Info", u.getUsuario(),
						e);
			}
		}
		return e;
	}

	@Override
	public SolicitudEntity crearSolicitudMasivo(SolicitudEntity r) {
		SolicitudEntity e = reqJpa.save(r);
		return e;
	}

	@Override
	public void actualizarSolicitud(SolicitudEntity r) {
		reqJpa.save(r);
	}

	@Override
	public void eliminarSolicitud(SolicitudEntity r) {
		reqJpa.delete(r);
	}

	@Override
	public int obtenerRevisorConMenorSolicitudes(int revisor) {

		UltimoUsuarioRevisor ur = reqJpa.obtenerRevisor(revisor);
		int tamRevisor = ur.getUsuario();

		return tamRevisor;
	}

	@Override
	public void actualizarEstatusSolicitud(int idSolicitud, int idEstatus) {
		reqJpa.actualizarEstatusSolicitud(idEstatus, idSolicitud);
	}

	@Override
	public List<SchedulersSolicitudes> obtenerSchedulersSolicitudes() {
		// return reqJpa.obtenerSchedulersSolicitudes();
		return null;
	}

	@Override
	public List<SchedulersSolicitudes> obtenerSchedulersSolicitud(int idSolicitud) {
		return reqJpa.obtenerSchedulersSolicitud(idSolicitud);
	}

	@Override
	public int obtenerReporteMailsAbogados(String firma) {
		return reqJpa.obtenerReporteMailsAbogados(firma);
	}

	@Override
	public List<SolicitudEntity> obtenerSolicitudesEstatusEnProcesos() {
		return reqJpa.obtenerSolicitudesEstatusEnProcesos();

	}

	@Override
	public List<ReporteSolsUsuario> obtenerNumeroSolicitudesAtendidasPorRevisores(Date fechai, Date fechaf) {
		return reqJpa.obtenerSolicitudesRevisores(fechai, fechaf);
	}
	
	@Override
	public List<ReporteSolsDeUsuario> obtenerSolicitudesAtendidasPorRevisor(Date fechai, Date fechaf,int idUsuario){
		return reqJpa.obtenerSolicitudesRevisor(fechai, fechaf, idUsuario);
	}

	@Override
	public List<ReporteSolsUsuario> obtenerNumeroSolicitudesAtendidasPorRevisorAdicional(Date fechai, Date fechaf) {
		return reqJpa.obtenerSolicitudesRevisoresAd(fechai, fechaf);
	}
	
	@Override
	public List<ReporteSolsUsuario> obtenerSolicitudesClinicians(Date fechai, Date fechaf){
		return reqJpa.obtenerSolicitudesClinicians(fechai, fechaf);
	}
	
	@Override
	public List<ReporteSolsDeUsuario> obtenerSolicitudesClinician(Date fechai, Date fechaf,int idUsuario){
		return reqJpa.obtenerSolicitudesClinician(fechai, fechaf,idUsuario);
	}
	
	@Override
	public List<ReporteSolsDeUsuario> obtenerSolicitudesAtendidasPorRevisorAdicional(Date fechai, Date fechaf,int idUsuario) {
		return reqJpa.obtenerSolicitudesRevisorAd(fechai, fechaf, idUsuario);
	}

	@Override
	public List<ReporteSolsUsuario> obtenerNumeroSolicitudesAtendidasPorTemplates(String fechai, String fechaf) {
		return reqJpa.obtenerSolicitudesTemplates(fechai, fechaf);
	}
	
	@Override
	public List<ReporteSolsDeUsuario> obteneSolicitudesAtendidasPorTemplate(Date fechai, Date fechaf,int idUsuario) {
		return reqJpa.obtenerSolicitudesDeUsuarioTemplate(fechai, fechaf, idUsuario);
	}

	@Override
	public int obtenerUsuarioRevisandoPorRolCoMenosSols(int idRol) {
		UltimoUsuarioRevisor ur = reqJpa.obtenerUltimoUsuarioRevisandoPorRol(idRol);
		int usuario = 0;
		if (ur != null) {
			usuario = ur.getUsuario();
		} else {
			usuario = uPort.obtenerUsuarioPorRol(idRol).getIdUsuario();
		}
		return usuario;
	}

	@Override
	public int obtenerUsuarioSiEsRevisor(int idRol, int revisor) {
		UltimoUsuarioRevisor ur = reqJpa.obtenerUsuarioSiEsRevisor(idRol, revisor);
		int usuario = 0;
		if (ur != null) {
			usuario = ur.getUsuario();
		}
		return usuario;
	}

	@Override
	public void actualizarUsuarioRevisando(int idUsuarioRevisando, int idSolicitud) {
		reqJpa.actualizarUsuarioRevisando(idUsuarioRevisando, idSolicitud);
	}

	@Override
	public void actualizarUsuariosRevisores(int idUsuario, int idSolicitud) {
		reqJpa.actualizarUsuariosRevisores(idUsuario, idSolicitud);
	}

	@Override
	public void actualizarEstatusPago(int idEstatusPago, int idSolicitud) {
		reqJpa.actualizarEstatusPago(idEstatusPago, idSolicitud);
	}

	public int ageSolicitud(String fi, String ff) {
		int df = UtilidadesAdapter.diferenciaDias(fi, ff);
		if (df < 0) {
			df = df * -1;
		} else {
			df = 0;
		}
		return df;
	}

	@Override
	public BigDecimal obtenerSumaAmountPorFechayCliente(Date fechai, Date fechaf, String cliente) {
		BigDecimal salida = BigDecimal.ZERO;
		if ("".equals(cliente)) {
			salida = reqJpa.obtenerSumaAmountSolicitudesPorFecha(fechai, fechaf);
		} else {
			salida = reqJpa.obtenerSumaAmountSolicitudesPorFechaYClt(fechai, fechaf, cliente);
		}
		return salida;
	}

	@Override
	public List<TelefonosSolicitudes> obtenerSolicitudesDeTelefono(String telefono) {
		return reqJpa.obtenerSolicitudesDeTelefono(telefono);
	}

	@Override
	public List<String> obtenerFirmasAbogados(String fechai, String fechaf) {
		return reqJpa.obtenerFirmasAbogados(fechai, fechaf);
	}
	
	@Override
	public List<FilesFirmaAbogadoObj> obtenerSolsDeFirmaYFechas(String fechai, String fechaf,String firmaAbogado,int idAbogado,boolean all){
			
		List<Object[]> rows = null;	
		rows = obtenerSolsDeFirmaYFechasQ(fechai, fechaf, firmaAbogado,idAbogado,all);				
		List<FilesFirmaAbogadoObj> result = null;

		result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirAFilesFirmaAbogadoObj(row));
		}
		
		return result;
		
	}
	
	@Override
	public List<NumFilesAbogados> obtenerFirmasAbogadosyNumFiles(String fechai, String fechaf) {
		
		List<Object[]> rows = null;
	    rows = obtenerNumFilesAbo(fechai, fechaf);
		
	    List<NumFilesAbogados> result = null;

	    result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirANumFilesAbo(row));
		}
		
		return result;
	}

	@Override
	public List<CorreosFirmas> obtenerCorreosAbogados(String firmas) {

		List<CorreosFirmas> salida = null;
		List<String> listaCorreos = Arrays.asList(firmas.split(","));
		salida = reqJpa.obtenerCorreosFirmas(listaCorreos);
		return salida;
	}

	@Override
	public List<SolicitudEntity> obtenerSolicitudesPorFiltros(String valor) {
		return reqJpa.obtenerSolicitudesPorFiltros(valor);
	}

	@Override
	public void actualizarAsignacionTemplate(int asignacionTemplate, int idUsuarioTemp, int idSolicitud) {
		reqJpa.actualizarAsignacionTemplate(asignacionTemplate, idUsuarioTemp, idSolicitud);
	}

	@Override
	public void actualizarNumeroEntrevistas(int num, int idSolicitud) {
		reqJpa.actualizarNumeroEntrevistas(num, idSolicitud);
	}

	@Override
	public void actualizarImportante(String importante, int idSolicitud) {
		reqJpa.actualizarImportante(importante, idSolicitud);
	}

	@Override
	public void actualizarAsignacionIntSc(int asignacionIntSc, int idUsuario, int idSolicitud) {
		reqJpa.actualizarAsignacionIntSc(asignacionIntSc, idUsuario, idSolicitud);
	} 
	
	@Override
	public void actualizarAsignacionClnc(int idUsuario, int idSolicitud) {
		reqJpa.actualizarAsignacionClnc(idUsuario, idSolicitud);
	} 

	@Override
	public void actualizarUsuarioTraductor(int idUsuario,int idSolicitud) {
		reqJpa.actualizarUsuarioTraductor(idUsuario, idSolicitud);
	}

	@Override
	public void actualizarAsignacionIntScReset(int idSolicitud) {
		reqJpa.actualizarAsignacionIntScReset(idSolicitud);
	}
	
	@Override
	public void actualizarAsignacionClncReset(int idSolicitud) {
		reqJpa.actualizarAsignacionIntScReset(idSolicitud);
	}

	@Override
	public void actualizarFinIntSc(String valor, int idSolicitud) {
		reqJpa.actualizarFinIntSc(valor, idSolicitud);
	}
	
	@Override
	public void actualizarFinAsgTmp(String valor, int idSolicitud){
		reqJpa.actualizarFinAsgTmp(valor, idSolicitud);
	}
	
	@Override
	public void actualizarFinAsgClnc(String valor, int idSolicitud){
		reqJpa.actualizarFinAsgClnc(valor, idSolicitud);
	}

	@Override
	public List<ReporteAnios> reporteDeAnio(String anios, String valorFiltro) {

		String query = "";

		if (valorFiltro.equals("No show") || valorFiltro.equals("Won") || valorFiltro.equals("Lost")
				|| valorFiltro.equals("Refused")) {

			try (Scanner scanner = new Scanner(queryReporteAniosByEvento.getInputStream(),
					StandardCharsets.UTF_8.name())) {
				query = scanner.useDelimiter("\\A").next();
			} catch (Exception e) {
				throw new RuntimeException("Error al leer el archivo", e);
			}
		} else {

			try (Scanner scanner = new Scanner(queryReporteAnios.getInputStream(), StandardCharsets.UTF_8.name())) {
				query = scanner.useDelimiter("\\A").next();
			} catch (Exception e) {
				throw new RuntimeException("Error al leer el archivo", e);
			}
		}

		List<Object[]> rows = null;
		if (valorFiltro.equals("No show") || valorFiltro.equals("Won") | valorFiltro.equals("Lost")) {
			rows = obtenerReporteAniosByEvento(anios, valorFiltro, null, query);
		} else if (valorFiltro.equals("Refused")) {
			rows = obtenerReporteAniosByEvento(anios, null, valorFiltro, query);
		} else {
			String estatusF = "";
			if (valorFiltro.equals("All")) {
				estatusF = "1,2,3,10,11";
			} else {

				EstatusSolicitudEntity estSol = esPort.obtenerEstatusSolicitudPorDescripcion(valorFiltro);
				estatusF = "" + estSol.getIdEstatusSolicitud();

			}
			rows = obtenerReporteAniosByEstatus(anios, estatusF, query);
		}

		List<ReporteAnios> result = null;

		result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirAReporteAniosN(row));
		}

		return result;
	}
	
	@Override
	public ReporteDash reporteDash(String fechai, String fechaf,int usuario) {
		
		List<Object[]> rows = null;
	    rows = obtenerReporteDash(fechai, fechaf,usuario);	
		
		ReporteDash result = null;

		if(!rows.isEmpty()){
			Object[] v = rows.get(0);
			result = convertirAReporteDash(v);
		}

		if(usuario > 0){

			UsuarioEntity objUsuario = new UsuarioEntity();
			objUsuario = uPort.buscarPorId(usuario);

			if(objUsuario.getRol().equals("4")||objUsuario.getRol().equals("3")){

				if(!objUsuario.isRevisor()){
					String use = objUsuario.getUsuario();
					int ventas = obtenerDashVentas(fechai, fechaf, use);
					result.setVentas(ventas);
					result.setVerVentas(true);
				}else{
					result.setVentas(0);
					result.setVerVentas(false);
				}

			}	

		}else{
			result.setVerVentas(false);
			result.setVentas(0);
		}	


		return result;
		
	}

	@Override
	public void actualizarInterviewMaster(String valor, int idSolicitud) {
		reqJpa.actualizarInterviewMaster(valor, idSolicitud);
	}

	@Override
	public void actualizarInterview(int idUsuario, int idSolicitud) {
		reqJpa.actualizarInterview(idUsuario, idSolicitud);
	}
	
	@Override
	public void actualizarAsignacionIntReset(int idSolicitud){
		reqJpa.actualizarAsignacionIntReset(idSolicitud);
	}
	
	@Override
	public void actualizarFinInterview(int finIntIni,int idSolicitud){
		reqJpa.actualizarFinInterview(finIntIni, idSolicitud);
	}

	@Override
	public void actualizarIntScalesSolicitud(int idSolicitud) {
		reqJpa.actualizarIntSc(idSolicitud);
	}

	@Override
	public List<Solicitud> obtenerSolicitudesDeUsuarioPorQueryV2(UsuarioEntity us, int estatus, int idSolicitud,
			String fechai, String fechaf, String ordenarPor, String orden, String campo, String valor, boolean myFiles,
			String cerradas,boolean primeraVez, int usuario) {

		List<Solicitud> result = null;
		String rol = us.getRol();
		int idUsuario = us.getIdUsuario();

		if (estatus > 0) {

			List<Object[]> rows = obtenerSolsFiltro(false, rol, us.isRevisor(), fechai, fechaf, idUsuario, idSolicitud,
					cerradas, ordenarPor, orden, campo, valor, myFiles,primeraVez,usuario);
			result = new ArrayList<>(rows.size());
			for (Object[] row : rows) {
				result.add(convertirSolQueryASolicitud(row, false));
			}

		} else {

			UtilidadesAdapter.pintarLog("idSol:"+idSolicitud);
			if (idSolicitud > 0) {
				List<Object[]> rows = obtenerSolsFiltro(true, rol, us.isRevisor(), fechai, fechaf, idUsuario,
						idSolicitud, "OPEN", ordenarPor, orden, campo, valor, myFiles,primeraVez,usuario);
				result = new ArrayList<>(rows.size());
				for (Object[] row : rows) {
					result.add(convertirSolQueryASolicitud(row, true));
				}
			} else {

				List<Object[]> rows = obtenerSolsFiltro(false, rol, us.isRevisor(), fechai, fechaf, idUsuario,
						idSolicitud, cerradas, ordenarPor, orden, campo, valor, myFiles,primeraVez,usuario);
				result = new ArrayList<>(rows.size());
				for (Object[] row : rows) {
					result.add(convertirSolQueryASolicitud(row, false));
				}
			}

		}

		return result;
	}

	@Override
	public List<Solicitud> obtenerSolicitudesDeUsuarioQueryFiltros(boolean soloUnObjeto, UsuarioEntity us, String cerradas,
			boolean primeraVez, String ordenarPor, String orden,
			String fechai, String fechaf, int idSolicitud, String cliente, String telefono, String email, String estado,
			int idEstatusSolicitud, int idEstatusPago,
			int idTipoSolicitud, String waiver, String noshow, String importante, String asignado,String zipcodes,String consentimiento) {

		List<Solicitud> result = null;
		UtilidadesAdapter.pintarLog("idSol:" + idSolicitud);
		
		/*boolean soloUnObjeto = false;

		boolean filtrosSinSolicitudConDatos = hayFiltrosActivos(cliente, telefono, email, estado, idEstatusSolicitud, idEstatusPago, idTipoSolicitud, waiver, noshow, importante, asignado, zipcodes);
		if(!filtrosSinSolicitudConDatos){
			if (idSolicitud > 0) {
				soloUnObjeto = true;
			}
		}*/

		logger.info("Obteniendo solicitudes con filtros - soloUnObjeto: " + soloUnObjeto + ", idSolicitud: " + idSolicitud);

		List<Object[]> rows = obtenerSolsConVariosFiltros(soloUnObjeto, us, cerradas, primeraVez, ordenarPor, orden,
				fechai, fechaf, idSolicitud,
				cliente, telefono, email, estado, idEstatusSolicitud, idEstatusPago, idTipoSolicitud, waiver, noshow,
				importante, asignado,zipcodes, consentimiento);
		result = new ArrayList<>(rows.size());



		for (Object[] row : rows) {
			result.add(convertirSolQueryASolicitud(row, soloUnObjeto));
		}

		return result;
	}

	/**
     * Verifica si todos los filtros están vacíos, nulos o en cero.
     */
    public static boolean hayFiltrosActivos(String cliente, String telefono, String email, String estado,
            int idEstatusSolicitud, int idEstatusPago, int idTipoSolicitud, String waiver, String noshow, String importante, String asignado, String zipcodes) {
        return (
            (cliente != null && !cliente.trim().isEmpty()) ||
            (telefono != null && !telefono.trim().isEmpty()) ||
            (email != null && !email.trim().isEmpty()) ||
            (estado != null && !estado.trim().isEmpty()) ||
            idEstatusSolicitud != 0 ||
            idEstatusPago != 0 ||
            idTipoSolicitud != 0 ||
            (waiver != null && !waiver.trim().isEmpty()) ||
            (noshow != null && !noshow.trim().isEmpty()) ||
            (importante != null && !importante.trim().isEmpty()) ||
            (asignado != null && !asignado.trim().isEmpty()) ||
            (zipcodes != null && !zipcodes.trim().isEmpty())
        );
    }

	private Solicitud convertirSolQueryASolicitud(Object[] row, boolean soloUno) {

		Solicitud s = new Solicitud();

		s.setIdSolicitud((Integer) row[0]);
		s.setFechaInicio((Date) row[1]);
		s.setCliente((String) row[2] == null ? "" : (String) row[2]);
		s.setApellidos((String) row[3]);
		s.setTelefono((String) row[4]);
		s.setIdTipoSolicitud((Integer) row[5]);
		s.setTipoSolicitud((String) row[6]);
		s.setEstado((String) row[8]);
		s.setReferencia((String) row[9]);
		s.setIdioma((String) row[10]);
		s.setDireccion((String) row[11]);
		s.setAmount((BigDecimal) row[12]);
		s.setAbogado((String) row[13]);
		s.setEmail((String) row[14]);
		s.setAdicional((String) row[15]);
		s.setIdEstatusPago((Integer) row[16]);
		s.setEstatusPago((String) row[17]);
		s.setIdEstatusSolicitud((int) row[18]);
		s.setEstatusSolicitud((String) row[19]);
		s.setWaiver(Integer.valueOf(row[20].toString()) == 0 ? false : true);
		s.setInterviewMaster(Integer.valueOf(row[21].toString()) == 0 ? false : true);
		s.setExternal(Integer.valueOf(row[22].toString()) == 0 ? false : true);
		s.setAsignacionTemplate(Integer.valueOf(row[23].toString()) == 0 ? false : true);
		s.setAsignacionIntSc(Integer.valueOf(row[24].toString()) == 0 ? false : true);
		s.setDueDate((String) row[25]);
		s.setNumeroEntrevistas((Integer) row[26]);
		s.setUsuarioIntSc(Integer.valueOf(row[27].toString()));
		s.setUsuarioIntScNombre(s.getUsuarioIntSc() == 0 ? "Unassigned" : (String) row[28]);
		s.setIdUsuarioRevisor((Integer) row[29]);
		s.setUsuarioRevisandoNombre((String) row[30]);
		s.setUsuarioRevisor((String) row[31]);
		s.setIdUsuarioRevisando(Integer.valueOf(row[32].toString()));
		s.setUsuarioRevisandoNombre((String) row[33]);
		s.setUsuarioRevisando((String) row[34]);
		s.setIdUsuarioTemplate(Integer.valueOf(row[35].toString()));
		s.setUsuarioTemplate(s.getIdUsuarioTemplate() == 0 ? "Unassigned" : (String) row[36]);
		s.setIdUsuarioExternal(Integer.valueOf(row[37].toString()));
		s.setUsuarioExternal((Integer) row[38] == null ? 0 : (Integer) row[38]);
		s.setFecha_schedule((String) row[39] == null ? "" : (String) row[39]);
		s.setFecha_schedule_scales((String) row[40] == null ? "" : (String) row[40]);
		s.setTieneScale(Integer.valueOf(row[41].toString()) == 0 ? false : true);
		s.setNumeroDeCaso((String) row[42] == null ? "" : (String) row[42]);
		s.setDocusign((String) row[43] == null ? "" : (String) row[43]);
		s.setEmail_abogado((String) row[44] == null ? "" : (String) row[44]);
		s.setFirmaAbogados((String) row[45] == null ? "" : (String) row[45]);
		s.setFechaNacimiento((String) row[46] == null ? "" : (String) row[46]);
		s.setTipoEntrevista((String) row[47] == null ? "" : (String) row[47]);
		s.setParalegalName((String) row[48] == null ? "" : (String) row[48]);
		s.setParalegalEmails((String) row[49] == null ? "" : (String) row[49]);
		s.setParalegalTelefonos((String) row[50] == null ? "" : (String) row[50]);

		if (soloUno) {

			if (s.getAmount() != null) {
				BigDecimal pending = BigDecimal.ZERO;
				if (s.getAmount().compareTo(BigDecimal.ZERO) == 1) {
					BigDecimal sm = (BigDecimal) row[51];
					if (sm == null) {
						s.setAdeudo("$0 paid, $" + s.getAmount() + " pending");
					} else {
						pending = s.getAmount();
						pending = pending.subtract(sm);
						s.setAdeudo("$" + sm + " paid, $" + pending + " pending");
					}
				} else if (s.getAmount().compareTo(BigDecimal.ZERO) == 0) {
					s.setAdeudo("Paid");
				}
			} else {
				s.setAdeudo("There is no amount in the request");
			}

		} else {
			
			BigDecimal sm = (BigDecimal) row[51];
			s.setPagos(sm);
			s.setCliente(s.getCliente() + " " + this.getApellidosSinNull(s.getApellidos()));

		}
		
		s.setUsuarioInterview(Integer.valueOf(row[52].toString()));
		s.setUsuarioInterviewNombre(s.getUsuarioInterview() == 0 ? "Unassigned" : (String) row[53]);
		s.setFinIntIni(Integer.valueOf(row[54].toString()) == 0 ? false : true);
		s.setFinIntSc(Integer.valueOf(row[55].toString()) == 0 ? false : true);
		s.setAssignedClinician(Integer.valueOf(row[56].toString()));
		s.setIdAbogado(Integer.valueOf(row[57].toString()));
		s.setFinAsgTmp(Integer.valueOf(row[58].toString()) == 0 ? false : true);
		s.setEmailAboSel((String) row[59] == null ? "" : (String) row[59]);
		s.setLostFile(row[60] == null ? false : row[60].toString().equals("x") ? true : false);
		s.setConCupon(row[61] !=  null ? Integer.valueOf(row[61].toString()) == 0 ? false : true : false);
		s.setFechaCupon(row[62] != null ? (String) row[62] == null ? "" : (String) row[63] :  "");
		s.setSexo((String) row[63] == null ? "" : (String) row[63]);
		s.setUsuarioClinicianNombre(s.getAssignedClinician() == 0 ? "Unassigned" : (String) row[64]);
		s.setIdRolUsInt(row[65] == null ? 0 : Integer.valueOf(row[65].toString()));
		s.setFinAsgClnc(Integer.valueOf(row[66].toString()) == 0 ? false : true);
		s.setFechaClinicianAppo((String) row[67] == null ? "" : (String) row[67]);
		s.setImportantNotes((String) row[68] == null ? "" : (String) row[68]);
		s.setSignedClnc(Integer.valueOf(row[69].toString()) == 0 ? false : true);
		s.setFechaDeCrimen((String) row[70] == null ? "" : (String) row[70]);
		s.setIdUsuarioTraductor(Integer.valueOf(row[71].toString()));
		s.setUsuarioTraductorNombre((String) row[72]);
		s.setNoShow(row[73] !=  null ? Integer.valueOf(row[73].toString()) == 0 ? false : true : false);
		s.setInicialesInterview((String) row[74] == null ? "" : (String) row[74]);
		s.setInicialesClinician((String) row[75] == null ? "" : (String) row[75]);
		s.setInicialesIntSc((String) row[76] == null ? "" : (String) row[76]);
		s.setInicialesTemplate((String) row[77] == null ? "" : (String) row[77]);
		s.setTiposPagosRealizados(((String) row[78] == null ? "" : (String) row[78]));
		s.setConsentimiento(Integer.valueOf(row[79].toString()) == 0 ? false : true);
		s.setVerificacionCliente(Integer.valueOf(row[80].toString()) == 0 ? false : true);

		return s;

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerSolsConVariosFiltros(boolean soloUnObjeto, UsuarioEntity us, String cerradas, boolean primeraVez,String ordenarPor, String orden,
			String fechai,String fechaf, int idSolicitud,String cliente,String telefono,String email,String estado,int idEstatusSolicitud, int idEstatusPago,
		int idTipoSolicitud, String waiver,String noshow,String importante,String asignado,String zipcodes,String consentimiento) {

		boolean isRevisor = us.isRevisor();
		String rol = us.getRol();
		int idUsuario = us.getIdUsuario();
		boolean seUsaronFilros = false;

		UtilidadesAdapter.pintarLog("Solo un objeto:"+soloUnObjeto);
		UtilidadesAdapter.pintarLog("ejecutando query filtro | rol:"+rol+" | isRevisor:"+isRevisor+" | fechai:"+fechai+" | fechaf:"+fechaf+" | idUsuario:"+idUsuario+" | idSolicitud:"+idSolicitud+" | cerradas:"+cerradas+" | ordenarPor:"+ordenarPor+" | orden:"+orden+" | campo:"+cliente+" | valor:"+telefono+" | primeraVez:"+primeraVez+
			" | Estado:"+estado+" | idEstatusSolicitud:"+idEstatusSolicitud+" | idEstatusPago:"+idEstatusPago+" | idTipoSolicitud:"+idTipoSolicitud+" | waiver:"+waiver+" | noshow:"+noshow+" | importante:"+importante+" | asignado:"+asignado
		);
		StringBuilder sb = new StringBuilder();
		StringBuilder sbW = new StringBuilder();

		String queryS = "";

		try (Scanner scanner = new Scanner(querySolicitudes.getInputStream(), StandardCharsets.UTF_8.name())) {
			queryS = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}

		//log.info("Query solicitudes:"+queryS);
		sb.append(queryS);
			
		if (idSolicitud == 0) {
			if(!rol.equals("11")){
				if (sbW.length() != 0)
				sbW.append(" AND ");
				sbW.append("OPEN".equals(cerradas)?" s.id_estatus_solicitud <> 11 ":" s.id_estatus_solicitud = 11 ");
			}
		}	
		
		

		if (!soloUnObjeto) {

			if (!"".equals(fechai)) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.fecha_inicio BETWEEN :fechai AND :fechaf ");
			}

			if (idSolicitud != 0) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" CAST(s.id_solicitud AS CHAR) LIKE :idSolicitud ");
				seUsaronFilros = true;
			}

			if (cliente != null && !"".equals(cliente)) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" ( s.cliente LIKE :cliente OR s.apellidos LIKE :cliente ) ");
				seUsaronFilros = true;
			}

			if (telefono != null && !"".equals(telefono)) {

				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.telefono LIKE :telefono ");
				seUsaronFilros = true;
			}

			if (email != null && !"".equals(email)) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.email LIKE :email ");
				seUsaronFilros = true;
			}

			if (estado != null && !"".equals(estado)) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.estado = :estado ");
				seUsaronFilros = true;
			}

			if (idEstatusSolicitud != 0) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.id_estatus_solicitud = :idEstatusSolicitud ");
				seUsaronFilros = true;
			}

			if (idEstatusPago != 0) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.id_estatus_pago = :idEstatusPago ");
				seUsaronFilros = true;
			}

			if (idTipoSolicitud != 0) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.id_tipo_solicitud = :idTipoSolicitud ");
				seUsaronFilros = true;
			}

			if (waiver != null && !waiver.isEmpty()) {
				if ("Yes".equals(waiver)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.waiver = 1 ");
					seUsaronFilros = true;
				}
				if ("No".equals(waiver)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.waiver = 0 ");
					seUsaronFilros = true;
				}
			}

			if (noshow != null && !noshow.isEmpty()) {
				if ("Yes".equalsIgnoreCase(noshow)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" EXISTS (" +
							"  SELECT 1" +
							"  FROM evento_solicitud e2 " +
							"  WHERE e2.id_solicitud = s.id_solicitud " +
							"    AND LOWER(e2.descripcion) LIKE '%no show%' " +
							"    AND LOWER(e2.descripcion) NOT LIKE '%re-scheduled%' " +
							"    AND LOWER(e2.descripcion) NOT LIKE '%was changed%'" +
							") ");
					seUsaronFilros = true;
				}
				if ("No".equalsIgnoreCase(noshow)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" NOT EXISTS (" +
							"  SELECT 1" +
							"  FROM evento_solicitud e2 " +
							"  WHERE e2.id_solicitud = s.id_solicitud " +
							"    AND LOWER(e2.descripcion) LIKE '%no show%' " +
							"    AND LOWER(e2.descripcion) NOT LIKE '%re-scheduled%' " +
							"    AND LOWER(e2.descripcion) NOT LIKE '%was changed%'" +
							") ");
					seUsaronFilros = true;
				}
			}

			if (importante != null && !importante.isEmpty()) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				if ("Yes".equalsIgnoreCase(importante)) {
					sbW.append(
							" EXISTS (SELECT 1 FROM evento_solicitud evImp WHERE evImp.id_solicitud = s.id_solicitud AND evImp.tipo IN ('Important','Suicide')) ");
				} else if ("No".equalsIgnoreCase(importante)) {
					sbW.append(
							" NOT EXISTS (SELECT 1 FROM evento_solicitud evImp WHERE evImp.id_solicitud = s.id_solicitud AND evImp.tipo IN ('Important','Suicide')) ");
				}
				seUsaronFilros = true;
			}

			if (consentimiento != null && !consentimiento.isEmpty()) {
				if ("Yes".equals(consentimiento)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.consentimiento = 1 ");
					seUsaronFilros = true;
				}
				if ("No".equals(consentimiento)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.consentimiento = 0 ");
					seUsaronFilros = true;
				}
			}

			if (asignado != null && !"".equals(asignado)) {

				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(
						" (ucl.nombre LIKE :asignado OR uri.nombre LIKE :asignado OR us.nombre LIKE :asignado OR urt.nombre LIKE :asignado) ");
				seUsaronFilros = true;

			}

			if (zipcodes != null && !"".equals(zipcodes)) {

				String zcodes = buildZipCodeFilter(zipcodes);
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(zcodes);
				seUsaronFilros = true;

			}

		}else{

			if (idSolicitud != 0) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.id_solicitud = :idSolicitud ");
			}

		}
		

		if (idSolicitud == 0) {

			if ("CLOSED".equals(cerradas)) {

				if (!"".equals(fechai)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.fecha_inicio BETWEEN :fechai AND :fechaf ");
				}

			} else {

				if (primeraVez) {
					if (!seUsaronFilros) {
						if (rol.equals("4")) {
							if (isRevisor) {
								if (sbW.length() != 0)
									sbW.append(" AND ");
								sbW.append(" s.usuario_revisando = :idUsuario AND ( s.usuario_template = 0 || s.id_estatus_solicitud = 10) ");
							
							} else {
								if (sbW.length() != 0)
									sbW.append(" AND ");
								sbW.append(
										" usuario_revisor = :idUsuario AND s.id_estatus_solicitud NOT IN(11,2,3) OR s.asignacion_int_sc = 0 ");
							}
						} else if (rol.equals("7")) {
							if (sbW.length() != 0)
								sbW.append(" AND ");
							sbW.append(" s.usuario_template = :idUsuario AND s.fin_asg_tmp = 0 ");
						} else if (rol.equals("9")) {
							if (sbW.length() != 0)
								sbW.append(" AND ");
							sbW.append(" usuario_external = :idUsuario  ");
						} else if (rol.equals("5")) {
							if (sbW.length() != 0)
								sbW.append(" AND ");
							sbW.append(" s.usuario_interview = :idUsuario AND s.fin_int_ini = 0 ");
						} else if (rol.equals("8")) {
							if (sbW.length() != 0)
								sbW.append(" AND ");
							sbW.append(" s.usuario_int_sc = :idUsuario AND s.fin_int_sc = 0 ");
						}

					}

				}

				if (!"".equals(fechai)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.fecha_inicio BETWEEN :fechai AND :fechaf ");
				}

			}
			
			if (rol.equals("11")) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" ( s.assigned_clinician = :idUsuario AND s.fin_asg_clnc = 0 ) OR ( s.usuario_interview = :idUsuario AND s.fin_int_ini = 0 )  ");
			}
			
		} 
		/*else {
			UtilidadesAdapter.pintarLog("Agregar id solicitud al query");
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(" s.id_solicitud = :idSolicitud ");
		}*/


		sb.append("\n");
		if (sbW.length() != 0) {
			sb.append(" WHERE " + sbW.toString());
		}

		sb.append("\n");
			sb.append(" GROUP BY id_solicitud,cliente ");
		

		sb.append("\n");
		
		if (!"".equals(ordenarPor)) {
			if ("".equals(orden)) {
				orden = "ASC";
			}
			if (ordenarPor.equals("File")) {
				sb.append(" ORDER BY s.id_solicitud " + orden);
			}
			if (ordenarPor.equals("Creation Date")) {
				sb.append(" ORDER BY fecha_inicio " + orden);
			}
			if (ordenarPor.equals("Due date")) {
				sb.append(" ORDER BY due_date " + orden);
			}
			if (ordenarPor.equals("Additional")) {
				sb.append(" ORDER BY adicional " + orden);
			}
			if (ordenarPor.equals("Importance")) {
				sb.append(" ORDER BY importante " + orden);
			}
			if (ordenarPor.equals("Interview Appointment")) {
				sb.append(" ORDER BY fechaint " + orden);
			}
			if (ordenarPor.equals("Scales Appointment")) {
				sb.append(" ORDER BY fechascale " + orden);
			}
			if (ordenarPor.equals("Interview review")) {
				sb.append(" ORDER BY interview_master " + orden);
			}
			if (ordenarPor.equals("Interview Scale")) {
				sb.append(" ORDER BY fechascale " + orden);
			}

		} else {

			if (rol.equals("1") || rol.equals("3")) {
				sb.append("  ORDER BY s.fecha_inicio DESC ");
			} else if (rol.equals("4")) {
				if (isRevisor) {
					sb.append("  ORDER BY dueDate DESC");
				} else {
					sb.append("  ORDER BY s.fecha_inicio DESC ");
				}
			} else if (rol.equals("5") || rol.equals("7")) {
				sb.append("  ORDER BY fechaint DESC ");
			} else if (rol.equals("9")) {
				sb.append("  ORDER BY s.fecha_inicio DESC ");
			} else if (rol.equals("8")) {
				sb.append("  ORDER BY fechascale DESC ");
			} else {
				sb.append("  ORDER BY s.fecha_inicio DESC ");
			}

		}
	

		UtilidadesAdapter.pintarLog("query:\n" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

		if (!soloUnObjeto) {

			if (idSolicitud == 0) {
				if (!rol.equals("11")) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(cerradas.equals("OPEN") ? " s.id_estatus_solicitud <> 11 "
							: " s.id_estatus_solicitud = 11 ");
				}
			}

			if (idSolicitud != 0) {
				if(soloUnObjeto){
					query.setParameter("idSolicitud",idSolicitud);
				}else{
					query.setParameter("idSolicitud", "%" + idSolicitud + "%" );
				}
			}

			if (cliente != null && !"".equals(cliente)) {
				query.setParameter("cliente", "%" + cliente + "%");
			}

			if (telefono != null && !"".equals(telefono)) {
				telefono = UtilidadesAdapter.soloNumeros(telefono);
				//System.out.println("Telefono solo numeros:"+telefono);
				query.setParameter("telefono", "%" + telefono + "%");
			}

			if (email != null && !"".equals(email)) {
				query.setParameter("email", "%" + email + "%");
			}

			if (estado != null && !"".equals(estado)) {
				query.setParameter("estado", estado);
			}

			if (idEstatusSolicitud != 0) {
				query.setParameter("idEstatusSolicitud", idEstatusSolicitud);
			}

			if (idEstatusPago != 0) {
				query.setParameter("idEstatusPago", idEstatusPago);
			}

			if (idTipoSolicitud != 0) {
				query.setParameter("idTipoSolicitud", idTipoSolicitud);
			}

			if (importante != null && !importante.isEmpty() && !"Yes".equalsIgnoreCase(importante)
					&& !"No".equalsIgnoreCase(importante)) {
				query.setParameter("importante", "%" + importante + "%");
			}

			if (asignado != null && !"".equals(asignado)) {
				query.setParameter("asignado", "%" + asignado + "%");
			}

			if (!"".equals(fechai)) {
				query.setParameter("fechai", fechai);
				query.setParameter("fechaf", fechaf);
			}

			if (primeraVez) {
				if (!seUsaronFilros) {
					if (rol.equals("4") || rol.equals("5") || rol.equals("7") || rol.equals("8") || rol.equals("9")) {
						query.setParameter("idUsuario", idUsuario);
					}
				}
			}

			if (rol.equals("11")) {
				query.setParameter("idUsuario", idUsuario);
			}

		} else {
			query.setParameter("idSolicitud", idSolicitud);
		}

		List<Object[]> rows = query.getResultList();
		return rows;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerSolsFiltro(boolean soloUnObjeto, String rol, boolean isRevisor, String fechai,
			String fechaf, int idUsuario, int idSolicitud, String cerradas, String ordenarPor, String orden,
			String campo, String valor, boolean myFiles,boolean primeraVez,int usuario) {

		UtilidadesAdapter.pintarLog("ejecutando query filtro | cerradas: " + cerradas+"| solo un objeto:"+soloUnObjeto+"| fechai:"+fechai+"| idSolicitud:"+idSolicitud);
		StringBuilder sb = new StringBuilder();
		StringBuilder sbW = new StringBuilder();

		String queryS = "";

		try (Scanner scanner = new Scanner(querySolicitudes.getInputStream(), StandardCharsets.UTF_8.name())) {
			queryS = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}

		//System.out.println("Query solicitudes:"+queryS);
		sb.append(queryS);
		
			
		if (idSolicitud == 0) {
			if(!rol.equals("11")){
				if (sbW.length() != 0)
				sbW.append(" AND ");
				sbW.append(cerradas.equals("OPEN")?" s.id_estatus_solicitud <> 11 ":" s.id_estatus_solicitud = 11 ");
			}
		}

		
		if (idSolicitud == 0) {
			// if (!cerradas) {
			if (campo != null) {
				switch (campo) {
				case "All":
					if(!"".equals(valor)){
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append(" (s.id_solicitud LIKE '%" + valor + "%' OR s.apellidos LIKE '%" + valor + "%' OR s.telefono LIKE '%" + valor + "%' OR s.email LIKE '%" + valor + "%') ");
					}
					break;
				case "File":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.id_solicitud LIKE '%" + valor + "%'  ");
					break;
				case "Customer":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" (s.cliente LIKE '%" + valor + "%' OR s.apellidos LIKE '%" + valor + "%') ");
					break;
				case "Phone":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.telefono LIKE '%" + valor + "%' ");
					break;
				case "Email":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.email LIKE '%" + valor + "%'  ");
					break;
				case "File Status":
					UtilidadesAdapter.pintarLog("Busqueda por file estatus:" + valor);
					if (valor.equals("No show") || valor.equals("Won") | valor.equals("Lost")) {
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append(" e.descripcion LIKE '%" + valor + "%'  ");
					} else if (valor.equals("Refused")) {
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append(" e.evento LIKE '%" + valor + "%'  ");
					} else {

						String es = "";
						if (valor.equals("All")) {
							es = "1,2,3,10";
						} else {

							EstatusSolicitudEntity estSol = esPort.obtenerEstatusSolicitudPorDescripcion(valor);
							es = "" + estSol.getIdEstatusSolicitud();

						}
						UtilidadesAdapter.pintarLog("Buscar file estatus:" + es);
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append(" s.id_estatus_solicitud IN(" + es + ") ");
					}
					break;
				case "Payment Status":

					String pe = "";
					if (valor.equals("All")) {
						pe = "1,2";
					} else {
						EstatusPagoEntity ep = esPPort.obtenerEstatusPagoPorDescripcion(valor);
						pe = "" + ep.getIdEstatusPago();
					}

					UtilidadesAdapter.pintarLog("Busqueda por payment estatus:" + pe.toString());
					if (valor.equals("Partially")) {
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append(" s.id_estatus_pago IN(1) AND s.id_estatus_solicitud = 10  ");
					} else {
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append(" s.id_estatus_pago IN(" + pe + ") ");
					}
					break;
				case "File Type":

					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" ts.nombre = '"+valor+"' ");
					break;
				case "Date":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.fecha_inicio BETWEEN :fechai AND :fechaf ");
					break;
				case "Responsible User":
					UtilidadesAdapter.pintarLog("Busqueda por usuario reponsable:" + valor);
					List<Integer> usE = uPort.encontrarUsuariosPorNombre(valor);
					UtilidadesAdapter.pintarLog("usuarios rev:" + usE.toString());
					if (usE.isEmpty()) {
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append(" usuario_revisando IN(0)  ");
					} else {
						String usEint = usE.toString().replace("[", "").replaceAll("]", "");
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append(" usuario_revisando IN(" + usEint + ")  ");
					}

					break;
				case "Interviewer User":
					UtilidadesAdapter.pintarLog("Busqueda por usuario entrevistador:" + valor);
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" us.nombre like '%"+valor+"%' " 
					+"OR uri.nombre like '%"+valor+"%' " 
					+"OR ucl.nombre like '%"+valor+"%' ");
					break;
				case "Editor User":
					UtilidadesAdapter.pintarLog("Busqueda por usuario editor:" + valor);
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" urt.nombre like '%"+valor+"%' ");
					break;
				case "Waiver":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" waiver = " + valor + " ");
					break;
				case "Assignment editor":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" asignacion_template = " + valor + " ");
					break;
				case "Interview review":
					if(!valor.equals("All")){
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append(" interview_master = " + valor + " ");
					}
					break;
				case "Important":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" importante = 'Important' ");
					break;
				case "No show":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" importante = 'No show' ");
					break;
				case "State":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.estado = '" + valor + "' ");
					break;
				default:
					/*
					 * if (sbW.length() != 0) sbW.append(" AND ");
					 * sbW.append(" s.id_solicitud LIKE '%" + valor +
					 * "%' OR s.cliente LIKE '%" + valor +
					 * "%' OR s.apellidos LIKE '%" + valor +
					 * "%' OR s.telefono lIKE '%" + valor +
					 * "%' GROUP BY id_solicitud,cliente  ");
					 */
					break;
				}
			}
			// }
		}
	
		
		boolean porFechaEvento = false;

		if (!porFechaEvento) {
		if (idSolicitud == 0) {
			if (cerradas.equals("CLOSED")) {

				if (!"".equals(fechai)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.fecha_inicio BETWEEN :fechai AND :fechaf ");
				}
			} else {

				if (primeraVez) {
					// if(!"All".equals(campo)){
					if ("".equals(valor)) {

						if (rol.equals("4")) {
							if (isRevisor) {
								if (sbW.length() != 0)
									sbW.append(" AND ");
								/*sbW.append(" s.usuario_revisando = :idUsuario AND s.id_solicitud "
										+ "NOT IN( SELECT id_solicitud FROM solicitud su WHERE su.usuario_revisando = :idUsuario AND su.id_estatus_pago =1 AND su.id_estatus_solicitud = 10 ) ");*/
								sbW.append(" s.usuario_revisando = :idUsuario AND ( s.usuario_template = 0 || s.id_estatus_solicitud = 10) ");
							} else {
								if (sbW.length() != 0)
									sbW.append(" AND ");
								sbW.append(
										" usuario_revisor = :idUsuario AND s.id_estatus_solicitud NOT IN(11,2,3) OR s.asignacion_int_sc = 0 ");
							}
						} else if (rol.equals("7")) {
							if (sbW.length() != 0)
								sbW.append(" AND ");
							sbW.append(" s.usuario_template = :idUsuario AND s.fin_asg_tmp = 0 ");
						} else if (rol.equals("9")) {
							if (sbW.length() != 0)
								sbW.append(" AND ");
							sbW.append(" usuario_external = :idUsuario  ");
						} else if (rol.equals("5")) {
							if (sbW.length() != 0)
								sbW.append(" AND ");
							sbW.append(" s.usuario_interview = :idUsuario AND s.fin_int_ini = 0 ");
						} else if (rol.equals("8")) {
							if (sbW.length() != 0)
								sbW.append(" AND ");
							sbW.append(" s.usuario_int_sc = :idUsuario AND s.fin_int_sc = 0 ");
						}

					}
				}

				if (!"".equals(fechai)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.fecha_inicio BETWEEN :fechai AND :fechaf ");
				}

			}
			
				if (rol.equals("11")) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" ( s.assigned_clinician = :idUsuario AND s.fin_asg_clnc = 0 ) OR ( s.usuario_interview = :idUsuario AND s.fin_int_ini = 0 )  ");
				}
			
		} else {
			UtilidadesAdapter.pintarLog("Agregar id solicitud al query");
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(" s.id_solicitud = :idSolicitud ");
		}
	}else{
		
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(" e.fecha BETWEEN :fechai AND :fechaf ");
		
	}

		sb.append("\n");
		if (sbW.length() != 0) {
			sb.append(" WHERE " + sbW.toString());
		}

		sb.append("\n");
			sb.append(" GROUP BY id_solicitud,cliente ");
		

		sb.append("\n");
		
		if (!"".equals(ordenarPor)) {
			if ("".equals(orden)) {
				orden = "ASC";
			}
			if (ordenarPor.equals("File")) {
				sb.append(" ORDER BY s.id_solicitud " + orden);
			}
			if (ordenarPor.equals("Creation Date")) {
				sb.append(" ORDER BY fecha_inicio " + orden);
			}
			if (ordenarPor.equals("Due date")) {
				sb.append(" ORDER BY due_date " + orden);
			}
			if (ordenarPor.equals("Additional")) {
				sb.append(" ORDER BY adicional " + orden);
			}
			if (ordenarPor.equals("Importance")) {
				sb.append(" ORDER BY importante " + orden);
			}
			if (ordenarPor.equals("Interview Appointment")) {
				sb.append(" ORDER BY fechaint " + orden);
			}
			if (ordenarPor.equals("Scales Appointment")) {
				sb.append(" ORDER BY fechascale " + orden);
			}
			if (ordenarPor.equals("Interview review")) {
				sb.append(" ORDER BY interview_master " + orden);
			}
			if (ordenarPor.equals("Interview Scale")) {
				sb.append(" ORDER BY fechascale " + orden);
			}

		} else {

			if (rol.equals("1") || rol.equals("3")) {
				sb.append("  ORDER BY s.fecha_inicio DESC ");
			} else if (rol.equals("4")) {
				if (isRevisor) {
					sb.append("  ORDER BY dueDate DESC");
				} else {
					sb.append("  ORDER BY s.fecha_inicio DESC ");
				}
			} else if (rol.equals("5") || rol.equals("7")) {
				sb.append("  ORDER BY fechaint DESC ");
			} else if (rol.equals("9")) {
				sb.append("  ORDER BY s.fecha_inicio DESC ");
			} else if (rol.equals("8")) {
				sb.append("  ORDER BY fechascale DESC ");
			} else {
				sb.append("  ORDER BY s.fecha_inicio DESC ");
			}

		}
	

		UtilidadesAdapter.pintarLog("query:\n" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

		if (!soloUnObjeto) {
			if (!"".equals(fechai)) {
				query.setParameter("fechai", fechai);
				query.setParameter("fechaf", fechaf);
			}

			if (primeraVez) {
			//if(!"All".equals(campo)){
				if("".equals(valor)){
					if (rol.equals("4") || rol.equals("5") || rol.equals("7") || rol.equals("8") || rol.equals("9")) {
						query.setParameter("idUsuario", idUsuario);
					}
				}
			//}
			}
			
			if (rol.equals("11")) {
				query.setParameter("idUsuario", idUsuario);
			}
			
		} else {
			query.setParameter("idSolicitud", idSolicitud);
		}

		List<Object[]> rows = query.getResultList();
		return rows;
	}
	
	private ReporteAnios convertirAReporteAniosN(Object[] row) {
		ReporteAnios s = new ReporteAnios();
		s.setMes((String) row[0]);
		String st = (String) row[1];
		s.setMesNum(Integer.valueOf(st));
		BigInteger ns = (BigInteger) row[2];
		s.setNumSolicitudes(ns.intValue());
		s.setMonto((BigDecimal) row[3]);
		BigInteger an = (BigInteger) row[4];
		s.setAnio(an.intValue());
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerReporteAniosByEstatus(String anios, String estatus,String queryReporte) {

		UtilidadesAdapter.pintarLog("ejecutando query anios by estatus");
		StringBuilder sb = new StringBuilder();
		
		queryReporte = queryReporte.replace("$anios", anios);
		queryReporte = queryReporte.replace("$estatus", estatus);
		
		String[] partes = anios.split(",");
		String anioActual = partes[0].trim(); 
		String anioAnterior = partes[1].trim(); 
		
		queryReporte = queryReporte.replace("$anioActual", anioActual);
		queryReporte = queryReporte.replace("$anioAnterior", anioAnterior);
		sb.append(queryReporte);

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

		List<Object[]> rows = query.getResultList();
		return rows;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerReporteAniosByEvento(String anios, String descripcionEvento, String evento,String queryReporte) {

		UtilidadesAdapter.pintarLog("ejecutando query anios by evento");
		
		UtilidadesAdapter.pintarLog("descripcionEvento:"+descripcionEvento);
		UtilidadesAdapter.pintarLog("evento:"+evento);
		StringBuilder sb = new StringBuilder();
		
		queryReporte = queryReporte.replace("$anios", anios);
		
		String[] partes = anios.split(",");
		String anioActual = partes[0].trim(); 
		String anioAnterior = partes[1].trim(); 
		
		queryReporte = queryReporte.replace("$anioActual", anioActual);
		queryReporte = queryReporte.replace("$anioAnterior", anioAnterior);
		
		if (descripcionEvento != null) {
			String de = " AND e.descripcion LIKE '%"+descripcionEvento+"%'";
			queryReporte = queryReporte.replace("$eventoBuscar", de);
		}
		
		if (evento != null) {
			String e = " AND  e.evento LIKE '%"+evento+"%' ";
			queryReporte = queryReporte.replace("$eventoBuscar", e);
		}
		
		sb.append(queryReporte);

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

		List<Object[]> rows = query.getResultList();
		return rows;
	}
	
	
	private ReporteDash convertirAReporteDash(Object[] row) {
		String st ="";
		ReporteDash s = new ReporteDash();
		st = row[0].toString();
		s.setTodas(Integer.valueOf(st));
		st = row[1].toString();
		s.setActivas(Integer.valueOf(st));
		st = row[2].toString();
		s.setCompletas(Integer.valueOf(st));
		st = row[3].toString();
		s.setLost(Integer.valueOf(st));
		
		UtilidadesAdapter.pintarLog("lost:"+row[4].toString());
		st = row[4].toString();
		
		s.setNoShow(Integer.valueOf(st));
		st = row[5].toString();
		s.setRejectFile(Integer.valueOf(st));
		st = row[6].toString();
		s.setCancelSchedules(Integer.valueOf(st));
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerReporteDash(String fechai, String fechaf,int usuario) {
		
		fechai = fechai + " 00:00:00";
		fechaf = fechaf + " 23:59:59";

		UtilidadesAdapter.pintarLog("ejecutando query dash");
		StringBuilder sb = new StringBuilder();
		
		String byUsuario = "";
		String byUsuario2 = "";
		
		if(usuario > 0){
			
			UsuarioEntity usE = uPort.buscarPorId(usuario);
			if(usE.getRol().equals("4")){
				if(usE.isRevisor()){
					byUsuario = " AND s.usuario_revisor = "+usuario+" ";
				}else{
					byUsuario = "  AND EXISTS (SELECT 1 FROM evento_solicitud e "
					+"WHERE e.id_solicitud = s.id_solicitud "
					+"AND e.usuario = '"+usE.getUsuario()+"' AND e.evento = 'Request creation' ) ";

				}
				byUsuario2 = " AND u.id_usuario = "+usuario+" ";
			}
			if(usE.getRol().equals("5")){
				byUsuario = " AND s.usuario_interview = "+usuario+" ";
				byUsuario2 = " AND u.id_usuario = "+usuario+" ";
			}
			if(usE.getRol().equals("7")){
				byUsuario = " AND s.usuario_template = "+usuario+" ";
				byUsuario2 = " AND u.id_usuario = "+usuario+" ";
			}
			if(usE.getRol().equals("8")){
				byUsuario = " AND s.usuario_int_sc = "+usuario+" ";
				byUsuario2 = " AND u.id_usuario = "+usuario+" ";
			}
			if(usE.getRol().equals("11")){
				byUsuario = " AND s.assigned_clinician = "+usuario+" ";
				byUsuario2 = " AND u.id_usuario = "+usuario+" ";
			}
			
		}
		
		String queryS = "";

		try (Scanner scanner = new Scanner(queryReporteDash.getInputStream(), StandardCharsets.UTF_8.name())) {
			queryS = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}

		if("".equals(byUsuario)){
			queryS = queryS.replace("${byUsuario}", "");
		}else{
			queryS = queryS.replace("${byUsuario}", byUsuario);
		}

		if("".equals(byUsuario2)){
			queryS = queryS.replace("${byUsuario2}", "");
		}else{
			queryS = queryS.replace("${byUsuario2}", byUsuario2);
		}


		sb.append(queryS);

		UtilidadesAdapter.pintarLog("query:\n" + sb.toString().replace(":fechai", "'"+fechai+"'").replace(":fechaf", "'"+fechaf+"'"));

		Query query = entityManager.createNativeQuery(sb.toString());
		
		query.setParameter("fechai", fechai);
		query.setParameter("fechaf", fechaf);

		List<Object[]> rows = query.getResultList();
		UtilidadesAdapter.pintarLog("registros encontrados:" + rows.size());
		return rows;
		
	}


	public int obtenerDashVentas(String fechai, String fechaf,String usuario) {

		
		fechai = fechai + " 00:00:00";
		fechaf = fechaf + " 23:59:59";

		UtilidadesAdapter.pintarLog("ejecutando query dash ventas");
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT count(e.id_solicitud) as ventas FROM evento_solicitud e  ");sb.append("\n "); 
		sb.append("JOIN solicitud s ON s.id_solicitud = e.id_solicitud  ");   sb.append("\n "); 
		sb.append("WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf  AND  s.id_estatus_solicitud IN (1,2,3,10) ");sb.append("\n "); 
		sb.append("AND e.descripcion = 'The request was created' and usuario = :usuario  ");
		

		UtilidadesAdapter.pintarLog("query:\n" + sb.toString().replace(":fechai", "'"+fechai+"'").replace(":fechaf", "'"+fechaf+"'").replace(":usuario", "'"+usuario+"'"));

		Query query = entityManager.createNativeQuery(sb.toString());
		
		query.setParameter("fechai", fechai);
		query.setParameter("fechaf", fechaf);
		query.setParameter("usuario", usuario);

		Number count = (Number) query.getSingleResult();
		int ventas = count != null ? count.intValue() : 0;
		UtilidadesAdapter.pintarLog("ventas encontrados:" + ventas);
		return ventas;
		
	}
	
	private FilesFirmaAbogadoObj convertirAFilesFirmaAbogadoObj(Object[] row) {
		FilesFirmaAbogadoObj s = new FilesFirmaAbogadoObj((Integer) row[0],(String) row[1],(String) row[2],(String) row[3],(String) row[4],(String) row[5],(String) row[6],(Integer) row[7]);
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerSolsDeFirmaYFechasQ(String fechai, String fechaf, String firma, int idAbogado,boolean all) {

		UtilidadesAdapter.pintarLog("ejecutando query anios");
		StringBuilder sb = new StringBuilder();
		StringBuilder sbW = new StringBuilder();

		if (idAbogado > 0) {
			sb.append(
					"SELECT s.id_solicitud as idSolicitud,s.email,s.cliente,s.abogado, s.email_abo_sel as emailAbogado,s.estado,s.firma_de_abogados,s.id_abogado FROM solicitud s  ");
		} else {
			sb.append(
					"SELECT id_solicitud as idSolicitud,email,cliente,abogado, email_abogado as emailAbogado,estado,firma_de_abogados,id_abogado FROM solicitud  ");
		}

		sbW.append("fecha_inicio BETWEEN :fechai AND :fechaf");

		if (idAbogado > 0) {
			if(!all){
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" id_abogado = :idAbogado ");
			}else{
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" id_abogado > 0 ");
			}
		} else {

			if ("".equals(firma)) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" firma_de_abogados = '' AND id_abogado = 0 ");
			} else {
				if(!all){
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" firma_de_abogados = :firma AND id_abogado = 0 ");
				}else{
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" firma_de_abogados != '' AND id_abogado = 0 ");
				}
			}

		}

		if (sbW.length() != 0) {
			sb.append(" WHERE " + sbW.toString());
		}

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

		query.setParameter("fechai", fechai);
		query.setParameter("fechaf", fechaf);

		if (idAbogado > 0) {
			if(!all){
				query.setParameter("idAbogado", idAbogado);
			}
		} else {
			if(!all){
				if (!"".equals(firma)) {
					query.setParameter("firma", firma);
				}
			}
		}

		List<Object[]> rows = query.getResultList();
		return rows;
	}

	public String buildZipCodeFilter(String zipcodes) {
		if (zipcodes == null || zipcodes.trim().isEmpty()) {
			return "";
		}
		String[] parts = zipcodes.split(",");
		StringBuilder sb = new StringBuilder(" (");
		for (int i = 0; i < parts.length; i++) {
			String zip = parts[i].trim();
			if (i > 0) {
				sb.append(" OR ");
			}
			sb.append("s.direccion LIKE '%").append(zip).append("%'");
		}
		sb.append(")");
		return sb.toString();
	}

	public String getApellidosSinNull(String apellidos) {
		String salida = "";
		if (apellidos == null) {
			salida = "";
		} else {
			salida = apellidos;
		}
		return salida;
	}
	
	@Override
	public void actualizarAssignedClinician(int assignedClinician,int idSolicitud){
		reqJpa.actualizarAssignedClinician(assignedClinician, idSolicitud);
	}
	
	@Override
	public List<Solicitud> obtenerSolicitudesDetalleReporteDash(int dash,String fechai, String fechaf,int usuario) {

		List<Solicitud> result = null;

		List<Object[]> rows = obtenerDetalleReporteDash(dash, fechai, fechaf, usuario);
		result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirDetalleReporteDash(row,dash));
		}

		return result;
	}
	
	private Solicitud convertirDetalleReporteDash(Object[] row,int dash) {
		
		Solicitud s = new Solicitud();

		s.setIdSolicitud((Integer) row[0]);
		s.setCliente((String) row[2] == null ? "" : (String) row[2]);
		s.setApellidos((String) row[3]);
		s.setTelefono((String) row[4]);
		s.setIdTipoSolicitud((Integer) row[5]);
		s.setTipoSolicitud((String) row[6] == null ? "" : (String) row[6]);
		s.setEstado((String) row[7]);
		s.setReferencia((String) row[8]);
		s.setIdioma((String) row[9]);
		s.setDireccion((String) row[10]);
		//s.setAmount((BigDecimal) row[11]);
		s.setAbogado((String) row[12]);
		s.setEmail((String) row[13]);
		s.setAdicional((String) row[14]);
		s.setImportantNotes((String) row[15]);
		s.setIdEstatusPago((Integer) row[16]);
		s.setEstatusPago((String) row[17]);
		s.setIdEstatusSolicitud((int) row[18]);
		s.setEstatusSolicitud((String) row[19]);
		s.setWaiver(Integer.valueOf(row[20].toString()) == 0 ? false : true);
		s.setInterviewMaster(Integer.valueOf(row[21].toString()) == 0 ? false : true);
		s.setDueDate((String) row[22]);
		if (dash == 3) {
			s.setMotivo((String) row[23]);
			s.setUsuarioCreacion((String) row[24]);
		}else{
			s.setFecha_schedule((String) row[23]);
			s.setFecha_schedule_scales((String) row[24]);
			s.setUsuarioCreacion((String) row[25]);
			s.setTiposPagosRealizados((String) row[27]);
		}
		

		return s;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerDetalleReporteDash(int dash,String fechai, String fechaf,int usuario) {
		
		fechai = fechai + " 00:00:00";
		fechaf = fechaf + " 23:59:59";

		UtilidadesAdapter.pintarLog("ejecutando query dash");
		StringBuilder sb = new StringBuilder();
		
		String byUsuario = "";
		
		if(usuario > 0){
			
			UsuarioEntity usE = uPort.buscarPorId(usuario);
			if(usE.getRol().equals("4")){
				if(!usE.isRevisor()){
					byUsuario = " AND EXISTS (SELECT 1 FROM evento_solicitud e  WHERE e.id_solicitud = s.id_solicitud AND e.usuario = '"+usE.getUsuario()+"' AND e.evento = 'Request creation') ";
				}else{
				byUsuario = " AND s.usuario_revisor = "+usuario+" ";
				}
				
			}
			if(usE.getRol().equals("5")){
				byUsuario = " AND s.usuario_interview = "+usuario+" ";
			}
			if(usE.getRol().equals("7")){
				byUsuario = " AND s.usuario_template = "+usuario+" ";
			}
			if(usE.getRol().equals("8")){
				byUsuario = " AND s.usuario_int_sc = "+usuario+" ";
			}
			if(usE.getRol().equals("11")){
				byUsuario = " AND s.assigned_clinician = "+usuario+" ";
			}
			
		}
		
		String queryS = "";
		String q1 = "";
		String q2 = "";
		String q3 = "";

		try (Scanner scanner = new Scanner(queryDetallesDash.getInputStream(), StandardCharsets.UTF_8.name())) {
			queryS = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}

		try (Scanner scanner = new Scanner(whereDash1.getInputStream(), StandardCharsets.UTF_8.name())) {
			q1 = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}

		try (Scanner scanner = new Scanner(whereDash2.getInputStream(), StandardCharsets.UTF_8.name())) {
			q2 = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}

		try (Scanner scanner = new Scanner(whereDash3.getInputStream(), StandardCharsets.UTF_8.name())) {
			q3 = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}

		if (dash == 1) {
			queryS = queryS.replace("${WHERE}", q1);
		} else if (dash == 2) {
			queryS = queryS.replace("${WHERE}", q2);
		} else if (dash == 3) {
			queryS = queryS.replace("${WHERE}", q3);
		}


		if("".equals(byUsuario)){
			queryS = queryS.replace("${byUsuario}", "");
		}else{
			queryS = queryS.replace("${byUsuario}", byUsuario);
		}


		sb.append(queryS);

		UtilidadesAdapter.pintarLog("query:" + sb.toString().replace(":fechai", "'"+fechai+"'").replace(":fechaf", "'"+fechaf+"'"));

		Query query = entityManager.createNativeQuery(sb.toString());
		
		query.setParameter("fechai", fechai);
		query.setParameter("fechaf", fechaf);

		List<Object[]> rows = query.getResultList();
		UtilidadesAdapter.pintarLog("registros encontrados:" + rows.size());
		return rows;
		
	}
	
	@Override
	public List<DetalleSolsPorFecha> reporteDetalleSolsFecha(String fechai, String fechaf,int usuario) {
		
		List<Object[]> rows = null;
	    rows = obtenerDetalleSolsPorRango(fechai, fechaf,usuario);
		
	    List<DetalleSolsPorFecha> result = null;

	    result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirADetalleSolsPorFecha(row));
		}

		return result;
		
	}
	
	private DetalleSolsPorFecha convertirADetalleSolsPorFecha(Object[] row) {
		DetalleSolsPorFecha o = new DetalleSolsPorFecha();
		o.setFecha((Date) row[0]);
		BigInteger bi = (BigInteger) row[1];
		o.setNumero(bi.intValue());
		BigInteger a = (BigInteger) row[2];
		o.setActivas(a.intValue());
		BigInteger c = (BigInteger) row[3];
		o.setCerradas(c.intValue());
		BigInteger l = (BigInteger) row[4];
		o.setLost(l.intValue());
		BigInteger ns = (BigInteger) row[5];
		o.setNoshow(ns.intValue());
		return o;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerDetalleSolsPorRango(String fechai, String fechaf,int usuario) {

		UtilidadesAdapter.pintarLog("ejecutando query anios");
		StringBuilder sb = new StringBuilder();


		String byUsuario = "";

		if(usuario > 0){
			
			UsuarioEntity usE = uPort.buscarPorId(usuario);
			if(usE.getRol().equals("4")){
				if(usE.isRevisor()){
					byUsuario = " AND s.usuario_revisor = "+usuario+" ";
				}else{
					byUsuario = "  AND EXISTS (SELECT 1 FROM evento_solicitud e "
					+"WHERE e.id_solicitud = s.id_solicitud "
					+"AND e.usuario = '"+usE.getUsuario()+"' AND e.evento = 'Request creation' ) ";

				}
			}
			if(usE.getRol().equals("5")){
				byUsuario = " AND s.usuario_interview = "+usuario+" ";
			}
			if(usE.getRol().equals("7")){
				byUsuario = " AND s.usuario_template = "+usuario+" ";
			}
			if(usE.getRol().equals("8")){
				byUsuario = " AND s.usuario_int_sc = "+usuario+" ";
			}
			if(usE.getRol().equals("11")){
				byUsuario = " AND s.assigned_clinician = "+usuario+" ";
			}
			
		}

		String queryS = "";

		try (Scanner scanner = new Scanner(queryDetalleSolsAll.getInputStream(), StandardCharsets.UTF_8.name())) {
			queryS = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}

		if("".equals(byUsuario)){
			queryS = queryS.replace("${byUsuario}", "");
		}else{
			queryS = queryS.replace("${byUsuario}", byUsuario);
		}

		sb.append(queryS);

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

		query.setParameter("fechai", fechai);
		query.setParameter("fechaf", fechaf);

		List<Object[]> rows = query.getResultList();
		return rows;
	}
	
	private NumFilesAbogados convertirANumFilesAbo(Object[] row) {
		
		NumFilesAbogados o = new NumFilesAbogados();
		Integer id = (Integer) row[0];
		o.setIdAbogado(id != null ? id : 0);
		String nomb = (String) row[1];
		o.setNombre(nomb);
		String f = (String) row[2];
		o.setFirma(f);
		BigInteger n = (BigInteger) row[3];
		o.setNumero(n.intValue());
		String em = (String) row[4];
		o.setEmails(em);
		
		return o;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerNumFilesAbo(String fechai, String fechaf) {

		StringBuilder sb = new StringBuilder();

		String queryS = "";

		try (Scanner scanner = new Scanner(queryNumFilesAbo.getInputStream(), StandardCharsets.UTF_8.name())) {
			queryS = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}
		queryS = queryS.replace("$fechai", fechai);
		queryS = queryS.replace("$fechaf", fechaf);

		sb.append(queryS);

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

		List<Object[]> rows = query.getResultList();
		return rows;
	}
	
	
	public void actualizarSolicitudConCupon(int idSolicitud){
		reqJpa.actualizarSolicitudConCupon(idSolicitud);
	}

	@Override
	public void actualizarEmailAbo(String emailsAbogado, int idSolicitud) {
		reqJpa.actualizarEmailAbo(emailsAbogado, idSolicitud);
	}

	@Override
	public int esFinEntrevistas(int idSolicitud) {
		return reqJpa.esFinEntrevistas(idSolicitud);	
	}

	@Override
	public int esFinEntrevistasWithoutClinician(int idSolicitud) {
		return reqJpa.esFinEntrevistasWithoutClinician(idSolicitud);	
	}
	

}
