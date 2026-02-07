package com.cargosyabonos.application;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.in.TareaProgramadaUseCase;
import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.cargosyabonos.application.port.out.EnviarCorreoPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.MovimientosPort;
import com.cargosyabonos.application.port.out.MsgPort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.application.port.out.SolicitudVocPort;
import com.cargosyabonos.application.port.out.TareaProgramadaPort;
import com.cargosyabonos.application.port.out.TextosPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.AdeudoSolicitudes;
import com.cargosyabonos.domain.ConfiguracionEntity;
import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.SolicitudVocEndingSessions;
import com.cargosyabonos.domain.TareaProgramadaEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class TareaProgramadaService implements TareaProgramadaUseCase {

	Logger log = LoggerFactory.getLogger(TareaProgramadaService.class);
	
	@Value("${ambiente}")
	private String ambiente;
	
	private static String amb = "pro";

	@Autowired
	private TareaProgramadaPort tPort;

	@Autowired
	private SolicitudPort solPort;

	@Autowired
	private MovimientosPort movPort;

	@Autowired
	private EventoSolicitudPort evPort;

	@Autowired
	private EnviarCorreoPort envCorrPort;

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
											envCorrPort.enviarCorreoSaldoVencido(a.getemail(), params);
										}
									}

								}
							}

							if (a.gettelefono() != null && !"".equals(a.gettelefono())) {

								String monto = "";
								monto = UtilidadesAdapter.currencyFormat(a.getdeuda());
								SolicitudEntity s = solPort.obtenerSolicitud(a.getid_solicitud());
								msgPort.envioMensaje(a.gettelefono(),
										"Dear " + a.getcliente() + ",you have an overdue balance of $" + monto
												+ " USD for the " + a.gettipo()
												+ " Psychological evaluation with Mental Health Evaluation Group by Familias Unidas. Please reach out to us to settle your balance. You can contact us at: 877-958-6432",
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

							Map<String, Object> params = new HashMap<>();
							UsuarioEntity us = usPort.buscarPorId(s.getUsuarioRevisando());
							params.put("${usuario}", us.getNombre());
							params.put("${num-solicitud}", s.getIdSolicitud());
							params.put("${days}", difDias);

							envCorrPort.enviarCorreoRetrasoSolicitudes(us.getCorreoElectronico(), params,
									s.getIdSolicitud());

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
			
			UtilidadesAdapter.pintarLog("File VOC:" + s.getIdSolicitud() + "|SesionesAprobadas:" + s.getSesionesPendientes() + "|SesionesPendientes:" + s.getSesionesPendientes()
			+ "|Terapeuta:" + s.getNombreTerapeuta());
			
			Map<String, Object> params = new HashMap<>();
			params.put("${usuario}", s.getNombreTerapeuta());
			params.put("${cliente}", s.getCliente());
			params.put("${citas-aprobadas}",s.getNumSesiones()); 
			params.put("${citas-pedientes}", s.getSesionesPendientes());

			correoUs.enviarCorreoFileVOCEndingSessions(s.getNombreTerapeuta(), s.getEmailTerapeuta(), params);
			correoUs.enviarCorreoFileVOCEndingSessions("Admin VOC", correoAdminVOC, params);
			
		}
		
	}

}
