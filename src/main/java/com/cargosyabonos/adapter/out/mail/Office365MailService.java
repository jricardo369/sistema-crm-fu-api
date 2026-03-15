package com.cargosyabonos.adapter.out.mail;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.cargosyabonos.domain.ConfiguracionEntity;

@Service
@PropertySource(value = "classpath:configuraciones-global.properties")
public class Office365MailService {

    Logger logger = Logger.getLogger(Office365MailService.class.getName());

    @Value("${office365.tenant-id}")
    private String tenantId;
    @Value("${office365.client-id}")
    private String clientId;
    @Value("${office365.client-secret}")
    private String clientSecret;
    @Value("${office365.from-user}")
    private String fromUser;

    @Autowired
    private ConfiguracionPort confPort;

    // Flag global para habilitar/deshabilitar envío de correos
    private boolean enviarCorreos = false;

    private final RestTemplate rest = new RestTemplate();

    @PostConstruct
    public void initConfig() {
        ConfiguracionEntity confj = confPort.obtenerConfiguracionPorCodigo("ENVIAR-CORREOS");
        this.enviarCorreos = Boolean.valueOf(confj.getValor());
        logger.info("[EnvioCorreoAdapter] Config enviarCorreos=" + this.enviarCorreos);
    }

    private String getAccessToken() {
            String url = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";

            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("client_id", clientId);
            form.add("client_secret", clientSecret);
            form.add("grant_type", "client_credentials");
            form.add("scope", "https://graph.microsoft.com/.default");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

            Map<String, Object> resp = rest.postForObject(url, request, Map.class);
            return (String) resp.get("access_token");
    }

    /**
     * Envía un correo HTML usando Microsoft Graph y puede adjuntar
     * un evento de calendario (ICS) y un archivo binario.
     *
     * @param to             correo del destinatario
     * @param subject        asunto del correo
     * @param htmlBody       cuerpo HTML del mensaje
     * @param icsContent     contenido del archivo ICS en formato texto (opcional)
     * @param attachment     bytes del archivo adjunto (opcional)
     * @param attachmentName nombre del archivo adjunto (opcional, requerido si hay
     *                       attachment)
     */
    public void sendHtmlMailWithCalendar(String to, String subject, String htmlBody,
            String icsContent,
            byte[] attachment,
            String attachmentName) {

        if (enviarCorreos) {

            // Soportar uno o varios correos separados por coma
            String[] destinatarios = to.split(",");
            List<String> correosValidos = new ArrayList<>();
            for (String dest : destinatarios) {
                String emailTrim = dest.trim();
                if (!emailTrim.isEmpty() && UtilidadesAdapter.isCorreoValido(emailTrim)) {
                    correosValidos.add(emailTrim);
                } else if (!emailTrim.isEmpty()) {
                    logger.info("Correo no valido para ser enviado:" + emailTrim);
                }
            }

            if (correosValidos.isEmpty()) {
                logger.info("No se encontraron correos válidos en 'to': " + to);
                return;
            }

            String accessToken = getAccessToken();

            String url = "https://graph.microsoft.com/v1.0/users/" + fromUser + "/sendMail";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            String icsBase64 = null;
            if (icsContent != null && !icsContent.trim().isEmpty()) {
                icsBase64 = Base64.getEncoder()
                        .encodeToString(icsContent.getBytes(StandardCharsets.UTF_8));
            }

            // Construir JSON para Graph usando mapas
            Map<String, Object> email = new HashMap<>();
            Map<String, Object> message = new HashMap<>();

            message.put("subject", subject);

            Map<String, String> body = new HashMap<>();
            body.put("contentType", "HTML");
            body.put("content", htmlBody);
            message.put("body", body);

            // Destinatarios (uno o varios)
            List<Map<String, Object>> toRecipients = new ArrayList<>();
            for (String correo : correosValidos) {
                Map<String, Object> toEmailAddress = new HashMap<>();
                toEmailAddress.put("address", correo);

                Map<String, Object> toRecipient = new HashMap<>();
                toRecipient.put("emailAddress", toEmailAddress);

                toRecipients.add(toRecipient);
            }
            message.put("toRecipients", toRecipients);

            // Adjuntos (lista común para ICS y archivo binario)
            List<Map<String, Object>> attachments = new ArrayList<>();

            // Adjuntar archivo ICS como evento de calendario solo si hay contenido
            if (icsBase64 != null) {
                Map<String, Object> icsAttachment = new HashMap<>();
                icsAttachment.put("@odata.type", "#microsoft.graph.fileAttachment");
                icsAttachment.put("name", "invitation.ics");
                icsAttachment.put("contentType", "text/calendar");
                icsAttachment.put("contentBytes", icsBase64);
                attachments.add(icsAttachment);
            }

            // Adjuntar archivo binario si viene como parámetro
            if (attachment != null && attachment.length > 0 && attachmentName != null && !attachmentName.isEmpty()) {
                String fileBase64 = Base64.getEncoder().encodeToString(attachment);
                Map<String, Object> fileAttachment = new HashMap<>();
                fileAttachment.put("@odata.type", "#microsoft.graph.fileAttachment");
                fileAttachment.put("name", attachmentName);
                // contentType genérico; si sabes el MIME real puedes cambiarlo
                fileAttachment.put("contentType", "application/octet-stream");
                fileAttachment.put("contentBytes", fileBase64);
                attachments.add(fileAttachment);
            }

            if (!attachments.isEmpty()) {
                message.put("attachments", attachments);
            }

            email.put("message", message);
            email.put("saveToSentItems", Boolean.TRUE);

            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(email, headers);

            logger.info("Ojbeto json");

            rest.postForEntity(url, httpEntity, Void.class);

        } else {
            logger.warning("Email sending is disabled by configuration. No email will be sent.");
        }
    }

}
