package com.cargosyabonos.application;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.in.TareaProgramadaUseCase;
import com.cargosyabonos.application.port.out.CitaPort;
import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.cargosyabonos.application.port.out.CorreosPort;
import com.cargosyabonos.application.port.out.EmailProspectoAbogadoPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.MovimientosPort;
import com.cargosyabonos.application.port.out.MsgPort;
import com.cargosyabonos.application.port.out.ProspectoAbogadoPort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.application.port.out.SolicitudVocPort;
import com.cargosyabonos.application.port.out.TareaProgramadaPort;
import com.cargosyabonos.application.port.out.TextosPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.AdeudoSolicitudes;
import com.cargosyabonos.domain.CitaSql;
import com.cargosyabonos.domain.ConfiguracionEntity;
import com.cargosyabonos.domain.EmailProspectoAbogadoEntity;
import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.ProspectoAbogadoEntity;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.SolicitudVocEndingSessions;
import com.cargosyabonos.domain.SolicitudVoc;
import com.cargosyabonos.domain.TareaProgramadaEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class TareaProgramadaService implements TareaProgramadaUseCase {

	Logger log = LoggerFactory.getLogger(TareaProgramadaService.class);
	
	@Value("${ambiente}")
	private String ambiente;
	
	private static String amb = "local";

	@Autowired
	private TareaProgramadaPort tPort;

	@Autowired
	private SolicitudPort solPort;

	@Autowired
	private CitaPort citaPort;

	@Autowired
	private MovimientosPort movPort;

	@Autowired
	private EventoSolicitudPort evPort;

	@Autowired
	private CorreosPort correosPort;

	@Autowired
	private ConfiguracionPort confPort;

	@Autowired
	private UsuariosPort usPort;

	@Autowired
	private CorreoElectronicoUseCase correoUs;

	@Autowired
	private MsgPort msgPort;
	
	@Autowired
	private TextosPort txtsPort;
	
	@Autowired
	private SolicitudVocPort solVocPort;

	@Autowired
	private TextosPort textosPort;

	@Autowired
	private ProspectoAbogadoPort prosAboPort;

	@Autowired
	private EmailProspectoAbogadoPort emAProsboPort;

	@Override
	public List<TareaProgramadaEntity> obtenerTareasProgramadas() {
		return tPort.obtenerTareasProgramadas();
	}

	@Override
	public TareaProgramadaEntity obtenerPorCodigo(String codigo) {
		return tPort.obtenerPorCodigo(codigo);
	}

	@Override
	public void actualizarTareaProgramada(TareaProgramadaEntity tp) {
		tPort.actualizarTareaProgramada(tp);
	}

	@Override
	public void limpiezaDisponibilidad() {
		tPort.eliminarDisponibilidadesAnteriores();
	}

	@Override
	public void ejecutarTareaProgramada(String codigoTarea, Date fechai, Date fechaf,int tipoPayment) {
		
		UtilidadesAdapter.pintarLog("Ejecutar tarea:" + codigoTarea);
		
		TareaProgramadaEntity tp = tPort.obtenerPorCodigo(codigoTarea);
		UtilidadesAdapter.pintarLog("Tarea programada activa?" + tp.isActivo() +" en ambiente "+ambiente);	

		if (codigoTarea.equals("payments")) {

			/*ConfiguracionEntity confj = confPort.obtenerConfiguracionPorCodigo("JOB-PAYMENTS");
			UtilidadesAdapter.pintarLog("Ejecutar job payments?" + confj.getValor());
			boolean exc = Boolean.valueOf(confj.getValor());*/

			if (tp.isActivo()) {

				if (ambiente.equals(amb)) {

					boolean enviarMail = true;
					List<AdeudoSolicitudes> ds = movPort.obtenerDeudasSolicitudes(fechai, fechaf, tipoPayment);

					UtilidadesAdapter.pintarLog("Se enviaran " + ds.size() + " registros");

					for (AdeudoSolicitudes a : ds) {

						UtilidadesAdapter.pintarLog("Idsol:" + a.getid_solicitud() + "|email:" + a.getemail() + "|tel:"
								+ a.gettelefono() + "|tipo:" + a.gettipo() + "|deuda:" + a.getdeuda() + "|amount:"
								+ a.getamount());

						if (enviarMail) {

							if (a.getemail() != null && !"".equals(a.getemail())) {

								// UtilidadesAdapter.pintarLog("Se enviará
								// mail");
								if (a.getamount() != null) {
									Map<String, Object> params = new HashMap<>();
									params.put("${cliente}", a.getcliente());
									String monto = "";
									if (a.getdeuda() != null) {
										if (a.getdeuda().compareTo(BigDecimal.ZERO) != 0) {
											monto = UtilidadesAdapter.currencyFormat(a.getdeuda());
											params.put("${monto}", monto);
											params.put("${tipo-servicio}", a.gettipo());
											correosPort.enviarCorreoSaldoVencido(a.getemail(), params);
										}
									}

								}
							}

							if (a.gettelefono() != null && !"".equals(a.gettelefono())) {

								String monto = "";
								monto = UtilidadesAdapter.currencyFormat(a.getdeuda());
								SolicitudEntity s = solPort.obtenerSolicitud(a.getid_solicitud());

								msgPort.envioMensaje(a.gettelefono(),textosPort.textoEnvioPayment(null, a.getdeuda(), s.getTipoSolicitud().getNombre(), "US"),
									s, false);


							}

						}

					}

				}
				
			}

		} else if (codigoTarea.equals("late-requests")) {

			/*ConfiguracionEntity confj = confPort.obtenerConfiguracionPorCodigo("JOB-LATE-REQUESTS");
			UtilidadesAdapter.pintarLog("Ejecutar job late requests?" + confj.getValor());
			boolean exc = Boolean.valueOf(confj.getValor());*/
			
			if (tp.isActivo()) {

				if (ambiente.equals(amb)) {

					int difDias = 0;
					List<SolicitudEntity> ls = solPort.obtenerSolicitudesEstatusEnProcesos();
					ConfiguracionEntity conf = confPort.obtenerConfiguracionPorCodigo("LIMIT-TO-PAY");
					int diasPago = 0;
					if (conf != null) {
						diasPago = Integer.valueOf(conf.getValor());
					}
					UtilidadesAdapter.pintarLog("Dif pago:" + diasPago);

					for (SolicitudEntity s : ls) {

						difDias = UtilidadesAdapter.diferenciaDias(UtilidadesAdapter.formatearFecha(s.getFechaInicio()),
								UtilidadesAdapter.fechaActual());
						UtilidadesAdapter.pintarLog("Dif. dias:" + difDias);

						if (difDias > diasPago) {

							UsuarioEntity us = usPort.buscarPorId(s.getUsuarioRevisando());

							correoUs.enviarCorreoRetrasoSolicitudes(us.getCorreoElectronico(),s.getIdSolicitud(),us.getNombre(),difDias);

						}

					}

				}

			}

		} else if (codigoTarea.equals("appointment-reminder")) {

			/*ConfiguracionEntity confj = confPort.obtenerConfiguracionPorCodigo("JOB-RECORDATORIO");
			UtilidadesAdapter.pintarLog("Ejecutar job recordatorio?" + confj.getValor());
			boolean exc = Boolean.valueOf(confj.getValor());*/
			
			java.util.Date d = null;
			
			if (tp.isActivo()) {

				if (ambiente.equals(amb)) {

					String fecha = "";
					String hora = "";
					String fs = "";
					java.util.Date f = new java.util.Date();
					d = UtilidadesAdapter.sumarDiasAFecha(f, 1);
					java.sql.Date sd = new java.sql.Date(d.getTime());

					UtilidadesAdapter.pintarLog("Ejecutar job recordatorio de fecha:" + sd);

					List<EventoSolicitudEntity> l = evPort.obtenerSchedulesPendientesPorFecha(sd);

					UtilidadesAdapter.pintarLog("Se obtuvieron " + l.size() + " eventos\n");

					for (EventoSolicitudEntity e : l) {

						SolicitudEntity s = e.getSolicitud();

						UtilidadesAdapter.pintarLog("Sol:" + e.getSolicitud().getIdSolicitud() + "|Fecha Schedule:"
								+ e.getFechaSchedule() + "|Correo:" + s.getEmail() + "|Tel:" + s.getTelefono());

						if (e.getFechaSchedule() != null) {

							fecha = UtilidadesAdapter.formatearFechaUS(e.getFechaSchedule());
							// hora = e.getHoraSchedule() + " " +
							// e.getTipoSchedule();
							hora = UtilidadesAdapter.convertirAHoraEstado(fecha, hora, e.getTipo(), s.getEstado(),
									e.getTimeZoneSchedule());

							// Enviando recordatorio a cliente
							envioMail(s.getNombreClienteCompleto(), s.getEmail(), fecha, hora, e, s, true, null);
							// Enviando recordatorio a entrevistador
							/*
							 * UsuarioEntity usEnt =
							 * usPort.buscarPorId(Integer.valueOf(e.
							 * getUsuarioSchedule())); if (usEnt != null) {
							 * envioMail(usEnt.getNombre(),
							 * usEnt.getCorreoElectronico(), descFecha,
							 * e,s,false,usEnt); }
							 */

						} else {

							UtilidadesAdapter.pintarLog("Desc:" + e.getDescripcion());
							fs = e.getDescripcion().split("[,]")[1];
							UtilidadesAdapter.pintarLog("fs:" + fs);
							fecha = fs.substring(25, 35);
							hora = fs.substring(39, 46);
							UtilidadesAdapter.pintarLog("fecha:" + fecha + " hora:" + hora);
							// Enviando mail cliente
							envioMail(s.getNombreClienteCompleto(), s.getEmail(), fecha, hora, e, s, true, null);

						}

						fecha = "";
						hora = "";
						fs = "";
					}

				}

			}

		} else if (codigoTarea.equals("limpieza-disponibilidad")) {
			tPort.eliminarDisponibilidadesAnteriores();
		} else if (codigoTarea.equals("validacion-msms")) {
			validacionMsms();
		} else if(codigoTarea.equals("sols-voc-end-sessions")){
			if (tp.isActivo()) {
				if(ambiente.equals(amb)){
					solicitudesVocEndingSessions();
				}
			}
		}  else if(codigoTarea.equals("rem-law-prospcts-liaison")){
			if (tp.isActivo()) {
				if(ambiente.equals(amb)){
					reminderLawyerProspectsLiaison();
				}
			}
		} else if(codigoTarea.equals("rem-trat-plan")){
			if (tp.isActivo()) {
				if(ambiente.equals(amb)){
					reminderSolsVocPendTratmentPlan();
				}
			}
		} else if(codigoTarea.equals("rem-nota-sin-cita")){
			if (tp.isActivo()) {
				if(ambiente.equals(amb)){
					reminderCitasSinNotaDiaAnterior();
				}
			}
		}
		

	}

	public void envioMail(String nombre, String mail, String fecha, String hora, EventoSolicitudEntity e,
			SolicitudEntity s, boolean cliente, UsuarioEntity usEnt) {
		correoUs.enviarCorreoRecordatorio(nombre, mail, fecha, hora, e, s);
		if (cliente) {
			if (s.getTelefono() != null) {
				if (!"".equals(s.getTelefono())) {
					String texto = "";
					texto = txtsPort.textoEnvioRecordatorio(s.getNombreClienteCompleto(), fecha, hora, "US");
					msgPort.envioMensaje(s.getTelefono(), texto, s, false);
					texto = "";
					texto = txtsPort.textoEnvioRecordatorio(s.getNombreClienteCompleto(), fecha, hora, "ES");
					msgPort.envioMensaje(s.getTelefono(), texto, s, false);
					texto = "";
				}
			}
		} else {
			if (usEnt.getTelefono() != null) {
				if (!"".equals(usEnt.getTelefono())) {
					String texto = "";
					texto = txtsPort.textoEnvioRecordatorio(usEnt.getNombre(), fecha, hora, "US");
					msgPort.envioMensaje(usEnt.getTelefono(), texto, s, false);
					texto = txtsPort.textoEnvioRecordatorio(usEnt.getNombre(), fecha, hora, "ES");
					msgPort.envioMensaje(usEnt.getTelefono(), texto, s, false);
					texto = "";
				}
			}
		}
	}

	@Override
	public void validacionMsms() {
		msgPort.envioMensajePrueba();
	}

	@Override
	public void solicitudesVocEndingSessions() {

		List<SolicitudVocEndingSessions> svoc = solVocPort.obtenerSolicitudesVOCEndingSessions();
		UtilidadesAdapter.pintarLog("Solicitudes obtenidas:" + svoc.size());

		ConfiguracionEntity confj = confPort.obtenerConfiguracionPorCodigo("VOC-ADMIN-MAIL");
		String correoAdminVOC = confj.getValor();
		UtilidadesAdapter.pintarLog("Correo admin voc " + correoAdminVOC);

		for (SolicitudVocEndingSessions s : svoc) {

			UtilidadesAdapter.pintarLog("File VOC:" + s.getIdSolicitud() + "|SesionesAprobadas:"
					+ s.getSesionesPendientes() + "|SesionesPendientes:" + s.getSesionesPendientes()
					+ "|Terapeuta:" + s.getNombreTerapeuta());

			Map<String, Object> params = new HashMap<>();
			params.put("${usuario}", s.getNombreTerapeuta());
			params.put("${cliente}", s.getCliente());
			params.put("${citas-aprobadas}", s.getNumSesiones());
			params.put("${citas-pedientes}", s.getSesionesPendientes());

			correoUs.enviarCorreoFileVOCEndingSessions(s.getNombreTerapeuta(), s.getEmailTerapeuta(), params);
			correoUs.enviarCorreoFileVOCEndingSessions("Admin VOC", correoAdminVOC, params);

		}

	}

	@Override
	public void reminderSolsVocPendTratmentPlan() {

		List<SolicitudVoc> svoc = solVocPort.obtenerSolsVocPendTratmentPlan();
		log.info("Solicitudes obtenidas:" + svoc.size());

		// Obtener correos voc
		List<Integer> li = new ArrayList<>();
		li.add(6);
		List<UsuarioEntity> usList = usPort.obtenerUsuariosDeRoles(li);
		String cn = "";

		for (SolicitudVoc s : svoc) {

			cn = s.getNumeroDeCaso() == null ? "" : s.getNumeroDeCaso();

			log.info("File VOC:" + s.getIdSolicitud() + "|Cliente:"
					+ s.getCliente() + "|Telefono:" + s.getTelefono()
					+ "|Email:" + s.getEmail() + "|Numero de caso:" + s.getNumeroDeCaso());

			Map<String, Object> params = new HashMap<>();
			params.put("${terapeuta}", s.getNombreTerapeuta());
			params.put("${fecha-creacion}", s.getFechaInicio());
			params.put("${cliente}", s.getCliente());
			params.put("${numero-caso}", cn);

			String subject = "";
			if(!"".equals(cn)){
				subject = "Reminder: Appointment Note Missing for case number " + cn;
			}else{
				subject = "Reminder: Appointment Note Missing for file " + s.getIdSolicitud();
			}

			if (s.getEmail() != null && !"".equals(s.getEmail())) {
				log.info("Enviando correo a terapeuta:" + s.getEmail() + "|Numero de caso:" + cn);
				correosPort.enviarCorreoDeLayout(s.getEmail(), subject ,params,
						"email-recordatorio-tratamiento-pendiente");
			}

			for (UsuarioEntity o : usList) {

				params.put("${terapeuta}", o.getNombre());
				if (o.getCorreoElectronico() != null && !"".equals(o.getCorreoElectronico())) {
						log.info("Enviando correo a usuario tipo VOC:" + o.getCorreoElectronico() + "|Numero de caso:" + cn);
						correosPort.enviarCorreoDeLayout(o.getCorreoElectronico(), subject ,params,
						"email-recordatorio-tratamiento-pendiente");
					
				}

			}

		}

	}

	@Override
	public void reminderCitasSinNotaDiaAnterior(){

		List<CitaSql> cs = citaPort.obtenerCitasSinNotaDiaAnterior();
		log.info("Citas obtenidas:" + cs.size());

		// Obtener correos voc
		List<Integer> li = new ArrayList<>();
		li.add(6);
		List<UsuarioEntity> usList = usPort.obtenerUsuariosDeRoles(li);

		String cn = "";

		for (CitaSql c : cs) {

			cn = c.getcasenumber() == null ? "" : c.getcasenumber();

			log.info("File VOC:" + c.getcasenumber() + "|Terapeuta:"
					+ c.getnombreterapeuta() + "|Email:" + c.getemailterapeuta() + "|Numero de caso:" + c.getcasenumber());

			Map<String, Object> params = new HashMap<>();
			params.put("${terapeuta}", c.getnombreterapeuta());
			params.put("${numero-caso}", cn);
			params.put("${fecha-cita}", c.getfecha() +" "+ c.gethora() +" "+ c.gettipo());
			params.put("${cliente}", c.getcliente());

			String subject = "";
			if(!"".equals(cn)){
				subject = "Reminder: Appointment Note Missing for case number " + cn;
			}else{
				subject = "Reminder: Appointment Note Missing for file " + c.getidSolicitud();
			}

			if (c.getemailterapeuta() != null && !"".equals(c.getemailterapeuta())) {
				log.info("Enviando correo a terapeuta:" + c.getemailterapeuta() + "|Numero de caso:" + cn);
				correosPort.enviarCorreoDeLayout(c.getemailterapeuta(),subject,params,
						"email-recordatorio-cita-sin-nota");
			}

			for (UsuarioEntity o : usList) {

				params.put("${terapeuta}", o.getNombre());
				if (o.getCorreoElectronico() != null && !"".equals(o.getCorreoElectronico())) {
						log.info("Enviando correo a usuario tipo VOC:" + o.getCorreoElectronico() + "|Numero de caso:" + cn);
						correosPort.enviarCorreoDeLayout(o.getCorreoElectronico(), subject ,params,
						"email-recordatorio-cita-sin-nota");
					
				}

			}

		}

	}


	@Override
	public void reminderLawyerProspectsLiaison() {

		List<ProspectoAbogadoEntity> l = prosAboPort.obtenerProspectosAbogadosParaReminderLiaison();
		UtilidadesAdapter.pintarLog("Prospectos obtenidos:" + l.size());

		List<Integer> li = new ArrayList<>();
		li.add(13);

		List<UsuarioEntity> usList = usPort.obtenerUsuariosDeRoles(li);

		String emailsPros= "";
	    for(UsuarioEntity us : usList) {
			for (ProspectoAbogadoEntity o : l) {

				emailsPros= "";
				UtilidadesAdapter.pintarLog("Pros id:" + o.getIdProspectoAbogado());

				List<EmailProspectoAbogadoEntity> emails = emAProsboPort.obtenerEmailsDeProspectoAbogado(o.getIdProspectoAbogado());
				if(!emails.isEmpty()) {
					emailsPros = emails.stream().map(EmailProspectoAbogadoEntity::getEmail).collect(Collectors.joining(","));
				}

				correoUs.enviarCorreoReminderParaEnviarCorreoLiaison(us.getCorreoElectronico(), o.getIdProspectoAbogado(), us.getNombre(), o.getNombre(), o.getFirma(), o.getTelefono(), o.getFechaAlta(),emailsPros);

				java.util.Date fechaRecordatorioLiaisonDate = UtilidadesAdapter.sumarDiasAFecha(new java.util.Date(), 15);
				String fechaRecordatorioLiaison = UtilidadesAdapter.formatearFecha(fechaRecordatorioLiaisonDate);
				prosAboPort.actualizarFechaRecordatorioLiaison(o.getIdProspectoAbogado(), fechaRecordatorioLiaison);

			}
		}

	}

}
