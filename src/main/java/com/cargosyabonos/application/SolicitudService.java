package com.cargosyabonos.application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.in.SolicitudUseCase;
import com.cargosyabonos.application.port.out.AbogadoPort;
import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.cargosyabonos.application.port.out.DisponibilidadUsuarioPort;
import com.cargosyabonos.application.port.out.EstatusPagoPort;
import com.cargosyabonos.application.port.out.EstatusSolicitudPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.MovimientosPort;
import com.cargosyabonos.application.port.out.MsgPort;
import com.cargosyabonos.application.port.out.ScalePort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.application.port.out.SolicitudVocPort;
import com.cargosyabonos.application.port.out.TipoPagoPort;
import com.cargosyabonos.application.port.out.TipoSolicitudPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.ConfiguracionEntity;
import com.cargosyabonos.domain.DetalleSolsPorFecha;
import com.cargosyabonos.domain.DisponibilidadUsuarioEntity;
import com.cargosyabonos.domain.EstatusPagoEntity;
import com.cargosyabonos.domain.EstatusSolicitudEntity;
import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.FilesFirmaAbogadoObj;
import com.cargosyabonos.domain.MovimientoEntity;
import com.cargosyabonos.domain.NumFilesAbogados;
import com.cargosyabonos.domain.ReporteAnios;
import com.cargosyabonos.domain.ReporteComparacionAnios;
import com.cargosyabonos.domain.ReporteContador;
import com.cargosyabonos.domain.ReporteDash;
import com.cargosyabonos.domain.ReporteSolsDeUsuario;
import com.cargosyabonos.domain.ReporteSolsDeUsuarioObj;
import com.cargosyabonos.domain.ReporteSolsUsuario;
import com.cargosyabonos.domain.ScaleEntity;
import com.cargosyabonos.domain.Solicitud;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.TelefonosSolicitudes;
import com.cargosyabonos.domain.TipoPagoEntity;
import com.cargosyabonos.domain.TipoSolicitudEntity;
import com.cargosyabonos.domain.UsuarioEntity;

import java.lang.reflect.Field;

@Service
@PropertySource(value = "classpath:configuraciones-global.properties")
public class SolicitudService implements SolicitudUseCase {

	Logger log = LoggerFactory.getLogger(SolicitudService.class);

	@Value("${ambiente}")
	private String ambiente;
	
	@Value("${ruta.servidor}")
	private String rutaServidor;

	@Value("${ruta.servidor.qas}")
	private String rutaServidorQas;

	@Value("${ruta.servidor.pro}")
	private String rutaServidorPro;

	@Autowired
	private SolicitudPort reqPort;

	@Autowired
	private SolicitudVocPort reqVocPort;

	@Autowired
	private TipoSolicitudPort tsPort;

	@Autowired
	private TipoPagoPort tpPort;

	@Autowired
	private EstatusSolicitudPort esPort;

	@Autowired
	private MovimientosPort movPort;

	@Autowired
	private EstatusPagoPort epPort;

	@Autowired
	private EventoSolicitudPort evPort;

	@Autowired
	private UsuariosPort usPort;

	@Autowired
	private DisponibilidadUsuarioPort dispPort;
	
	@Autowired
	private AbogadoPort abPort;

	@Autowired
	private ScalePort scPort;

	@Autowired
	private CorreoElectronicoUseCase correoUs;

	@Autowired
	private MsgPort msgPort;

	@Autowired
	private ConfiguracionPort confPort;

	@Override
	public List<Solicitud> obtenerSolicitudesV2(int idUsuario, int estatus, String fechai, String fechaf,
			String ordenarPor, String orden, String campo, String valor, boolean myFiles, String cerradas,
			boolean primeraVez,int usuario) {
		
		if(campo != null){
			if(campo.equals("Phone")){
				valor = valor.replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
				UtilidadesAdapter.pintarLog("valor para phone:"+valor);
			}
		}

		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		List<Solicitud> salida = new ArrayList<>();

		salida = reqPort.obtenerSolicitudesDeUsuarioPorQueryV2(us, estatus, 0, fechai, fechaf, ordenarPor, orden, campo,
				valor, myFiles, cerradas, primeraVez,usuario);

		return salida;

	}

	@Override
	public SolicitudEntity obtenerSolicitud(int idRequest) {
		return reqPort.obtenerSolicitud(idRequest);
	}

	@Override
	public Solicitud obtenerSolicitudObjV2(int idRequest, int idUsuario) {

		List<Solicitud> list = null;
		Solicitud solicitud = null;
		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		List<String> fechas = UtilidadesAdapter.fechasMesYMesAnterior();

		list = reqPort.obtenerSolicitudesDeUsuarioPorQueryV2(us, 0, idRequest, fechas.get(0), fechas.get(1), "", "", "",
				"", false, "NO CLOSED", false,0);
		if (!list.isEmpty()) {
			solicitud = list.get(0);
		}

		return solicitud;
	}

	@Override
	public void crearSolicitud(Solicitud r, int idUsuario) {

		if (r.getTelefono() == null || r.getFechaInicio() == null) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Phone and Creation Date are mandatory");
		}

		UsuarioEntity u = usPort.buscarPorId(idUsuario);

		if (!u.getRol().equals("6")) {

			r.setIdEstatusPago(1);
			r.setIdEstatusSolicitud(1);
			r.setTelefono(r.getTelefono().replaceAll("\\D+", ""));
			int idUs = reqPort.obtenerRevisorConMenorSolicitudes(0);
			r.setIdUsuarioRevisor(idUs);
			r.setIdUsuarioRevisando(idUs);

			SolicitudEntity s = reqPort.crearSolicitud(convertirARequestEntity(r), idUsuario, true);

			// Evneto si tiene comentario
			if (r.getComentario() != null) {
				if (!"".equals(r.getComentario())) {
					evPort.ingresarEventoDeSolicitud("Comment request creation", r.getComentario(), "Info",
							u.getUsuario(), s);
				}
			}

			ConfiguracionEntity confj = confPort.obtenerConfiguracionPorCodigo("MAIL-BVN-CLT");
			UtilidadesAdapter.pintarLog("Se envia mail a cliente?" + confj.getValor());
			boolean exc = Boolean.valueOf(confj.getValor());

			if (exc) {

				// Enviar correo cliente
				UtilidadesAdapter.pintarLog("Datos cliente vacio:" + s.datosClienteEEmailVacios());
				if (s.datosClienteEEmailVacios()) {
				} else {
					correoUs.enviarCorreoNuevaSolicitudCliente(idUsuario, s, false, true);

				}

				UtilidadesAdapter.pintarLog("Tel:" + r.getTelefono() + "|s:" + s.getTelefono());
				if (r.getTelefono() != null) {
					if (!"".equals(r.getTelefono())) {
						msgPort.envioMensaje(r.getTelefono(),
								"Thank you for your call! Please watch this video. We will call you soon! https://drive.google.com/file/d/1GVAXTSvIj9FQRnBJlSg1Y_NpOujM1eZv/view , ¡Gracias por tu llamada! Mira este video. ¡Te llamaremos pronto! https://drive.google.com/file/d/1qSIK1T1AMPL_fzVAR8mCNRJ8qPMJ7RK0/view?usp=sharing",
								s, false);
						evPort.ingresarEventoDeSolicitud("Welcome text message sent", "Thank you text message sent",
								"Info", u.getUsuario(), s);
					}
				}

			} else {
				UtilidadesAdapter.pintarLog("No se envio correo ni mensaje texto de bienvenida a cliente");
			}

			ConfiguracionEntity confbvnAbo = confPort.obtenerConfiguracionPorCodigo("MAIL-BVN-ABO");
			UtilidadesAdapter.pintarLog("Se envia mail a abogado?" + confbvnAbo.getValor());
			boolean excAbo = Boolean.valueOf(confj.getValor());

			if (excAbo) {
				// Enviar correo abogado
				UtilidadesAdapter.pintarLog("Datos abogado vacio:" + s.datosAbogadoVacios());
				if (s.datosAbogadoVacios()) {
					evPort.ingresarEventoDeSolicitud("Email to lawyer not sent",
							"Email to lawyer was not sent due to lack of data", "Info", u.getUsuario(), s);
				} else {
					correoUs.enviarCorreoNotifNuevaSolAAbogado(idUsuario, s, false, true);
					evPort.ingresarEventoDeSolicitud("Email to lawyer sent", "Thank you email sent", "Info",
							u.getUsuario(), s);
				}
			} else {
				UtilidadesAdapter.pintarLog("No se envio correo ni mensaje texto de bienvenida a abogado");
			}

		} else {

			r.setIdEstatusPago(4);
			r.setIdEstatusSolicitud(6);
			r.setTelefono(r.getTelefono().replaceAll("\\D+", ""));
			r.setIdUsuarioRevisor(u.getIdUsuario());
			r.setIdUsuarioRevisando(u.getIdUsuario());
			SolicitudEntity s = reqPort.crearSolicitud(convertirARequestEntity(r), idUsuario, true);
			// Evneto si tiene comentario
			if (r.getComentario() != null) {
				if (!"".equals(r.getComentario())) {
					evPort.ingresarEventoDeSolicitud("Comment request creation", r.getComentario(), "Info",
							u.getUsuario(), s);
				}
			}

		}

	}

	public SolicitudEntity crearSolicitudMasivo(Solicitud r, int idUsuario) {
		if (!"".equals(r.getTelefono())) {
			r.setTelefono(r.getTelefono().replaceAll("\\D+", ""));
		}
		return reqPort.crearSolicitud(convertirARequestEntity(r), idUsuario, false);
	}

	@Override
	public void actualizarSolicitud(Solicitud r, String estatus, int idUsuario) {
		if (estatus != null) {
			if (estatus.equals("cerrado")) {
				UsuarioEntity us = usPort.buscarPorId(idUsuario);
				/*
				 * UtilidadesAdapter.pintarLog("Actualizar datos"); BigDecimal a
				 * = new BigDecimal(r.getAmount().toString()); a.setScale(2,
				 * BigDecimal.ROUND_UP); r.setAmount(a); SolicitudEntity s1 =
				 * convertirARequestEntity(r); SolicitudEntity s2 =
				 * reqPort.obtenerSolicitud(r.getIdSolicitud()); try {
				 * Map<String, String> cambios = comparar(s2, s1);
				 * cambios.forEach((campo, cambio) -> System.out.println(campo +
				 * ": " + cambio)); } catch (IllegalAccessException e) {
				 * e.printStackTrace(); }
				 */
				evPort.ingresarEventoDeSolicitud("Update", "The request has been updated", "Info", us.getUsuario(),
						convertirARequestEntity(r));
			}
			
		}
		/*if(r.getAssignedClinician()>0){
			SolicitudEntity se = reqPort.obtenerSolicitud(r.getIdSolicitud());
			if(se.getAssignedClinician() != r.getAssignedClinician()){
				UsuarioEntity us = usPort.buscarPorId(idUsuario);
				UsuarioEntity use = usPort.buscarPorId(r.getAssignedClinician());
				evPort.ingresarEventoDeSolicitud("Update", "Clinician assigned - "+use.getUsuario(), "Info", us.getUsuario(),
						convertirARequestEntity(r));
			}
		}*/
		if(r.getIdAbogado() == 0){
			r.setEmailAboSel(null);
		}
		r.setTelefono(r.getTelefono().replaceAll("\\D+", ""));
		UtilidadesAdapter.pintarLog("emailAboSel:"+r.getEmailAboSel());
		reqPort.actualizarSolicitud(convertirARequestEntity(r));
	}

	public static Map<String, String> comparar(Object obj1, Object obj2) throws IllegalAccessException {
		Map<String, String> cambios = new HashMap<>();

		Class<?> clazz = obj1.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			Object valor1 = field.get(obj1);
			Object valor2 = field.get(obj2);

			if (!Objects.equals(valor1, valor2)) {
				cambios.put(field.getName(), "Antes: " + valor1 + " | Ahora: " + valor2);
			}
		}

		return cambios;
	}

	@Override
	public void eliminarSolicitud(int r) {
		SolicitudEntity re = reqPort.obtenerSolicitud(r);
		reqPort.eliminarSolicitud(re);
	}

	private SolicitudEntity convertirARequestEntity(Solicitud r) {

		SolicitudEntity s = new SolicitudEntity();

		s.setIdSolicitud(r.getIdSolicitud());
		s.setFechaInicio(r.getFechaInicio());
		s.setCliente(r.getCliente());
		s.setTelefono(r.getTelefono());
		s.setEmail(r.getEmail());
		s.setDocusign(r.getDocusign());
		s.setAmount(r.getAmount());
		s.setAbogado(r.getAbogado());
		s.setEmail_abogado(r.getEmail_abogado());
		s.setNumeroDeCaso(r.getNumeroDeCaso());
		s.setFirmaAbogados(r.getFirmaAbogados());
		TipoSolicitudEntity ts = tsPort.obtenerTipoSolicitudPorId(r.getIdTipoSolicitud());
		s.setTipoSolicitud(ts);
		EstatusSolicitudEntity es = esPort.obtenerEstatusSolicitudPorId(r.getIdEstatusSolicitud());
		s.setEstatusSolicitud(es);
		EstatusPagoEntity ep = epPort.obtenerEstatusPagoPorId(r.getIdEstatusPago());
		s.setEstatusPago(ep);
		s.setUsuarioRevisando(r.getIdUsuarioRevisando());
		s.setUsuarioRevisor(r.getIdUsuarioRevisor());
		s.setFechaNacimiento(r.getFechaNacimiento());
		s.setAdicional(r.getAdicional());
		s.setIdioma(r.getIdioma());
		s.setTipoEntrevista(r.getTipoEntrevista());
		s.setDireccion(r.getDireccion());
		s.setEstado(r.getEstado());
		s.setReferencia(r.getReferencia());
		s.setApellidos(r.getApellidos());
		s.setWaiver(r.isWaiver());
		s.setParalegalName(r.getParalegalName());
		s.setParalegalEmails(r.getParalegalEmails());
		s.setParalegalTelefonos(r.getParalegalTelefonos());
		s.setAsignacionTemplate(r.isAsignacionTemplate());
		s.setNumeroEntrevistas(r.getNumeroEntrevistas());
		s.setNumeroDeCaso(r.getNumeroDeCaso());
		s.setExternal(r.isExternal());
		s.setUsuarioExternal(r.getUsuarioExternal());
		s.setImportante(r.getImportante() == null ? "" : r.getImportante());
		s.setAsignacionIntSc(r.isAsignacionIntSc());
		s.setUsuarioIntSc(r.getUsuarioIntSc());
		s.setInterviewMaster(r.isInterviewMaster());
		s.setDueDate(r.getDueDate());
		s.setAssignedClinician(r.getAssignedClinician());
		s.setUsuarioInterview(r.getUsuarioInterview());
		s.setIdAbogado(r.getIdAbogado());
		s.setFinIntIni(r.isFinIntIni());
		s.setFinIntSc(r.isFinIntSc());
		s.setFinAsgTmp(r.isFinAsgTmp());
		s.setEmailAboSel(r.getEmailAboSel());
		s.setSexo(r.getSexo());
		s.setFinAsgClnc(r.isFinAsgClnc());

		return s;
	}

	/*private SolicitudVocEntity convertirARequestEntityVoc(SolicitudVoc r) {

		SolicitudVocEntity s = new SolicitudVocEntity();

		s.setIdSolicitud(r.getIdSolicitud());
		s.setFechaInicio(r.getFechaInicio());
		s.setCliente(r.getCliente());
		s.setTelefono(r.getTelefono());
		s.setEmail(r.getEmail());
		s.setDocusign(r.getDocusign());
		s.setAbogado(r.getAbogado());
		s.setEmail_abogado(r.getEmail_abogado());
		s.setNumeroDeCaso(r.getNumeroDeCaso());
		s.setFirmaAbogados(r.getFirmaAbogados());
		TipoSolicitudEntity ts = tsPort.obtenerTipoSolicitudPorId(r.getIdTipoSolicitud());
		s.setTipoSolicitud(ts);
		EstatusSolicitudEntity es = esPort.obtenerEstatusSolicitudPorId(r.getIdEstatusSolicitud());
		s.setEstatusSolicitud(es);
		EstatusPagoEntity ep = epPort.obtenerEstatusPagoPorId(r.getIdEstatusPago());
		s.setEstatusPago(ep);
		s.setFechaNacimiento(r.getFechaNacimiento());
		s.setAdicional(r.getAdicional());
		s.setIdioma(r.getIdioma());
		s.setDireccion(r.getDireccion());
		s.setEstado(r.getEstado());
		s.setApellidos(r.getApellidos());
		s.setParalegalName(r.getParalegalName());
		s.setParalegalEmails(r.getParalegalEmails());
		s.setParalegalTelefonos(r.getParalegalTelefonos());
		s.setImportante(r.getImportante());
		s.setNumSesiones(r.getNumSesiones());
		s.setNumSchedules(r.getNumSchedules());
		s.setSesionesPendientes(r.getSesionesPendientes());
		s.setDocumento1(r.isDocumento1());
		s.setFechaDoc1(r.getFechaDoc1());
		s.setDocumento2(r.isDocumento2());
		s.setFechaDoc2(r.getFechaDoc2());
		s.setTerapeuta(r.getTerapeuta());
		s.setUsuarioRevisando(r.getUsuarioRevisando());

		return s;
	}*/

	@Override
	public int obtenerRevisorConMenorSolicitudes() {
		return reqPort.obtenerRevisorConMenorSolicitudes(0);
	}

	@Override
	public void actualizarEstatusSolicitud(int idSolicitud, int idEstatus, int idUsuario, boolean closed,String motivo) {

		UtilidadesAdapter.pintarLog("Closed:" + closed+"|motivo:"+motivo);
		boolean montosCeros = false;

		EstatusSolicitudEntity es = esPort.obtenerEstatusSolicitudPorId(idEstatus);
		UsuarioEntity u = usPort.buscarPorId(idUsuario);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);

		//solo se dejo Lost, won ya no se utiliza
		if (idEstatus == 7) {
			
			if ("".equals(motivo)||motivo == null) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Lost reason cannot be empty");
			} else {
				if (motivo.length() > 250) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"The length of the lost reason cannot be greater than 250");
				}
			}

			evPort.ingresarEventoDeSolicitud("Update", "Request " + es.getDescripcion()+" - reason:"+motivo, "Info", u.getUsuario(), s);
			reqPort.actualizarEstatusSolicitud(idSolicitud, 11);
			reqPort.actualizarImportante("", s.getIdSolicitud());
			reqPort.actualizarAsignacionIntSc(1, 0, idSolicitud);
			
			
		}
		if (idEstatus == 11) {

			if (!closed) {

				BigDecimal amount = s.getAmount() == null ? BigDecimal.ZERO : s.getAmount();
				UtilidadesAdapter
						.pintarLog("email cliente:" + s.getEmail() + " |email abogado:" + s.getEmail_abogado());
				if ("".equals(s.getEmail()) && "".equals(s.getEmail_abogado())) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Customer email and lawyer email are mandatory");
				} else {
					BigDecimal sm = movPort.obtenerSumaMovimientosSolicitud(idSolicitud);

					if (amount.compareTo(BigDecimal.ZERO) == 0 && sm.compareTo(BigDecimal.ZERO) == 0) {
						UtilidadesAdapter.pintarLog("Amount y suma son 0");
						montosCeros = true;
					}
					if (montosCeros) {
						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"The request does not have an assigned amount or does not have payments");
					} else {

						if (amount.compareTo(sm) == 0) {
							// Enviar correo a abogado que ya se cerro la solicitud
							correoUs.enviarCorreoAAbogadoPorFinSolicitud(s);
							correoUs.enviarCorreoAMasterPorFinSolicitud(s);
							evPort.ingresarEventoDeSolicitud("Update status", "Change to status " + es.getDescripcion(), "Info",
									u.getUsuario(), s);
							reqPort.actualizarEstatusSolicitud(idSolicitud, idEstatus);
						} else {
							throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
									"This request has pending payments");

						}
					}
				}
			}
			
			evPort.ingresarEventoDeSolicitud("Update status", "Change to status " + es.getDescripcion(), "Info",
					u.getUsuario(), s);
			reqPort.actualizarEstatusSolicitud(idSolicitud, idEstatus);

		}

	}
	
	@Override
	public void envioInterviewerCaseManager(int idSolicitud, int idUsuarioCambio, int idDisponibilidad,
			boolean fechaAnterior) {
		
		int idUsuarioRevisando = 0;

		UtilidadesAdapter.pintarLog("idSolicitud:" + idSolicitud + " | idUsuarioCambio:" + idUsuarioCambio + " | es fecha anterior:"+fechaAnterior );
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		DisponibilidadUsuarioEntity disp = dispPort.obtenerDisponibilidadPorId(idDisponibilidad);
		EventoSolicitudEntity eventoFirstSchedule = null;

		String tinenInt = evPort.saberQueSchedulesInterviewSeTienen(idSolicitud);
		if(tinenInt != null){
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"This file already have interview for user: "+tinenInt);
		}

		if (disp != null) {
			
			idUsuarioRevisando = disp.getUsuario().getIdUsuario();
			
			@Nonnull
			UsuarioEntity us = disp.getUsuario();
			UsuarioEntity usCambio = usPort.buscarPorId(idUsuarioCambio);
			
			//Validacion en caso que clinician halla sido asignado primero, al asignar el case manager ya no puede ser clinician
			if(s.getAssignedClinician() != 0){
				if(us.getRol().equals("11")){
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"The case manager cannot be assigned to a clinician user because the clinician's interview has already been assigned. "
							+ "If you wish to do so, cancel the clinician's appointment and then assign the case manager as needed.");
				}
			}
			
			UtilidadesAdapter.pintarLog("fechaAnterior:" + fechaAnterior + " | usuario revisando:"+idUsuarioRevisando);
			UtilidadesAdapter.pintarLog("usuario disponibilidad:" + us.getIdUsuario() + " | usuario cambio:"+usCambio.getIdUsuario());

			String fecha = UtilidadesAdapter.formatearFechaUS(disp.getFecha());
			String hora = disp.getHora();
			String tipo = disp.getTipo();
			String zonaHoraria = disp.getZonaHoraria();

			// Convertir a horario del State seleccionado en solicitud, si viene vacio dejar la hora noramal
			boolean horaConvertidaFlag = false;
			String horaConvertida = "";
			String tipoConvertido = "";
			
			if (s.getEstado() != null) {

				if (!s.getEstado().equals("")) {
					horaConvertidaFlag = true;
					horaConvertida = UtilidadesAdapter.convertirAHoraEstado(fecha, hora, tipo,
							s.getEstado(), zonaHoraria);

					UtilidadesAdapter.pintarLog(
							"Se convirtio hora a estado " + s.getEstado() + " la hora " + horaConvertida);
					if ("".equals(horaConvertida)) {
						horaConvertidaFlag = false;
					} else {
						String[] valores = horaConvertida.split(" ");
						horaConvertida = valores[0];
						tipoConvertido = valores[1];
					}
				}
			}

			UtilidadesAdapter.pintarLog("fecha disp:" + fecha + " " + hora);
			// Agregar evento de cita
			zonaHoraria = zonaHoraria == null ? "" : zonaHoraria;
			eventoFirstSchedule = evPort.ingresarEventoScheduleDeSolicitud("Schedule",
					"Scheduled appointment " + fecha + ", " + hora + " " + tipo +" " + zonaHoraria + " to "
							+ disp.getUsuario().getUsuario(),
					"Schedule", usCambio.getUsuario(), disp.getFecha(), disp.getHora(), disp.getTipo(),
					String.valueOf(disp.getUsuario().getIdUsuario()), s, disp.getZonaHoraria());
			
			if(disp.getUsuario().getRol().equals("11")){
				reqPort.actualizarAssignedClinician(us.getIdUsuario(), idSolicitud);
			}

			if (!fechaAnterior) {

				if (s.getEmail() != null) {

					// Enviar notificacion a cliente
					if (horaConvertidaFlag) {
						correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, horaConvertida,
								tipoConvertido, s.getEmail(), s.getIdSolicitud(), "US");

					} else {
						correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, hora, tipo,
								s.getEmail(), s.getIdSolicitud(), "US");
					}

				}

				// Enviar notificacion a entrevisador
				if (us != null) {
					
					correoUs.enviarCorreoCita(us.getNombre(), fecha, hora, tipo, us.getCorreoElectronico(),s.getIdSolicitud(), "US");
					
					// Enviar correo Amanda por cita
					UsuarioEntity usAmanda = usPort.buscarPorUsuario("amanda");
					if (usAmanda != null) {
						correoUs.enviarCorreoPrimeraCita(us.getNombre(), fecha, hora + " " + tipo,usAmanda.getCorreoElectronico(), s);
					}
				}

				// Enviar mensaje a cliente
				if (horaConvertidaFlag) {

					msgPort.envioMensaje(s.getTelefono(),"Your appointment for FamiliasUnidas has been scheduled for: " + fecha + " " + horaConvertida + " " + tipoConvertido,s, false);

				} else {
					msgPort.envioMensaje(s.getTelefono(),
							"Your appointment for FamiliasUnidas has been scheduled for: " + fecha + " "
									+ hora + " " + tipo,s, false);
				}

				// Agregar evento de cita con horario nuevo cliente
				String horaE = horaConvertidaFlag ? horaConvertida : hora;
				String tipoE = horaConvertidaFlag ? tipoConvertido : tipo;
				String estado = s.getEstado() == null ? "" : s.getEstado();
				estado = "".equals(estado) ? "" : " " + s.getEstado() + " time";
				evPort.ingresarEventoDeSolicitud("Info",
						"Customer first appointment " + fecha + " " + horaE + " " + tipoE + estado, "Info",
						usCambio.getUsuario(), s);
				
				EstatusSolicitudEntity es = esPort.obtenerEstatusSolicitudPorId(3);
				reqPort.actualizarEstatusSolicitud(idSolicitud, es.getIdEstatusSolicitud());
				/*if (idUsuarioRevisando != 0) {
					reqPort.actualizarUsuarioRevisando(idUsuarioRevisando, idSolicitud);
				}*/
				
				// Agregar evento
				evPort.ingresarEventoDeSolicitud("Sending process",
						"It was sent to the next process " + es.getDescripcion(), "Info", usCambio.getUsuario(), s);
				
			} else { 
				
				reqPort.actualizarEstatusSolicitud(idSolicitud, 2);
				
				int usRevisor = reqPort.obtenerUsuarioSiEsRevisor(4, 1);
				UtilidadesAdapter.pintarLog("Usuario revisor:" + usRevisor);

				if (usRevisor != 0) {
					idUsuarioRevisando = usRevisor;
				} else {
					idUsuarioRevisando = s.getUsuarioRevisor();
				}
				reqPort.actualizarUsuarioRevisando(idUsuarioRevisando, idSolicitud);
				
				reqPort.actualizarFinInterview(1, idSolicitud);
				evPort.actualizarFinSchedule("1", eventoFirstSchedule.getIdEvento());
				
			}
			
			reqPort.actualizarInterview(us.getIdUsuario(), idSolicitud);

			int imp = evPort.obtenerSolicitudesSiTieneAlgunEventoDeSolicitud("Important", s.getIdSolicitud());
			if (imp != 0) {
				reqPort.actualizarImportante("Important", s.getIdSolicitud());
			} else {
				reqPort.actualizarImportante("", s.getIdSolicitud());
			}

		}else{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error, availability needs to be sent");
		}
		
		
		
		
	}
	
	

	@Override
	public void envioInterviewerScales(int idSolicitud, int idUsuarioCambio, int idDisponibilidad,
			boolean fechaAnterior) {

		int idUsuarioRevisando = 0;

		UtilidadesAdapter.pintarLog("idSolicitud:" + idSolicitud + "/idUsuarioCambio:" + idUsuarioCambio);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		DisponibilidadUsuarioEntity disp = dispPort.obtenerDisponibilidadPorId(idDisponibilidad);
		EventoSolicitudEntity eventoScalesSchedule = null;

		if (disp != null) {
			
			idUsuarioRevisando = disp.getUsuario().getIdUsuario();

			UsuarioEntity us = disp.getUsuario();
			UsuarioEntity usCambio = usPort.buscarPorId(idUsuarioCambio);
			String fecha = UtilidadesAdapter.formatearFechaUS(disp.getFecha());
			String hora = disp.getHora();
			String tipo = disp.getTipo();
			String zonaHoraria = disp.getZonaHoraria();
			UtilidadesAdapter.pintarLog("fecha disp:" + fecha + hora);

			// Convertir a horario del State seleccionado en solicitud, si viene
			// vacio dejar la hora noramal
			boolean horaConvertidaFlag = false;
			String horaConvertida = "";
			String tipoConvertido = "";
			if (s.getEstado() != null) {
				if (!s.getEstado().equals("")) {
					horaConvertidaFlag = true;
					horaConvertida = UtilidadesAdapter.convertirAHoraEstado(fecha, hora, tipo, s.getEstado(),
							zonaHoraria);

					UtilidadesAdapter
							.pintarLog("Se convirtio hora a estado " + s.getEstado() + " la hora " + horaConvertida);
					if ("".equals(horaConvertida)) {
						horaConvertidaFlag = false;
					} else {
						String[] valores = horaConvertida.split(" ");
						horaConvertida = valores[0];
						tipoConvertido = valores[1];
					}
				}
			}

			// Agregar evento
			zonaHoraria = zonaHoraria == null ? "" : zonaHoraria;
			eventoScalesSchedule = evPort.ingresarEventoScheduleDeSolicitud("Schedule",
					"Scheduled scales appointment " + fecha + ", " + hora + " " + tipo + " " + zonaHoraria + " to "
							+ disp.getUsuario().getUsuario(),
					"Schedule", usCambio.getUsuario(), disp.getFecha(), disp.getHora(), disp.getTipo(),
					String.valueOf(disp.getUsuario().getIdUsuario()), s, disp.getZonaHoraria());

			if (!fechaAnterior) {

				if (s.getEmail() != null) {

					// Enviar notificacion a cliente
					if (horaConvertidaFlag) {

						correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, horaConvertida, tipoConvertido,
								s.getEmail(), s.getIdSolicitud(), "US");

					} else {
						correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, hora, tipo, s.getEmail(),
								s.getIdSolicitud(), "US");
					}

					// Enviar notificacion a entrevisador
					correoUs.enviarCorreoCita(us.getNombre(), fecha, hora, tipo, us.getCorreoElectronico(),
							s.getIdSolicitud(), "US");
				}
				// Enviar mensaje a cliente
				if (horaConvertidaFlag) {

					msgPort.envioMensaje(s.getTelefono(), "Your appointment for FamiliasUnidas has been scheduled for: "
							+ fecha + " " + horaConvertida + " " + tipoConvertido, s, false);

				} else {
					msgPort.envioMensaje(s.getTelefono(), "Your appointment for FamiliasUnidas has been scheduled for: "
							+ fecha + " " + hora + " " + tipo, s, false);
				}

				// Agregar evento de cita con horario nuevo cliente
				String horaE = horaConvertidaFlag ? horaConvertida : hora;
				String tipoE = horaConvertidaFlag ? tipoConvertido : tipo;
				String estado = s.getEstado() == null ? "" : s.getEstado();
				estado = "".equals(estado) ? "" : " " + s.getEstado() + " time";
				evPort.ingresarEventoDeSolicitud("Info",
						"Customer scales appointment " + fecha + " " + horaE + " " + tipoE + estado, "Info",
						usCambio.getUsuario(), s);

			} else {

				reqPort.actualizarEstatusSolicitud(idSolicitud, 2);

				int usRevisor = reqPort.obtenerUsuarioSiEsRevisor(4, 1);
				UtilidadesAdapter.pintarLog("Usuario revisor:" + usRevisor);

				if (usRevisor != 0) {
					idUsuarioRevisando = usRevisor;
				} else {
					idUsuarioRevisando = s.getUsuarioRevisor();
				}
				reqPort.actualizarUsuarioRevisando(idUsuarioRevisando, idSolicitud);

				reqPort.actualizarFinIntSc("1", idSolicitud);
				evPort.actualizarFinSchedule("1", eventoScalesSchedule.getIdEvento());

			}

			int ne = s.getNumeroEntrevistas() + 1;
			reqPort.actualizarNumeroEntrevistas(ne, idSolicitud);
			// reqPort.actualizarEstatusSolicitud(idSolicitud, 3);
			reqPort.actualizarAsignacionIntSc(1, us.getIdUsuario(), idSolicitud);

			int imp = evPort.obtenerSolicitudesSiTieneAlgunEventoDeSolicitud("Important", s.getIdSolicitud());
			if (imp != 0) {
				reqPort.actualizarImportante("Important", s.getIdSolicitud());
			} else {
				reqPort.actualizarImportante("", s.getIdSolicitud());
			}

		}
	}
	
	@Override
	public void envioInterviewerClinician(int idSolicitud, int idUsuarioCambio, int idDisponibilidad,
			boolean fechaAnterior) {

		int idUsuarioRevisando = 0;

		UtilidadesAdapter.pintarLog("idSolicitud:" + idSolicitud + "/idUsuarioCambio:" + idUsuarioCambio);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		DisponibilidadUsuarioEntity disp = dispPort.obtenerDisponibilidadPorId(idDisponibilidad);
		EventoSolicitudEntity eventoScalesSchedule = null;

		if (disp != null) {
			
			idUsuarioRevisando = disp.getUsuario().getIdUsuario();

			UsuarioEntity us = disp.getUsuario();
			UsuarioEntity usCambio = usPort.buscarPorId(idUsuarioCambio);
			String fecha = UtilidadesAdapter.formatearFechaUS(disp.getFecha());
			String hora = disp.getHora();
			String tipo = disp.getTipo();
			String zonaHoraria = disp.getZonaHoraria();
			UtilidadesAdapter.pintarLog("fecha disp:" + fecha + hora);

			// Convertir a horario del State seleccionado en solicitud, si viene
			// vacio dejar la hora noramal
			boolean horaConvertidaFlag = false;
			String horaConvertida = "";
			String tipoConvertido = "";
			if (s.getEstado() != null) {

				if (!s.getEstado().equals("")) {

					horaConvertidaFlag = true;
					horaConvertida = UtilidadesAdapter.convertirAHoraEstado(fecha, hora, tipo, s.getEstado(),zonaHoraria);

					UtilidadesAdapter.pintarLog("Se convirtio hora a estado " + s.getEstado() + " la hora " + horaConvertida);
					if ("".equals(horaConvertida)) {
						horaConvertidaFlag = false;
					} else {
						String[] valores = horaConvertida.split(" ");
						horaConvertida = valores[0];
						tipoConvertido = valores[1];
					}

				}
			}

			// Agregar evento
			zonaHoraria = zonaHoraria == null ? "" : zonaHoraria;
			eventoScalesSchedule = evPort.ingresarEventoScheduleDeSolicitud("Schedule",
					"Scheduled clinician appointment " + fecha + ", " + hora + " " + tipo + " " + zonaHoraria + " to "
							+ disp.getUsuario().getUsuario(),
					"Schedule", usCambio.getUsuario(), disp.getFecha(), disp.getHora(), disp.getTipo(),
					String.valueOf(disp.getUsuario().getIdUsuario()), s, disp.getZonaHoraria());

			if (!fechaAnterior) {

				if (s.getEmail() != null) {

					// Enviar notificacion a cliente
					if (horaConvertidaFlag) {

						correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, horaConvertida, tipoConvertido,
								s.getEmail(), s.getIdSolicitud(), "US");

					} else {
						correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, hora, tipo, s.getEmail(),
								s.getIdSolicitud(), "US");
					}

					// Enviar notificacion a entrevisador
					correoUs.enviarCorreoCita(us.getNombre(), fecha, hora, tipo, us.getCorreoElectronico(),
							s.getIdSolicitud(), "US");
				}
				// Enviar mensaje a cliente
				if (horaConvertidaFlag) {

					msgPort.envioMensaje(s.getTelefono(), "Your appointment for FamiliasUnidas has been scheduled for: "
							+ fecha + " " + horaConvertida + " " + tipoConvertido, s, false);

				} else {
					msgPort.envioMensaje(s.getTelefono(), "Your appointment for FamiliasUnidas has been scheduled for: "
							+ fecha + " " + hora + " " + tipo, s, false);
				}

				// Agregar evento de cita con horario nuevo cliente
				String horaE = horaConvertidaFlag ? horaConvertida : hora;
				String tipoE = horaConvertidaFlag ? tipoConvertido : tipo;
				String estado = s.getEstado() == null ? "" : s.getEstado();
				estado = "".equals(estado) ? "" : " " + s.getEstado() + " time";
				evPort.ingresarEventoDeSolicitud("Info",
						"Customer clinician appointment " + fecha + " " + horaE + " " + tipoE + estado, "Info",
						usCambio.getUsuario(), s);

			} else {

				reqPort.actualizarEstatusSolicitud(idSolicitud, 2);
				int usRevisor = reqPort.obtenerUsuarioSiEsRevisor(4, 1);
				UtilidadesAdapter.pintarLog("Usuario revisor:" + usRevisor);

				if (usRevisor != 0) {
					idUsuarioRevisando = usRevisor;
				} else {
					idUsuarioRevisando = s.getUsuarioRevisor();
				}

				reqPort.actualizarUsuarioRevisando(idUsuarioRevisando, idSolicitud);
				reqPort.actualizarFinAsgClnc("1", idSolicitud);
				evPort.actualizarFinSchedule("1", eventoScalesSchedule.getIdEvento());

			}

			int ne = s.getNumeroEntrevistas() + 1;
			reqPort.actualizarNumeroEntrevistas(ne, idSolicitud);
			reqPort.actualizarAsignacionClnc(us.getIdUsuario(), idSolicitud);

			int imp = evPort.obtenerSolicitudesSiTieneAlgunEventoDeSolicitud("Important", s.getIdSolicitud());
			if (imp != 0) {
				reqPort.actualizarImportante("Important", s.getIdSolicitud());
			} else {
				reqPort.actualizarImportante("", s.getIdSolicitud());
			}

		}
	}
	

	@Override
	public void envioSiguienteProceso(int idSolicitud, int idUsuarioCambio, int idDisponibilidad,
			boolean fechaAnterior) {

		UtilidadesAdapter.pintarLog("idSolicitud:" + idSolicitud + "/idUsuarioCambio:" + idUsuarioCambio);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity u = usPort.buscarPorId(idUsuarioCambio);

		DisponibilidadUsuarioEntity disp = null;
		int idUsuarioRevisando = 0;
		int estatusSol = s.getEstatusSolicitud().getIdEstatusSolicitud();
		int estatusNuevo = 0;

		if (s.isExternal()) {
			
			// Recibido a template
			if (estatusSol == 1) {
				estatusNuevo = 2;
				idUsuarioRevisando = reqPort.obtenerRevisorConMenorSolicitudes(1);
			}
			
		} else {
			
			// Recibido a entrevistador
			if (estatusSol == 1) {
				estatusNuevo = 3;
				disp = dispPort.obtenerDisponibilidadPorId(idDisponibilidad);
				idUsuarioRevisando = disp.getUsuario().getIdUsuario();
			}

			// Entrevistador a Revisor
			if (estatusSol == 3) {

				UsuarioEntity uint = usPort.buscarPorId(s.getUsuarioInterview());
				String rolUsuarioCaseManger = uint == null ? "" : uint.getRol();
				
				boolean terminaroCitas = validacionTerminacionCitas(idSolicitud, rolUsuarioCaseManger);
				
				if (!terminaroCitas) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"To move on to the next process, the interviews must be completed.");
				}

				estatusNuevo = 2;
				idUsuarioRevisando = reqPort.obtenerRevisorConMenorSolicitudes(1);
			}

			if (estatusSol == 2) {

				boolean seguir = false;

				if (s.isAsignacionTemplate() == true) {

					// Revisar si case manager es clinician
					UsuarioEntity uuc = usPort.buscarPorId(s.getUsuarioInterview());

					if (uuc.getRol().equals("11")) {

						if (s.isFinIntSc() == false || s.isFinIntIni() == false) {
							throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
									"Interview case manager and scales need to be completed before we can proceed.");
						} else {
							seguir = true;
						}
					} else {

						if (s.isFinIntSc() == false || s.isFinIntIni() == false || s.isFinAsgClnc() == false) {
							throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
									"Interview case manager, scales and clinician need to be completed before we can proceed.");
						} else {
							seguir = true;
						}
					}

				}

				// Validar si es un aprobador o admin, que si esta en estatus 2
				// y no tiene asignacion tempalte mande mensaje
				if (u.getRol().equals("1") || u.getRol().equals("2") || u.getRol().equals("4")) {
					
					if (s.isAsignacionTemplate() == false) {
						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"Editor user has not been assigned; this is necessary to proceed to the next process.");
					}
					

					if(!s.isFinAsgTmp()){
						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"Editor's review is necessary to proceed to the next process.");
					}
				}
				

				if (seguir) {
					estatusNuevo = 10;
					int usRevisor = reqPort.obtenerUsuarioSiEsRevisor(4, 1);
					UtilidadesAdapter.pintarLog("Usuario revisor:" + usRevisor);
					if (usRevisor != 0) {
						idUsuarioRevisando = usRevisor;
					} else {
						idUsuarioRevisando = s.getUsuarioRevisor();
					}
					reqPort.actualizarFinAsgTmp("1", idSolicitud);

				}
			}
		}

		if (estatusSol == 6 || estatusSol == 10) {
			estatusNuevo = 11;
		}
		if (estatusSol == 7 || estatusSol == 8 || estatusSol == 9) {
			estatusNuevo = 1;
		}

		if (estatusNuevo != 0) {

			EstatusSolicitudEntity es = esPort.obtenerEstatusSolicitudPorId(estatusNuevo);

			if (estatusNuevo == 10) {
				if (s.isAsignacionTemplate() == false) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"You must assign a reviewer user to continue.");
				}
			}

			if (estatusNuevo == 11) {

				BigDecimal amount = s.getAmount() == null ? BigDecimal.ZERO : s.getAmount();
				UtilidadesAdapter
						.pintarLog("email cliente:" + s.getEmail() + " |email abogado:" + s.getEmail_abogado());

				if ("".equals(s.getEmail()) && "".equals(s.getEmail_abogado())) {

					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Customer email and lawyer email are mandatory");

				} else {

					boolean montosCeros = false;
					BigDecimal sm = movPort.obtenerSumaMovimientosSolicitud(idSolicitud);

					if (amount.compareTo(BigDecimal.ZERO) == 0 && sm.compareTo(BigDecimal.ZERO) == 0) {

						UtilidadesAdapter.pintarLog("Amount y suma son 0");
						montosCeros = true;

					}
					if (montosCeros) {

						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"The request does not have an assigned amount or does not have payments");

					} else {

						if (amount.compareTo(sm) == 0) {

							int proBono = movPort.tieneProbono(s.getIdSolicitud());

							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										Thread.sleep(5000);
										// UtilidadesAdapter.pintarLog("Se
										// espero 5 segundos para enviar");
										// Enviar correo a abogado que ya se
										// cerro la
										// solicitud
										correoUs.enviarCorreoAAbogadoPorFinSolicitud(s);
										correoUs.enviarCorreoAMasterPorFinSolicitud(s);

										UtilidadesAdapter.pintarLog("proBono" + proBono);
										if (proBono > 0) {
											UtilidadesAdapter.pintarLog("No enviara notificacion de invoice");
										} else {
											UtilidadesAdapter.pintarLog("Se enviara notificacion de invoice");
											correoUs.enviarCorreoInvoice(s.getEmail(), s);
										}
									} catch (InterruptedException e) {
										e.printStackTrace();
									}

								}
							}).start();

							reqPort.actualizarImportante("", s.getIdSolicitud());

						} else {
							throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
									"This request has pending payments");

						}
					}

				}
			}

			reqPort.actualizarEstatusSolicitud(idSolicitud, estatusNuevo);
			if (idUsuarioRevisando != 0) {
				reqPort.actualizarUsuarioRevisando(idUsuarioRevisando, idSolicitud);
			}
			// Agregar evento
			evPort.ingresarEventoDeSolicitud("Sending process",
					"It was sent to the next process " + es.getDescripcion(), "Info", u.getUsuario(), s);

		}

	}
	
	public void envioSiguienteProcesoBackup(int idSolicitud, int idUsuarioCambio, int idDisponibilidad,
			boolean fechaAnterior) {

		UtilidadesAdapter.pintarLog("idSolicitud:" + idSolicitud + "/idUsuarioCambio:" + idUsuarioCambio);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity u = usPort.buscarPorId(idUsuarioCambio);
		EventoSolicitudEntity eventoFirstSchedule = null;

		DisponibilidadUsuarioEntity disp = null;
		int idUsuarioRevisando = 0;
		int estatusNuevo = 0;

		if (u.getRol().equals("8")) {

			if (!s.isFinIntSc()) {
				UtilidadesAdapter.pintarLog("Es SCALE");
				evPort.ingresarEventoDeSolicitud("Info", "Finished the interview scale", "Info", u.getUsuario(), s);
				reqPort.actualizarFinIntSc("1", idSolicitud);
				EventoSolicitudEntity ev = evPort.obtenerUltimoScheduleScales(idSolicitud, "1");
				reqPort.actualizarAsignacionIntSc(1, Integer.valueOf(ev.getUsuarioSchedule()), idSolicitud);
				UtilidadesAdapter.pintarLog("id evento:" + ev.getIdEvento());
				evPort.actualizarFinSchedule("1", ev.getIdEvento());
				
			} else {
				UtilidadesAdapter.pintarLog("Ya se termino scale y volvio a mandar");
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "You have already sent this file.");
			}

		} else {

			if (s.isExternal()) {
				// Recibido a template
				if (s.getEstatusSolicitud().getIdEstatusSolicitud() == 1) {
					estatusNuevo = 2;
					idUsuarioRevisando = reqPort.obtenerRevisorConMenorSolicitudes(1);
				}
			} else {
				// Recibido a entrevistador
				if (s.getEstatusSolicitud().getIdEstatusSolicitud() == 1) {
					estatusNuevo = 3;
					disp = dispPort.obtenerDisponibilidadPorId(idDisponibilidad);
					idUsuarioRevisando = disp.getUsuario().getIdUsuario();
				}
				
				// Entrevistador a Revisor
				if (s.getEstatusSolicitud().getIdEstatusSolicitud() == 3) {
					estatusNuevo = 2;
					idUsuarioRevisando = reqPort.obtenerRevisorConMenorSolicitudes(1);
				}
				
				if (s.getEstatusSolicitud().getIdEstatusSolicitud() == 2) {
					
					boolean seguir = false;
					
					if (s.isAsignacionTemplate() == true) {
					
						//Revisar si case manager es clinician
						UsuarioEntity uuc = usPort.buscarPorId(s.getAssignedClinician());
						
						if(uuc.getRol().equals("11")){

							if (s.isFinIntSc() == false || s.isFinIntIni() == false) {
								throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
										"Interview case manager and scales need to be completed before we can proceed.");
							} else {
								seguir = true;
							}
						}else{

							if (s.isFinIntSc() == false || s.isFinIntIni() == false || s.isFinAsgClnc() == false) {
								throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
										"Interview case manager, scales and clinician need to be completed before we can proceed.");
							} else {
								seguir = true;
							}
						}
						
					}
					
					//Validar si es un aprobador o admin, que si esta en estatus 2 y no tiene asignacion tempalte mande mensaje
					if(u.getRol().equals("1")||u.getRol().equals("2")||u.getRol().equals("4")){
						if (s.isAsignacionTemplate() == false) {
							throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
									"Editor user has not been assigned; this is necessary to proceed to the next process.");
						}
					}

					if (seguir) {
						estatusNuevo = 10;
						int usRevisor = reqPort.obtenerUsuarioSiEsRevisor(4, 1);
						UtilidadesAdapter.pintarLog("Usuario revisor:" + usRevisor);
						if (usRevisor != 0) {
							idUsuarioRevisando = usRevisor;
						} else {
							idUsuarioRevisando = s.getUsuarioRevisor();
						}
						reqPort.actualizarFinAsgTmp("1", idSolicitud);
						
					}
				}
			}
			
			if (s.getEstatusSolicitud().getIdEstatusSolicitud() == 6
					|| s.getEstatusSolicitud().getIdEstatusSolicitud() == 10) {
				estatusNuevo = 11;
			}
			if (s.getEstatusSolicitud().getIdEstatusSolicitud() == 7
					|| s.getEstatusSolicitud().getIdEstatusSolicitud() == 8
					|| s.getEstatusSolicitud().getIdEstatusSolicitud() == 9) {
				estatusNuevo = 1;
			}

			if (estatusNuevo != 0) {

				boolean actualizar = true;
				EstatusSolicitudEntity es = esPort.obtenerEstatusSolicitudPorId(estatusNuevo);

				if (estatusNuevo == 10) {
					if (s.isAsignacionTemplate() == false) {
						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"You must assign a reviewer user to continue.");
					}
				}

				if (estatusNuevo == 11) {

					BigDecimal amount = s.getAmount() == null ? BigDecimal.ZERO : s.getAmount();
					UtilidadesAdapter
							.pintarLog("email cliente:" + s.getEmail() + " |email abogado:" + s.getEmail_abogado());

					if ("".equals(s.getEmail()) && "".equals(s.getEmail_abogado())) {

						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"Customer email and lawyer email are mandatory");

					} else {

						boolean montosCeros = false;
						BigDecimal sm = movPort.obtenerSumaMovimientosSolicitud(idSolicitud);

						if (amount.compareTo(BigDecimal.ZERO) == 0 && sm.compareTo(BigDecimal.ZERO) == 0) {

							UtilidadesAdapter.pintarLog("Amount y suma son 0");
							montosCeros = true;

						}
						if (montosCeros) {

							actualizar = false;
							throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
									"The request does not have an assigned amount or does not have payments");

						} else {

							if (amount.compareTo(sm) == 0) {
								
								int proBono = movPort.tieneProbono(s.getIdSolicitud());

								new Thread(new Runnable() {
									@Override
									public void run() {
										 try {
											Thread.sleep(5000);
											//UtilidadesAdapter.pintarLog("Se espero 5 segundos para enviar");
											// Enviar correo a abogado que ya se cerro la
											// solicitud
											correoUs.enviarCorreoAAbogadoPorFinSolicitud(s);
											correoUs.enviarCorreoAMasterPorFinSolicitud(s);
											
											UtilidadesAdapter.pintarLog("proBono" + proBono);
											if (proBono > 0) {
												UtilidadesAdapter.pintarLog("No enviara notificacion de invoice");
											} else {
												UtilidadesAdapter.pintarLog("Se enviara notificacion de invoice");
												correoUs.enviarCorreoInvoice(s.getEmail(), s);
											}
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										
									}
								}).start();
								
								
								reqPort.actualizarImportante("", s.getIdSolicitud());

							} else {
								actualizar = false;
								throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
										"This request has pending payments");

							}
						}

					}
				}

				if (estatusNuevo == 3) {
					
					String tinenInt = evPort.saberQueSchedulesInterviewSeTienen(idSolicitud);
					if(tinenInt != null){
						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"This file already have interview for user: "+tinenInt);
					}

					if (disp != null) {

						String fecha = UtilidadesAdapter.formatearFechaUS(disp.getFecha());
						String hora = disp.getHora();
						String tipo = disp.getTipo();
						String zonaHoraria = disp.getZonaHoraria();

						// Convertir a horario del State seleccionado en solicitud, si viene vacio dejar la hora noramal
						boolean horaConvertidaFlag = false;
						String horaConvertida = "";
						String tipoConvertido = "";
						if (s.getEstado() != null) {
							if (!s.getEstado().equals("")) {
								horaConvertidaFlag = true;
								horaConvertida = UtilidadesAdapter.convertirAHoraEstado(fecha, hora, tipo,
										s.getEstado(), zonaHoraria);

								UtilidadesAdapter.pintarLog(
										"Se convirtio hora a estado " + s.getEstado() + " la hora " + horaConvertida);
								if ("".equals(horaConvertida)) {
									horaConvertidaFlag = false;
								} else {
									String[] valores = horaConvertida.split(" ");
									horaConvertida = valores[0];
									tipoConvertido = valores[1];
								}
							}
						}

						UtilidadesAdapter.pintarLog("fecha disp:" + fecha + " " + hora);
						// Agregar evento de cita
						zonaHoraria = zonaHoraria == null ? "" : zonaHoraria;
						eventoFirstSchedule = evPort.ingresarEventoScheduleDeSolicitud("Schedule",
								"Scheduled appointment " + fecha + ", " + hora + " " + tipo +" " + zonaHoraria + " to "
										+ disp.getUsuario().getUsuario(),
								"Schedule", u.getUsuario(), disp.getFecha(), disp.getHora(), disp.getTipo(),
								String.valueOf(disp.getUsuario().getIdUsuario()), s, disp.getZonaHoraria());
						
						if(disp.getUsuario().getRol().equals("11")){
							reqPort.actualizarAssignedClinician(disp.getUsuario().getIdUsuario(), idSolicitud);
						}

						if (!fechaAnterior) {

							if (s.getEmail() != null) {

								// Enviar notificacion a cliente
								if (horaConvertidaFlag) {
									correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, horaConvertida,
											tipoConvertido, s.getEmail(), s.getIdSolicitud(), "US");

								} else {
									correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, hora, tipo,
											s.getEmail(), s.getIdSolicitud(), "US");
								}

							}

							// Enviar notificacion a entrevisador
							UsuarioEntity us = usPort.buscarPorId(idUsuarioRevisando);
							if (us != null) {
								
								correoUs.enviarCorreoCita(us.getNombre(), fecha, hora, tipo, us.getCorreoElectronico(),s.getIdSolicitud(), "US");
								
								// Enviar correo Amanda por cita
								UsuarioEntity usAmanda = usPort.buscarPorUsuario("amanda");
								if (usAmanda != null) {
									correoUs.enviarCorreoPrimeraCita(us.getNombre(), fecha, hora + " " + tipo,usAmanda.getCorreoElectronico(), s);
								}
							}

							

							// Enviar mensaje a cliente
							if (horaConvertidaFlag) {

								msgPort.envioMensaje(s.getTelefono(),"Your appointment for FamiliasUnidas has been scheduled for: " + fecha + " " + horaConvertida + " " + tipoConvertido,s, false);

							} else {
								msgPort.envioMensaje(s.getTelefono(),
										"Your appointment for FamiliasUnidas has been scheduled for: " + fecha + " "
												+ hora + " " + tipo,s, false);
							}

							// Agregar evento de cita con horario nuevo cliente
							String horaE = horaConvertidaFlag ? horaConvertida : hora;
							String tipoE = horaConvertidaFlag ? tipoConvertido : tipo;
							String estado = s.getEstado() == null ? "" : s.getEstado();
							estado = "".equals(estado) ? "" : " " + s.getEstado() + " time";
							evPort.ingresarEventoDeSolicitud("Info",
									"Customer first appointment " + fecha + " " + horaE + " " + tipoE + estado, "Info",
									u.getUsuario(), s);

							int imp = evPort.obtenerSolicitudesSiTieneAlgunEventoDeSolicitud("Important",
									s.getIdSolicitud());

							if (imp != 0) {

								reqPort.actualizarImportante("Important", s.getIdSolicitud());

							} else {

								reqPort.actualizarImportante("", s.getIdSolicitud());

							}
						} else {
							actualizar = false;
						}

					}
				}

				if (estatusNuevo == 3) {

					int ne = s.getNumeroEntrevistas() + 1;
					reqPort.actualizarNumeroEntrevistas(ne, idSolicitud);
					reqPort.actualizarInterview(idUsuarioRevisando, idSolicitud);

				}
				

				if (estatusNuevo == 2) {

					EventoSolicitudEntity ev = null;
					// Codigo para agregar el usuario que entrevisto y que ya se termino entrevista
					UsuarioEntity usEntSol = usPort.buscarPorId(s.getUsuarioInterview());	
					if(u.getRol().equals("5")||u.getRol().equals("11")){
						ev = evPort.obtenerUltimoScheduleInterviewByUser(idSolicitud, "1",usEntSol.getIdUsuario());
					}else{
						ev = evPort.obtenerUltimoScheduleInt(idSolicitud, "1");
					}
					
					//reqPort.actualizarInterview(Integer.valueOf(ev.getUsuarioSchedule()), idSolicitud);
					UtilidadesAdapter.pintarLog("id evento:" + ev.getIdEvento());
					evPort.actualizarFinSchedule("1", ev.getIdEvento());
					reqPort.actualizarFinInterview(1, idSolicitud);
					evPort.ingresarEventoDeSolicitud("Info", "Finished the first interview by user "+u.getUsuario(), "Info", u.getNombre(), s);
					UsuarioEntity ui = usPort.buscarPorId(s.getUsuarioInterview());
					if(ui.getRol().equals("11")){
						reqPort.actualizarInterviewMaster("1", idSolicitud);
					}

				}
				

				if (actualizar) {

					reqPort.actualizarEstatusSolicitud(idSolicitud, estatusNuevo);
					if (idUsuarioRevisando != 0) {
						reqPort.actualizarUsuarioRevisando(idUsuarioRevisando, idSolicitud);
					}
					// Agregar evento
					evPort.ingresarEventoDeSolicitud("Sending process",
							"It was sent to the next process " + es.getDescripcion(), "Info", u.getUsuario(), s);

				}

				
				if (fechaAnterior) {

					int usRevisor = reqPort.obtenerUsuarioSiEsRevisor(4, 1);
					UtilidadesAdapter.pintarLog("Usuario revisor:" + usRevisor);

					if (usRevisor != 0) {
						idUsuarioRevisando = usRevisor;
					} else {
						idUsuarioRevisando = s.getUsuarioRevisor();
					}

					reqPort.actualizarEstatusSolicitud(idSolicitud, 2);
					reqPort.actualizarUsuarioRevisando(usRevisor, idSolicitud);
					if (eventoFirstSchedule != null) {
						evPort.actualizarFinSchedule("1", eventoFirstSchedule.getIdEvento());
					}
					reqPort.actualizarFinInterview(1, idSolicitud);

				}
				

			}

		}

	}
	
	@Override
	public void envioFinEntrevistaCaseManager(int idSolicitud,int idUsuarioCambio){
		//reqPort.actualizarInterviewMaster("1", idSolicitud);
		//evPort.actualizarFinScheduleDeSolicitud(idSolicitud);
		
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity u = usPort.buscarPorId(idUsuarioCambio);
		
		if (!s.isFinIntIni()) {
			envioFinEntrevistaCaseManager(idSolicitud, s, u);
			
		} else {
			UtilidadesAdapter.pintarLog("Ya se termino case manager y volvio a mandar");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "You have already sent this file.");
		}
		
	}
	
	public void envioFinEntrevistaCaseManager(int idSolicitud,SolicitudEntity s, UsuarioEntity usCambio){
		UtilidadesAdapter.pintarLog("Es CASE MANAGER");
		evPort.ingresarEventoDeSolicitud("Info", "Finished the first interview by user "+usCambio.getUsuario(), "Info", usCambio.getNombre(), s);
		reqPort.actualizarFinInterview(1, idSolicitud);
		EventoSolicitudEntity ev = evPort.obtenerUltimoScheduleInt(idSolicitud, "1");
		UtilidadesAdapter.pintarLog("id evento:" + ev.getIdEvento());
		evPort.actualizarFinSchedule("1", ev.getIdEvento());
		validacionTerminacionCitasParaEntrevistas(idSolicitud);
	}
	
	@Override
	public void envioFinEntrevistaClinician(int idSolicitud,int idUsuarioCambio){
		//reqPort.actualizarInterviewMaster("1", idSolicitud);
		//evPort.actualizarFinScheduleDeSolicitud(idSolicitud);
		
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity u = usPort.buscarPorId(idUsuarioCambio);
		
		if (!s.isFinAsgClnc()) {
			
			UsuarioEntity uint = usPort.buscarPorId(s.getUsuarioInterview());

			String rolUsCaseManager = uint == null ? "" : uint.getRol();
			
			if(rolUsCaseManager.equals("11")){
				
				envioFinEntrevistaCaseManager(idSolicitud, s, u);
				
			}else{
				
				UtilidadesAdapter.pintarLog("Es CLINICIAN");
				evPort.ingresarEventoDeSolicitud("Info", "Finished the interview clinician", "Info", u.getUsuario(), s);
				reqPort.actualizarFinAsgClnc("1", idSolicitud);
				EventoSolicitudEntity ev = evPort.obtenerUltimoScheduleClinician(idSolicitud, "1");
				UtilidadesAdapter.pintarLog("id evento:" + ev.getIdEvento());
				evPort.actualizarFinSchedule("1", ev.getIdEvento());
				validacionTerminacionCitasParaEntrevistas(idSolicitud);
			
			}

			
			
		} else {
			
			UtilidadesAdapter.pintarLog("Ya se termino clinician y volvio a mandar");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "You have already sent this file.");
			
		}
		
	}

	@Override
	public void parcheFinEntrevistaClinician(int idSolicitud,int idUsuarioCambio){

		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity u = usPort.buscarPorId(idUsuarioCambio);
		
		if (!s.isFinAsgClnc()) {
				
			UtilidadesAdapter.pintarLog("Fin Clnc");
			evPort.ingresarEventoDeSolicitud("Info", "Finished the interview clinician", "Info", u.getUsuario(), s);
			reqPort.actualizarFinAsgClnc("1", idSolicitud);
			//EventoSolicitudEntity ev = evPort.obtenerUltimoScheduleClinician(idSolicitud, "1");
			//UtilidadesAdapter.pintarLog("id evento:" + ev.getIdEvento());
			//evPort.actualizarFinSchedule("1", ev.getIdEvento());
			
		} else {
			
			UtilidadesAdapter.pintarLog("Ya se termino clinician y volvio a mandar");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "You have already sent this file.");
			
		}
	}
	
	@Override
	public void envioFinEntrevistaScales(int idSolicitud,int idUsuarioCambio){
		//reqPort.actualizarInterviewMaster("1", idSolicitud);
		//evPort.actualizarFinScheduleDeSolicitud(idSolicitud);
		
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity u = usPort.buscarPorId(idUsuarioCambio);
		
		if (!s.isFinIntSc()) {
			UtilidadesAdapter.pintarLog("Es SCALE");
			evPort.ingresarEventoDeSolicitud("Info", "Finished the interview scale", "Info", u.getUsuario(), s);
			reqPort.actualizarFinIntSc("1", idSolicitud);
			EventoSolicitudEntity ev = evPort.obtenerUltimoScheduleScales(idSolicitud, "1");
			reqPort.actualizarAsignacionIntSc(1, Integer.valueOf(ev.getUsuarioSchedule()), idSolicitud);
			UtilidadesAdapter.pintarLog("id evento:" + ev.getIdEvento());
			evPort.actualizarFinSchedule("1", ev.getIdEvento());
			validacionTerminacionCitasParaEntrevistas(idSolicitud);
			
		} else {
			UtilidadesAdapter.pintarLog("Ya se termino scale y volvio a mandar");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "You have already sent this file.");
		}
		
	}

	public void validacionTerminacionCitasParaEntrevistas(int idSolicitud) {

		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		

		UsuarioEntity uint = usPort.buscarPorId(s.getUsuarioInterview());

		String rolUsCaseManager = uint == null ? "" : uint.getRol();
		
		if (rolUsCaseManager.equals("11")) {

			int esFinEntrevistas = reqPort.esFinEntrevistasWithoutClinician(idSolicitud);

			if (esFinEntrevistas == 1) {
				UtilidadesAdapter.pintarLog("Todas las entrevistas terminadas");
				int idUsuarioRevisando = reqPort.obtenerRevisorConMenorSolicitudes(1);
				reqPort.actualizarUsuarioRevisando(idUsuarioRevisando, idSolicitud);
			}

		} else {

			int esFinEntrevistas = reqPort.esFinEntrevistas(idSolicitud);

			if (esFinEntrevistas == 1) {
				UtilidadesAdapter.pintarLog("Todas las entrevistas terminadas");
				int idUsuarioRevisando = reqPort.obtenerRevisorConMenorSolicitudes(1);
				reqPort.actualizarUsuarioRevisando(idUsuarioRevisando, idSolicitud);
			}

		}

	}

	public boolean validacionTerminacionCitas(int idSolicitud, String usuarioInterviewRol) {

		boolean salida = false;

		if (usuarioInterviewRol.equals("11")) {

			int esFinEntrevistas = reqPort.esFinEntrevistasWithoutClinician(idSolicitud);

			if (esFinEntrevistas == 1) {
				salida = true;
			}

		} else {

			int esFinEntrevistas = reqPort.esFinEntrevistas(idSolicitud);

			if (esFinEntrevistas == 1) {
				salida = true;
			}

		}

		return salida;

	}

	@Override
	public List<ReporteSolsDeUsuarioObj> obtenerReporteSolicitudesDeUsuario(Date fechai, Date fechaf, int idUsuario) {
		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		UtilidadesAdapter.pintarLog("Usario rol:" + us.getRol());
		List<ReporteSolsDeUsuario> l = null;
		switch (us.getRol()) {
		case "7":
			l = reqPort.obteneSolicitudesAtendidasPorTemplate(fechai, fechaf, idUsuario);
			break;
		case "5":
			l = evPort.obtenerSolicitudesAtendidasPorEntrevistador(fechai, fechaf, idUsuario);
			break;
		case "8":
			l = evPort.obtenerSolicitudesAtendidasPorEntrevistador(fechai, fechaf, idUsuario);
			break;
		case "4":
			if (us.isRevisor()) {
				l = reqPort.obtenerSolicitudesAtendidasPorRevisorAdicional(fechai, fechaf, idUsuario);
			} else {
				l = reqPort.obtenerSolicitudesAtendidasPorRevisor(fechai, fechaf, idUsuario);
			}
			break;
		case "11":
			l = reqPort.obtenerSolicitudesClinician(fechai, fechaf, idUsuario);
			break;
		default:
			break;
		}
		@SuppressWarnings("null")
		List<ReporteSolsDeUsuarioObj> salida = l.stream()
				.map(r -> new ReporteSolsDeUsuarioObj(r.getid_solicitud(), r.getfecha())).collect(Collectors.toList());
		return salida;
	}

	@Override
	public List<ReporteContador> obtenerReporteSolicitudesDeUsuarios(int idUsuario, Date fechai, Date fechaf) {

		UtilidadesAdapter.pintarLog("Usuario:" + idUsuario);
		UsuarioEntity u = usPort.buscarPorId(idUsuario);
		List<ReporteContador> lsalida = new ArrayList<>();
		List<ReporteSolsUsuario> l = evPort.obtenerNumeroSolicitudesAtendidasPorEntrevistadores("Schedule", "1", fechai,
				fechaf);
		UtilidadesAdapter.pintarLog("Entrevsitadores:" + l.size());
		ReporteContador o = null;
		String fechaiFormato = fechai + " 00:00:00";
		String fechafFormato = fechaf + " 23:59:59";

		for (ReporteSolsUsuario r : l) {
			o = new ReporteContador();
			o.setColor(r.getcolor());
			o.setIniciales(UtilidadesAdapter.obtenerIniciales(r.getUsuario()));
			if(r.getimage() != null){
				String rutaServidorFinal = rutaServidorFinal();
				o.setImage(rutaServidorFinal+r.getimage());
				}
			o.setNombre(r.getUsuario());
			o.setNumero(r.getNumero());
			o.setIdUsuario(r.getId_Usuario());
			if (o.getNumero() > 0) {
				lsalida.add(o);
			}
		}

		List<ReporteSolsUsuario> lr = reqPort.obtenerNumeroSolicitudesAtendidasPorRevisores(fechai, fechaf);
		UtilidadesAdapter.pintarLog("Revisores:" + lr.size());
		for (ReporteSolsUsuario r : lr) {
			o = new ReporteContador();
			o.setColor(r.getcolor());
			o.setIniciales(UtilidadesAdapter.obtenerIniciales(r.getUsuario()));
			if(r.getimage() != null){
				String rutaServidorFinal = rutaServidorFinal();
				o.setImage(rutaServidorFinal+r.getimage());
				}
			o.setNombre(r.getUsuario());
			o.setNumero(r.getNumero());
			o.setIdUsuario(r.getId_Usuario());
			if (o.getNumero() > 0) {
				lsalida.add(o);
			}
		}

		List<ReporteSolsUsuario> lrt = reqPort.obtenerNumeroSolicitudesAtendidasPorTemplates(fechaiFormato, fechafFormato);
		UtilidadesAdapter.pintarLog("Templates:" + lrt.size());
		for (ReporteSolsUsuario r : lrt) {
			o = new ReporteContador();
			o.setColor(r.getcolor());
			o.setIniciales(UtilidadesAdapter.obtenerIniciales(r.getUsuario()));
			if(r.getimage() != null){
				String rutaServidorFinal = rutaServidorFinal();
				o.setImage(rutaServidorFinal+r.getimage());
				}
			o.setNombre(r.getUsuario());
			o.setNumero(r.getNumero());
			o.setIdUsuario(r.getId_Usuario());
			if (o.getNumero() > 0) {
				lsalida.add(o);
			}
		}

		List<ReporteSolsUsuario> lrad = reqPort.obtenerNumeroSolicitudesAtendidasPorRevisorAdicional(fechai, fechaf);
		UtilidadesAdapter.pintarLog("Revisors ad:" + lrad.size());
		for (ReporteSolsUsuario r : lrad) {
			o = new ReporteContador();
			o.setColor(r.getcolor());
			o.setIniciales(UtilidadesAdapter.obtenerIniciales(r.getUsuario()));
			if(r.getimage() != null){
			String rutaServidorFinal = rutaServidorFinal();
			o.setImage(rutaServidorFinal+r.getimage());
			}
			o.setNombre(r.getUsuario());
			o.setNumero(r.getNumero());
			o.setIdUsuario(r.getId_Usuario());
			if (o.getNumero() > 0) {
				lsalida.add(o);
			}
		}

		if (u.getRol().equals("2") || u.getRol().equals("6") || u.getRol().equals("4")) {
			List<ReporteSolsUsuario> lrvoc = reqVocPort.obtenerNumeroSolicitudesDeTemperatura(fechai, fechaf);
			UtilidadesAdapter.pintarLog("Terapeutas:" + lrvoc.size());
			for (ReporteSolsUsuario r : lrvoc) {
				o = new ReporteContador();
				o.setColor(r.getcolor());
				o.setIniciales(UtilidadesAdapter.obtenerIniciales(r.getUsuario()));
				if(r.getimage() != null){
					String rutaServidorFinal = rutaServidorFinal();
					o.setImage(rutaServidorFinal+r.getimage());
					}
				o.setNombre(r.getUsuario());
				o.setNumero(r.getNumero());
				o.setIdUsuario(r.getId_Usuario());
				if (o.getNumero() > 0) {
					lsalida.add(o);
				}
			}
		}
		
		
			List<ReporteSolsUsuario> lc = reqPort.obtenerSolicitudesClinicians(fechai, fechaf);
			UtilidadesAdapter.pintarLog("Revisors clinician:" + lc.size());
			for (ReporteSolsUsuario r : lc) {
				o = new ReporteContador();
				o.setColor(r.getcolor());
				o.setIniciales(UtilidadesAdapter.obtenerIniciales(r.getUsuario()));
				if(r.getimage() != null){
					String rutaServidorFinal = rutaServidorFinal();
					o.setImage(rutaServidorFinal+r.getimage());
					}
				o.setNombre(r.getUsuario());
				o.setNumero(r.getNumero());
				o.setIdUsuario(r.getId_Usuario());
				if (o.getNumero() > 0) {
					lsalida.add(o);
				}
			}
		
		return lsalida;
	}

	@Override
	public List<ReporteContador> obtenerReporteMailsAbogados(String fechai, String fechaf) {
		List<String> listaFirmas = reqPort.obtenerFirmasAbogados(fechai, fechaf);
		UtilidadesAdapter.pintarLog("firmas:" + listaFirmas);
		List<ReporteContador> lsalida = new ArrayList<>();
		ReporteContador o = null;
		int nf = 0;
		for (String v : listaFirmas) {
			o = new ReporteContador();
			o.setNombre(v);
			UtilidadesAdapter.pintarLog("nombre:" + v);
			nf = reqPort.obtenerReporteMailsAbogados(v);
			UtilidadesAdapter.pintarLog("nf:" + nf);
			if (nf != 0) {
				o.setNumero(nf);
				lsalida.add(o);
			}
		}
		return lsalida;
	}

	@Override
	public List<NumFilesAbogados> obtenerFirmasAbogadosyNumFiles(String fechai, String fechaf) {
		
		 List<NumFilesAbogados> l = reqPort.obtenerFirmasAbogadosyNumFiles(fechai, fechaf);
		 
		 l.sort(Comparator.comparing(NumFilesAbogados::getNumero).reversed());
		
		return l;
	}

	@Override
	public List<FilesFirmaAbogadoObj> obtenerSolsDeFirmaYFechas(String fechai, String fechaf, String firmaAbogado,int idAbogado,boolean all) {

		System.out.println("firma:" + firmaAbogado + "-idAbogado:" + idAbogado);
		List<FilesFirmaAbogadoObj> l = reqPort.obtenerSolsDeFirmaYFechas(fechai, fechaf, firmaAbogado,idAbogado,all);

		return l;
	}

	@Override
	public void envioReadyOnDraft(int idSolicitud, int idUsuario) {

		UtilidadesAdapter.pintarLog("idSolicitud:" + idSolicitud + "/idUsuario:" + idUsuario);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);

		// Agregar evento
		EstatusSolicitudEntity es = esPort.obtenerEstatusSolicitudPorId(10);
		UsuarioEntity u = usPort.buscarPorId(idSolicitud);
		evPort.ingresarEventoDeSolicitud("Sending process",
				"It was force to sent to the next process " + es.getDescripcion(), "Info", u.getUsuario(), s);

		reqPort.actualizarEstatusSolicitud(idSolicitud, 10);

		UtilidadesAdapter.pintarLog("email cliente:" + s.getEmail() + " |email abogado:" + s.getEmail_abogado());
		if ("".equals(s.getEmail()) || "".equals(s.getEmail_abogado())) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Customer email and lawyer email are mandatory");
		} else {
			// Enviar correo a abogado que ya se cerro la solicitud
			correoUs.enviarCorreoAAbogadoPorFinSolicitud(s);
			correoUs.enviarCorreoAMasterPorFinSolicitud(s);
		}

	}

	public void reasginar(int idUsuario, String motivo, int idSolicitud, int idUsuarioEnvio) {

		UsuarioEntity usEnvio = usPort.buscarPorId(idUsuarioEnvio);
		if (!usEnvio.getRol().equals("8")) {
			reqPort.actualizarUsuarioRevisando(idUsuario, idSolicitud);
		}

		if (motivo != null) {

			UtilidadesAdapter.pintarLog("Con motivo");

			if (!"".equals(motivo)) {

				UtilidadesAdapter.pintarLog("Motivo lleno");

				if ("".equals(motivo)) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Rejection reason cannot be empty");
				} else {
					if (motivo.length() > 250) {
						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"The length of the rejection reason cannot be greater than 250");
					}
				}

				SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);

				evPort.ingresarEventoDeSolicitud("Reject File", motivo, "Reject File", usEnvio.getUsuario(), s);

				EventoSolicitudEntity evSol = null;
				if (usEnvio.getRol().equals("5")||usEnvio.getRol().equals("11")) {
					//evSol = evPort.obtenerUltimoScheduleInt(idSolicitud, "1");
					evSol = evPort.obtenerUltimoScheduleInterviewByUser(idSolicitud, "1", idUsuarioEnvio);
				} else if (usEnvio.getRol().equals("8")) {
					evSol = evPort.obtenerUltimoScheduleScales(idSolicitud, "1");
				}

				if (evSol != null) {
					// Enviar mail
					correoUs.enviarCorreoNoShow(usEnvio.getCorreoElectronico(), usEnvio.getNombre(), s, evSol);
					// Enviar mensaje texto
					if (s.getTelefono() != null) {
						if (!"".equals(s.getTelefono())) {

							if (evSol != null) {
								String f = UtilidadesAdapter.formatearFecha(evSol.getFechaSchedule()) + " "
										+ evSol.getHoraSchedule() + " " + evSol.getTipoSchedule();
								msgPort.envioMensaje(s.getTelefono(),
										" The client " + s.getCliente()
												+ " did not show up for the interview scheduled for request "
												+ s.getIdSolicitud() + " in the date " + f + ".",
										s, false);
							}
						}
					}

					UtilidadesAdapter.pintarLog("Actualizar evento solicitud:" + evSol.getIdEvento());
					evSol.setEstatusSchedule("0");
					evPort.actualizarEventoSolicitud(evSol);
					
					// Si es rol 8 (interviewer scale)
					if (usEnvio.getRol().equals("5")||usEnvio.getRol().equals("11")) {
						reqPort.actualizarFinInterview(0, idSolicitud);
						reqPort.actualizarAsignacionIntReset(idSolicitud);
					}
					if (usEnvio.getRol().equals("8")) {
						reqPort.actualizarAsignacionIntScReset(idSolicitud);
						reqPort.actualizarFinIntSc("0", idSolicitud);
					}
					
				}
				
				if (usEnvio.getRol().equals("11")) {	
					
					if(usEnvio.getIdUsuario() == s.getAssignedClinician()){
						
						reqPort.actualizarAssignedClinician(0, idSolicitud);
						
					}
					
				}

				if (!usEnvio.getRol().equals("8")) {
					
					if (s.getEstatusSolicitud().getIdEstatusSolicitud() == 2) {
						// Actualizar estatus a Reviewing
						reqPort.actualizarEstatusSolicitud(idSolicitud, 2);
						reqPort.actualizarAsignacionTemplate(0, 0, idSolicitud);
					} else {
						// Actualizar estatus a Received
						reqPort.actualizarEstatusSolicitud(idSolicitud, 1);
					}

				}

				if (usEnvio.getRol().equals("5") || usEnvio.getRol().equals("8")) {
					int ne = s.getNumeroEntrevistas() - 1;
					reqPort.actualizarNumeroEntrevistas(ne, idSolicitud);
				}

			}

			if (usEnvio.getRol().equals("7")) {

				UtilidadesAdapter.pintarLog("actualizar asignacion template");
				reqPort.actualizarAsignacionTemplate(0, 0, idSolicitud);

			}

		} else {

			UtilidadesAdapter.pintarLog("Sin motivo");

			SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
			UsuarioEntity us = usPort.buscarPorId(idUsuario);

			if (us.getRol().equals("7")) {
				UtilidadesAdapter.pintarLog("actualizar asignacion template");
				reqPort.actualizarAsignacionTemplate(1, idUsuario, idSolicitud);
				reqPort.actualizarEstatusSolicitud(idSolicitud, 2);
			}

			evPort.ingresarEventoDeSolicitud("File Assignment", "File was assigned to user " + us.getNombre(), "Info",
					usEnvio.getUsuario(), s);
			correoUs.enviarCorreoAsignacion(s, us.getNombre(), us.getCorreoElectronico());

		}

	}
	
	public void noShow(String motivo, int idSolicitud, int idUsuarioEnvio) {
		
		UsuarioEntity u = usPort.buscarPorId(idUsuarioEnvio);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		
		if ("".equals(motivo)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"No show reason cannot be empty");
		} else {
			if (motivo.length() > 250) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"The length of the no show reason cannot be greater than 250");
			}
		}

		evPort.ingresarEventoDeSolicitud("Update", "Status changed to no show, reason: " + motivo, "No show",
				u.getUsuario(), s);

		String rol = u.getRol();
		boolean actualizarUsuarioRevisando = true;

		boolean esClinicianComoCaseManager = false;

		
		UsuarioEntity rolInt = usPort.buscarPorId(s.getUsuarioInterview());
		String rolUsCasManager = rolInt == null ? "" : rolInt.getRol();
		if (rolUsCasManager.equals("11")) {
			esClinicianComoCaseManager = true;
		}

		EventoSolicitudEntity evSol = null;
		if (rol.equals("5")) {
			evSol = evPort.obtenerUltimoScheduleInterviewByUser(idSolicitud, "1", idUsuarioEnvio);
		} else if (rol.equals("8")) {
			evSol = evPort.obtenerUltimoScheduleScales(idSolicitud, "1");
		} else if (rol.equals("11")) {
			if(esClinicianComoCaseManager){
				evSol = evPort.obtenerUltimoScheduleInterviewByUser(idSolicitud, "1", idUsuarioEnvio);
			}else{
				evSol = evPort.obtenerUltimoScheduleInterviewClinicianByUser(idSolicitud, "1", idUsuarioEnvio);
			}	
		}

		if (evSol != null) {

			reqPort.actualizarImportante("No show", s.getIdSolicitud());
			UsuarioEntity uRev = usPort.buscarPorId(s.getUsuarioRevisor());
			// Enviar mail
			correoUs.enviarCorreoNoShow(uRev.getCorreoElectronico(), uRev.getNombre(), s, evSol);
			// Enviar mensaje texto
			if (s.getTelefono() != null) {
				if (!"".equals(s.getTelefono())) {

					if (evSol != null) {
						
						String f = UtilidadesAdapter.formatearFecha(evSol.getFechaSchedule()) + " "
								+ evSol.getHoraSchedule() + " " + evSol.getTipoSchedule();
						
						msgPort.envioMensaje(uRev.getTelefono(),
								" The client " + s.getCliente()
										+ " did not show up for the interview scheduled for request "
										+ s.getIdSolicitud() + " in the date " + f + ".",
								s, false);
					}
				}
			}

			UtilidadesAdapter.pintarLog("Actualizar evento solicitud:" + evSol.getIdEvento());
			evSol.setEstatusSchedule("0");
			evPort.actualizarEventoSolicitud(evSol);
		}

		if (rol.equals("8")) {
			actualizarUsuarioRevisando = false;
		}

		if (actualizarUsuarioRevisando) {
			s.setUsuarioRevisando(s.getUsuarioRevisor());
			reqPort.actualizarEstatusSolicitud(idSolicitud, 1);
		}
		
		reqPort.actualizarSolicitud(s);


		if (rol.equals("11")) {

			if(esClinicianComoCaseManager){
				reqPort.actualizarFinAsgClnc("0", idSolicitud);
			}else{
				reqPort.actualizarAssignedClinician(0, idSolicitud);
			}

		}
		if (rol.equals("8")) {
			reqPort.actualizarAsignacionIntScReset(idSolicitud);
		}
		if (rol.equals("5") ) {
			reqPort.actualizarAsignacionIntReset(idSolicitud);
		}

		reqPort.actualizarImportante("No show", s.getIdSolicitud());
				

	}
	
	public void cancelTemplate(int idUsuario, String motivo, int idSolicitud, int idUsuarioEnvio){
	
		UsuarioEntity usEnvio = usPort.buscarPorId(idUsuarioEnvio);

		if (!"".equals(motivo)) {

			UtilidadesAdapter.pintarLog("Motivo lleno");

			
			if (motivo.length() > 250) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"The length of the rejection reason cannot be greater than 250");
			}
			

			SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);

			evPort.ingresarEventoDeSolicitud("Cancel assign template", motivo, "Info", usEnvio.getUsuario(), s);

		}else{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Rejection reason cannot be empty");
		}

		UtilidadesAdapter.pintarLog("actualizar asignacion template");
		reqPort.actualizarAsignacionTemplate(0, 0, idSolicitud);

	}


	@Override
	public String cargarExcel(MultipartFile archivo) {

		return cargaExcelv2(archivo);
	}

	public void pintarLog(String mensaje, StringBuilder sb) {
		if (ambiente.equals("test")) {
			// if(false){
			// UtilidadesAdapter.pintarLog(mensaje);
			// sb.append(mensaje+"\n");
		}

	}

	@Override
	public List<TelefonosSolicitudes> obtenerSolicitudesDeTelefono(String telefono) {
		return reqPort.obtenerSolicitudesDeTelefono(telefono);
	}

	public static void main(String args[]) {
		SolicitudService sc = new SolicitudService();
		sc.cargaExcelv2(null);
	}

	@Override
	public ReporteComparacionAnios reporteDeAnio(int anio, String valorFiltro) {

		// Tomar anio actual
		int anioActual = anio;
		int anioAnterior = anioActual - 1;
		String anios = "";

		ReporteComparacionAnios rca = new ReporteComparacionAnios();

		anios = anioActual + "," + anioAnterior;
		List<ReporteAnios> raT = reqPort.reporteDeAnio(anios, valorFiltro);

		List<ReporteAnios> ras = generarListaRepAnios(anioActual,
				raT.stream().filter(a -> a.getAnio() == anioActual).collect(Collectors.toList()));
		List<ReporteAnios> rasAnt = generarListaRepAnios(anioAnterior,
				raT.stream().filter(a -> a.getAnio() == anioAnterior).collect(Collectors.toList()));

		rca.setAnioActual(anioActual);
		rca.setMesesAnioActual(ras);
		rca.setAnioAnterior(anioAnterior);
		rca.setMesesAnioAnterior(rasAnt);
		
		BigDecimal totalActual = ras.stream()
	            .filter(r -> r.getAnio() == anioActual)
	            .map(ReporteAnios::getMonto)
	            .reduce(BigDecimal.ZERO, BigDecimal::add);
		
		BigDecimal totalAnterior = rasAnt.stream()
	            .filter(r -> r.getAnio() == anioAnterior)
	            .map(ReporteAnios::getMonto)
	            .reduce(BigDecimal.ZERO, BigDecimal::add);
		
		rca.setMontoTotalAnioActual(totalActual);
		rca.setMontoTotalAnioAnterior(totalAnterior);

		return rca;
	}
	
	@Override
	public ReporteDash reporteDash(String fechai, String fechaf,int usuario){
		return reqPort.reporteDash(fechai, fechaf,usuario);
	}

	public List<ReporteAnios> generarListaRepAnios(int anio, List<ReporteAnios> lRa) {
		List<ReporteAnios> ras = new ArrayList<>();

		for (int i = 0; i <= 12; i++) {

			ReporteAnios a = new ReporteAnios();
			// UtilidadesAdapter.pintarLog("i:"+i);

			if (i == 1) {

				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("January")).findAny().orElse(null);
				if (r == null) {
					a.setMes("January");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}

			}
			if (i == 2) {
				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("February")).findAny().orElse(null);
				if (r == null) {
					a.setMes("February");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}
			}
			if (i == 3) {
				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("March")).findAny().orElse(null);
				if (r == null) {
					a.setMes("March");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}
			}
			if (i == 4) {
				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("April")).findAny().orElse(null);
				if (r == null) {
					a.setMes("April");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}
			}
			if (i == 5) {
				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("May")).findAny().orElse(null);
				if (r == null) {
					a.setMes("May");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}
			}
			if (i == 6) {
				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("June")).findAny().orElse(null);
				if (r == null) {
					a.setMes("June");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}
			}
			if (i == 7) {
				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("July")).findAny().orElse(null);
				if (r == null) {
					a.setMes("July");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}
			}
			if (i == 8) {
				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("August")).findAny().orElse(null);
				if (r == null) {
					a.setMes("August");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}
			}
			if (i == 9) {
				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("September")).findAny().orElse(null);
				if (r == null) {
					a.setMes("September");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}
			}
			if (i == 10) {
				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("October")).findAny().orElse(null);
				if (r == null) {
					a.setMes("October");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}
			}
			if (i == 11) {
				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("November")).findAny().orElse(null);
				if (r == null) {
					a.setMes("November");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}
			}
			if (i == 12) {
				ReporteAnios r = lRa.stream().filter(o -> o.getMes().equals("December")).findAny().orElse(null);
				if (r == null) {
					a.setMes("December");
					a.setMesNum(0);
					a.setMonto(BigDecimal.ZERO);
					a.setAnio(anio);
					ras.add(a);
				} else {
					ras.add(r);
				}
			}

		}
		return ras;
	}

	public String cargaExcelv2(MultipartFile archivo) {

		UtilidadesAdapter.pintarLog("Ambiente:" + ambiente);
		Workbook workbook = null;
		String salida = "";
		StringBuilder sb = new StringBuilder();

		try {

			// workbook = WorkbookFactory.create(new
			// File("/Users/joser.vazquez/ExcelUnif.xlsx"));
			InputStream is = new ByteArrayInputStream(archivo.getBytes());
			workbook = WorkbookFactory.create(is);

			UtilidadesAdapter.pintarLog("Number of sheetss: " + workbook.getNumberOfSheets());
			int cargados = 0;
			boolean solicitud = false;

			String telefono = "";
			String cliente = "";
			String type = "";
			String paymentType = "";
			int idEstatusSolicitud = 0;
			TipoPagoEntity tipoPago = null;
			int idTipoSolicitud = 0;
			BigDecimal pago = BigDecimal.ZERO;
			Double amount = 0.0;
			String scale = "";
			Date fecha = null;
			int mes = 0;
			String sheetName = "";

			TipoPagoEntity tp3 = tpPort.obtenerTiposPago(3);
			List<TipoSolicitudEntity> ls = tsPort.obtenerTiposSolicitudes();
			List<TipoPagoEntity> ltp = tpPort.obtenerTiposPagos();
			int countGen = 0;

			// Obtener usuario revisor
			int idUsRevisando = usPort.buscarPorUsuario("juan").getIdUsuario();
			int idUsRevisor = usPort.buscarPorUsuario("edgar").getIdUsuario();

			ambiente = "qas";

			// for (int i = 2; i < 9; i++) {
			for (int i = 4; i < 12; i++) {

				Sheet sheet = workbook.getSheetAt(i);
				sheetName = sheet.getSheetName();
				UtilidadesAdapter.pintarLog("NombreSheet:" + sheetName);

				UtilidadesAdapter.pintarLog("Sheet:" + i);

				for (Row row : sheet) {
					Solicitud r = new Solicitud();

					// if (index++ == 0) continue;
					// UtilidadesAdapter.pintarLog("row:"+ index);

					if (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.STRING) {
						pintarLog("#################", sb);
						if (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.STRING) {
							pintarLog("Cliente:" + row.getCell(0).getStringCellValue(), sb);
							cliente = row.getCell(0).getStringCellValue();
						}
						if (row.getCell(1) != null && row.getCell(1).getCellType() == CellType.STRING) {
							pintarLog("Type:" + row.getCell(1).getStringCellValue(), sb);
							type = row.getCell(1).getStringCellValue();
							if (type.equals("TYPE")) {
								solicitud = false;
							}
							if ("".equals(type)) {
								solicitud = false;
							}
						} else {
							solicitud = false;
						}

						if (row.getCell(11) != null && row.getCell(11).getCellType() == CellType.STRING) {
							if ("Cancelled".equals(row.getCell(11).getStringCellValue())) {
								solicitud = false;
							}
						}

						if (solicitud) {

							pintarLog("··Llenando datos solicitud", sb);

							r.setCliente(cliente);
							final String typeF = type;
							TipoSolicitudEntity se = ls.stream().filter(a -> typeF.contains(a.getNombre())).findAny()
									.orElse(null);

							if (se == null) {
								idTipoSolicitud = 6;
							} else {
								idTipoSolicitud = se.getIdTipoSolicitud();
							}

							pintarLog("Id tipo solicitud:" + idTipoSolicitud, sb);
							r.setIdTipoSolicitud(idTipoSolicitud);

							if (row.getCell(2) != null && row.getCell(2).getCellType() == CellType.STRING) {
								telefono = row.getCell(2).getStringCellValue();
								pintarLog("Telefono inicial:" + telefono, sb);
								if (telefono.length() > 12) {
									telefono = telefono.substring(0, 12);
									telefono = telefono.replaceAll("-", "");
								} else {
									telefono = telefono.replaceAll("-", "");
								}
								pintarLog("Telefono final:" + telefono, sb);
								if (ambiente.equals("test")) {
									r.setTelefono("0000000000");
								} else {
									r.setTelefono(telefono);
								}
							} else {
								r.setTelefono("");
							}
							if (row.getCell(3) != null && row.getCell(3).getCellType() == CellType.STRING) {
								pintarLog("Email cliente:" + row.getCell(3).getStringCellValue(), sb);
								if (ambiente.equals("test")) {
									r.setEmail("test@gm.com");
								} else {
									r.setEmail(row.getCell(3).getStringCellValue());
								}
							}
							if (row.getCell(4) != null && row.getCell(4).getCellType() == CellType.STRING) {
								pintarLog("Email lawyer:" + row.getCell(4).getStringCellValue(), sb);
								if (ambiente.equals("test")) {
									r.setEmail_abogado("t");
								} else {
									r.setEmail_abogado(row.getCell(4).getStringCellValue());
								}
							}
							if (row.getCell(5) != null && row.getCell(5).getCellType() == CellType.STRING) {
								paymentType = row.getCell(5).getStringCellValue();
								pintarLog("Tipo payment:" + paymentType, sb);
								final String paymentTypeF = paymentType;
								TipoPagoEntity tpa = ltp.stream()
										.filter(a -> paymentTypeF.toUpperCase().contains(a.getNombre().toUpperCase()))
										.findAny().orElse(null);
								if (tpa == null) {
									tipoPago = tp3;
								} else {
									tipoPago = tpa;
								}
								pintarLog("Tipo pago final:" + tipoPago.getNombre(), sb);
							}
							if (tipoPago == null) {
								tipoPago = tp3;
							}
							if (row.getCell(6) != null && row.getCell(6).getCellType() == CellType.NUMERIC) {
								pintarLog("Amout:" + row.getCell(6).getNumericCellValue(), sb);
								amount = row.getCell(6).getNumericCellValue();
								amount = amount * 100;
								r.setAmount(new BigDecimal(amount));

							} else {
								r.setAmount(BigDecimal.ZERO);
							}

							if (row.getCell(7) != null && row.getCell(7).getCellType() == CellType.STRING) {
								pintarLog("Scale:" + row.getCell(7).getStringCellValue(), sb);
								if (!"".equals(row.getCell(7).getStringCellValue())) {
									scale = row.getCell(7).getStringCellValue();
									if (scale.length() > 100) {
										scale = scale.substring(0, 100);
									}
								}
							}
							/*
							 * if (row.getCell(8) != null &&
							 * row.getCell(8).getCellType() == CellType.STRING)
							 * { if (row.getCell(8).getStringCellValue().equals(
							 * "READY")) { idEstatusSolicitud = 10; } else {
							 * idEstatusSolicitud = 1; } pintarLog("Ready:" +
							 * row.getCell(8).getStringCellValue() +
							 * " | idEstatusSolicitud:" +
							 * idEstatusSolicitud,sb);
							 * r.setIdEstatusSolicitud(idEstatusSolicitud); }
							 */
							if (row.getCell(9) != null && row.getCell(9).getCellType() == CellType.STRING) {
								pintarLog("Firma lawyer:" + row.getCell(9).getStringCellValue(), sb);
								r.setFirmaAbogados(row.getCell(9).getStringCellValue());
							}
							if (row.getCell(10) != null && row.getCell(10).getCellType() == CellType.STRING) {
								pintarLog("Pago text:" + row.getCell(10).getStringCellValue(), sb);
								// if
								// ("PAID".equals(row.getCell(10).getStringCellValue()))
								// {
								if (row.getCell(10).getStringCellValue().contains("PAID")) {
									r.setIdEstatusPago(2);
									pago = r.getAmount();
									idEstatusSolicitud = 11;
									r.setIdEstatusSolicitud(idEstatusSolicitud);
								}

							}
							if (row.getCell(10) != null && row.getCell(10).getCellType() == CellType.NUMERIC) {
								pintarLog("Pago numeric:" + row.getCell(10).getNumericCellValue(), sb);
								pago = new BigDecimal(row.getCell(10).getNumericCellValue());
								r.setIdEstatusPago(1);
							}
							if (row.getCell(11) != null && row.getCell(11).getCellType() == CellType.STRING) {
								if ("".equals(row.getCell(11).getStringCellValue())) {
									idEstatusSolicitud = 10;
								} else {
									idEstatusSolicitud = 11;
								}
								pintarLog("Ready:" + row.getCell(8).getStringCellValue() + " | idEstatusSolicitud:"
										+ idEstatusSolicitud, sb);
								r.setIdEstatusSolicitud(idEstatusSolicitud);
							}
							if (pago.compareTo(BigDecimal.ZERO) == 0) {
								r.setIdEstatusPago(1);
							}
							if (i == 4 || i == 5 || i == 6) {
								if (row.getCell(11) != null && row.getCell(12).getCellType() == CellType.STRING) {
									pintarLog("Direccion:" + row.getCell(12).getStringCellValue(), sb);
									if (!"".equals(row.getCell(12).getStringCellValue())) {
										r.setDireccion(row.getCell(12).getStringCellValue());
									}

								}
							}

							pintarLog("idEstatusSolicitud:" + idEstatusSolicitud, sb);
							if (idEstatusSolicitud == 0) {
								r.setIdEstatusSolicitud(1);
							}

							r.setIdUsuarioRevisor(idUsRevisor);
							r.setIdUsuarioRevisando(idUsRevisando);
							pintarLog("Pago:" + pago, sb);
							pintarLog("Tipo Pago:" + tipoPago.getNombre(), sb);
							pintarLog("Scale:" + scale, sb);
							pintarLog("idEstatusSolicitud:" + idEstatusSolicitud, sb);
							pintarLog("idUsRevisor:" + idUsRevisor, sb);
							pintarLog("idUsRevisando:" + idUsRevisando, sb);

							if (idEstatusSolicitud == 10) {
								r.setAsignacionIntSc(true);
							}

							try {
								mes = UtilidadesAdapter.obtenerMesSheet(sheetName.toUpperCase());
								fecha = UtilidadesAdapter.cadenaAFecha("2024-" + mes + "-15");
							} catch (ParseException e) {
								e.printStackTrace();
							}

							r.setFechaInicio(fecha);
							if (type != null) {

								if (type.length() > 350) {
									r.setAdicional(type.substring(0, 350));
								} else {
									r.setAdicional(type);
								}
							}

							SolicitudEntity s = crearSolicitudMasivo(r, 1);
							cargados++;

							if (!"".equals(scale)) {

								// insertar scale
								ScaleEntity es = new ScaleEntity();
								es.setScale(scale);
								es.setSolicitud(s);
								scPort.crearScale(es);

							}
							if (pago.compareTo(BigDecimal.ZERO) == 1) {
								// insertar pago
								MovimientoEntity mov = new MovimientoEntity();
								mov.setDescripcion("Pago");
								mov.setMonto(pago);
								mov.setFecha(fecha);
								mov.setSolicitud(s);
								mov.setTipoPago(tipoPago);
								mov.setTipo("Payment");
								movPort.crearMovimiento(mov);

							}

							pintarLog("_______________", sb);
						}
						solicitud = true;
						pago = BigDecimal.ZERO;
						scale = "";
						idEstatusSolicitud = 0;
						idTipoSolicitud = 0;
						tipoPago = null;
						type = "";
					}

				}
				// UtilidadesAdapter.pintarLog(sb.toString());
				UtilidadesAdapter.pintarLog("sols cargadas:" + cargados + "\n");
				countGen = countGen + cargados;
				cargados = 0;
			}

			// UtilidadesAdapter.pintarLog("sheetV:" + sheetV);

			salida = "Cargados " + countGen;

		} catch (EncryptedDocumentException | IOException e) {
			UtilidadesAdapter.pintarLog(e.getMessage());
		} finally {
			try {
				if (workbook != null)
					workbook.close();
			} catch (IOException e) {
				UtilidadesAdapter.pintarLog(e.getMessage());
			}
			UtilidadesAdapter.pintarLog(sb.toString());
		}
		return salida;
	}

	@Override
	public List<String> obtenerTextosOrdenarPor(int idUsuario) {
		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		String rol = us.getRol();
		List<String> s = new ArrayList<>();

		if (rol.equals("2") || rol.equals("3") || rol.equals("4")) {

			s.add("File");
			s.add("Creation Date");
			s.add("Due date");
			s.add("Assigned");
			s.add("Customer");
			s.add("Phone");
			s.add("Type request");
			s.add("Additional");
			s.add("Importance");
			s.add("Interview Appointment");
			s.add("Scales Appointment");
			s.add("Interview review");
			s.add("Interview Scale");

		}
		if (rol.equals("5")) {

			s.add("File");
			s.add("Due date");
			s.add("Customer");
			s.add("Phone");
			s.add("Type request");
			s.add("Additional");
			s.add("Interview Appointment");

		}
		if (rol.equals("8")) {

			s.add("File");
			s.add("Due date");
			s.add("Customer");
			s.add("Phone");
			s.add("Type request");
			s.add("Additional");
			s.add("Scales Appointment");

		}
		if (rol.equals("7")) {

			s.add("File");
			s.add("Due date");
			s.add("Customer");
			s.add("Phone");
			s.add("Type request");
			s.add("Additional");
			s.add("Scales Appointment");
			s.add("Interview Scale");

		}
		if (rol.equals("11")) {

			s.add("File");
			s.add("Due date");
			s.add("Customer");
			s.add("Phone");
			s.add("Type request");
			s.add("Additional");

		}

		return s;
	}

	@Override
	public List<String> obtenerTextosTipoParaFiltros(int idUsuario) {

		UtilidadesAdapter.pintarLog("Obtener textos");
		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		String rol = us.getRol();
		List<String> s = new ArrayList<>();

		if (rol.equals("2") || rol.equals("3") || rol.equals("4")) {
			// s.add("All");
			s.add("File");
			s.add("Customer");
			s.add("Phone");
			s.add("File Type");
			s.add("Email");
			s.add("File Status");
			s.add("Payment Status");
			s.add("Responsible User");
			s.add("Interviewer User");
			s.add("Waiver");
			s.add("Interview review");
			s.add("Assignment editor");
			s.add("Important");
			s.add("No show");
			s.add("State");
		}
		if (rol.equals("5")) {
			// s.add("All");
			s.add("File");
			s.add("Customer");
			s.add("Phone");
			s.add("Email");
			s.add("File Status");
			s.add("Payment Status");
			s.add("Waiver");
		}
		if (rol.equals("8")) {
			// s.add("All");
			s.add("File");
			s.add("Customer");
			s.add("Phone");
			s.add("Email");
			s.add("File Status");
			s.add("Payment Status");
			s.add("Waiver");
		}
		if (rol.equals("7")) {
			// s.add("All");
			s.add("File");
			s.add("Customer");
			s.add("Phone");
			s.add("Email");
			s.add("File Status");
			s.add("Payment Status");
			s.add("Responsible User");
			s.add("Waiver");
		}
		if (rol.equals("6")) {
			s.add("All");
			s.add("Therapist");
			s.add("Case number");
			s.add("Customer");
			s.add("File");
			s.add("Email");
			s.add("Payment Status");
			s.add("File Status");
		}
		if (rol.equals("10")) {
			s.add("All");
			s.add("Case number");
			s.add("Customer");
			s.add("File");
			s.add("Email");
			s.add("Treatment plan");
			s.add("File Status");
		}
		if (rol.equals("11")) {
			// s.add("All");
			s.add("File");
			s.add("Customer");
			s.add("Phone");
			s.add("Email");
			s.add("File Status");
		}

		return s;
	}
	
	private String rutaServidorFinal(){
		String rutaServidorFinal = "";	
		switch (ambiente) {
		case "qas":
			rutaServidorFinal = rutaServidorQas;
			break;
		case "pro":
			rutaServidorFinal = rutaServidorPro;
			break;
		case "test":
			rutaServidorFinal = rutaServidor;
			break;
		}
		return rutaServidorFinal;
	}

	@Override
	public List<Solicitud> obtenerSolicitudesDetalleReporteDash(int dash, String fechai, String fechaf, int usuario) {
		return reqPort.obtenerSolicitudesDetalleReporteDash(dash, fechai, fechaf, usuario);
	}

	@Override
	public List<DetalleSolsPorFecha> reporteDetalleSolsFecha(String fechai, String fechaf, int usuario) {
		return reqPort.reporteDetalleSolsFecha(fechai, fechaf, usuario);
	}

	@Override
	public void actualizarSolicitudConCupon(int idSolicitud, int idUsuario) {
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UtilidadesAdapter.pintarLog("abogado:"+s.getIdAbogado());
		abPort.actualizarCuponAbogado(s.getIdAbogado());
	}

	@Override
	public void actualizarEmailAbo(String emailsAbogado, int idSolicitud) {
		reqPort.actualizarEmailAbo(emailsAbogado, idSolicitud);
	}

}
