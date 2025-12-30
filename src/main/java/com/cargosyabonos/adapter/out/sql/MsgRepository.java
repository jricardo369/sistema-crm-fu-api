package com.cargosyabonos.adapter.out.sql;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.cargosyabonos.application.port.out.EnviarCorreoPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.MsgPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.Channel;
import com.cargosyabonos.domain.ConfiguracionEntity;
import com.cargosyabonos.domain.Mensaje;
import com.cargosyabonos.domain.MensajePodium;
import com.cargosyabonos.domain.RespuestaServicioToken;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.UsuarioEntity;
import com.google.gson.Gson;
import com.itextpdf.xmp.impl.Base64;

@Service
@PropertySource(value = "classpath:configuraciones-global.properties")
public class MsgRepository implements MsgPort {

	@Value("${servicio.msg-envio}")
	private String servicioMsgEnvio;

	@Value("${ambiente}")
	private String ambiente;

	private static String amb = "pro";

	private String email;

	@Autowired
	private ConfiguracionPort confPort;

	@Autowired
	private EventoSolicitudPort evPort;

	@Autowired
	private UsuariosPort usPort;

	@Autowired
	private EnviarCorreoPort correoPort;

	@Override
	public boolean envioMensaje(String telefono, String mensaje, SolicitudEntity s, boolean segundaVez) {
		
		//UtilidadesAdapter.pintarLog("ambiente:"+ambiente+"|amb:"+amb);

		boolean salida = true;

		if (ambiente.equals(amb)) {

			List<ConfiguracionEntity> confs = confPort.obtenerConfiguraciones();
			if (servicioMsgEnvio.equals("goto")) {
				envioMensajeAsync(telefono, mensaje, s, confs, false);
			} else if (servicioMsgEnvio.equals("podium")) {
				envioMensajePodiumAsync(telefono, mensaje, s, confs, segundaVez);
			}

		}else{
			UtilidadesAdapter.pintarLog("No se envia mensaje en ambiente diferente a produccion");
		}
		return salida;

	}

	public String formatoTelefono(String telefono) {

		if (telefono.startsWith("352")) {
			telefono = "+521" + telefono;
		}
		return telefono;

	}

	public void envioMensajeAsyncN(String telefono, String mensaje, SolicitudEntity s, List<ConfiguracionEntity> confs,
			boolean segundaVez) {

		new Thread(new Runnable() {
			@Override
			public void run() {

				UtilidadesAdapter.pintarLog("Enviando mensaje por goto...");

				RestTemplate restTemplate = new RestTemplate();
				URI uri;
				String resultado = "";

				try {

					UtilidadesAdapter.pintarLog("Comenzo a enviar mensaje de texto goto");
					String telefonoEnvio = confs.stream().filter(a -> a.getCodigo().equals("TEL-ENVIO")).findFirst()
							.get().getValor();
					telefonoEnvio = "+" + telefonoEnvio;

					uri = new URI("https://api.goto.com/messaging/v1/messages");
					Mensaje m = new Mensaje();
					m.setOwnerPhoneNumber(telefonoEnvio);
					String t = formatoTelefono(telefono);
					m.setContactPhoneNumbers(t);
					m.setBody(mensaje);
					UtilidadesAdapter.pintarLog("t:" + t);

					String token = confs.stream().filter(a -> a.getCodigo().equals("TOKEN")).findFirst().get()
							.getValor();

					HttpHeaders headers = new HttpHeaders();
					headers.set("Authorization", "Bearer " + token);
					HttpEntity<?> httpEntity = new HttpEntity<>(m, headers);
					ResponseEntity<String> result = restTemplate.postForEntity(uri, httpEntity, String.class);
					resultado = result.getStatusCode().toString();

					if (resultado.contains("201")) {
						UtilidadesAdapter.pintarLog("Se envio correctamente mensaje de texto");
					}

				} catch (Exception e) {

					UtilidadesAdapter.pintarLog("Error:" + e.getMessage());

					if (!segundaVez) {

						if (e.getMessage().contains("401")) {

							boolean rt = refreshToken(confs);
							UtilidadesAdapter.pintarLog("Se termino refresh token y respondio:" + rt);
							envioMensajeAsync(telefono, mensaje, s, confs, true);

						}

					} else {

						UtilidadesAdapter.pintarLog("Se intento enviar nuevamente mensaje pero no pudo");
						UsuarioEntity us = usPort.buscarPorId(1);

						if (s != null) {

							evPort.ingresarEventoDeSolicitud("Info",
									"An attempt was made to send a text message to the registered number but it was not possible.",
									"Info", us.getUsuario(), s);

						} else {

							String emailAdmin = confs.stream().filter(a -> a.getCodigo().equals("ADMIN-EMAIL"))
									.findFirst().get().getValor();
							Map<String, Object> params = new HashMap<>();
							params.put("${cliente}", "");
							params.put("${cuerpo}",
									"There was an error when testing text message sending in the FamiliasUnidas system, please validate with the administrator.");
							correoPort.enviarCorreoNotificacion(emailAdmin, "Error sending msm on FamiliasUnidas",
									params, null, "", true);

						}

					}

					e.printStackTrace();

				}

			}

		}).start();
	}

	public void envioMensajeAsync(String telefono, String mensaje, SolicitudEntity s, List<ConfiguracionEntity> confs,
			boolean segundaVez) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				
				UtilidadesAdapter.pintarLog("Enviando mensaje por goto...");
				
				RestTemplate restTemplate = new RestTemplate();
				URI uri;
				String resultado = "";
				@SuppressWarnings("unused")
				boolean envioExitoso = false;

				try {
					UtilidadesAdapter.pintarLog("Comenzo a enviar mensaje de texto Goto");
					String telefonoEnvio = confs.stream().filter(a -> a.getCodigo().equals("TEL-ENVIO")).findFirst()
							.get().getValor();
					telefonoEnvio = "+" + telefonoEnvio;

					uri = new URI("https://api.goto.com/messaging/v1/messages");
					Mensaje m = new Mensaje();
					m.setOwnerPhoneNumber(telefonoEnvio);
					String t = formatoTelefono(telefono);
					m.setContactPhoneNumbers(t);
					m.setBody(mensaje);
					UtilidadesAdapter.pintarLog("t:" + t);

					String token = confs.stream().filter(a -> a.getCodigo().equals("TOKEN")).findFirst().get()
							.getValor();

					HttpHeaders headers = new HttpHeaders();
					headers.set("Authorization", "Bearer " + token);
					HttpEntity<?> httpEntity = new HttpEntity<>(m, headers);
					ResponseEntity<String> result = restTemplate.postForEntity(uri, httpEntity, String.class);
					resultado = result.getStatusCode().toString();

					if (resultado.contains("201")) {
						UtilidadesAdapter.pintarLog("Se envio correctamente mensaje de texto Goto");
						envioExitoso = true;
						return; // Salir del método si el envío fue exitoso
					}

				} catch (Exception e) {

					UtilidadesAdapter.pintarLog("Error:" + e.getMessage());

					if (!segundaVez && e.getMessage().contains("401")) {
						boolean rt = refreshToken(confs);
						UtilidadesAdapter.pintarLog("Se termino refresh token y respondio:" + rt);
						envioMensajeAsync(telefono, mensaje, s, confs, true);
						return; // Salir después de reintentar
					}

					// Solo enviar correo si el envío falló y ya se intentó una
					// segunda vez
					if (segundaVez || (e.getMessage() != null && !e.getMessage().contains("401"))) {
						UtilidadesAdapter.pintarLog("Se intento enviar nuevamente mensaje pero no pudo");
						UsuarioEntity us = usPort.buscarPorId(1);

						if (s != null) {
							evPort.ingresarEventoDeSolicitud("Info",
									"An attempt was made to send a text message to the registered number but it was not possible.",
									"Info", us.getUsuario(), s);
						} else {
							String emailAdmin = confs.stream().filter(a -> a.getCodigo().equals("ADMIN-EMAIL"))
									.findFirst().get().getValor();
							Map<String, Object> params = new HashMap<>();
							params.put("${cliente}", "");
							params.put("${cuerpo}",
									"There was an error when testing text message sending in the FamiliasUnidas system, please validate with the administrator.");
							correoPort.enviarCorreoNotificacion(emailAdmin, "Error sending msm on FamiliasUnidas",
									params, null, "", true);
						}
					}

					e.printStackTrace();
				}
			}
		}).start();
	}

	public void envioMensajePodiumAsync(String telefono, String mensaje, SolicitudEntity s,
			List<ConfiguracionEntity> confs, boolean segundaVez) {

		new Thread(new Runnable() {
			@Override
			public void run() {

				UtilidadesAdapter.pintarLog("Enviando mensaje por Podium...");

				RestTemplate restTemplate = new RestTemplate();
				URI uri;
				String resultado = "";
				@SuppressWarnings("unused")
				boolean envioExitoso = false;

				try {

					uri = new URI("https://api.podium.com/v4/messages");
					MensajePodium m = new MensajePodium();
					String locationUid = confs.stream().filter(a -> a.getCodigo().equals("LOCATIONUID")).findFirst()
							.get().getValor();
					m.setLocationUid(locationUid);
					m.setDirection("outbound");
					m.setBody(mensaje);
					Channel c = new Channel();
					c.setType("phone");
					String t = formatoTelefono(UtilidadesAdapter.limpiarCaracteres(telefono)).trim();
					c.setIdentifier(t);
					m.setChannel(c);
					UtilidadesAdapter.pintarLog("t:" + t);
					String token = confs.stream().filter(a -> a.getCodigo().equals("TOKEN")).findFirst().get()
							.getValor();
					HttpHeaders headers = new HttpHeaders();
					headers.set("Authorization", "Bearer " + token);
					HttpEntity<?> httpEntity = new HttpEntity<>(m, headers);
					ResponseEntity<String> result = restTemplate.postForEntity(uri, httpEntity, String.class);
					resultado = result.getStatusCode().toString();

					if (resultado.contains("200")) {
						UtilidadesAdapter.pintarLog("Se envio correctamente mensaje de texto Podium");
						envioExitoso = true;
						return;
					}

				} catch (Exception e) {

					UtilidadesAdapter.pintarLog("Error:" + e.getMessage());

					if (!segundaVez && e.getMessage().contains("401")) {

						boolean rt = refreshTokenPodium(confs);
						UtilidadesAdapter.pintarLog("Se termino refresh token y respondio:" + rt);
						envioMensajePodiumAsync(telefono, mensaje, s, confs, true);
						return;

					}

					if (segundaVez || (e.getMessage() != null && !e.getMessage().contains("401"))) {

						UtilidadesAdapter.pintarLog("Se intento enviar nuevamente mensaje pero no pudo");
						UsuarioEntity us = usPort.buscarPorId(1);

						if (s != null) {

							evPort.ingresarEventoDeSolicitud("Info",
									"An attempt was made to send a text message to the registered number but it was not possible.",
									"Info", us.getUsuario(), s);

						} else {

							String emailAdmin = confs.stream().filter(a -> a.getCodigo().equals("ADMIN-EMAIL"))
									.findFirst().get().getValor();
							Map<String, Object> params = new HashMap<>();
							params.put("${cliente}", "");
							params.put("${cuerpo}",
									"There was an error when testing text message sending in the FamiliasUnidas system, please validate with the administrator.");
							correoPort.enviarCorreoNotificacion(emailAdmin, "Error sending msm on FamiliasUnidas",
									params, null, "", true);

						}

					}

					e.printStackTrace();

				}
			}
		}).start();

	}

	@Override
	public boolean refreshToken(List<ConfiguracionEntity> confs) {

		boolean salida = true;
		UtilidadesAdapter.pintarLog("Refresh token");
		RestTemplate restTemplate = new RestTemplate();
		URI uri;
		String resultado = "";
		String token = "";
		String urlF = "";

		try {

			String tokenRefresh = confs.stream().filter(a -> a.getCodigo().equals("REFRESH-TOKEN")).findFirst().get()
					.getValor();
			String clientSecret = confs.stream().filter(a -> a.getCodigo().equals("CLIENT-SECRET")).findFirst().get()
					.getValor();
			String clientId = confs.stream().filter(a -> a.getCodigo().equals("CLIENT-ID")).findFirst().get()
					.getValor();
			StringBuilder url = new StringBuilder();
			url.append("https://authentication.logmeininc.com/oauth/token?grant_type=refresh_token&");
			url.append("refresh_token=");
			url.append(tokenRefresh);
			url.append("&client_secret=");
			url.append(clientSecret);
			url.append("&client_id=");
			url.append(clientId);
			urlF = url.toString().trim();
			// UtilidadesAdapter.pintarLog("urlF:"+urlF);
			uri = new URI(urlF);

			String b64 = Base64.encode(clientId + ":" + clientSecret);

			/*
			 * UtilidadesAdapter.pintarLog("tokenRefresh:"+tokenRefresh);
			 * UtilidadesAdapter.pintarLog("client_secret:"+clientSecret);
			 * UtilidadesAdapter.pintarLog("clientId:"+clientId);
			 * UtilidadesAdapter.pintarLog("b64:"+b64);
			 */

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Basic " + b64);
			headers.set("Accept", "application/json");
			headers.set("Content-Type", "application/x-www-form-urlencoded");

			HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);

			ResponseEntity<String> result = restTemplate.postForEntity(uri, httpEntity, String.class);
			resultado = result.getStatusCode().toString();

			Gson gson = new Gson();
			RespuestaServicioToken r = gson.fromJson(result.getBody(), RespuestaServicioToken.class);
			token = r.getAccess_token();

			if (resultado.contains("200")) {
				// Actulizar token
				UtilidadesAdapter.pintarLog("Se actualizo token correctamente");
				ConfiguracionEntity t = confs.stream().filter(a -> a.getCodigo().equals("TOKEN")).findAny()
						.orElse(null);
				t.setValor(token);
				confPort.actualizarConf(t);
			} else {
				UtilidadesAdapter.pintarLog("No se pudo actualizar el token correctamente");
			}

		} catch (URISyntaxException e) {
			UtilidadesAdapter.pintarLog("Error al refresh token");
			salida = false;
			e.printStackTrace();
		} catch (HttpClientErrorException e2) {
			UtilidadesAdapter.pintarLog("Error al refresh token http:");
			salida = false;
		}

		return salida;

	}

	public boolean refreshTokenPodium(List<ConfiguracionEntity> confs) {

		boolean salida = true;
		UtilidadesAdapter.pintarLog("Refresh token");
		RestTemplate restTemplate = new RestTemplate();
		URI uri;
		String resultado = "";
		String token = "";
		String urlF = "";
		try {

			String tokenRefresh = confs.stream().filter(a -> a.getCodigo().equals("REFRESH-TOKEN")).findFirst().get()
					.getValor();
			String clientSecret = confs.stream().filter(a -> a.getCodigo().equals("CLIENT-SECRET")).findFirst().get()
					.getValor();
			String clientId = confs.stream().filter(a -> a.getCodigo().equals("CLIENT-ID")).findFirst().get()
					.getValor();
			StringBuilder url = new StringBuilder();
			url.append("https://api.podium.com/oauth/token?grant_type=refresh_token&");
			url.append("refresh_token=");
			url.append(tokenRefresh);
			url.append("&client_secret=");
			url.append(clientSecret);
			url.append("&client_id=");
			url.append(clientId);
			urlF = url.toString().trim();
			// UtilidadesAdapter.pintarLog("urlF:"+urlF);
			uri = new URI(urlF);

			String b64 = Base64.encode(clientId + ":" + clientSecret);

			/*
			 * UtilidadesAdapter.pintarLog("tokenRefresh:"+tokenRefresh);
			 * UtilidadesAdapter.pintarLog("client_secret:"+clientSecret);
			 * UtilidadesAdapter.pintarLog("clientId:"+clientId);
			 * UtilidadesAdapter.pintarLog("b64:"+b64);
			 */

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Basic " + b64);
			headers.set("Accept", "application/json");
			headers.set("Content-Type", "application/x-www-form-urlencoded");

			HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);

			ResponseEntity<String> result = restTemplate.postForEntity(uri, httpEntity, String.class);
			resultado = result.getStatusCode().toString();

			Gson gson = new Gson();
			RespuestaServicioToken r = gson.fromJson(result.getBody(), RespuestaServicioToken.class);
			token = r.getAccess_token();

			if (resultado.contains("200")) {
				// Actulizar token
				UtilidadesAdapter.pintarLog("Se actualizo token correctamente");
				ConfiguracionEntity t = confs.stream().filter(a -> a.getCodigo().equals("TOKEN")).findAny()
						.orElse(null);
				t.setValor(token);
				confPort.actualizarConf(t);
			} else {
				UtilidadesAdapter.pintarLog("No se pudo actualizar el token correctamente");
			}

		} catch (URISyntaxException e) {
			UtilidadesAdapter.pintarLog("Error al refresh token");
			salida = false;
			e.printStackTrace();
		} catch (HttpClientErrorException e2) {
			UtilidadesAdapter.pintarLog("Error al refresh token http:");
			salida = false;
		}

		return salida;

	}

	@Override
	public boolean envioMensajePrueba() {
		ConfiguracionEntity c = confPort.obtenerConfiguracionPorCodigo("TEL-PRUEBA");
		String sme = servicioMsgEnvio;
		return envioMensaje(c.getValor(), "MSGS working correctly message "+sme, null, false);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void envioMail(String emailAdmin) {
		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}", "");
		params.put("${cuerpo}",
				"There was an error when testing text message sending in the FamiliasUnidas system, please validate with the administrator.");
		correoPort.enviarCorreoNotificacion(emailAdmin, "Error sending msm on FamiliasUnidas", params, null, "", true);
	}

}
