package com.cargosyabonos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
import com.cargosyabonos.domain.NumFilesAbogados;
import com.cargosyabonos.domain.ReporteAnios;
import com.cargosyabonos.domain.ReporteComparacionAnios;
import com.cargosyabonos.domain.ReporteContador;
import com.cargosyabonos.domain.ReporteDash;
import com.cargosyabonos.domain.ReporteSolsDeUsuario;
import com.cargosyabonos.domain.ReporteSolsDeUsuarioObj;
import com.cargosyabonos.domain.ReporteSolsUsuario;
import com.cargosyabonos.domain.Solicitud;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.TelefonosSolicitudes;
import com.cargosyabonos.domain.TipoSolicitudEntity;
import com.cargosyabonos.domain.UsuarioEntity;

import java.lang.reflect.Field;

@Service
@PropertySource(value = "classpath:configuraciones-global.properties")
public class SolicitudService implements SolicitudUseCase {

	private static final Logger logger = LoggerFactory.getLogger(SolicitudService.class);

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

				logger.info("Obtener solicitudes");
		
		if(campo != null){
			if(campo.equals("Phone")){
				valor = valor.replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
				logger.info("valor para phone:"+valor);
			}
		}

		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		List<Solicitud> salida = new ArrayList<>();

		salida = reqPort.obtenerSolicitudesDeUsuarioPorQueryV2(us, estatus, 0, fechai, fechaf, ordenarPor, orden, campo,
				valor, myFiles, cerradas, primeraVez,usuario);

		return salida;

	}

	@Override
	public List<Solicitud> obtenerSolicitudesDeUsuarioQueryFiltros(int idUsuario, String cerradas, boolean primeraVez,String ordenarPor, String orden,
			String fechai,String fechaf, int idSolicitud,String cliente,String telefono,String email,String estado,int idEstatusSolicitud, int idEstatusPago,
		int idTipoSolicitud, String waiver,String noshow,String importante,String asignado,String zipcodes,String consentimiento) {
			
		logger.info("Obtener solicitudes por filtros");

		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		List<Solicitud> salida = new ArrayList<>();

		salida = reqPort.obtenerSolicitudesDeUsuarioQueryFiltros(false,us, cerradas, primeraVez, ordenarPor, orden, fechai, fechaf, idSolicitud, cliente, telefono, email, estado, idEstatusSolicitud, idEstatusPago, idTipoSolicitud, waiver, noshow, importante, asignado,zipcodes, consentimiento);

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

		/*List<String> fechas = UtilidadesAdapter.fechasMesYMesAnterior();
		list = reqPort.obtenerSolicitudesDeUsuarioPorQueryV2(us, 0, idRequest, fechas.get(0), fechas.get(1), "", "", "",
				"", false, "NO CLOSED", false,0);*/

		list = reqPort.obtenerSolicitudesDeUsuarioQueryFiltros(true,us, "NO CLOSED", false, "", "", "", "", idRequest, 
		"", "", "", "", 0, 0, 0, null, null, null, "","", null);

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
			if(u.getRol().equals("4")&& !u.isRevisor()){
				r.setIdUsuarioRevisor(idUsuario);
				r.setIdUsuarioRevisando(idUsuario);
			}else{
				int idUs = reqPort.obtenerRevisorConMenorSolicitudes(0);
				r.setIdUsuarioRevisor(idUs);
				r.setIdUsuarioRevisando(idUs);
			}

			SolicitudEntity s = reqPort.crearSolicitud(convertirARequestEntity(r), idUsuario, true);

			// Evneto si tiene comentario
			if (r.getComentario() != null) {
				if (!"".equals(r.getComentario())) {
					evPort.ingresarEventoDeSolicitud("Comment request creation", r.getComentario(), "Info",
							u.getUsuario(), s);
				}
			}

			ConfiguracionEntity confj = confPort.obtenerConfiguracionPorCodigo("MAIL-BVN-CLT");
			logger.info("Se envia mail a cliente?" + confj.getValor());
			boolean exc = Boolean.valueOf(confj.getValor());

			if (exc) {

				// Enviar correo cliente
				logger.info("Datos cliente vacio:" + s.datosClienteEEmailVacios());
				if (s.datosClienteEEmailVacios()) {
				} else {
					correoUs.enviarCorreoNuevaSolicitudCliente(idUsuario, s, false, true);

				}

				logger.info("Tel:" + r.getTelefono() + "|s:" + s.getTelefono());
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
				logger.info("No se envio correo ni mensaje texto de bienvenida a cliente");
			}

			ConfiguracionEntity confbvnAbo = confPort.obtenerConfiguracionPorCodigo("MAIL-BVN-ABO");
			logger.info("Se envia mail a abogado?" + confbvnAbo.getValor());
			boolean excAbo = Boolean.valueOf(confj.getValor());

			if (excAbo) {
				// Enviar correo abogado
				logger.info("Datos abogado vacio:" + s.datosAbogadoVacios());
				if (s.datosAbogadoVacios()) {
					evPort.ingresarEventoDeSolicitud("Email to lawyer not sent",
							"Email to lawyer was not sent due to lack of data", "Info", u.getUsuario(), s);
				} else {
					correoUs.enviarCorreoNotifNuevaSolAAbogado(idUsuario, s, false, true);
					evPort.ingresarEventoDeSolicitud("Email to lawyer sent", "Thank you email sent", "Info",
							u.getUsuario(), s);
				}
			} else {
				logger.info("No se envio correo ni mensaje texto de bienvenida a abogado");
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
				evPort.ingresarEventoDeSolicitud("Update", "The request has been updated", "Info", us.getUsuario(),
						convertirARequestEntity(r));
			}
			
		}
		if(r.getIdAbogado() == 0){
			r.setEmailAboSel(null);
		}
		r.setTelefono(r.getTelefono().replaceAll("\\D+", ""));
		logger.info("emailAboSel:"+r.getEmailAboSel());
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
		s.setSignedClnc(r.isSignedClnc());
		s.setFechaDeCrimen(r.getFechaDeCrimen());
		s.setConsentimiento(r.getConsentimiento());
		s.setVerificacionCliente(r.isVerificacionCliente());

		return s;
	}

	@Override
	public int obtenerRevisorConMenorSolicitudes() {
		return reqPort.obtenerRevisorConMenorSolicitudes(0);
	}

	@Override
	public void actualizarEstatusSolicitud(int idSolicitud, int idEstatus, int idUsuario, boolean closed,String motivo) {

		logger.info("Closed:" + closed+"|motivo:"+motivo);
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
		if (idEstatus == 10) {
			evPort.ingresarEventoDeSolicitud("Update", "The user "+u.getUsuario()+ " send to \"Finish to Ready on Draft\"", "Info", u.getUsuario(), s);
			reqPort.actualizarEstatusSolicitud(idSolicitud, 10);
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
						logger.info("Amount y suma son 0");
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

		logger.info("idSolicitud:" + idSolicitud + " | idUsuarioCambio:" + idUsuarioCambio + " | es fecha anterior:"+fechaAnterior );
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

			UsuarioEntity us = disp.getUsuario();
			UsuarioEntity usCambio = usPort.buscarPorId(idUsuarioCambio);

			//Validar que el horaria y entervistador no se halla asigando ya 
			String fechaMx = UtilidadesAdapter.formatearFecha(disp.getFecha());
			EventoSolicitudEntity eventoDispoExistenteEnFile = evPort.existeEventoSchedulePortipo(fechaMx, disp.getHora(), disp.getTipo(), disp.getUsuario().getIdUsuario(), "1", "Scheduled appointment");
			
			if (eventoDispoExistenteEnFile != null) {
				logger.info("eventoDispoExistenteEnFile: "+disp.getUsuario().getUsuario()
			+" | fecha:"+fechaMx+" | hora:"+disp.getHora()+" | tipo:"+disp.getTipo() );
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"This user already has a scheduled appointment for the given date and time in file "
						+eventoDispoExistenteEnFile.getSolicitud().getIdSolicitud()+".");
			}else{
				logger.info("Evento no existe en ninguna solicitud, se puede agendar sin problemas");
			}
			
			//Validacion en caso que clinician halla sido asignado primero, al asignar el case manager ya no puede ser clinician
			if(s.getAssignedClinician() != 0){
				if(us.getRol().equals("11")){
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"The case manager cannot be assigned to a clinician user because the clinician's interview has already been assigned. "
							+ "If you wish to do so, cancel the clinician's appointment and then assign the case manager as needed.");
				}
			}
			
			logger.info("fechaAnterior:" + fechaAnterior + " | usuario revisando:"+idUsuarioRevisando);
			logger.info("usuario disponibilidad:" + us.getIdUsuario() + " | usuario cambio:"+usCambio.getIdUsuario());

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

					logger.info(
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

			logger.info("fecha disp:" + fecha + " " + hora);
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
								tipoConvertido, s.getEmail(), s.getIdSolicitud(), "US",us.getNombre(),UtilidadesAdapter.formarUidEvento(eventoFirstSchedule));

					} else {
						correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, hora, tipo,
								s.getEmail(), s.getIdSolicitud(), "US",us.getNombre(),UtilidadesAdapter.formarUidEvento(eventoFirstSchedule));
					}

				}

				// Enviar notificacion a entrevisador
				if (us != null) {
					
					correoUs.enviarCorreoCita(us.getNombre(), fecha, hora, tipo, us.getCorreoElectronico(),s.getIdSolicitud(), "US",us.getNombre(),UtilidadesAdapter.formarUidEvento(eventoFirstSchedule));
					
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
				logger.info("Usuario revisor:" + usRevisor);

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

		logger.info("idSolicitud:" + idSolicitud + "/idUsuarioCambio:" + idUsuarioCambio);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		DisponibilidadUsuarioEntity disp = dispPort.obtenerDisponibilidadPorId(idDisponibilidad);
		EventoSolicitudEntity eventoScalesSchedule = null;

		if (disp != null) {
			
			idUsuarioRevisando = disp.getUsuario().getIdUsuario();

			UsuarioEntity us = disp.getUsuario();
			UsuarioEntity usCambio = usPort.buscarPorId(idUsuarioCambio);

			//Validar que el horaria y entervistador no se halla asigando ya 
			String fechaMx = UtilidadesAdapter.formatearFecha(disp.getFecha());
			EventoSolicitudEntity eventoDispoExistenteEnFile = evPort.existeEventoSchedulePortipo(fechaMx, disp.getHora(), disp.getTipo(), disp.getUsuario().getIdUsuario(), "1", "Scheduled scales");
			
			if (eventoDispoExistenteEnFile != null) {
				logger.info("eventoDispoExistenteEnFile: "+disp.getUsuario().getUsuario()
			+" | fecha:"+fechaMx+" | hora:"+disp.getHora()+" | tipo:"+disp.getTipo() );
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"This user already has a scheduled appointment for the given date and time in file "
						+eventoDispoExistenteEnFile.getSolicitud().getIdSolicitud()+".");
			}else{
				logger.info("Evento no existe en ninguna solicitud, se puede agendar sin problemas");
			}

			String fecha = UtilidadesAdapter.formatearFechaUS(disp.getFecha());
			String hora = disp.getHora();
			String tipo = disp.getTipo();
			String zonaHoraria = disp.getZonaHoraria();
			logger.info("fecha disp:" + fecha + hora);

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
								s.getEmail(), s.getIdSolicitud(), "US",us.getNombre(),UtilidadesAdapter.formarUidEvento(eventoScalesSchedule));

					} else {
						correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, hora, tipo, s.getEmail(),
								s.getIdSolicitud(), "US",us.getNombre(),UtilidadesAdapter.formarUidEvento(eventoScalesSchedule));
					}

					// Enviar notificacion a entrevisador
					correoUs.enviarCorreoCita(us.getNombre(), fecha, hora, tipo, us.getCorreoElectronico(),
							s.getIdSolicitud(), "US",us.getNombre(),UtilidadesAdapter.formarUidEvento(eventoScalesSchedule));
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
				logger.info("Usuario revisor:" + usRevisor);

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
			boolean fechaAnterior,int idDisponibilidadTraductor) {

		int idUsuarioRevisando = 0;

		logger.info("idSolicitud:" + idSolicitud + "/idUsuarioCambio:" + idUsuarioCambio);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		DisponibilidadUsuarioEntity disp = dispPort.obtenerDisponibilidadPorId(idDisponibilidad);
		EventoSolicitudEntity eventoClncSchedule = null;

		if (disp != null) {
			
			idUsuarioRevisando = disp.getUsuario().getIdUsuario();

			UsuarioEntity us = disp.getUsuario();
			UsuarioEntity usCambio = usPort.buscarPorId(idUsuarioCambio);

			//Validar que el horaria y entervistador no se halla asigando ya 
			String fechaMx = UtilidadesAdapter.formatearFecha(disp.getFecha());
			EventoSolicitudEntity eventoDispoExistenteEnFile = evPort.existeEventoSchedulePortipo(fechaMx, disp.getHora(), disp.getTipo(), disp.getUsuario().getIdUsuario(), "1", "Scheduled clinician");
			
			if (eventoDispoExistenteEnFile != null) {
				logger.info("eventoDispoExistenteEnFile: "+disp.getUsuario().getUsuario()
			+" | fecha:"+fechaMx+" | hora:"+disp.getHora()+" | tipo:"+disp.getTipo() );
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"This user already has a scheduled appointment for the given date and time in file "
						+eventoDispoExistenteEnFile.getSolicitud().getIdSolicitud()+".");
			}else{
				logger.info("Evento no existe en ninguna solicitud, se puede agendar sin problemas");
			}


			String fecha = UtilidadesAdapter.formatearFechaUS(disp.getFecha());
			String hora = disp.getHora();
			String tipo = disp.getTipo();
			String zonaHoraria = disp.getZonaHoraria();
			logger.info("fecha disp:" + fecha + hora);

			// Convertir a horario del State seleccionado en solicitud, si viene
			// vacio dejar la hora noramal
			boolean horaConvertidaFlag = false;
			String horaConvertida = "";
			String tipoConvertido = "";
			if (s.getEstado() != null) {

				if (!s.getEstado().equals("")) {

					horaConvertidaFlag = true;
					horaConvertida = UtilidadesAdapter.convertirAHoraEstado(fecha, hora, tipo, s.getEstado(),zonaHoraria);

					logger.info("Se convirtio hora a estado " + s.getEstado() + " la hora " + horaConvertida);
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
			eventoClncSchedule = evPort.ingresarEventoScheduleDeSolicitud("Schedule",
					"Scheduled clinician appointment " + fecha + ", " + hora + " " + tipo + " " + zonaHoraria + " to "
							+ disp.getUsuario().getUsuario(),
					"Schedule", usCambio.getUsuario(), disp.getFecha(), disp.getHora(), disp.getTipo(),
					String.valueOf(disp.getUsuario().getIdUsuario()), s, disp.getZonaHoraria());

			if (!fechaAnterior) {

				if (s.getEmail() != null) {

					// Enviar notificacion a cliente
					if (horaConvertidaFlag) {

						correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, horaConvertida, tipoConvertido,
								s.getEmail(), s.getIdSolicitud(), "US",us.getNombre(),UtilidadesAdapter.formarUidEvento(eventoClncSchedule));

					} else {
						correoUs.enviarCorreoCita(s.getNombreClienteCompleto(), fecha, hora, tipo, s.getEmail(),
								s.getIdSolicitud(), "US",us.getNombre(),UtilidadesAdapter.formarUidEvento(eventoClncSchedule));
					}

					// Enviar notificacion a entrevisador
					correoUs.enviarCorreoCita(us.getNombre(), fecha, hora, tipo, us.getCorreoElectronico(),
							s.getIdSolicitud(), "US",us.getNombre(),UtilidadesAdapter.formarUidEvento(eventoClncSchedule));
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
				logger.info("Usuario revisor:" + usRevisor);

				if (usRevisor != 0) {
					idUsuarioRevisando = usRevisor;
				} else {
					idUsuarioRevisando = s.getUsuarioRevisor();
				}

				reqPort.actualizarUsuarioRevisando(idUsuarioRevisando, idSolicitud);
				reqPort.actualizarFinAsgClnc("1", idSolicitud);
				evPort.actualizarFinSchedule("1", eventoClncSchedule.getIdEvento());

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

			logger.info("idDisponibilidadTraductor:" + idDisponibilidadTraductor);
			DisponibilidadUsuarioEntity dispTrad = dispPort.obtenerDisponibilidadPorId(idDisponibilidadTraductor);

			if (dispTrad != null) {
				envioATraductor(dispTrad, s, fechaAnterior, eventoClncSchedule,usCambio);
			}

		}
	}

	public void envioATraductor(DisponibilidadUsuarioEntity disp,SolicitudEntity s,boolean fechaAnterior,EventoSolicitudEntity eventoClncSchedule, UsuarioEntity usCambio) {

		logger.info("------------ Datos disponibilidad traductor");
		int idUsuarioTraductor = disp.getUsuario().getIdUsuario();

			UsuarioEntity us = disp.getUsuario();
			String fecha = UtilidadesAdapter.formatearFechaUS(disp.getFecha());
			String hora = disp.getHora();
			String tipo = disp.getTipo();
			String zonaHoraria = disp.getZonaHoraria();
			logger.info("fecha disp:" + fecha + hora);

			// Convertir a horario del State seleccionado en solicitud, si viene
			// vacio dejar la hora noramal
			boolean horaConvertidaFlag = false;
			String horaConvertida = "";
			String tipoConvertido = "";
			if (s.getEstado() != null) {

				if (!s.getEstado().equals("")) {

					horaConvertidaFlag = true;
					horaConvertida = UtilidadesAdapter.convertirAHoraEstado(fecha, hora, tipo, s.getEstado(),zonaHoraria);

					logger.info("Se convirtio hora a estado " + s.getEstado() + " la hora " + horaConvertida);
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
			eventoClncSchedule = evPort.ingresarEventoScheduleDeSolicitud("Schedule",
					"Scheduled translator appointment " + fecha + ", " + hora + " " + tipo + " " + zonaHoraria + " to "
							+ disp.getUsuario().getUsuario(),
					"Schedule", usCambio.getUsuario(), disp.getFecha(), disp.getHora(), disp.getTipo(),
					String.valueOf(disp.getUsuario().getIdUsuario()), s, disp.getZonaHoraria());

			if (!fechaAnterior) {

				if (s.getEmail() != null) {

					// Enviar notificacion a traductor
					correoUs.enviarCorreoCita(us.getNombre(), fecha, hora, tipo, us.getCorreoElectronico(),
							s.getIdSolicitud(), "US",us.getNombre(),UtilidadesAdapter.formarUidEvento(eventoClncSchedule));

				}

			} else {

				//Revisar si es fecha anterior que hacer?
			}

			//Agregar el usuario traductor a solicitud
			reqPort.actualizarUsuarioTraductor(idUsuarioTraductor, s.getIdSolicitud());

	}
	

	@Override
	public void envioSiguienteProceso(int idSolicitud, int idUsuarioCambio, int idDisponibilidad,
			boolean fechaAnterior) {

		logger.info("idSolicitud:" + idSolicitud + "/idUsuarioCambio:" + idUsuarioCambio);
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
					logger.info("Usuario revisor:" + usRevisor);
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

						logger.info("Amount y suma son 0");
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
										// logger.info("Se
										// espero 5 segundos para enviar");
										// Enviar correo a abogado que ya se
										// cerro la
										// solicitud
										correoUs.enviarCorreoAAbogadoPorFinSolicitud(s);
										correoUs.enviarCorreoAMasterPorFinSolicitud(s);

										logger.info("proBono" + proBono);
										if (proBono > 0) {
											logger.info("No enviara notificacion de invoice");
										} else {
											logger.info("Se enviara notificacion de invoice");
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
	
	@Override
	public void envioFinEntrevistaCaseManager(int idSolicitud,int idUsuarioCambio){
		
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity u = usPort.buscarPorId(idUsuarioCambio);
		
		if (!s.isFinIntIni()) {
			envioFinEntrevistaCaseManager(idSolicitud, s, u);
			
		} else {
			logger.info("Ya se termino case manager y volvio a mandar");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "You have already sent this file.");
		}
		
	}
	
	public void envioFinEntrevistaCaseManager(int idSolicitud,SolicitudEntity s, UsuarioEntity usCambio){
		logger.info("Es CASE MANAGER");
		evPort.ingresarEventoDeSolicitud("Info", "Finished the first interview by user "+usCambio.getUsuario(), "Info", usCambio.getNombre(), s);
		reqPort.actualizarFinInterview(1, idSolicitud);
		EventoSolicitudEntity ev = evPort.obtenerUltimoScheduleInt(idSolicitud, "1");
		logger.info("id evento:" + ev.getIdEvento());
		evPort.actualizarFinSchedule("1", ev.getIdEvento());
		validacionTerminacionCitasParaEntrevistas(idSolicitud);
	}
	
	@Override
	public void envioFinEntrevistaClinician(int idSolicitud,int idUsuarioCambio){
		
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity u = usPort.buscarPorId(idUsuarioCambio);
		
		if (!s.isFinAsgClnc()) {
			
			UsuarioEntity uint = usPort.buscarPorId(s.getUsuarioInterview());

			String rolUsCaseManager = uint == null ? "" : uint.getRol();
			
			if(rolUsCaseManager.equals("11")){
				
				envioFinEntrevistaCaseManager(idSolicitud, s, u);
				
			}else{
				
				logger.info("Es CLINICIAN");
				evPort.ingresarEventoDeSolicitud("Info", "Finished the interview clinician", "Info", u.getUsuario(), s);
				reqPort.actualizarFinAsgClnc("1", idSolicitud);
				EventoSolicitudEntity ev = evPort.obtenerUltimoScheduleClinician(idSolicitud, "1");
				logger.info("id evento:" + ev.getIdEvento());
				evPort.actualizarFinSchedule("1", ev.getIdEvento());
				validacionTerminacionCitasParaEntrevistas(idSolicitud);
			
			}
			
		} else {
			
			logger.info("Ya se termino clinician y volvio a mandar");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "You have already sent this file.");
			
		}
		
	}

	@Override
	public void parcheFinEntrevistaClinician(int idSolicitud,int idUsuarioCambio){

		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity u = usPort.buscarPorId(idUsuarioCambio);
		
		if (!s.isFinAsgClnc()) {
				
			logger.info("Fin Clnc");
			evPort.ingresarEventoDeSolicitud("Info", "Finished the interview clinician", "Info", u.getUsuario(), s);
			reqPort.actualizarFinAsgClnc("1", idSolicitud);
			
		} else {
			
			logger.info("Ya se termino clinician y volvio a mandar");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "You have already sent this file.");
			
		}
	}
	
	@Override
	public void envioFinEntrevistaScales(int idSolicitud,int idUsuarioCambio){
		
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity u = usPort.buscarPorId(idUsuarioCambio);
		
		if (!s.isFinIntSc()) {
			logger.info("Es SCALE");
			evPort.ingresarEventoDeSolicitud("Info", "Finished the interview scale", "Info", u.getUsuario(), s);
			reqPort.actualizarFinIntSc("1", idSolicitud);
			EventoSolicitudEntity ev = evPort.obtenerUltimoScheduleScales(idSolicitud, "1");
			reqPort.actualizarAsignacionIntSc(1, Integer.valueOf(ev.getUsuarioSchedule()), idSolicitud);
			logger.info("id evento:" + ev.getIdEvento());
			evPort.actualizarFinSchedule("1", ev.getIdEvento());
			validacionTerminacionCitasParaEntrevistas(idSolicitud);
			
		} else {
			logger.info("Ya se termino scale y volvio a mandar");
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
				logger.info("Todas las entrevistas terminadas");
				int idUsuarioRevisando = reqPort.obtenerRevisorConMenorSolicitudes(1);
				reqPort.actualizarUsuarioRevisando(idUsuarioRevisando, idSolicitud);
			}

		} else {

			int esFinEntrevistas = reqPort.esFinEntrevistas(idSolicitud);

			if (esFinEntrevistas == 1) {
				logger.info("Todas las entrevistas terminadas");
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
		logger.info("Usario rol:" + us.getRol());
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

		logger.info("Usuario:" + idUsuario);
		UsuarioEntity u = usPort.buscarPorId(idUsuario);
		List<ReporteContador> lsalida = new ArrayList<>();
		List<ReporteSolsUsuario> l = evPort.obtenerNumeroSolicitudesAtendidasPorEntrevistadores("Schedule", "1", fechai,
				fechaf);
		logger.info("Entrevsitadores:" + l.size());
		ReporteContador o = null;
		String fechaiFormato = fechai + " 00:00:00";
		String fechafFormato = fechaf + " 23:59:59";

		logger.info("fechai:" + fechai + " | fechaf:" + fechaf);

		

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
			o.setRol(r.getrol());
		}

		List<ReporteSolsUsuario> lc = reqPort.obtenerSolicitudesClinicians(fechai, fechaf);
			logger.info("Revisors clinician:" + lc.size());
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
				
				o.setRol(r.getrol());
				if (o.getNumero() > 0) {
					lsalida.add(o);
				}
			}

		List<ReporteSolsUsuario> lr = reqPort.obtenerNumeroSolicitudesAtendidasPorRevisores(fechai, fechaf);
		logger.info("Revisores:" + lr.size());
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
			o.setRol(r.getrol());
		}

		List<ReporteSolsUsuario> lrt = reqPort.obtenerNumeroSolicitudesAtendidasPorTemplates(fechaiFormato, fechafFormato);
		logger.info("Templates:" + lrt.size());
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
			o.setRol(r.getrol());
		}

		List<ReporteSolsUsuario> lrad = reqPort.obtenerNumeroSolicitudesAtendidasPorRevisorAdicional(fechai, fechaf);
		logger.info("Revisors ad:" + lrad.size());
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
			o.setRol(r.getrol());
		}

		if (u.getRol().equals("2") || u.getRol().equals("6") || u.getRol().equals("4")) {
			List<ReporteSolsUsuario> lrvoc = reqVocPort.obtenerNumeroSolicitudesDeTemperatura(fechai, fechaf);
			logger.info("Terapeutas:" + lrvoc.size());
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
				o.setRol(r.getrol());
			}
			
		}
		
		
			

		
		return lsalida;
	}

	@Override
	public List<ReporteContador> obtenerReporteMailsAbogados(String fechai, String fechaf) {
		List<String> listaFirmas = reqPort.obtenerFirmasAbogados(fechai, fechaf);
		logger.info("firmas:" + listaFirmas);
		List<ReporteContador> lsalida = new ArrayList<>();
		ReporteContador o = null;
		int nf = 0;
		for (String v : listaFirmas) {
			o = new ReporteContador();
			o.setNombre(v);
			logger.info("nombre:" + v);
			nf = reqPort.obtenerReporteMailsAbogados(v);
			logger.info("nf:" + nf);
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

		logger.info("idSolicitud:" + idSolicitud + "/idUsuario:" + idUsuario);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);

		// Agregar evento
		EstatusSolicitudEntity es = esPort.obtenerEstatusSolicitudPorId(10);
		UsuarioEntity u = usPort.buscarPorId(idSolicitud);
		evPort.ingresarEventoDeSolicitud("Sending process",
				"It was force to sent to the next process " + es.getDescripcion(), "Info", u.getUsuario(), s);

		reqPort.actualizarEstatusSolicitud(idSolicitud, 10);

		logger.info("email cliente:" + s.getEmail() + " |email abogado:" + s.getEmail_abogado());
		if ("".equals(s.getEmail()) || "".equals(s.getEmail_abogado())) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Customer email and lawyer email are mandatory");
		} else {
			// Enviar correo a abogado que ya se cerro la solicitud
			correoUs.enviarCorreoAAbogadoPorFinSolicitud(s);
			correoUs.enviarCorreoAMasterPorFinSolicitud(s);
		}

	}

	public void envioTemplate(int idUsuario, int idSolicitud, int idUsuarioEnvio) {

		logger.info("Envio template");

		reqPort.actualizarUsuarioRevisando(idUsuario, idSolicitud);

		UsuarioEntity usEnvio = usPort.buscarPorId(idUsuarioEnvio);

		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity us = usPort.buscarPorId(idUsuario);

		logger.info("actualizar asignacion template");
		reqPort.actualizarAsignacionTemplate(1, idUsuario, idSolicitud);
		reqPort.actualizarEstatusSolicitud(idSolicitud, 2);

		evPort.ingresarEventoDeSolicitud("File Assignment", "File was assigned to user " + us.getNombre(), "Info",
				usEnvio.getUsuario(), s);
		correoUs.enviarCorreoAsignacion(s, us.getNombre(), us.getCorreoElectronico());

	}

	public void rejectSolicitud(int idUsuario, String motivo, int idSolicitud, int idUsuarioEnvio) {

		UsuarioEntity usEnvio = usPort.buscarPorId(idUsuarioEnvio);
		UsuarioEntity usRevisor = usPort.buscarPorId(idUsuario);
		String rol = usEnvio.getRol();
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);

		// Otbener traductor en caso que exista
		int idTraductor = 0;
		boolean conTraductor = false;
		if(s.getUsuarioTraductor() != 0){
			idTraductor = s.getUsuarioTraductor();
			conTraductor = true;
		}

		boolean esClinicianComoCaseManager = false;

		if (!usEnvio.getRol().equals("8")) {
			reqPort.actualizarUsuarioRevisando(idUsuario, idSolicitud);
		}

		String motivoTrim = motivo == null ? "" : motivo.trim();

		if (motivoTrim.isEmpty()) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Rejection reason cannot be empty");
		}

		if (motivoTrim.length() > 250) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"The length of the rejection reason cannot be greater than 250");
		}

		evPort.ingresarEventoDeSolicitud("Reject File", motivo, "Reject File", usEnvio.getUsuario(), s);

		UsuarioEntity rolInt = usPort.buscarPorId(s.getUsuarioInterview());
		String rolUsCasManager = rolInt == null ? "" : rolInt.getRol();
		esClinicianComoCaseManager = "11".equals(rolUsCasManager);

		EventoSolicitudEntity evSol = null;
		if (rol.equals("5")) {
				evSol = evPort.obtenerUltimoScheduleInterviewByUser(idSolicitud, "1", idUsuarioEnvio);		
		} else if (rol.equals("8")) {
			evSol = evPort.obtenerUltimoScheduleScales(idSolicitud, "1");
		} else if (rol.equals("11")) {

			if (conTraductor) {
				if (idTraductor == idUsuarioEnvio) {

					logger.info("El mismo usuario que rechazo es el traductor");
					reqPort.actualizarUsuarioTraductor(0, s.getIdSolicitud());

					// Obtener evento de solicitud para entrevista a la que se relaciona eltraductor
					logger.info("Usuario interview del traductor:" + s.getUsuarioInterview());
					evSol = evPort.obtenerUltimoScheduleInterviewByUser(idSolicitud, "1", s.getUsuarioInterview());
					// Obtener usuario entrevistador relacionado al entrevistador
					usEnvio = usPort.buscarPorId(s.getUsuarioInterview());

				}
			} else {

				if (esClinicianComoCaseManager) {
					evSol = evPort.obtenerUltimoScheduleInterviewByUser(idSolicitud, "1", idUsuarioEnvio);
				} else {
					evSol = evPort.obtenerUltimoScheduleInterviewClinicianByUser(idSolicitud, "1", idUsuarioEnvio);
				}

			}
		}

		if (evSol != null) {

			correoUs.enviarCorreoReject(usRevisor.getCorreoElectronico(), usEnvio.getCorreoElectronico(),
					usRevisor.getNombre(), usEnvio.getNombre(), s, evSol, motivoTrim);
			if (s.getTelefono() != null) {
				if (!"".equals(s.getTelefono())) {

					if (evSol != null) {
						String f = UtilidadesAdapter.formatearFecha(evSol.getFechaSchedule()) + " "
								+ evSol.getHoraSchedule() + " " + evSol.getTipoSchedule();
						msgPort.envioMensaje(s.getTelefono(),
								" The client " + s.getCliente()
										+ " did reject up for the interview scheduled for request "
										+ s.getIdSolicitud() + " in the date " + f + ".",
								s, false);
					}
				}
			}

			logger.info("Actualizar evento solicitud:" + evSol.getIdEvento());
			evSol.setEstatusSchedule("0");
			evPort.actualizarEventoSolicitud(evSol);

			// Correo cancelacion entrevista a cliente
			String fecha = UtilidadesAdapter.formatearFechaUSDesdeString(evSol.getFecha());
			correoUs.enviarCorreoCitaCancelacion(s.getNombreClienteCompleto(), fecha, evSol.getHoraSchedule(),
					evSol.getTipoSchedule(), s.getEmail(), s.getEstado(),
					s.getIdSolicitud(), "US", evSol.getTimeZoneSchedule(), evSol.getUsuarioSchedule(),
					UtilidadesAdapter.formarUidEvento(evSol), true);

		}

		if (rol.equals("11")) {

			if (conTraductor) {
				if (idTraductor == idUsuarioEnvio) {
					//Actualizar fin asigancion entrevistador relacionado al traductor
					reqPort.actualizarAsignacionIntReset(idSolicitud);
				}

			}else{

				if (esClinicianComoCaseManager) {
					reqPort.actualizarFinAsgClnc("0", idSolicitud);
					reqPort.actualizarAsignacionIntReset(idSolicitud);
					reqPort.actualizarAssignedClinician(0, idSolicitud);
				} else {
					reqPort.actualizarAssignedClinician(0, idSolicitud);
				}

			}
		}
		if (rol.equals("8")) {
			reqPort.actualizarAsignacionIntScReset(idSolicitud);
		}
		if (rol.equals("5")) {
			reqPort.actualizarAsignacionIntReset(idSolicitud);
		}
		if (rol.equals("7")) {

			reqPort.actualizarAsignacionTemplate(0, 0, idSolicitud);

		}

		//Cambiar estatus solicitud dependiendo en que rol y estatus esta
		if (!usEnvio.getRol().equals("8")) {

			if (s.getEstatusSolicitud().getIdEstatusSolicitud() == 2) {
				// Actualizar estatus a Reviewing
				reqPort.actualizarEstatusSolicitud(idSolicitud, 2);
			} else {
				// Actualizar estatus a Received
				reqPort.actualizarEstatusSolicitud(idSolicitud, 1);
			}

		}

		if(idTraductor != 0){

			logger.info("Solicitud tiene asignado traductor, actualizar evento y enviar correo no show a traductor");

			if(idTraductor != usEnvio.getIdUsuario()){

				if (evSol != null) {

					logger.info("Si obtuvo evento de solicitud para entrevista a la que se relaciona el traductor");

					String fechaEvT = UtilidadesAdapter.formatearFecha(evSol.getFechaSchedule());

					logger.info("Solicitud:" + s.getIdSolicitud()+"|traductor:" + idTraductor + "|fecha:" + fechaEvT);
					EventoSolicitudEntity evTraductor = evPort.obtenerScheduleTraductor(s.getIdSolicitud(), "1", idTraductor, fechaEvT);
					UsuarioEntity usTraductor = usPort.buscarPorId(idTraductor);

					// Correo reject entrevista a traductor
					correoUs.enviarCorreoReject(usRevisor.getCorreoElectronico(), usTraductor.getCorreoElectronico(),
						usRevisor.getNombre(), usTraductor.getNombre(), s, evSol, motivoTrim);

					reqPort.actualizarUsuarioTraductor(0, s.getIdSolicitud());
					evTraductor.setEstatusSchedule("0");
					evPort.actualizarEventoSolicitud(evTraductor);

				}

			}else{

				logger.info("El mismo usuario que rechazo es el traductor");
				reqPort.actualizarUsuarioTraductor(0, s.getIdSolicitud());

			}

		}

	}

	@Override
	public void reopen(int idUsuarioEnvio,String motivo,int idSolicitud){

		UsuarioEntity usEnvio = usPort.buscarPorId(idUsuarioEnvio);

		String motivoTrim = motivo == null ? "" : motivo.trim();

		if (motivoTrim.isEmpty()) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Reopen reason cannot be empty");
		}

		if (motivoTrim.length() > 250) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"The length of the reopen reason cannot be greater than 250");
		}

		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);

		evPort.ingresarEventoDeSolicitud("Reopen File", motivo, "Reopen File", usEnvio.getUsuario(), s);

		reqPort.actualizarEstatusSolicitud(idSolicitud, 1);
		
		evPort.obtenerSchedulesActivasDeSolicitudAll(idSolicitud).forEach(ev -> {
			
			int evento = Integer.valueOf(ev.getidEvento());
			int rol = Integer.valueOf(ev.getidrol());

			evPort.actualizarEstatusEvento(0, evento);
			if (rol == 8) {
				reqPort.actualizarAsignacionIntScReset(idSolicitud);
			}
			if (rol == 5) {
				reqPort.actualizarAsignacionIntReset(idSolicitud);
			}
			if (rol == 7) {
				reqPort.actualizarAsignacionTemplate(0, 0, idSolicitud);
			}

		});

	}
	
	public void noShow(String motivo, int idSolicitud, int idUsuarioEnvio) {
		
		UsuarioEntity u = usPort.buscarPorId(idUsuarioEnvio);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		UsuarioEntity usRevisor = usPort.buscarPorId(s.getUsuarioRevisor());
		
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
		esClinicianComoCaseManager = "11".equals(rolUsCasManager);

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
			
			// Enviar mail
			correoUs.enviarCorreoNoShow(usRevisor.getCorreoElectronico(),u.getCorreoElectronico(),usRevisor.getNombre(), u.getNombre(), s, evSol,motivo);
			
			// Enviar mensaje texto
			if (s.getTelefono() != null) {
				if (!"".equals(s.getTelefono())) {
					if (evSol != null) {					
						String f = UtilidadesAdapter.formatearFecha(evSol.getFechaSchedule()) + " "
								+ evSol.getHoraSchedule() + " " + evSol.getTipoSchedule();
						msgPort.envioMensaje(usRevisor.getTelefono(),
								" The client " + s.getCliente()
										+ " did not show up for the interview scheduled for request "
										+ s.getIdSolicitud() + " in the date " + f + ".",
								s, false);
					}
				}
			}

			logger.info("Actualizar evento solicitud:" + evSol.getIdEvento());
			evSol.setEstatusSchedule("0");
			evPort.actualizarEventoSolicitud(evSol);

			// Correo cancelacion entrevista a cliente
			String fecha = UtilidadesAdapter.formatearFechaUSDesdeString(evSol.getFecha());
			correoUs.enviarCorreoCitaCancelacion(s.getNombreClienteCompleto(), fecha, evSol.getHoraSchedule(),
					evSol.getTipoSchedule(), s.getEmail(), s.getEstado(),
					s.getIdSolicitud(), "US", evSol.getTimeZoneSchedule(), evSol.getUsuarioSchedule(),
					UtilidadesAdapter.formarUidEvento(evSol), true);

			
			

		}

		actualizarUsuarioRevisando = !"8".equals(rol);

		if (actualizarUsuarioRevisando) {
			s.setUsuarioRevisando(s.getUsuarioRevisor());
			reqPort.actualizarEstatusSolicitud(idSolicitud, 1);
		}
		
		reqPort.actualizarSolicitud(s);


		if (rol.equals("11")) {
			if(esClinicianComoCaseManager){
				reqPort.actualizarFinAsgClnc("0", idSolicitud);
				reqPort.actualizarAsignacionIntReset(idSolicitud);
				reqPort.actualizarAssignedClinician(0, idSolicitud);
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

		if(s.getUsuarioTraductor() != 0){

			logger.info("Solicitud tiene asignado traductor, actualizar evento y enviar correo no show a traductor");

			if (evSol != null) {

				logger.info("Si obtuvo evento de solicitud para entrevista a la que se relaciona el traductor");

				String fechaEvT = UtilidadesAdapter.formatearFecha(evSol.getFechaSchedule());

				logger.info("Solicitud:" + s.getIdSolicitud()+"|traductor:" + s.getUsuarioTraductor() + "|fecha:" + fechaEvT);
				EventoSolicitudEntity evTraductor = evPort.obtenerScheduleTraductor(s.getIdSolicitud(), "1", s.getUsuarioTraductor(), fechaEvT);
				UsuarioEntity usTraductor = usPort.buscarPorId(s.getUsuarioTraductor());

				// Correo no-show entrevista a traductor
				correoUs.enviarCorreoNoShow(usRevisor.getCorreoElectronico(),usTraductor.getCorreoElectronico(),usRevisor.getNombre(), usTraductor.getNombre(), s, evSol,motivo);		

				reqPort.actualizarUsuarioTraductor(0, s.getIdSolicitud());
				evTraductor.setEstatusSchedule("0");
				evPort.actualizarEventoSolicitud(evTraductor);

			}

		}

	}
	
	public void cancelTemplate(int idUsuario, String motivo, int idSolicitud, int idUsuarioEnvio){
	
		UsuarioEntity usEnvio = usPort.buscarPorId(idUsuarioEnvio);

		if (!"".equals(motivo)) {

			logger.info("Motivo lleno");

			
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

		logger.info("actualizar asignacion template");
		reqPort.actualizarAsignacionTemplate(0, 0, idSolicitud);

	}

	public void pintarLog(String mensaje, StringBuilder sb) {
		if (ambiente.equals("local")) {
			// if(false){
			// logger.info(mensaje);
			// sb.append(mensaje+"\n");
		}

	}

	@Override
	public List<TelefonosSolicitudes> obtenerSolicitudesDeTelefono(String telefono) {
		return reqPort.obtenerSolicitudesDeTelefono(telefono);
	}

	public static void main(String args[]) {
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
			// logger.info("i:"+i);

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

		logger.info("Obtener textos");
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
			s.add("Editor User");
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
			//s.add("All");
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
		case "local":
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
		logger.info("abogado:"+s.getIdAbogado());
		abPort.actualizarCuponAbogado(s.getIdAbogado());
	}

	@Override
	public void actualizarEmailAbo(String emailsAbogado, int idSolicitud) {
		reqPort.actualizarEmailAbo(emailsAbogado, idSolicitud);
	}

	@Override
	public void actualizarInterviewToCaseManager(int idSolicitud,int idUsuario,int idUsuarioEnvio){

		UsuarioEntity uEntity = usPort.buscarPorId(idUsuario);
		UsuarioEntity usCambio = usPort.buscarPorId(idUsuarioEnvio);
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		EventoSolicitudEntity ev = evPort.obtenerUltimoScheduleClinician(idSolicitud, "1");

		String fecha = UtilidadesAdapter.formatearFechaUS(ev.getFechaSchedule());

		evPort.ingresarEventoScheduleDeSolicitud("Schedule",
					"Scheduled appointment " + fecha + ", " + ev.getHoraSchedule() + " " + ev.getTipo() +" " + ev.getTimeZoneSchedule() + " to "+ uEntity.getUsuario(),
					"Schedule", usCambio.getUsuario(), ev.getFechaSchedule(), ev.getHoraSchedule(), ev.getTipoSchedule(),
					String.valueOf(uEntity.getIdUsuario()), s, ev.getTimeZoneSchedule());

		ev.setEstatusSchedule("0");
		evPort.actualizarEventoSolicitud(ev);
		reqPort.actualizarAssignedClinician(0, idSolicitud);
		reqPort.actualizarInterview(uEntity.getIdUsuario(),idSolicitud);
		logger.info("id evento:" + ev.getIdEvento());
	}

}
