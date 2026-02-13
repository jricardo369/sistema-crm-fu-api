package com.cargosyabonos.adapter.out.mail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.MailException;
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

	@PostConstruct
	public void initConfig() {
		ConfiguracionEntity confj = confPort.obtenerConfiguracionPorCodigo("ENVIAR-CORREOS");
		this.enviarCorreos = Boolean.valueOf(confj.getValor());
		logger.info("[EnvioCorreoAdapter] Config enviarCorreos=" + this.enviarCorreos);
	}

	Logger log = LoggerFactory.getLogger(EnvioCorreoAdapter.class);

	public void enviarCorreo(String email, String subject, String html, byte[] archivo, String extension) {

		if (enviarCorreos) {

			MimeMessage mailMessage = javaMailSender.createMimeMessage();
			try {
				mailMessage.setSubject(subject, "UTF-8");
				MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
				// helper.setTo(email);
				helper.setTo(InternetAddress.parse(email));
				// logger.info(html);
				helper.setText(html.trim(), true);
				if (archivo != null) {
					File f = new File("C:\\Users\\user\\Desktop\\output\\myfile.pdf");
					FileUtils.writeByteArrayToFile(f, archivo);
					FileSystemResource file = new FileSystemResource(f);
					helper.addAttachment("file." + extension, file);
				}
				javaMailSender.send(mailMessage);
				logger.info("Se envió correo....");
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			logger.info("No se envio mail ya que no es " + ambiente);
		}

	}

	public void enviarCorreoInvoice(String email, String subject, String html, int idSolicitud, boolean archivo) {

		if (enviarCorreos) {

			logger.info("Envio de correo");
			MimeMessage mailMessage = javaMailSender.createMimeMessage();
			try {
				mailMessage.setSubject(subject, "UTF-8");
				MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
				helper.setTo(email);
				// logger.info(html);
				logger.info("Enviando correo....");
				helper.setText(html.trim(), true);
				if (archivo) {

					byte[] a = pdfPagos.generarPdf(idSolicitud);
					File f = new File("C:\\Users\\user\\Desktop\\output\\myfile.pdf");

					FileUtils.writeByteArrayToFile(f, a);

					FileSystemResource file = new FileSystemResource(f);
					helper.addAttachment("invoice.pdf", file);
				}
				if (UtilidadesAdapter.isCorreoValido(email)) {
					javaMailSender.send(mailMessage);
					logger.info("Se envió correo....");
				} else {
					logger.info("No se envió correo el mail no es valid:" + email);
				}

			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		} else {
			logger.info("No se envio mail ya que no es " + ambiente);
		}

	}

	@Async
	public void enviarAsync(String email, String subject, String html, byte[] archivo, String extension) {
		if (!enviarCorreos) {
			logger.info("No se envio mail ya que no es " + ambiente);
			return;
		}

		if (!UtilidadesAdapter.isCorreoValido(email)) {
			logger.info("Correo no valido para ser enviado:" + email);
			return;
		}

		try {
			Thread.sleep(5000); // si quieres seguir esperando 5s
			enviarCorreo(email, subject, html, archivo, extension);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); 
			e.printStackTrace();
		}
	}

	public void sendCalendarInvite(String toEmail, String subject, String body, Date fecha, String hora)
			throws MessagingException {

		if (enviarCorreos) {

			boolean mailValido = UtilidadesAdapter.isCorreoValido(toEmail);
			if (mailValido) {
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

				helper.setTo(toEmail);
				helper.setSubject(subject);
				helper.setText(body, true);

				String calendarContent = generateCalendarContent(toEmail,
						"Appointment for interview with familias unidas", subject, fecha, hora);
				helper.addAttachment("invite.ics",
						new ByteArrayResource(calendarContent.getBytes(StandardCharsets.UTF_8)));

				new Thread(new Runnable() {
					@Override
					public void run() {

						// logger.info("Se espero 5 segundos
						// para enviar");
						javaMailSender.send(message);
						// logger.info("Se envio");

					}
				}).start();

			} else {
				logger.info("Correo no valido para ser enviado:" + toEmail);
			}

		} else {
			logger.info("No se envio mail ya que no es " + ambiente);
		}

	}

	public void sendMailWithSchedule(String toEmail, String subject, String body, List<EventoCalendario> eventos)
			throws MessagingException {

		logger.info("Enviando correo schedule a:" + toEmail);

		if (enviarCorreos) {

			boolean mailValido = UtilidadesAdapter.isCorreoValido(toEmail);

			if (mailValido) {

				// Un solo correo con dos ICS adjuntos, uno por cada evento
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

				helper.setFrom(fromEmail);
				helper.setTo(toEmail);
				helper.setSubject(subject);
				helper.setText(body, true);

				int index = 1;
				for (EventoCalendario evento : eventos) {
					String calendarContent = generarInviteEvento(fromEmail, toEmail, evento);
					ByteArrayDataSource dataSource = new ByteArrayDataSource(
							calendarContent.getBytes(StandardCharsets.UTF_8),
							"text/calendar;method=REQUEST;charset=UTF-8");
					helper.addAttachment("Invite_" + index + ".ics", dataSource);
					index++;
				}

				try {
					javaMailSender.send(message);
					logger.info("Correo enviado con 2 ICS a: " + toEmail);
				} catch (MailException ex1) {
					logger.info("Fallo envío (intento 1) con 2 ICS | " + ex1.getMessage());
					try {
						Thread.sleep(1500);
						javaMailSender.send(message);
						logger.info("Correo enviado en reintento (2 ICS) a: " + toEmail);
					} catch (InterruptedException | MailException ex2) {
						logger.info("Fallo envío definitivo (2 ICS): " + ex2.getMessage());
					}
				}

			} else {
				logger.info("Correo no valido para ser enviado:" + toEmail);
			}

		} else {
			logger.info("No se envio mail ya que no es " + ambiente);
		}

		logger.info("Fin correo");

	}

	public void sendSchedulesInterviews(String toEmail, String subject, String body, List<EventoCalendario> eventos)
			throws MessagingException {

		logger.info("Enviando correo a:" + toEmail);

		if (enviarCorreos) {

			boolean mailValido = UtilidadesAdapter.isCorreoValido(toEmail);

			if (mailValido) {

				// Un solo correo con dos ICS adjuntos, uno por cada evento
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

				helper.setFrom(fromEmail);
				helper.setTo(toEmail);
				helper.setSubject(subject);
				helper.setText(body, true);

				int index = 1;
				for (EventoCalendario evento : eventos) {
					String calendarContent = generarInviteEvento(fromEmail, toEmail, evento);
					ByteArrayDataSource dataSource = new ByteArrayDataSource(
							calendarContent.getBytes(StandardCharsets.UTF_8),
							"text/calendar;method=REQUEST;charset=UTF-8");
					helper.addAttachment("Invite_" + index + ".ics", dataSource);
					index++;
				}

				try {
					javaMailSender.send(message);
					logger.info("Correo enviado con 2 ICS a: " + toEmail);
				} catch (MailException ex1) {
					logger.info("Fallo envío (intento 1) con 2 ICS | " + ex1.getMessage());
					try {
						Thread.sleep(1500);
						javaMailSender.send(message);
						logger.info("Correo enviado en reintento (2 ICS) a: " + toEmail);
					} catch (InterruptedException | MailException ex2) {
						logger.info("Fallo envío definitivo (2 ICS): " + ex2.getMessage());
					}
				}

			} else {
				logger.info("Correo no valido para ser enviado:" + toEmail);
			}

		} else {
			logger.info("No se envio mail ya que no es " + ambiente);
		}

		logger.info("Fin correo");

	}

	private String generateCalendarContent(String toEmail, String description, String subject, Date fecha,
			String hora) {

		String f = "";
		String f2 = "";
		try {
			f = UtilidadesAdapter.formatearFechaConHoraCalendar(
					UtilidadesAdapter.cadenaAFechaYHora(UtilidadesAdapter.formatearFecha(fecha) + " " + hora + ":00"));
			hora = hora + 1;
			f2 = UtilidadesAdapter.formatearFechaConHoraCalendar(
					UtilidadesAdapter.cadenaAFechaYHora(UtilidadesAdapter.formatearFecha(fecha) + " " + hora + ":00"));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "BEGIN:VCALENDAR\n" +
				"METHOD:REQUEST\n" +
				"PRODID:-//FamiliasUnidas//FamiliasUnidas//EN\n" +
				"VERSION:2.0\n" +
				"BEGIN:VEVENT\n" +
				"DTSTAMP:" + f + "\n" +
				"DTSTART:" + f + "\n" +
				"DTEND:" + f2 + "\n" +
				"SUMMARY:" + subject + "\n" +
				"UID:" + System.currentTimeMillis() + "@familiasunidascc.com\n" +
				"ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:" + toEmail + "\n" +
				"ORGANIZER:MAILTO:" + toEmail + "\n" +
				"LOCATION:Call\n" +
				"DESCRIPTION:" + description + "\n" +
				"SEQUENCE:0\n" +
				"PRIORITY:5\n" +
				"CLASS:PUBLIC\n" +
				"STATUS:CONFIRMED\n" +
				"TRANSP:OPAQUE\n" +
				"BEGIN:VALARM\n" +
				"ACTION:DISPLAY\n" +
				"DESCRIPTION:REMINDER\n" +
				"TRIGGER;RELATED=START:-PT15M\n" +
				"END:VALARM\n" +
				"END:VEVENT\n" +
				"END:VCALENDAR";
	}

	private static final ZoneId ZONA_MX = ZoneId.of("America/Mexico_City");
	private static final DateTimeFormatter ICS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

	public static String generarInviteEvento(
			String organizerEmail,
			String attendeeEmail,
			EventoCalendario evento) {

		StringBuilder sb = new StringBuilder();
		sb.append("BEGIN:VCALENDAR\n")
				.append("PRODID:-//FamiliasUnidas//Calendario//ES\n")
				.append("VERSION:2.0\n")
				.append("CALSCALE:GREGORIAN\n")
				.append("METHOD:REQUEST\n");

		ZonedDateTime inicioUTC = evento.inicio().atZone(ZONA_MX).withZoneSameInstant(ZoneOffset.UTC);
		ZonedDateTime finUTC = evento.fin().atZone(ZONA_MX).withZoneSameInstant(ZoneOffset.UTC);

		sb.append("BEGIN:VEVENT\n")
				.append("UID:").append(UUID.randomUUID()).append("@familiasunidascc.com\n")
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

		return sb.toString();
	}

	public static void main(String[] args) {

	}

}
