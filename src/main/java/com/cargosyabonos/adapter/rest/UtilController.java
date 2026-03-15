package com.cargosyabonos.adapter.rest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.in.EventoSolicitudUseCase;
import com.cargosyabonos.application.port.out.CorreosPort;
import com.cargosyabonos.application.port.out.MsgPort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.application.port.out.TextosPort;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.SolicitudEntity;

@RequestMapping("/util")
@RestController
public class UtilController {

	Logger log = LoggerFactory.getLogger(UtilController.class);

	@Autowired
	private CorreosPort correosPort;

	@Autowired
	private CorreoElectronicoUseCase ceUc;
	
	@Autowired
	private EventoSolicitudUseCase ev;

	@Autowired
	private SolicitudPort solPort;

	@Autowired
	private MsgPort msgPort;

	@Autowired
	private TextosPort txtsPort;

	@GetMapping("envio-correo/{email}")
	public void envioCorreTest(@PathVariable("email") String email) {
		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}", "Jose");
		params.put("${fecha}", "2024-09-10");
		params.put("${servicio}", "Visa U");
		correosPort.enviarCorreoDeLayout(email, "Test", params, "email-recordatorio-cita");
	}

	@GetMapping("envio-correo-cita-cancelacion/{email}")
	public void envioCorreoCitaCancelacion(@PathVariable("email") String email) {
		ceUc.enviarCorreoCitaCancelacion("Jose Vazquez", "2026-02-18", "09:00", "AM", email, "FL", 123, "US", "","Entrevistador",null,false);
	}

	@GetMapping("envio-correo-pdf/{idSolicitud}/{email}")
	public void envioCorreoPdf(@PathVariable("idSolicitud") int idSolicitud, @PathVariable("email") String email) {
		SolicitudEntity s = solPort.obtenerSolicitud(idSolicitud);
		ceUc.enviarCorreoInvoice(email, s);
	}

	@GetMapping("envio-mensaje/{telefono}")
	public void envioNumero(@PathVariable("telefono") String telefono, @RequestParam("mensaje") String mensaje) {
		msgPort.envioMensaje(telefono, mensaje, null, false);
	}

	@GetMapping("envio-mensaje-de-job/{codigo}/{telefono}/{idioma}")
	public void envioMensajeDeJob(@PathVariable("codigo") String codigo,@PathVariable("telefono") String telefono,@PathVariable("idioma") String idioma) {

		if (codigo.equals("RECORDATORIO")) {
			msgPort.envioMensaje(telefono,
					txtsPort.textoEnvioRecordatorio("Nombre cliente", "2026-01-01", "09:00 AM", idioma), null, false);
		} else if (codigo.equals("PAYMENT")) {
			msgPort.envioMensaje(telefono, txtsPort.textoEnvioPayment(null, BigDecimal.ZERO, "Tipo Sol", idioma),
					null, false);
		}

	}

	@GetMapping("envio-mensaje-prueba")
	public boolean envioMensajePrueba() {
		return msgPort.envioMensajePrueba();
	}
	
	@GetMapping("probar-date")
	public Date probarDate() {
		Date d = UtilidadesAdapter.fechaActualDate();
		System.out.println("date:"+d);
		EventoSolicitud es = new EventoSolicitud();
		es.setEvento("local");
		es.setTipo("Update");
		es.setDescripcion("Test desc");
		es.setIdSolicitud(4);
		ev.crearEventoSolicitud(es, 1);
		return d;
	}

}
