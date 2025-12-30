package com.cargosyabonos.adapter.out.sql;

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


@Service
public class SolicitudRepository implements SolicitudPort {

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
		//return reqJpa.obtenerFirmasAbogadosyNumFiles(fechai, fechaf);
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
		s.setImportante((String) row[16]);
		s.setIdEstatusPago((Integer) row[17]);
		s.setEstatusPago((String) row[18]);
		s.setIdEstatusSolicitud((int) row[19]);
		s.setEstatusSolicitud((String) row[20]);
		s.setWaiver(Integer.valueOf(row[21].toString()) == 0 ? false : true);
		s.setInterviewMaster(Integer.valueOf(row[22].toString()) == 0 ? false : true);
		s.setExternal(Integer.valueOf(row[23].toString()) == 0 ? false : true);
		s.setAsignacionTemplate(Integer.valueOf(row[24].toString()) == 0 ? false : true);
		s.setAsignacionIntSc(Integer.valueOf(row[25].toString()) == 0 ? false : true);
		s.setDueDate((String) row[26]);
		s.setNumeroEntrevistas((Integer) row[27]);
		s.setUsuarioIntSc(Integer.valueOf(row[28].toString()));
		s.setUsuarioIntScNombre(s.getUsuarioIntSc() == 0 ? "Unassigned" : (String) row[29]);
		s.setIdUsuarioRevisor((Integer) row[30]);
		s.setUsuarioRevisandoNombre((String) row[31]);
		s.setUsuarioRevisor((String) row[32]);
		s.setIdUsuarioRevisando(Integer.valueOf(row[33].toString()));
		s.setUsuarioRevisandoNombre((String) row[34]);
		s.setUsuarioRevisando((String) row[35]);
		s.setIdUsuarioTemplate(Integer.valueOf(row[36].toString()));
		s.setUsuarioTemplate(s.getIdUsuarioTemplate() == 0 ? "Unassigned" : (String) row[37]);
		s.setIdUsuarioExternal(Integer.valueOf(row[38].toString()));
		s.setUsuarioExternal((Integer) row[39] == null ? 0 : (Integer) row[39]);
		s.setFecha_schedule((String) row[40] == null ? "" : (String) row[40]);
		s.setFecha_schedule_scales((String) row[41] == null ? "" : (String) row[41]);
		s.setTieneScale(Integer.valueOf(row[42].toString()) == 0 ? false : true);
		s.setNumeroDeCaso((String) row[43] == null ? "" : (String) row[43]);
		s.setDocusign((String) row[44] == null ? "" : (String) row[44]);
		s.setEmail_abogado((String) row[45] == null ? "" : (String) row[45]);
		s.setFirmaAbogados((String) row[46] == null ? "" : (String) row[46]);
		s.setFechaNacimiento((String) row[47] == null ? "" : (String) row[47]);
		s.setTipoEntrevista((String) row[48] == null ? "" : (String) row[48]);
		s.setParalegalName((String) row[49] == null ? "" : (String) row[49]);
		s.setParalegalEmails((String) row[50] == null ? "" : (String) row[50]);
		s.setParalegalTelefonos((String) row[51] == null ? "" : (String) row[51]);

		if (soloUno) {

			if (s.getAmount() != null) {
				BigDecimal pending = BigDecimal.ZERO;
				if (s.getAmount().compareTo(BigDecimal.ZERO) == 1) {
					BigDecimal sm = (BigDecimal) row[52];
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
			
			BigDecimal sm = (BigDecimal) row[52];
			s.setPagos(sm);
			s.setCliente(s.getCliente() + " " + this.getApellidosSinNull(s.getApellidos()));

		}
		
		s.setUsuarioInterview(Integer.valueOf(row[53].toString()));
		s.setUsuarioInterviewNombre(s.getUsuarioInterview() == 0 ? "Unassigned" : (String) row[54]);
		s.setFinIntIni(Integer.valueOf(row[55].toString()) == 0 ? false : true);
		s.setFinIntSc(Integer.valueOf(row[56].toString()) == 0 ? false : true);
		s.setAssignedClinician(Integer.valueOf(row[57].toString()));
		s.setIdAbogado(Integer.valueOf(row[58].toString()));
		s.setFinAsgTmp(Integer.valueOf(row[59].toString()) == 0 ? false : true);
		s.setEmailAboSel((String) row[60] == null ? "" : (String) row[60]);
		s.setLostFile(row[61] == null ? false : row[61].toString().equals("x") ? true : false);
		s.setConCupon(row[62] !=  null ? Integer.valueOf(row[62].toString()) == 0 ? false : true : false);
		s.setFechaCupon(row[63] != null ? (String) row[63] == null ? "" : (String) row[63] :  "");
		s.setSexo((String) row[64] == null ? "" : (String) row[64]);
		s.setUsuarioClinicianNombre(s.getAssignedClinician() == 0 ? "Unassigned" : (String) row[65]);
		s.setIdRolUsInt(row[66] == null ? 0 : Integer.valueOf(row[66].toString()));
		s.setFinAsgClnc(Integer.valueOf(row[67].toString()) == 0 ? false : true);
		s.setFechaClinicianAppo((String) row[68] == null ? "" : (String) row[68]);
		

		return s;

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
					sbW.append(" uri.nombre like '%"+valor+"%' " 
					+"OR uri.nombre like '%"+valor+"%' " 
					+"OR ucl.nombre like '%"+valor+"%' ");
					

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
					sbW.append(" estado = '" + valor + "' ");
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
								sbW.append(" s.usuario_revisando = :idUsuario AND s.id_solicitud "
										+ "NOT IN( SELECT id_solicitud FROM solicitud su WHERE su.usuario_revisando = :idUsuario AND su.id_estatus_pago =1 AND su.id_estatus_solicitud = 10 ) ");
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
				byUsuario = " AND s.usuario_revisor = "+usuario+" ";
				byUsuario2 = " AND u.id_usuario = "+usuario+" ";
			}
			if(usE.getRol().equals("5")||usE.getRol().equals("11")){
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
			
		}

		/*sb.append("SELECT s.todas,s.activas, s.completas, s.lost,e.noshow,  e.rejectFile ");
		sb.append("FROM (SELECT "); 
		
		sb.append(" COUNT(s.id_solicitud) AS todas, ");
		sb.append("COUNT(CASE WHEN s.id_estatus_solicitud IN (1, 2, 3,10) "+byUsuario+" THEN 1 END) AS activas, ");
		sb.append("COUNT(CASE WHEN s.id_estatus_solicitud IN (11) "+byUsuario+" THEN 1 END) AS completas ");
		
		sb.append("FROM solicitud s WHERE s.fecha_inicio BETWEEN :fechai AND :fechaf) s ");*/
		
		sb.append("SELECT a.todas,s.activas,s1.cerradas,s2.lost,e.noshow,  e.rejectFile,e.cancelSchedules ");sb.append("\n "); 
		sb.append("FROM ( ");   sb.append("\n "); 
		sb.append("SELECT count(s.id_solicitud) as todas  ");sb.append("\n "); 
		sb.append("FROM solicitud s ");sb.append("\n "); 
		sb.append("WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf   "+byUsuario+" ");sb.append("\n "); 
		sb.append(" ) a "); 
		sb.append("\n "); 
		sb.append("CROSS JOIN ( ");sb.append("\n "); 
		sb.append("SELECT count(s.id_solicitud) as activas ");sb.append("\n "); 
		sb.append("FROM solicitud s ");sb.append("\n "); 
		sb.append("WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf AND  s.id_estatus_solicitud IN (1,2,3,10)  "+byUsuario+" ");sb.append("\n "); 
		sb.append("AND id_solicitud ");sb.append("\n "); 
		sb.append("NOT IN ");sb.append("\n "); 
		sb.append("( "); sb.append("\n "); 
		sb.append("\n "); 
		sb.append("SELECT s.id_solicitud FROM evento_solicitud e ");sb.append("\n "); 
		sb.append("JOIN solicitud s ON s.id_solicitud = e.id_solicitud ");sb.append("\n "); 
		sb.append("WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf  AND  s.id_estatus_solicitud IN (1,2,3,10) ");sb.append("\n "); 
		sb.append("AND e.descripcion = 'Request Lost' group by e.id_solicitud ) ");sb.append("\n "); 
		sb.append(") s ");sb.append("\n "); 
		sb.append("\n "); 
		sb.append("CROSS JOIN ( ");sb.append("\n "); 
		sb.append("SELECT count(*) AS cerradas ");sb.append("\n "); 
		sb.append("FROM solicitud s ");sb.append("\n "); 
		sb.append("WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf AND  s.id_estatus_solicitud IN (11)   "+byUsuario+" ");sb.append("\n "); 
		sb.append("AND id_solicitud ");sb.append("\n "); 
		sb.append("NOT IN ");sb.append("\n "); 
		sb.append("(  ");
		sb.append("\n "); 
		sb.append("SELECT s.id_solicitud FROM evento_solicitud e ");sb.append("\n "); 
		sb.append("JOIN solicitud s ON s.id_solicitud = e.id_solicitud ");sb.append("\n "); 
		sb.append("WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf  AND  s.id_estatus_solicitud IN (11) ");sb.append("\n "); 
		sb.append("AND e.descripcion LIKE '%Request Lost%' group by e.id_solicitud ");sb.append("\n "); 
		sb.append(") ");
		sb.append("\n "); 
		sb.append(") s1   CROSS JOIN ( ");sb.append("\n "); 
		sb.append("SELECT count(*) AS lost ");sb.append("\n "); 
		sb.append("FROM solicitud s ");sb.append("\n "); 
		sb.append("JOIN evento_solicitud e ON e.id_solicitud = s.id_solicitud ");sb.append("\n "); 
		sb.append("WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf ");sb.append("\n "); 
		sb.append("AND e.descripcion LIKE '%Request Lost%' "+byUsuario+" ");sb.append("\n "); 
		sb.append(")  s2 ");    
	
		sb.append("\n "); 
		sb.append("CROSS JOIN (SELECT ");
		sb.append("\n "); 
		sb.append("COUNT(DISTINCT CASE WHEN  e.descripcion like '%No show%' AND (e.descripcion NOT LIKE '%re-scheduled%' AND e.descripcion NOT LIKE '%was changed%') THEN e.id_solicitud END) AS noshow, ");
		sb.append("\n "); 
		sb.append("COUNT(DISTINCT CASE WHEN e.evento = 'Reject File'  THEN e.id_solicitud END) AS rejectFile, ");
		sb.append("\n "); 
		sb.append("COUNT(DISTINCT CASE WHEN  e.evento = 'Update' AND e.tipo = 'Reject File' AND (e.descripcion NOT LIKE '%re-scheduled%' AND e.descripcion NOT LIKE '%was changed%' AND e.descripcion NOT LIKE '%is changed%') THEN e.id_solicitud END) AS cancelSchedules ");
		sb.append("\n "); 
		sb.append("FROM evento_solicitud e LEFT JOIN usuario u on u.usuario = e.usuario WHERE e.fecha BETWEEN :fechai AND :fechaf "+byUsuario2+") e;");
		


		UtilidadesAdapter.pintarLog("query:\n" + sb.toString().replace(":fechai", "'"+fechai+"'").replace(":fechaf", "'"+fechaf+"'"));

		Query query = entityManager.createNativeQuery(sb.toString());
		
		query.setParameter("fechai", fechai);
		query.setParameter("fechaf", fechaf);

		List<Object[]> rows = query.getResultList();
		UtilidadesAdapter.pintarLog("registros encontrados:" + rows.size());
		return rows;
		
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
		s.setImportante((String) row[15]);
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
				byUsuario = " AND s.usuario_revisor = "+usuario+" ";
			}
			if(usE.getRol().equals("5")||usE.getRol().equals("11")){
				byUsuario = " AND s.usuario_interview = "+usuario+" ";
			}
			if(usE.getRol().equals("7")){
				byUsuario = " AND s.usuario_template = "+usuario+" ";
			}
			if(usE.getRol().equals("8")){
				byUsuario = " AND s.usuario_int_sc = "+usuario+" ";
			}
			
		}
		
		if (dash == 1) {
			sb.append("SELECT s.id_solicitud,s.fecha_inicio,s.cliente,s.apellidos,s.telefono,ts.id_tipo_solicitud,ts.nombre tipoSolicitud,s.estado,s.referencia,s.idioma,s.direccion,"
					+ "s.amount,s.abogado,s.email,s.adicional,s.importante,ep.id_estatus_pago,ep.descripcion as descEstPago,es.id_estatus_solicitud,es.descripcion as descEstSol,s.waiver,s.interview_master,s.due_date, "
					+ "(SELECT CONCAT(fecha_schedule,' ',hora_schedule,tipo_schedule) " + "FROM evento_solicitud  "
					+ "WHERE tipo = 'Schedule' AND estatus_schedule = 1  "
					+ "AND id_solicitud = s.id_solicitud AND descripcion LIKE '%Scheduled app%' ORDER BY id_solicitud, fecha DESC LIMIT 1)  as fechaint, "
					+ "(SELECT CONCAT(fecha_schedule,' ',hora_schedule,tipo_schedule) " + "FROM evento_solicitud  "
					+ "WHERE tipo = 'Schedule' AND estatus_schedule = 1  "
					+ "AND id_solicitud = s.id_solicitud AND descripcion LIKE '%Scheduled scales%' ORDER BY id_solicitud, fecha DESC LIMIT 1)  as fechascale,ec.usuario AS usuario_creacion  "
					+ "FROM solicitud s "
					+ "LEFT JOIN tipo_solicitud ts ON ts.id_tipo_solicitud = s.id_tipo_solicitud "
					+ "LEFT JOIN estatus_pago ep ON ep.id_estatus_pago = s.id_estatus_pago "
					+ "LEFT JOIN estatus_solicitud es ON es.id_estatus_solicitud = s.id_estatus_solicitud "
					+ "LEFT JOIN evento_solicitud ec ON ec.id_solicitud = s.id_solicitud "
					+ "AND ec.evento = 'Request creation' "
					+ "WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf AND  s.id_estatus_solicitud IN (1,2,3,10) "
					+ byUsuario + "AND s.id_solicitud  " + "NOT IN  " + "(   "
					+ "SELECT s.id_solicitud FROM evento_solicitud e  "
					+ "JOIN solicitud s ON s.id_solicitud = e.id_solicitud "
					+ "WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf AND  s.id_estatus_solicitud IN (1,2,3,10)  "
					+ "AND e.descripcion = 'Request Lost' group by e.id_solicitud ) ;");
		} else if (dash == 2) {
			sb.append("SELECT s.id_solicitud,s.fecha_inicio,s.cliente,s.apellidos,s.telefono,ts.id_tipo_solicitud,ts.nombre tipoSolicitud,s.estado,s.referencia,s.idioma,s.direccion,"
					+ "s.amount,s.abogado,s.email,s.adicional,s.importante,ep.id_estatus_pago,ep.descripcion as descEstPago,es.id_estatus_solicitud,es.descripcion as descEstSol,s.waiver,s.interview_master,s.due_date, "
					+ "(SELECT CONCAT(fecha_schedule,' ',hora_schedule,tipo_schedule) " + "FROM evento_solicitud  "
					+ "WHERE tipo = 'Schedule' AND estatus_schedule = 1  "
					+ "AND id_solicitud = s.id_solicitud AND descripcion LIKE '%Scheduled app%' ORDER BY id_solicitud, fecha DESC LIMIT 1)  as fechaint, "
					+ "(SELECT CONCAT(fecha_schedule,' ',hora_schedule,tipo_schedule) " + "FROM evento_solicitud  "
					+ "WHERE tipo = 'Schedule' AND estatus_schedule = 1  "
					+ "AND id_solicitud = s.id_solicitud AND descripcion LIKE '%Scheduled scales%' ORDER BY id_solicitud, fecha DESC LIMIT 1)  as fechascale,ec.usuario AS usuario_creacion  "
					+ "FROM solicitud s "
					+ "LEFT JOIN tipo_solicitud ts ON ts.id_tipo_solicitud = s.id_tipo_solicitud "
					+ "LEFT JOIN estatus_pago ep ON ep.id_estatus_pago = s.id_estatus_pago "
					+ "LEFT JOIN estatus_solicitud es ON es.id_estatus_solicitud = s.id_estatus_solicitud "
					+ "LEFT JOIN evento_solicitud ec ON ec.id_solicitud = s.id_solicitud "
					+ "AND ec.evento = 'Request creation' "
					+ "WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf AND  s.id_estatus_solicitud IN (11) " + byUsuario
					+ "AND s.id_solicitud " + "NOT IN  " + "( " + "SELECT s.id_solicitud FROM evento_solicitud e "
					+ "JOIN solicitud s ON s.id_solicitud = e.id_solicitud "
					+ "WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf  AND  s.id_estatus_solicitud IN (11) "
					+ byUsuario + "AND e.descripcion = 'Request Lost' group by e.id_solicitud " + ") ;");
		} else if (dash == 3) {
			sb.append("SELECT s.id_solicitud,s.fecha_inicio,s.cliente,s.apellidos,s.telefono,ts.id_tipo_solicitud,ts.nombre tipoSolicitud,s.estado,s.referencia,s.idioma,s.direccion,"
					+ "s.amount,s.abogado,s.email,s.adicional,s.importante,ep.id_estatus_pago,ep.descripcion as descEstPago,es.id_estatus_solicitud,es.descripcion as descEstSol,s.waiver,s.interview_master,s.due_date,e.descripcion,ec.usuario AS usuario_creacion "
					+ "FROM solicitud s "
					+ "LEFT JOIN tipo_solicitud ts ON ts.id_tipo_solicitud = s.id_tipo_solicitud "
					+ "LEFT JOIN estatus_pago ep ON ep.id_estatus_pago = s.id_estatus_pago "
					+ "LEFT JOIN estatus_solicitud es ON es.id_estatus_solicitud = s.id_estatus_solicitud "
					+ "LEFT JOIN evento_solicitud ec ON ec.id_solicitud = s.id_solicitud "
					+ "AND ec.evento = 'Request creation' "
					+ "JOIN evento_solicitud e ON e.id_solicitud = s.id_solicitud " + byUsuario
					+ "WHERE  s.fecha_inicio BETWEEN :fechai AND :fechaf " + " AND e.descripcion LIKE '%Request Lost%';");
		}


		UtilidadesAdapter.pintarLog("query:" + sb.toString());

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
	    rows = obtenerDetalleSolsPorRango(fechai, fechaf);
		
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
	public List<Object[]> obtenerDetalleSolsPorRango(String fechai, String fechaf) {

		UtilidadesAdapter.pintarLog("ejecutando query anios");
		StringBuilder sb = new StringBuilder();

		String queryS = "";

		try (Scanner scanner = new Scanner(queryDetalleSolsAll.getInputStream(), StandardCharsets.UTF_8.name())) {
			queryS = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}
		queryS = queryS.replace("$fechai", fechai);
		queryS = queryS.replace("$fechaf", fechaf);

		sb.append(queryS);

		/*
		 * sb.
		 * append("SELECT DATE(fecha_inicio) AS fecha,CAST(COUNT(*) AS SIGNED) AS numero_solicitudes "
		 * +"FROM solicitud " +"WHERE fecha_inicio BETWEEN :fechai AND :fechaf "
		 * +"GROUP BY fecha_inicio ORDER BY fecha_inicio; ");
		 */

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

		// query.setParameter("fechai", fechai);
		// query.setParameter("fechaf", fechaf);

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

		UtilidadesAdapter.pintarLog("ejecutando query num files abo");
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
