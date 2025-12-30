package com.cargosyabonos.adapter.out.mail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;

import javax.mail.MessagingException;
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
import org.springframework.stereotype.Component;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.adapter.out.file.PdfPagos;
import com.itextpdf.text.DocumentException;

@Component
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class EnvioCorreoAdapter {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private PdfPagos pdfPagos;
	
	@Value("${ambiente}")
	private String ambiente;
	
	private static String amb = "pro";

	Logger log = LoggerFactory.getLogger(EnvioCorreoAdapter.class);

	public void enviarCorreo(String email, String subject, String html, byte[] archivo, String extension) {

		if (ambiente.equals(amb)) {

			MimeMessage mailMessage = javaMailSender.createMimeMessage();
			try {
				mailMessage.setSubject(subject, "UTF-8");
				MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
				// helper.setTo(email);
				helper.setTo(InternetAddress.parse(email));
				// UtilidadesAdapter.pintarLog(html);
				helper.setText(html.trim(), true);
				if (archivo != null) {
					File f = new File("C:\\Users\\user\\Desktop\\output\\myfile.pdf");
					FileUtils.writeByteArrayToFile(f, archivo);
					FileSystemResource file = new FileSystemResource(f);
					helper.addAttachment("file." + extension, file);
				}
				javaMailSender.send(mailMessage);
				UtilidadesAdapter.pintarLog("Se envió correo....");
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			UtilidadesAdapter.pintarLog("No se envio mail ya que no es " + ambiente);
		}

	}
	
	public void enviarCorreoInvoice(String email, String subject, String html, int idSolicitud, boolean archivo) {

		if (ambiente.equals(amb)) {

			UtilidadesAdapter.pintarLog("Envio de correo");
			MimeMessage mailMessage = javaMailSender.createMimeMessage();
			try {
				mailMessage.setSubject(subject, "UTF-8");
				MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
				helper.setTo(email);
				// UtilidadesAdapter.pintarLog(html);
				UtilidadesAdapter.pintarLog("Enviando correo....");
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
					UtilidadesAdapter.pintarLog("Se envió correo....");
				} else {
					UtilidadesAdapter.pintarLog("No se envió correo el mail no es valid:" + email);
				}

			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}else{
			UtilidadesAdapter.pintarLog("No se envio mail ya que no es " +  ambiente);
		}
		
	}

	public void enviarAsync(String email, String subject, String html, byte[] archivo, String extension) {

		if (ambiente.equals(amb)) {

			boolean mailValido = UtilidadesAdapter.isCorreoValido(email);
			if (mailValido) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(5000);
							// UtilidadesAdapter.pintarLog("Se espero 5 segundos
							// para enviar");
							enviarCorreo(email, subject, html, archivo, extension);
							// UtilidadesAdapter.pintarLog("Se envio");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}).start();
			} else {
				UtilidadesAdapter.pintarLog("Correo no valido para ser enviado:" + email);
			}

		}else{
			UtilidadesAdapter.pintarLog("No se envio mail ya que no es " +  ambiente);
		}

	}
	
	public void sendCalendarInvite(String toEmail, String subject, String body, Date fecha, String hora)
			throws MessagingException {

		if (ambiente.equals(amb)) {

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
						try {
							Thread.sleep(5000);
							// UtilidadesAdapter.pintarLog("Se espero 5 segundos
							// para enviar");
							javaMailSender.send(message);
							// UtilidadesAdapter.pintarLog("Se envio");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}).start();

			} else {
				UtilidadesAdapter.pintarLog("Correo no valido para ser enviado:" + toEmail);
			}

		} else {
			UtilidadesAdapter.pintarLog("No se envio mail ya que no es " + ambiente);
		}

	}
	
	private String generateCalendarContent(String toEmail,String description,String subject, Date fecha, String hora) {
		
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");
        //String startDateStr = dateFormat.format(startDate);
        //String endDateStr = dateFormat.format(endDate);
		
        String f = "";
        String f2 = "";
        try {
			 f = UtilidadesAdapter.formatearFechaConHoraCalendar(UtilidadesAdapter.cadenaAFechaYHora(UtilidadesAdapter.formatearFecha(fecha)+" "+hora+":00"));
			 hora = hora +1;
			 f2 = UtilidadesAdapter.formatearFechaConHoraCalendar(UtilidadesAdapter.cadenaAFechaYHora(UtilidadesAdapter.formatearFecha(fecha)+" "+hora+":00"));
			 
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
        "ORGANIZER:MAILTO:"+toEmail+"\n" +
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

	

	public static void main(String[] args) {

		EnvioCorreoAdapter e = new EnvioCorreoAdapter();
			//e.sendCalendarInvite("jricardo369@gmail.com", "Test ", "hola", new Date(), "9");
			e.enviarCorreo("jricardo369@gmail.com", "test", "hola", null, "");

		
	}

}
