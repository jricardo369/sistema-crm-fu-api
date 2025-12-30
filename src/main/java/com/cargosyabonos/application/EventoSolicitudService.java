package com.cargosyabonos.application;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.in.EventoSolicitudUseCase;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.MsgPort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.CitasDeUsuario;
import com.cargosyabonos.domain.CitasUsuario;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.SchedulersActivasDeSolicitud;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
public class EventoSolicitudService implements EventoSolicitudUseCase {

	Logger log = LoggerFactory.getLogger(EventoSolicitudService.class);

	@Autowired
	private EventoSolicitudPort esPort;

	@Autowired
	private SolicitudPort solPort;

	@Autowired
	private UsuariosPort usPort;

	@Autowired
	private CorreoElectronicoUseCase correoUs;

	@Autowired
	private MsgPort msgPort;

	@Override
	public List<EventoSolicitudEntity> obtenerEventosSolicitudes(int idSolicitud, int idUsuario) {
		UsuarioEntity u = usPort.buscarPorId(idUsuario);
		if (u.getRol().equals("5") || u.getRol().equals("7")) {
			return esPort.obtenerEventosSolicitudesSinPagos(idSolicitud);
		} else {
			return esPort.obtenerEventosSolicitudes(idSolicitud);
		}
	}

	@Override
	public void crearEventoSolicitud(EventoSolicitud es, int idUsuarioSchedule) {

		String evento = es.getEvento();
		String tipoEvento = es.getTipo();
		String fechaString = "";

		if (tipoEvento.equals("Interview review") || tipoEvento.equals("Schedule")) {
			if (es.getFecha() == null) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The date is mandatory");
			}
			if (es.getHora() == null) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The time is mandatory");
			}
			if (es.getTipoSchedule() == null) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The time type is mandatory");
			}
		}

		EventoSolicitudEntity e = new EventoSolicitudEntity();
		if (es.getFecha() == null) {
			Date d = UtilidadesAdapter.fechaActualDate();
			// System.out.println("d:"+d);
			e.setFecha(d);
		} else {
			try {
				e.setFecha(UtilidadesAdapter.cadenaAFecha(es.getFecha()));
			} catch (ParseException e1) {
			}
		}
		if (!tipoEvento.equals("Interview review")) {
			e.setDescripcion(es.getDescripcion());
			if (e.getDescripcion() == null) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Description cannot be null");
			}
			if ("".equals(e.getDescripcion())) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Description cannot be empty");
			} else {
				if (e.getDescripcion().length() > 250) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"The length of the description cannot be greater than 250");
				}
			}
		}

		// System.out.println("evento:"+evento);

		if (evento == null) {
			e.setEvento("");
		} else {
			e.setEvento(es.getEvento());
		}

		SolicitudEntity sol = solPort.obtenerSolicitud(es.getIdSolicitud());

		e.setSolicitud(sol);
		e.setTipo(es.getTipo());
		e.setUsuario(es.getUsuario());

		if (tipoEvento.equals("Important")) {
			solPort.actualizarImportante("Important", sol.getIdSolicitud());
		}

		if (tipoEvento.equals("Schedule")) {
			// Obtener fecha y hora
			// fechaString = UtilidadesAdapter.formatearFecha(es.getFecha());
			fechaString = UtilidadesAdapter.formatearFechaStringAtipoUS(es.getFecha());
			System.out
					.println("fechaString:" + fechaString + "|horaString:" + es.getHora() + "|" + es.getTipoSchedule());
			e.setDescripcion(e.getDescripcion() + "," + " Appointment for the day " + fechaString + " at "
					+ es.getHora() + " " + es.getTipoSchedule());
			e.setFecha(UtilidadesAdapter.fechaActualDate());
		}

		if (tipoEvento.equals("Interview review")) {

			String[] partes = es.getHora().split(":");

			if (partes.length >= 2) {
				String hora = partes[0];
				String minutos = partes[1];
				hora = UtilidadesAdapter.agregarCerosALaIzquierda(2, hora);
				es.setHora(hora + ":" + minutos);
			}

			// Validar que no este dada de alta
			List<EventoSolicitudEntity> le = esPort.obtenerEventosScheduleDeFechaPorUsuario(es.getFecha(), es.getHora(),
					es.getTipoSchedule(), idUsuarioSchedule);
			if (!le.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"You already have an appointment with the selected date and time");
			}

			UsuarioEntity ue = usPort.buscarPorId(idUsuarioSchedule);
			e.setFecha(UtilidadesAdapter.fechaActualDate());
			e.setTipo("Schedule");
			e.setEvento("Schedule");
			UtilidadesAdapter.pintarLog("fecha:" + es.getFecha());
			try {
				e.setFechaSchedule(UtilidadesAdapter.cadenaAFecha(es.getFecha()));
			} catch (ParseException e1) {
			}
			e.setTipoSchedule(es.getTipoSchedule());
			e.setUsuarioSchedule(String.valueOf(idUsuarioSchedule));
			e.setUsuario(ue.getUsuario());
			e.setHoraSchedule(es.getHora());
			e.setEstatusSchedule("1");

			e.setTimeZoneSchedule(es.getTimeZone() == null ? "PST" : es.getTimeZone());
			e.setDescripcion("Scheduled clinician appointment "
					+ UtilidadesAdapter.formatearFechaUS(e.getFechaSchedule()) + ", schedule " + e.getHoraSchedule()
					+ " " + e.getTipoSchedule() + " " + e.getTimeZoneSchedule() + " " + ue.getUsuario());

			// Enviar cita a cliente

		}

		esPort.crearEventoSolicitud(e);

		if (tipoEvento.equals("Schedule")) {

			// Enviar notificacion a cliente
			correoUs.enviarCorreoCita(sol.getNombreClienteCompleto(), fechaString, es.getHora(), es.getTipo(),
					sol.getEmail(), sol.getIdSolicitud(), null);

		}
		if (tipoEvento.equals("Suicide")) {

			// Correo y mensaje a revisor
			UsuarioEntity us = usPort.buscarPorId(sol.getUsuarioRevisor());
			correoUs.enviarCorreoSuicidio(sol, us.getCorreoElectronico(), us.getNombre());
			msgPort.envioMensaje(us.getTelefono(), "The customer " + sol.getCliente() + " from request "
					+ sol.getIdSolicitud() + " has suicidal signs", sol, false);
			// Correo y mensaje a master
			List<UsuarioEntity> listUsRol = usPort.obtenerUsuariosDeRol(2, 0);
			for (UsuarioEntity u : listUsRol) {
				correoUs.enviarCorreoSuicidio(sol, u.getCorreoElectronico(), u.getNombre());
				msgPort.envioMensaje(u.getTelefono(), "The customer " + sol.getCliente() + " from request "
						+ sol.getIdSolicitud() + " has suicidal signs", sol, false);
			}
		}

	}

	@Override
	public void eliminarEventoSolicitud(int idEvento) {
		esPort.eliminarEventoSolicitud(idEvento);
	}

	@Override
	public List<CitasDeUsuario> citasDeUsuarioEntrevistador(int idUsuario, Date fi, Date ff) {
		List<CitasDeUsuario> ls = new ArrayList<>();
		List<CitasUsuario> cu = esPort.citasDeUsuarioEntrevistador(idUsuario, fi, ff);
		for (CitasUsuario c : cu) {
			CitasDeUsuario cdu = new CitasDeUsuario();
			cdu.setFecha(c.getfecha().substring(0, 10));
			cdu.setHora(c.gethora_schedule());
			cdu.setTipo(c.gettipo_schedule());
			cdu.setCliente(c.getcliente());
			cdu.setTelefono(c.gettelefono());
			ls.add(cdu);
		}
		return ls;
	}

	@Override
	public List<Integer> solicitudesConTipoImportant(String tipoEvento) {
		return esPort.solicitudesConTipoImportant(tipoEvento);
	}

	@Override
	public void actualizarEstatusSchedule(int idEvento, String estatusSchedule, int idUsuario, String motivo) {

		if ("".equals(motivo)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Cancellation reason cannot be empty");
		}

		EventoSolicitudEntity e = esPort.findByIdEvento(idEvento);
		UsuarioEntity u = usPort.buscarPorId(Integer.valueOf(e.getUsuarioSchedule()));
		UsuarioEntity usEntrada = usPort.buscarPorId(idUsuario);
		SolicitudEntity s = e.getSolicitud();

		esPort.ingresarEventoDeSolicitud("Update",
				"The user " + usEntrada.getUsuario() + " canceled the file for the reason:" + motivo, "Reject File",
				usEntrada.getUsuario(), s);

		UtilidadesAdapter.pintarLog("Cancelar cita");
		String fecha = UtilidadesAdapter.formatearFechaUS(e.getFecha());
		esPort.ingresarEventoDeSolicitud("Update",
				"The user's appointment for the date " + fecha + " at " + e.getHoraSchedule() + " "
						+ e.getTipoSchedule() + " for the user " + u.getNombre() + " is cancelled",
				"Info", usEntrada.getUsuario(), s);

		String rolUsEvento = u.getRol();
		UtilidadesAdapter.pintarLog("Usuario rol:" + rolUsEvento);

		if (rolUsEvento.equals("5")) {
			// int idUs = solPort.obtenerRevisorConMenorSolicitudes(0);
			// s.setUsuarioRevisando(idUs);
			solPort.actualizarEstatusSolicitud(s.getIdSolicitud(), 1);
			solPort.actualizarAsignacionIntReset(s.getIdSolicitud());
			solPort.actualizarFinInterview(0, e.getSolicitud().getIdSolicitud());
		}

		if (rolUsEvento.equals("11")) {
			if (e.getDescripcion().contains("Scheduled appointment")) {
				solPort.actualizarEstatusSolicitud(s.getIdSolicitud(), 1);
				solPort.actualizarAsignacionIntReset(s.getIdSolicitud());
				solPort.actualizarFinInterview(0, e.getSolicitud().getIdSolicitud());
			} else {
				solPort.actualizarAssignedClinician(0, s.getIdSolicitud());
				solPort.actualizarFinAsgClnc("0", e.getSolicitud().getIdSolicitud());
			}
		}

		if (rolUsEvento.equals("8")) {
			solPort.actualizarIntScalesSolicitud(s.getIdSolicitud());
			solPort.actualizarFinIntSc("0", e.getSolicitud().getIdSolicitud());
		}

		e.setEstatusSchedule(estatusSchedule);
		esPort.actualizarEventoSolicitud(e);

	}

	@Override
	public List<SchedulersActivasDeSolicitud> obtenerSchedulesActivasDeSolicitud(int idSolicitud, int idUsuario) {
		return esPort.obtenerSchedulesActivasDeSolicitud(idSolicitud, idUsuario);
	}

	public static void main(String args[]) {
		String tipoEvento = "Interview review";
		String te = "No Am";
		if (tipoEvento.equals("Interview review")) {
			String t = te.toUpperCase();
			if (!t.contains("NO ANSWER") && !t.contains("NO RESPONSE")) {
				System.out.println("Si tuvo sesion");

			}
		}
	}

	@Override
	public List<EventoSolicitud> obtenerEventosDash(String fechai, String fechaf, int tileDash, int idUsuario,
			int usuario) {
		return esPort.obtenerEventosDash(fechai, fechaf, tileDash, idUsuario, usuario);
	}

}
