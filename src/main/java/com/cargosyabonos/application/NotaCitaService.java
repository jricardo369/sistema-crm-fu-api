package com.cargosyabonos.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.NotaCitaUseCase;
import com.cargosyabonos.application.port.out.EventoSolicitudVocPort;
import com.cargosyabonos.application.port.out.NotaCitaPort;
import com.cargosyabonos.application.port.out.SolicitudVocPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.NotaCitaEntity;
import com.cargosyabonos.domain.SolicitudVocEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class NotaCitaService implements NotaCitaUseCase {
	
	@Autowired
	private UsuariosPort usPort;

	@Autowired
	private EventoSolicitudVocPort evPort;
	
	@Autowired
	private NotaCitaPort ncPort;
	
	@Autowired
	private SolicitudVocPort solPort;

	@Override
	public List<NotaCitaEntity> obtenerNotasCitas(int idCita) {
		return ncPort.obtenerNotasCitas(idCita);
	}

	@Override
	public NotaCitaEntity obtenerNota(int idNota) {
		return ncPort.obtenerNotaCita(idNota);
	}

	@Override
	public void crearNotaCita(NotaCitaEntity a) {
		
		
		a = validacionCampos(a);
		validacionTamanioDatos(a);
		
		UtilidadesAdapter.pintarLog("objeto nota:"+a.toString());
		
		UtilidadesAdapter.pintarLog("a:"+a.getIdCita());
		NotaCitaEntity nc = ncPort.obtenerNotaDeCita(a.getIdCita());
		UtilidadesAdapter.pintarLog("nc:"+nc);
		if(nc != null){
			a.setIdNota(nc.getIdNota());
			ncPort.crearNotaCita(a);
		}else{
			ncPort.crearNotaCita(a);
		}
		
	}
	
	public NotaCitaEntity validacionCampos(NotaCitaEntity a){

		String tiempoSesion = a.getTiempoSesion() == null ? "" : a.getTiempoSesion();
		if (tiempoSesion.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Lenght of session is required");
		}

		String enfoquePrincipal = a.getEnfoquePrincipal() == null ? "" : a.getEnfoquePrincipal();
		if (enfoquePrincipal.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Main Focus is required");
		}

		String sintomaComp1 = a.getSintomaComportamiento1() == null ? "" : a.getSintomaComportamiento1();
		if (sintomaComp1.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Symptom/Behavior 1 es requerido");
		}		

		String rating1 = a.getRating1() == null ? "" : a.getRating1();
		if (rating1.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Rating 1 is required");
		}

		String sintomaComp2 = a.getSintomaComportamiento2() == null ? "" : a.getSintomaComportamiento2();
		if (sintomaComp2.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Symptom/Behavior 2 es requerido");
		}	

		String rating2 = a.getRating2() == null ? "" : a.getRating2();
		if (rating2.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Rating 2 is required");
		}

		String sintomaComp3 = a.getSintomaComportamiento3() == null ? "" : a.getSintomaComportamiento3();
		if (sintomaComp3.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Symptom/Behavior 3 es requerido");
		}	

		String rating3 = a.getRating3() == null ? "" : a.getRating3();
		if (rating3.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Rating3 is required");
		}
		
		String contenidoTerapia = a.getTipoContenidoSesion() == null ? "" : a.getTipoContenidoSesion();
		if (contenidoTerapia.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Content of the Therapy Session is required");
		}
		
		String comentarios = a.getComentarios() == null ? "" : a.getComentarios();
		if (comentarios.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Comments is required");
		}	

		String respuestaInte = a.getRespuestaDeIntervencion() == null ? "" : a.getRespuestaDeIntervencion();
		if (respuestaInte.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Response to Intervention is required");
		}

		String resumen = a.getResumenEvaluacion() == null ? "" : a.getResumenEvaluacion();
		if (resumen.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Summary of tour assessment is required");
		}

		String planFuturo = a.getPlanFuturo() == null ? "" : a.getPlanFuturo();
		if (planFuturo.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Plan for the immediate future is required");
		}
		
		

		a.setTiempoSesion(tiempoSesion);
		a.setEnfoquePrincipal(enfoquePrincipal);
		a.setSintomaComportamiento1(sintomaComp1);
		a.setRating1(rating1);
		a.setSintomaComportamiento2(sintomaComp2);
		a.setRating2(rating2);
		a.setSintomaComportamiento3(sintomaComp3);
		a.setRating3(rating3);
		a.setComentarios(comentarios);
		a.setRespuestaDeIntervencion(respuestaInte);
		a.setResumenEvaluacion(resumen);
		a.setPlanFuturo(planFuturo);

		return a;
	}
	
	public void validacionTamanioDatos(NotaCitaEntity a) {
		if (a.getEnfoquePrincipal().length() > 600) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Main Focus must not exceed 600 characters, you added " + a.getEnfoquePrincipal().length());
		}

		if (a.getSintomaComportamiento1().length() > 100) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Symptom/Behavior 1 must not exceed 100 characters, you added "
							+ a.getSintomaComportamiento1().length());
		}

		if (a.getSintomaComportamiento2().length() > 100) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Symptom/Behavior 2 must not exceed 100 characters, you added "
							+ a.getSintomaComportamiento2().length());
		}

		if (a.getSintomaComportamiento3().length() > 100) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Symptom/Behavior 3 must not exceed 100 characters, you added "
							+ a.getSintomaComportamiento3().length());
		}

		if (a.getComentarios().length() > 600) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Comments must not exceed 600 characters, you added " + a.getComentarios().length());
		}

		if (a.getResumenEvaluacion().length() > 600) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Summary of tour assessment not exceed 600 characters, you added "
							+ a.getResumenEvaluacion().length());
		}

		if (a.getPlanFuturo().length() > 600) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Plan for the immediate future not exceed 600 characters, you added " + a.getPlanFuturo().length());
		}
	}

	@Override
	public void actualizarNotaCita(NotaCitaEntity a) {
		
		validacionTamanioDatos(a);
		ncPort.actualizarNotaCita(a);
		
	}

	@Override
	public void eliminarNotaCita(int idNota,int idUsuario) {
		
		UsuarioEntity u = usPort.buscarPorId(idUsuario);
		
		SolicitudVocEntity s = solPort.obtenerSolicitud(ncPort.obtenerIdSolByIdNota(idNota));
		evPort.ingresarEventoDeSolicitud("Info", "Note " + idNota + " was deleted", "Info", u.getNombre(), s);
		NotaCitaEntity nc = ncPort.obtenerNotaCita(idNota);
		ncPort.eliminarNotaCita(nc);
		
	}

	@Override
	public NotaCitaEntity obtenerNotaDeCita(int idCita) {
		return ncPort.obtenerNotaDeCita(idCita);
	}

}
