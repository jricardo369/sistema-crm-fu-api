package com.cargosyabonos.adapter.out.sql;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
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
import com.cargosyabonos.application.port.out.EventoSolicitudVocPort;
import com.cargosyabonos.application.port.out.SolicitudVocPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.application.port.out.jpa.MovimientoJpa;
import com.cargosyabonos.application.port.out.jpa.SolicitudVocJpa;
import com.cargosyabonos.domain.EstatusPagoEntity;
import com.cargosyabonos.domain.EstatusSolicitudEntity;
import com.cargosyabonos.domain.NumeroCasosSolicitudes;
import com.cargosyabonos.domain.ReporteSolsUsuario;
import com.cargosyabonos.domain.SolicitudVoc;
import com.cargosyabonos.domain.SolicitudVocEndingSessions;
import com.cargosyabonos.domain.SolicitudVocEntity;
import com.cargosyabonos.domain.UltimoUsuarioRevisor;
import com.cargosyabonos.domain.UsuarioEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SolicitudVocRepository implements SolicitudVocPort{
	
	@Value("classpath:/querys/queryVOC.txt")
    private Resource queryVOC;
	
	@Value("classpath:/querys/queryVOCEndingSessions.txt")
    private Resource queryVOCEndingSessions;

	@Autowired
	SolicitudVocJpa reqJpa;
	
	@Autowired
	MovimientoJpa movJpa;
	
	@Autowired
	EventoSolicitudVocPort ePort;
	
	@Autowired
	private EstatusSolicitudPort esPort;
	
	@Autowired
	private EstatusPagoPort esPPort;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	private UsuariosPort uPort;
	
	 @Autowired
	    private Validator validator; 
	
	@Override
	public List<SolicitudVocEntity> obtenerSolicitudesDeUsuario(int idUsuario,int estatus) {	
			return reqJpa.obtenerTodas();
	}

	@Override
	public SolicitudVocEntity obtenerSolicitud(int idRequest) {
		return reqJpa.findByIdSolicitud(idRequest);
	}

	@Override
	public SolicitudVocEntity obtenerSolicitudByNumeroDeCaso(String numeroCaso) {
		return reqJpa.obtenerSolicitudByNumeroDeCaso(numeroCaso);
	}
	
	@Override
	public SolicitudVocEntity crearSolicitud(SolicitudVocEntity r,int idUsuario,boolean conEvento) {
		
		Set<ConstraintViolation<SolicitudVocEntity>> violations = validator.validate(r);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<SolicitudVocEntity> v : violations) {
                sb.append(v.getPropertyPath()).append(": ").append(v.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(sb.toString());
        }
		
		SolicitudVocEntity e = reqJpa.save(r);
		UsuarioEntity u = uPort.buscarPorId(idUsuario);
		if(idUsuario != 0){
			if(conEvento){
				ePort.ingresarEventoDeSolicitud("Request creation", "The request was created", "Info", u.getUsuario(), e);
			}
		}
		return e;
	}

	@Override
	public void actualizarSolicitud(SolicitudVocEntity r) {
		Set<ConstraintViolation<SolicitudVocEntity>> violations = validator.validate(r);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<SolicitudVocEntity> v : violations) {
                sb.append(v.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(sb.toString());
        }
        
		reqJpa.save(r);
	}

	@Override
	public void eliminarSolicitud(SolicitudVocEntity r) {
		reqJpa.delete(r);
	}

	@Override
	public void actualizarEstatusSolicitud(int idSolicitud, int idEstatus) {
		reqJpa.actualizarEstatusSolicitud(idEstatus,idSolicitud);
	}

	@Override
	public void actualizarEstatusPago(int idEstatusPago, int idSolicitud) {
		reqJpa.actualizarEstatusPago(idEstatusPago, idSolicitud);
	}
	
	public int ageSolicitud(String fi,String ff){
		int df = UtilidadesAdapter.diferenciaDias(fi, ff);
		if(df < 0){
			df = df * -1;
		}else{
			df = 0;
		}
		return df;
	}
	
	@Override
	public List<SolicitudVocEntity> obtenerTodasLasSolicitudes(int estatus) {
		if(estatus != 0){
			return reqJpa.obtenerTodasCerradas(estatus);
		}else{
			return reqJpa.obtenerTodas();
		}
		
	}

	@Override
	public List<SolicitudVoc> obtenerTodasLasSolicitudesV2(UsuarioEntity us, int estatus,int idSolicitud,String fechai,String fechaf,String ordenarPor,String orden,String campo,String valor,boolean myFiles,String cerradas) {

		List<SolicitudVoc> result = null;
		String rol = us.getRol();
		int idUsuario = us.getIdUsuario();
		
		UtilidadesAdapter.pintarLog("estatus:"+estatus+"|idSolicitud:"+idSolicitud);
		if (estatus > 0) {
			if(estatus == 11){
				cerradas = "CLOSED";
			}
			List<Object[]> rows = obtenerSolsVocFiltro(false,idUsuario,rol,0,campo, valor, fechai, fechaf, myFiles, ordenarPor, orden,cerradas);
			result = new ArrayList<>(rows.size());
			for (Object[] row : rows) {
			    result.add(convertirSolQueryASolicitudVoc(row,false));
			}

		} else {

			if (idSolicitud > 0) {
				List<Object[]> rows = obtenerSolsVocFiltro(true,idUsuario,rol,idSolicitud,campo, valor, fechai, fechaf, myFiles, ordenarPor, orden,cerradas);
				result = new ArrayList<>(rows.size());
				for (Object[] row : rows) {
				    result.add(convertirSolQueryASolicitudVoc(row,true));
				}
			} else {
				List<Object[]> rows = obtenerSolsVocFiltro(false,idUsuario,rol,0,campo, valor, fechai, fechaf, myFiles, ordenarPor, orden,cerradas);
				result = new ArrayList<>(rows.size());
				for (Object[] row : rows) {
				    result.add(convertirSolQueryASolicitudVoc(row,false));
				}
			}

		}
		
		return result;
		
	}
	
	@Override
	public List<SolicitudVocEndingSessions> obtenerSolicitudesVOCEndingSessions(){
		List<SolicitudVocEndingSessions> result = null;
		result = obtenerSolsVocEndingSessions().stream()
			    .map(this::convertirSolQueryASolicitudVocEndingSessions)
			    .collect(Collectors.toCollection(ArrayList::new));
		return result;
	}
	
	@Override
	public List<SolicitudVocEntity> obtenerSolicitudesTerapueta(int idTerapeuta) {
		return reqJpa.obtenerSolicitudesTerapeuta(idTerapeuta);
	}
	
	@Override
	public BigDecimal obtenerSumaAmountPorFechayCliente(Date fechai, Date fechaf,String cliente){
		BigDecimal salida = BigDecimal.ZERO;
		if("".equals(cliente)){
			salida = reqJpa.obtenerSumaAmountSolicitudesPorFecha(fechai, fechaf);
		}else{
			salida = reqJpa.obtenerSumaAmountSolicitudesPorFechaYClt(fechai, fechaf, cliente);
		}
		return salida;
	}

	@Override
	public void actualizarUsuarioTerapeuta(int idUsuario, int idSolicitud) {
		reqJpa.actualizarTerapeuta(idUsuario, idSolicitud);
	}

	@Override
	public void actualizarImportante(String importante, int idSolicitud) {
		reqJpa.actualizarImportante(importante, idSolicitud);
	}

	@Override
	public UltimoUsuarioRevisor obtenerUsuarioConMenosRevisiones(int idRol) {
		return reqJpa.obtenerUsuarioConMenosRevisiones(idRol);
	}

	@Override
	public void actualizarNumSesiones(int numSesiones, int numSesPend,int idSolicitud) {
		reqJpa.actualizarNumSesiones(numSesiones, numSesPend,idSolicitud);
	}

	@Override
	public List<SolicitudVocEntity> obtenerSolicitudesActivasTerapueta(int idTerapeuta) {
		return reqJpa.obtenerSolicitudesConCitasTerapeuta(idTerapeuta);
	}

	@Override
	public List<ReporteSolsUsuario> obtenerNumeroSolicitudesDeTemperatura(Date fi, Date ff) {
		return reqJpa.obtenerNumeroCitasTerapueta(fi, ff);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerSolsVocFiltro(boolean soloUnObjeto,int idUsuario,String rol,int idSolicitud,String campo,String valor,String fecha1,String fecha2,boolean myFiles,String ordenarPor,String orden,String cerradas) {

		UtilidadesAdapter.pintarLog("Ejecutando Query");
		StringBuilder sb = new StringBuilder();
		StringBuilder sbW = new StringBuilder();
		
		String queryS = "";

		try (Scanner scanner = new Scanner(queryVOC.getInputStream(), StandardCharsets.UTF_8.name())) {
			queryS = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}

		sb.append(queryS);
		
		if (idSolicitud == 0) {
			
				switch (campo) {
				case "All":
					
					/*if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.numero_de_caso LIKE '%"+valor+"%' ");*/
					
					break;
					
				case "File":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append("s.id_solicitud LIKE '%"+valor+"%' ");
					break;
				case "Customer":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append("s.cliente LIKE '%"+valor+"%' ");
					break;
				case "Case number":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append("s.numero_de_caso LIKE '%"+valor+"%' ");
					break;
				case "Email":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append("s.numero_de_caso LIKE '%"+valor+"%' ");
					break;
				case "File Status":
					UtilidadesAdapter.pintarLog("Busqueda por file estatus:" + valor);
					if (valor.equals("No show") || valor.equals("Won") | valor.equals("Lost")) {
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append("e.descripcion LIKE '%"+valor+"%'  ");
					} else if (valor.equals("Refused")) {
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append("e.evento LIKE '%"+valor+"%' ");
					} else {
		
						String es = "";
						if (valor.equals("All")) {
							es = "1,2,3,10";
						} else {
		
							EstatusSolicitudEntity estSol = esPort.obtenerEstatusSolicitudPorDescripcion(valor);
							es= ""+estSol.getIdEstatusSolicitud();
		
						}
						UtilidadesAdapter.pintarLog("Buscar file estatus:" + es);
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append("s.id_estatus_solicitud IN("+es+") ");
					}
					break;
				case "Payment Status":
		
					String pe = "";
					if (valor.equals("All")) {
						pe = "1,2";
					} else {
						EstatusPagoEntity ep = esPPort.obtenerEstatusPagoPorDescripcion(valor);
						pe = ""+ep.getIdEstatusPago();
					}
		
					UtilidadesAdapter.pintarLog("Busqueda por payment estatus:" + pe.toString());
					if (valor.equals("Partially")) {
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append("s.id_estatus_pago IN(1) AND s.id_estatus_solicitud = 10  ");
					}else{
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append("s.id_estatus_pago IN("+pe+") ");
					}
					break;
				case "Creation Date":
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append("s.fecha_inicio BETWEEN :fechai AND :fechaf ");
					break;
				case "Treatment plan":
					if (sbW.length() != 0)
						sbW.append(" AND ");
						sbW.append("s.documento_2 = "+valor+" ");
					break;
				case "Therapist":
					if (sbW.length() != 0)
						sbW.append(" AND ");
						sbW.append(" ut.nombre LIKE '%"+valor+"%' ");
					break;
				default:
					if (!rol.equals("10")){
					if (sbW.length() != 0)
						sbW.append(" AND ");
						sbW.append("s.id_solicitud LIKE '%"+valor+"%' OR s.cliente LIKE '%"+valor+"%' OR s.apellidos LIKE '%"+valor+"%' OR s.telefono lIKE '%"+valor+"%' OR s.numero_de_caso lIKE '%"+valor+"%' ");
					}
					break;
				}
			
				
		}
		
		if (idSolicitud == 0) {
			UtilidadesAdapter.pintarLog("cerradas:" + cerradas);
			if (cerradas.equals("CLOSED")) {
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.id_estatus_solicitud IN(11) ");
			} else {
				if (rol.equals("6") || rol.equals("10")) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.id_estatus_solicitud NOT IN(11) ");
				}
			}
		} else {
			UtilidadesAdapter.pintarLog("Agregar id solicitud al query");
			sbW.append(" s.id_solicitud = :idSolicitud ");
		}
		
		if (!"".equals(fecha1)) {
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(" s.fecha_inicio BETWEEN :fechai AND :fechaf ");
		}
		
		/*if (myFiles) {
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(" s.fecha_inicio BETWEEN :fechai AND :fechaf ");
		}*/
		
		if(rol.equals("10")){
			if(!soloUnObjeto){
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" terapeuta = :terapeuta ");
			}
		}
		
		sb.append("\n");
		if (sbW.length() != 0) {
				sb.append(" WHERE "+sbW.toString());
		}
		
		sb.append("\n");
		if(!soloUnObjeto){
			sb.append(" GROUP BY id_solicitud ");
		}
		
		sb.append("\n");
		if(!"".equals(ordenarPor)){
			if("".equals(orden)){
				orden = "ASC";
			}
			if(ordenarPor.equals("File")){
				sb.append(" ORDER BY s.id_solicitud "+orden);
			}
			if(ordenarPor.equals("Creation Date")){
				sb.append(" ORDER BY fecha_inicio "+orden);
			}
			if(ordenarPor.equals("Code")){
				sb.append(" ORDER BY code "+orden);
			}
			if(ordenarPor.equals("Customer")){
				sb.append(" ORDER BY cliente "+orden);
			}
			if(ordenarPor.equals("Email")){
				sb.append(" ORDER BY email "+orden);
			}
			if(ordenarPor.equals("documento_2")){
				sb.append(" ORDER BY tipoSolicitud "+orden);
			}
			
		}else{
				sb.append(" ORDER BY s.fecha_inicio DESC ");
		}
		
		UtilidadesAdapter.pintarLog("query:"+sb.toString());
		
		Query query = entityManager.createNativeQuery(sb.toString());
		
		if(idSolicitud != 0){
			query.setParameter("idSolicitud", idSolicitud);
		}
		
		if(rol.equals("10")){
			if(!soloUnObjeto){
				query.setParameter("terapeuta", idUsuario);
			}
		}
		
		if (!"".equals(fecha1)) {
			if (sbW.length() != 0)
				sbW.append(" AND ");
			query.setParameter("fechai", fecha1);
			query.setParameter("fechaf", fecha2);
		}

		List<Object[]> rows = query.getResultList();
		return rows;
		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerSolsVocEndingSessions() {

		UtilidadesAdapter.pintarLog("Ejecutando Query Ending Sessions");
		StringBuilder sb = new StringBuilder();
		
		String queryS = "";

		try (Scanner scanner = new Scanner(queryVOCEndingSessions.getInputStream(), StandardCharsets.UTF_8.name())) {
			queryS = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new RuntimeException("Error al leer el archivo", e);
		}

		sb.append(queryS);
		
		UtilidadesAdapter.pintarLog("query:"+sb.toString());
		
		Query query = entityManager.createNativeQuery(sb.toString());

		List<Object[]> rows = query.getResultList();
		return rows;
		
		
	}
	
	private SolicitudVocEndingSessions convertirSolQueryASolicitudVocEndingSessions(Object[] row) {
		
		SolicitudVocEndingSessions s = new SolicitudVocEndingSessions();
		
		s.setIdSolicitud((Integer)row[0]);
		s.setFechaInicio((Date)row[1]);
		s.setCliente((String)row[2]);
		s.setTelefono((String)row[3]);
		s.setEmail((String)row[4]);
		s.setIdTerapeuta((Integer)row[5]);
		s.setNombreTerapeuta((String)row[6]);
		s.setEmailTerapeuta((String)row[7]);
		s.setNumSesiones((Integer)row[8]);
		BigDecimal ns = (BigDecimal)row[9];
		s.setNumSchedules(ns.intValue());
		BigDecimal sp = (BigDecimal)row[10];
		s.setSesionesPendientes(sp.intValue());	
		
		
		return s;
	}
	
	private SolicitudVoc convertirSolQueryASolicitudVoc(Object[] row,boolean soloUno) {
			
			SolicitudVoc s = new SolicitudVoc();
			StringBuilder sb = new StringBuilder();
			
			s.setIdSolicitud((Integer)row[0]);
			s.setFechaInicio((Date)row[1]);
			s.setCliente((String)row[2]);
			s.setTelefono((String)row[3]);
			s.setEmail((String)row[4]);
			s.setDocusign((String)row[5]);
			s.setAbogado((String)row[6]);
			s.setEmail_abogado((String)row[7]);
			s.setFirmaAbogados((String)row[8]);
			s.setNumeroDeCaso((String)row[9]);
			s.setIdTipoSolicitud((Integer)row[10]);
			s.setIdEstatusSolicitud((Integer)row[11]);
			s.setIdEstatusPago((Integer)row[12]);
			s.setFechaNacimiento((String)row[13]);
			s.setAdicional((String)row[14]);
			s.setIdioma((String)row[15]);
			s.setDireccion((String)row[16]);
			s.setEstado((String)row[17]);
			s.setApellidos((String)row[18]);
			s.setParalegalName((String)row[19]);
			s.setParalegalEmails((String)row[20]);
			s.setParalegalTelefonos((String)row[21]);
			s.setImportante((String)row[22]);
			s.setNumSesiones((Integer)row[23]);
			s.setNumSchedules((Integer)row[24]);
			s.setSesionesPendientes((Integer)row[25]);
			s.setDocumento1(Integer.valueOf(row[26].toString())==0?false:true);
			s.setFechaDoc1((String)row[27]);
			s.setDocumento2(Integer.valueOf(row[28].toString())==0?false:true);
			s.setFechaDoc2((String)row[29]);
			s.setTerapeuta((Integer)row[30]);
			s.setUsuarioRevisando((Integer)row[31]);
			s.setCode((String)row[32]);
			s.setTipoSolicitud((String)row[33]);
			s.setEstatusPago((String)row[34]);
			s.setEstatusSolicitud((String)row[35]);
			s.setNombreTerapeuta((String)row[36]);
			s.setSexo((String)row[37]);
			s.setPurposeTreatament((String)row[38]);
			s.setParticipation((String)row[39]);
			s.setRecommendations((String)row[40]);
			
			LocalDate fechaLimite = LocalDate.of(2025, 11, 1);
	        
			String fi = UtilidadesAdapter.formatearFecha(s.getFechaInicio());
			
			String a = fi.subSequence(0, 4).toString();
			String m = fi.subSequence(5, 7).toString();
			String d = fi.subSequence(8, 10).toString();
			//UtilidadesAdapter.pintarLog("a:"+a+"-m:"+m+"-d:"+d);
			LocalDate fechaInicio = LocalDate.of(Integer.valueOf(a), Integer.valueOf(m), Integer.valueOf(d));

	        if (fechaInicio.isAfter(fechaLimite)) {
	        
	        	//UtilidadesAdapter.pintarLog("Nuevo calculo");
				//Apartir de nov 2025
				BigDecimal ns = (BigDecimal)row[41];
				s.setNumSchedules(ns == null ? 0 : ns.intValue());
				s.setSesionesPendientes(s.getNumSesiones()-s.getNumSchedules());
				BigDecimal ssn = (BigDecimal)row[42];
				s.setNumSesionesSinNota(ssn == null ? 0 : ssn.intValue());
				
			}
			
			
			if (!soloUno) {
				s.setCliente(s.getNombreClienteCompleto());
			}
			
			if(s.getNumSchedules() >= 2){
				if(!s.isDocumento2()){
					sb.append("You have two sessions and document 2 has not been loaded");
				}
			}
			
			if(s.getSesionesPendientes() < 10){
				if (sb.length() != 0){
					sb.append(", there are few sessions left to use");
				}else{
					sb.append("There are few sessions left to use");
				}
				
			}
			
			if(sb.length() != 0){
				s.setInfo(sb.toString());
			}
			
			return s;
			
	}

	@Override
	public List<NumeroCasosSolicitudes> obtenerNumerosCaso(String numeroCaso) {
		return reqJpa.obtenerSolicitudesDeNumeroDecaso(numeroCaso);
	}

	@Override
	public int obtenerSesionesDeSolicitud(int idSolicitud) {
		return reqJpa.obtenerSesionesDeSolicitud(idSolicitud);
	}	
	
}
