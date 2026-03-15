package com.cargosyabonos.adapter.out.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.adapter.out.file.PdfPagos;
import com.cargosyabonos.domain.ConfiguracionEntity;
import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.itextpdf.text.DocumentException;
import org.springframework.scheduling.annotation.Async;

@Component
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class EnvioCorreoAdapter {

	private static final Logger logger = LoggerFactory.getLogger(EnvioCorreoAdapter.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private PdfPagos pdfPagos;

	@Autowired
	private ConfiguracionPort confPort;

	@Value("${ambiente}")
	private String ambiente;

	@Value("${spring.mail.username}")
	private String fromEmail;

	// Flag global para habilitar/deshabilitar envío de correos
	private boolean enviarCorreos = false;

	private static final ZoneId ZONA_MX = ZoneId.of("America/Mexico_City");
	private static final DateTimeFormatter ICS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

	@Autowired
	private Office365MailService office365MailService;

	@PostConstruct
	public void initConfig() {
		ConfiguracionEntity confj = confPort.obtenerConfiguracionPorCodigo("ENVIAR-CORREOS");
		this.enviarCorreos = Boolean.valueOf(confj.getValor());
		logger.info("[EnvioCorreoAdapter] Config enviarCorreos=" + this.enviarCorreos);
	}

	Logger log = LoggerFactory.getLogger(EnvioCorreoAdapter.class);

	public void enviarCorreoInvoice(String email, String subject, String html, int idSolicitud, boolean archivo) {

		try {
			byte[] att = pdfPagos.generarPdf(idSolicitud);
			office365MailService.sendHtmlMailWithCalendar(email, subject, html, null, att, "invoice.pdf");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	@Async
	public void enviarAsync(String email, String subject, String html, byte[] archivo, String extension) {

		try {

			Thread.sleep(5000); // si quieres seguir esperando 5s
			// enviarCorreo(email, subject, html, archivo, extension);
			office365MailService.sendHtmlMailWithCalendar(email, subject, html, null, null, null);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}

	public void sendMailWithSchedule(String tipoInvitacion,String toEmail, String subject, String body, EventoCalendario evento,String uidSchedule)
			throws MessagingException {

		logger.info("Enviando correo schedule a:" + toEmail + " uidSchedule:" + uidSchedule);

		String calendarContent = "";

		if(tipoInvitacion.equals("CANCELACION")) {
			calendarContent = generarCancelacionEvento(fromEmail, toEmail, evento, uidSchedule);
		}else {
			calendarContent = generarInviteEvento(fromEmail, toEmail, evento, uidSchedule);
		}

		office365MailService.sendHtmlMailWithCalendar(toEmail, subject, body, calendarContent, null, null);

		logger.info("Fin correo");

	}

	public static String generarInviteEvento(
			String organizerEmail,
			String attendeeEmail,
			EventoCalendario evento,
			String uidSchedule) {

		StringBuilder sb = new StringBuilder();
		sb.append("BEGIN:VCALENDAR\n")
				.append("PRODID:-//FamiliasUnidas//Calendario//ES\n")
				.append("VERSION:2.0\n")
				.append("CALSCALE:GREGORIAN\n")
				.append("METHOD:REQUEST\n");

		ZonedDateTime inicioUTC = evento.inicio().atZone(ZONA_MX).withZoneSameInstant(ZoneOffset.UTC);
		ZonedDateTime finUTC = evento.fin().atZone(ZONA_MX).withZoneSameInstant(ZoneOffset.UTC);

		sb.append("BEGIN:VEVENT\n")
				.append("UID:").append(uidSchedule != null ? uidSchedule : UUID.randomUUID()).append("\n")
				.append("DTSTAMP:")
				.append(ZonedDateTime.now(ZoneOffset.UTC).format(ICS_FORMAT)).append("Z\n")
				.append("DTSTART:")
				.append(inicioUTC.format(ICS_FORMAT)).append("Z\n")
				.append("DTEND:")
				.append(finUTC.format(ICS_FORMAT)).append("Z\n")
				.append("SUMMARY:").append(evento.titulo()).append("\n")
				.append("DESCRIPTION:").append(evento.descripcion()).append("\n")
				.append("LOCATION:").append(evento.ubicacion()).append("\n")
				.append("ORGANIZER:MAILTO:").append(organizerEmail).append("\n")
				.append("ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:").append(attendeeEmail)
				.append("\n")
				.append("SEQUENCE:0\n")
				.append("STATUS:CONFIRMED\n")
				.append("TRANSP:OPAQUE\n")
				.append("BEGIN:VALARM\n")
				.append("TRIGGER:-PT15M\n")
				.append("ACTION:DISPLAY\n")
				.append("DESCRIPTION:Recordatorio\n")
				.append("END:VALARM\n")
				.append("END:VEVENT\n")
				.append("END:VCALENDAR");

		// logger.info("Contenido ICS generado:\n" + sb.toString());

		return sb.toString();
	}

	public static String generarCancelacionEvento(
			String organizerEmail,
			String attendeeEmail,
			EventoCalendario evento,
		String uidSchedule) {

		StringBuilder sb = new StringBuilder();
		sb.append("BEGIN:VCALENDAR\n")
				.append("PRODID:-//FamiliasUnidas//Calendario//ES\n")
				.append("VERSION:2.0\n")
				.append("CALSCALE:GREGORIAN\n")
				.append("METHOD:CANCEL\n");

		ZonedDateTime inicioUTC = evento.inicio().atZone(ZONA_MX).withZoneSameInstant(ZoneOffset.UTC);
		ZonedDateTime finUTC = evento.fin().atZone(ZONA_MX).withZoneSameInstant(ZoneOffset.UTC);

		sb.append("BEGIN:VEVENT\n")
				.append("UID:").append(uidSchedule != null ? uidSchedule : UUID.randomUUID()).append("\n")
				.append("DTSTAMP:")
				.append(ZonedDateTime.now(ZoneOffset.UTC).format(ICS_FORMAT)).append("Z\n")
				.append("DTSTART:")
				.append(inicioUTC.format(ICS_FORMAT)).append("Z\n")
				.append("DTEND:")
				.append(finUTC.format(ICS_FORMAT)).append("Z\n")
				.append("SUMMARY:").append(evento.titulo()).append("\n")
				.append("DESCRIPTION:").append(evento.descripcion()).append("\n")
				.append("LOCATION:").append(evento.ubicacion()).append("\n")
				.append("ORGANIZER:MAILTO:").append(organizerEmail).append("\n")
				.append("ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:").append(attendeeEmail)
				.append("\n")
				.append("END:VEVENT\n")
				.append("END:VCALENDAR");

		logger.info("Contenido ICS generado:\n" + sb.toString());

		return sb.toString();
	}

	public static void main(String[] args) {

	}

}
