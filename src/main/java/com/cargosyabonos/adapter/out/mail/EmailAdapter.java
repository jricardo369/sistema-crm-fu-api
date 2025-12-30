package com.cargosyabonos.adapter.out.mail;

import java.text.ParseException;
import java.util.Date;
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
import com.cargosyabonos.application.port.out.EnviarCorreoPort;

@Service
@PropertySource(value = "classpath:configuraciones-global.properties")
public class EmailAdapter implements EnviarCorreoPort {

	Logger log = LoggerFactory.getLogger(EmailAdapter.class);

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

		// System.out.println("rutaServer:"+rutaServer());
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
			//String tipo = params.get("${tipo}").toString();
			/*int hora = Integer.valueOf(params.get("${hora}").toString());
			if (tipo.equals("PM")) {
				hora = hora + 12;
			}*/
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
			// System.out.println("fecha:"+fecha);
			// System.out.println("hora:"+hora);
			envioCorreo.sendCalendarInvite(email, subject, template, fecha, hora);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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
