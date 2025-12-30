package com.cargosyabonos.adapter.rest;

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
import com.cargosyabonos.application.port.out.EnviarCorreoPort;
import com.cargosyabonos.application.port.out.MsgPort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.SolicitudEntity;

@RequestMapping("/util")
@RestController
public class UtilController {

	Logger log = LoggerFactory.getLogger(UtilController.class);

	@Autowired
	private EnviarCorreoPort envCorrPort;

	@Autowired
	private CorreoElectronicoUseCase ceUc;
	
	@Autowired
	private EventoSolicitudUseCase ev;

	@Autowired
	private SolicitudPort solPort;

	@Autowired
	private MsgPort msgPort;

	@GetMapping("envio-correo/{email}")
	public void envioCorreTest(@PathVariable("email") String email) {
		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}", "Jose");
		params.put("${fecha}", "2024-09-10");
		params.put("${servicio}", "Visa U");
		envCorrPort.enviarCorreoDeLayout(email, "Test", params, "email-recordatorio-cita");
	}

	@GetMapping("envio-correo-new/{email}")
	public void envioCorreoTestNew(@PathVariable("email") String email, @RequestParam("tipo") String tipo,
			@RequestParam(required = false) String fecha, @RequestParam(required = false) String hora,
			@RequestParam(required = false) String tipoH, @RequestParam(required = false) String estado,
			@RequestParam(required = false) String zonaHoraria) {
		envCorrPort.testMail(email, tipo, fecha, hora, tipoH, estado, zonaHoraria);
	}

	@GetMapping("envio-correo-calendar/{email}")
	public void envioCorreoCalendar(@PathVariable("email") String email) {
		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}", 1);
		params.put("${fecha}", "2024-07-17 0:00:00");
		params.put("${hora}", "1");
		params.put("${tipo}", "PM");
		envCorrPort.enviarCorreoDeLayoutCalendar(email, "Appointment for interview", params, "email-cita", null);
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

	@GetMapping("envio-mensaje-prueba")
	public boolean envioMensajePrueba() {
		return msgPort.envioMensajePrueba();
	}
	
	@GetMapping("probar-date")
	public Date probarDate() {
		Date d = UtilidadesAdapter.fechaActualDate();
		System.out.println("date:"+d);
		EventoSolicitud es = new EventoSolicitud();
		es.setEvento("test");
		es.setTipo("Update");
		es.setDescripcion("Test desc");
		es.setIdSolicitud(4);
		ev.crearEventoSolicitud(es, 1);
		return d;
	}

}
