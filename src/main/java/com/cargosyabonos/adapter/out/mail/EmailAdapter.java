package com.cargosyabonos.adapter.out.mail;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.in.UsuariosUseCase;
import com.cargosyabonos.application.port.out.EnviarCorreoPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.domain.EventoRecordatorioCita;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
@PropertySource(value = "classpath:configuraciones-global.properties")
public class EmailAdapter implements EnviarCorreoPort {

	private static final Logger logger = LoggerFactory.getLogger(EmailAdapter.class);

	@Value("${ruta.host}")
	private String rutaHost;

	@Value("${ruta.host.qas}")
	private String rutaHostQas;

	@Value("${ruta.host.pro}")
	private String rutaHostPro;

	@Value("${ruta.servidor}")
	private String rutaServidor;

	@Value("${ruta.servidor.qas}")
	private String rutaServidorQas;

	@Value("${ruta.servidor.pro}")
	private String rutaServidorPro;

	@Value("${ambiente}")
	private String ambiente;

	@Autowired
	private TemplateMail tpMail;

	@Autowired
	private EnvioCorreoAdapter envioCorreo;
	
	@Autowired
	private CorreoElectronicoUseCase correoUs;

	@Autowired
	private UsuariosUseCase usUC;

	@Autowired
	private EventoSolicitudPort usPort;

	@Override
	public void enviarCorreoRecuperacion(String email, Map<String, Object> params) {

		params.put("${url}", rutaServer());

		String template = "";

		try {
			template = tpMail.solveTemplate("email-templates/email-recuperacion-pwd.html", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		envioCorreo.enviarAsync(email, "Recuperación de contraseña de sistema " + "", template,null,"");
	}

	@Override
	public void enviarCorreoMovimiento(String email, Map<String, Object> params) {

		params.put("${rutahost}", rutaServer());
		String template = "";

		try {
			template = tpMail.solveTemplate("email-templates/email-movimiento.html", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		envioCorreo.enviarAsync(email, params.get("${tipo}") + " payment for FamiliasUnidas", template,null,"");
	}

	@Override
	public void enviarCorreoNuevaCita(String email, Map<String, Object> params) {

		String template = "";

		try {
			
			template = tpMail.solveTemplate("email-templates/email-cita-nueva.html", params);
			String emailEnvio = email == null ? "" : email;
			envioCorreo.enviarAsync(emailEnvio, "Abono generado para " + "", template,null,"");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void enviarCorreoSaldoVencido(String email, Map<String, Object> params) {

		params.put("${rutahost}", rutaServer());
		String template = "";

		try {
			template = tpMail.solveTemplate("email-templates/email-saldo-vencido.html", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		envioCorreo.enviarAsync(email,
				"Overdue balance for " + params.get("${tipo-servicio}") + " evaluation in Familias Unidas", template,null,"");

	}

	@Override
	public void enviarCorreoCalendario(String email, Map<String, Object> params) {

		params.put("${rutahost}", rutaServer());
		String template = "";

		try {
			template = tpMail.solveTemplate("email-templates/email-password-reset.html", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		envioCorreo.enviarAsync(email, "Reset password", template,null,"");
	}

	@Override
	public void enviarResetPassword(String email, Map<String, Object> params) {

		params.put("${rutahost}", rutaServer());
		String template = "";

		try {
			template = tpMail.solveTemplate("email-templates/email-password-reset.html", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		envioCorreo.enviarAsync(email, "Reset password", template,null,"");
	}

	public String rutaServer() {
		String rutaHostFinal = "";
		switch (ambiente) {
		case "qas":
			rutaHostFinal = rutaHostQas;
			break;
		case "pro":
			rutaHostFinal = rutaHostPro;
			break;
		case "test":
			rutaHostFinal = rutaHost;
			break;
		}
		return rutaHostFinal;
	}

	public String rutaServidor() {
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
	public void testMail(String mail, String tipo,String fecha,String hora,String tipoH,String estado,String zonaHoraria) {

		switch (tipo) {
		case "test":
			envioCorreo.enviarAsync(mail, "Test", "Hola", null, "");
			break;
		case "cita":
			String horaF = UtilidadesAdapter.convertirAHoraEstado(fecha, hora, tipoH, estado,zonaHoraria);
			correoUs.enviarCorreoCita("Test name", fecha, horaF, tipoH, mail, 1, null);
			break;

		default:
			break;
		}

	}

	@Override
	public void enviarCorreoNotificacion(String email, String titulo, Map<String, Object> params,byte[] archivo,String extension,boolean ingles) {

		params.put("${rutahost}", rutaServer());
		params.put("${url}", rutaServer());

		String template = "";

		try {
			
			if(ingles){
				template = tpMail.solveTemplate("email-templates/email-template-en.html", params);
			}else{
				template = tpMail.solveTemplate("email-templates/email-template.html", params);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		envioCorreo.enviarAsync(email, titulo, template,archivo,extension);

	}

	@Override
	public void enviarCorreoDeLayout(String email, String subject, Map<String, Object> params, String layout) {

		params.put("${rutahost}", rutaServer());
		params.put("${url}", rutaServer());

		String template = "";

		try {
			template = tpMail.solveTemplate("email-templates/" + layout + ".html", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		envioCorreo.enviarAsync(email, subject, template,null,"");
	}
	

	@Override
	public void enviarCorreoDeLayoutCalendar(String email, String subject, Map<String, Object> params, String layout,String formatoFecha) {

		boolean formatoFechaLleno = false;
		params.put("${rutahost}", rutaServer());
		params.put("${url}", rutaServer());
		
		String template = "";
		try {
			template = tpMail.solveTemplate("email-templates/" + layout + ".html", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String hora = params.get("${hora}").toString();
			Date fecha = null;
			if(formatoFecha != null){
				if(formatoFecha.equals("US")){
					formatoFechaLleno = true;
					fecha = UtilidadesAdapter.cadenaAFechaFormatUS(params.get("${fecha}").toString());
				}
			}
			if(!formatoFechaLleno){
				fecha = UtilidadesAdapter.cadenaAFecha(params.get("${fecha}").toString());
			}
			envioCorreo.sendCalendarInvite(email, subject, template, fecha, hora);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void enviarCorreoDeLayoutCalendarWithInvite(String email, String subject, Map<String, Object> params, String layout) {

		params.put("${rutahost}", rutaServer());
		params.put("${url}", rutaServer());
		
		String template = "";
		try {
			template = tpMail.solveTemplate("email-templates/" + layout + ".html", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			String hora = params.get("${hora}").toString();
			String fecha = params.get("${fecha}").toString();
			String tipo = params.get("${tipo}").toString();

			// true si la fecha viene en formato con "/" (ej. 02/02/2026),
			// false en caso contrario (ej. 2026-02-02)
			boolean formatoUs = fecha != null && fecha.contains("/");

			logger.info("fecha:"+fecha);
			logger.info("hora:"+hora);
			logger.info("tipo:"+tipo);

			if (tipo.equals("PM")) {

				String horaStr = hora.substring(0, 2);
				int horaInt = Integer.valueOf(horaStr);

				if (horaInt < 12) {
					horaInt = horaInt + 12;

					hora = hora.replace(horaStr, String.valueOf(horaInt));
					logger.info("hora PM modificada:" + hora);

				}

			}

			String anio = "";
			String dia = "";
			String mes = "";
			String horaStr = "";
			String minutoStr = "";

			if (formatoUs) {
				anio = fecha.substring(6, 10);
				dia = fecha.substring(3, 5);
				mes = fecha.substring(0, 2);
				horaStr = hora.substring(0, 2);
				minutoStr = hora.substring(3, 5);
			} else {
				anio = fecha.substring(0, 4);
				mes = fecha.substring(5, 7);
				dia = fecha.substring(8, 10);
				horaStr = hora.substring(0, 2);
				minutoStr = hora.substring(3, 5);
			}

			List<EventoCalendario> eventos = new ArrayList<>();
			
			EventoCalendario evento = new EventoCalendario(
						"Interview with Mental Health Evaluation Group by Familias Unidas",
						"Interview with Mental Health Evaluation Group by Familias Unidas",
						"",
						LocalDateTime.of(Integer.valueOf(anio), Integer.valueOf(mes), Integer.valueOf(dia), Integer.valueOf(horaStr), Integer.valueOf(minutoStr)),
						LocalDateTime.of(Integer.valueOf(anio), Integer.valueOf(mes), Integer.valueOf(dia), Integer.valueOf(horaStr), Integer.valueOf(minutoStr)));

			eventos.add(evento);

			envioCorreo.sendMailWithSchedule(email, subject, template, eventos);

		} catch (MessagingException e) {
			e.printStackTrace();
		} 
		
	}

	@Override
	public void enviarCorreoSincronizacionCitas(String layout, String fecha, int idUsuario, String formatoFecha) {

		Map<String, Object> params = new HashMap<>();

		params.put("${rutahost}", rutaServer());
		params.put("${url}", rutaServer());
		params.put("${fecha}", fecha);

		String template = "";
		String rangoFechasString = "";

		try {

			UsuarioEntity u = usUC.buscarPorId(idUsuario);

			params.put("${usuario}", u.getNombre());

			rangoFechasString = UtilidadesAdapter.rangoRangoFechasDeFecha(fecha, formatoFecha);
			logger.info("rangoFechasString:" + rangoFechasString);
			params.put("${rango-fechas}", rangoFechasString);

			template = tpMail.solveTemplate("email-templates/" + layout + ".html", params);

			List<EventoRecordatorioCita> eventos = usPort.obtenerRecordatoriosCitasDeUsuario(fecha, idUsuario);

			List<EventoCalendario> eventosCalendario = new ArrayList<>();
			for (EventoRecordatorioCita er : eventos) {

				EventoCalendario eventoCalendario = new EventoCalendario(
						"Interview with Mental Health Evaluation Group by Familias Unidas",
						"Interview with Mental Health Evaluation Group by Familias Unidas",
						"",
						LocalDateTime.of(Integer.valueOf(er.getAnio()), Integer.valueOf(er.getMes()),
								Integer.valueOf(er.getDia()), Integer.valueOf(er.getHora()),
								Integer.valueOf(er.getMinutos())),
						LocalDateTime.of(Integer.valueOf(er.getAnio()), Integer.valueOf(er.getMes()),
								Integer.valueOf(er.getDia()), Integer.valueOf(er.getHora()),
								Integer.valueOf(er.getMinutos())));

				eventosCalendario.add(eventoCalendario);
			}

			envioCorreo.sendMailWithSchedule(u.getCorreoElectronico(), "Appointment reminders for " + rangoFechasString
					+ " in Mental Health Evaluation Group by Familias Unidas", template, eventosCalendario);

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void enviarCorreoRetrasoSolicitudes(String email, Map<String, Object> params, int idSolicitud) {

		params.put("${rutahost}", rutaServer());
		String template = "";

		try {
			template = tpMail.solveTemplate("email-templates/email-retraso-solicitudes.html", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		envioCorreo.enviarAsync(email, "Request attention delay " + idSolicitud + " in application FamiliasUnidas",
				template,null,"");

	}
	
	@Override
	public void enviarCorreoInvoice(String email,Map<String, Object> params,int idSolicitud) {

		params.put("${rutahost}", rutaServer());
		params.put("${url}", rutaServer());

		String template = "";

		try {
			template = tpMail.solveTemplate("email-templates/email-invoice.html", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		envioCorreo.enviarCorreoInvoice(email, "Invoice", template, idSolicitud, true);
	}

}
