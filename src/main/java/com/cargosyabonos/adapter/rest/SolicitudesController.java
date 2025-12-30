package com.cargosyabonos.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.SolicitudUseCase;
import com.cargosyabonos.domain.ReporteComparacionAnios;
import com.cargosyabonos.domain.Solicitud;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.TelefonosSolicitudes;

@RequestMapping("/solicitudes")
@RestController
public class SolicitudesController {


	@Autowired
	private SolicitudUseCase oCase;

	@GetMapping("solicitudes-de-usuario/{idUsuario}")
	public List<Solicitud> obtenerSolicitudesDeUsuario(@PathVariable("idUsuario") int idUsuario,@RequestParam(required = false) String estatus,
			@RequestParam(required = false) String fechai,@RequestParam(required = false) String fechaf,@RequestParam(required = false) String ordenarPor,
			@RequestParam(required = false) String orden,@RequestParam(required = false) String valor,@RequestParam(required = false) String campo,
			@RequestParam(required = false) boolean myFiles,@RequestParam(required = false) String cerradas,@RequestParam(required = false) boolean primeraVez) {
		
		int e = 0;
		fechai = fechai == null ? "": fechai;
		fechaf = fechaf == null ? "": fechaf;
		ordenarPor = ordenarPor == null ? "": ordenarPor;
		UtilidadesAdapter.pintarLog("Estatus:" + estatus+"|Fechai:"+fechai+"|Fechaf:"+fechaf+"|OrdenarPor:"+ordenarPor+"|Campo:"+campo+"|Valor:"+valor+"|Cerradas:"+cerradas+"|PrimeraVez:"+primeraVez);
		
		if (estatus != null) {
			e = Integer.valueOf(estatus);
		}
		
		return oCase.obtenerSolicitudesV2(idUsuario, e, fechai, fechaf, ordenarPor, orden, campo, valor, myFiles, cerradas, primeraVez,0);
		
	}

	@GetMapping("/{idSolicitud}")
	public SolicitudEntity obtenerSolicitud(@PathVariable("idSolicitud") int idSolicitud) {
		return oCase.obtenerSolicitud(idSolicitud);
	}

	@GetMapping("solicitud-por-id/{idSolicitud}")
	public Solicitud obtenerSolicitudObj(@PathVariable("idSolicitud") int idSolicitud,@RequestParam(required = false) String idUsuario) {
		UtilidadesAdapter.pintarLog("idUsuario:"+idUsuario);
		int idUs = 0;
		if (idUsuario != null) {
			if (!"".equals(idUsuario)) {
				idUs = Integer.valueOf(idUsuario);
			}
		}
		return oCase.obtenerSolicitudObjV2(idSolicitud,idUs);
	}

	@GetMapping("/revisor-con-menos-solicitudes")
	public int obtenerSolsDeRevisor() {
		return oCase.obtenerRevisorConMenorSolicitudes();
	}
	
	@GetMapping("solicitudes-de-telefono/{telefono}")
	public List<TelefonosSolicitudes> obtenerSolicitudesDeUsuario(@PathVariable("telefono") String telefono) {
		return oCase.obtenerSolicitudesDeTelefono(telefono);
	}

	@PostMapping()
	public void crearSolicitud(@RequestBody Solicitud o, @RequestParam("idUsuario") int idUsuario) {
		oCase.crearSolicitud(o, idUsuario);
	}

	@PutMapping()
	public void actualizarSolicitud(@RequestBody Solicitud o,@RequestParam( required = false) String estatus,@RequestParam( required = false) int idUsuario) {
		oCase.actualizarSolicitud(o,estatus,idUsuario);
	}

	@PutMapping("actualizar-estatus-solicitud/{idSolicitud}/{idEstatus}")
	public void actualizarEstatusSolicitud(@PathVariable("idSolicitud") int idSolicitud,
			@PathVariable("idEstatus") int idEstatus, @RequestParam("idUsuario") int idUsuario,@RequestParam(required = false) boolean closed,@RequestParam(required = false) String motivo) {
		
		oCase.actualizarEstatusSolicitud(idSolicitud, idEstatus, idUsuario,closed,motivo);
	}

	@DeleteMapping("{idSolicitud}")
	public void eliminarSolicitud(@PathVariable("idSolicitud") int idSolicitud) {
		oCase.eliminarSolicitud(idSolicitud);
	}

	@PutMapping("envio-siguiente-proceso/{idSolicitud}")
	public void envioSiguienteProceso(@PathVariable("idSolicitud") int idSolicitud,
			@RequestParam("idUsuarioCambio") int idUsuarioCambio,
			@RequestParam(required = false) String idDisponibilidad,
			@RequestParam(required = false) boolean fechaAnterior) {
		
		int idDisp = 0;
		//UtilidadesAdapter.pintarLog("idDisponibilidad:" + idDisponibilidad);
		if (idDisponibilidad != null) {
			idDisp = Integer.valueOf(idDisponibilidad);
		}
		oCase.envioSiguienteProceso(idSolicitud, idUsuarioCambio, idDisp,fechaAnterior);
		
	}
	
	@PutMapping("envio-interviewer-case-manager/{idSolicitud}")
	public void envioInterviewerCaseManager(@PathVariable("idSolicitud") int idSolicitud,
			@RequestParam("idUsuarioCambio") int idUsuarioCambio,
			@RequestParam("idDisponibilidad") String idDisponibilidad,
			@RequestParam(required = false) boolean fechaAnterior) {
		
		int idDisp = 0;
		UtilidadesAdapter.pintarLog("Envio int clinician idDisponibilidad:" + idDisponibilidad+"|idUsuarioCambio:" + idUsuarioCambio+"|fechaAnterior:" + fechaAnterior);
		if (idDisponibilidad != null) {
			idDisp = Integer.valueOf(idDisponibilidad);
		}
		oCase.envioInterviewerCaseManager(idSolicitud, idUsuarioCambio, idDisp,fechaAnterior);
		
	}
	
	@PutMapping("envio-interviewer-scales/{idSolicitud}")
	public void envioInterviewerScales(@PathVariable("idSolicitud") int idSolicitud,
			@RequestParam("idUsuarioCambio") int idUsuarioCambio,
			@RequestParam("idDisponibilidad") String idDisponibilidad,
			@RequestParam(required = false) boolean fechaAnterior) {
		
		int idDisp = 0;
		UtilidadesAdapter.pintarLog("idDisponibilidad:" + idDisponibilidad+"|idUsuarioCambio:" + idUsuarioCambio+"|fechaAnterior:" + fechaAnterior);
		if (idDisponibilidad != null) {
			idDisp = Integer.valueOf(idDisponibilidad);
		}
		oCase.envioInterviewerScales(idSolicitud, idUsuarioCambio, idDisp,fechaAnterior);
		
	}
	
	@PutMapping("envio-interviewer-clinician/{idSolicitud}")
	public void envioInterviewerClinician(@PathVariable("idSolicitud") int idSolicitud,
			@RequestParam("idUsuarioCambio") int idUsuarioCambio,
			@RequestParam("idDisponibilidad") String idDisponibilidad,
			@RequestParam(required = false) boolean fechaAnterior) {
		
		int idDisp = 0;
		UtilidadesAdapter.pintarLog("Envio int clinician idDisponibilidad:" + idDisponibilidad+"|idUsuarioCambio:" + idUsuarioCambio+"|fechaAnterior:" + fechaAnterior);
		if (idDisponibilidad != null) {
			idDisp = Integer.valueOf(idDisponibilidad);
		}
		oCase.envioInterviewerClinician(idSolicitud, idUsuarioCambio, idDisp,fechaAnterior);
		
	}
	
	@PutMapping("envio-fin-entrevista-case-manager/{idSolicitud}")
	public void envioFinEntrevistaCasemanager(@PathVariable("idSolicitud") int idSolicitud,
			@RequestParam("idUsuarioCambio") int idUsuarioCambio) {
		
		oCase.envioFinEntrevistaCaseManager(idSolicitud, idUsuarioCambio);
		
	}
	
	@PutMapping("envio-fin-entrevista-scales/{idSolicitud}")
	public void envioFinEntrevistaScales(@PathVariable("idSolicitud") int idSolicitud,
			@RequestParam("idUsuarioCambio") int idUsuarioCambio) {
		 
		oCase.envioFinEntrevistaScales(idSolicitud, idUsuarioCambio);
		
	}
	
	@PutMapping("envio-fin-entrevista-clinician/{idSolicitud}")
	public void envioFinEntrevistaClinician(@PathVariable("idSolicitud") int idSolicitud,
			@RequestParam("idUsuarioCambio") int idUsuarioCambio) {
		
		oCase.envioFinEntrevistaClinician(idSolicitud, idUsuarioCambio);
		
	}

	@PutMapping("parche-fin-clinician/{idSolicitud}")
	public void factoryinEntrevistaClinician(@PathVariable("idSolicitud") int idSolicitud,
			@RequestParam("idUsuarioCambio") int idUsuarioCambio) {
		
		oCase.parcheFinEntrevistaClinician(idSolicitud, idUsuarioCambio);
		
	}
	
	@PutMapping("ready-on-draft/{idSolicitud}")
	public void envioReadyOnDraft(@PathVariable("idSolicitud") int idSolicitud,
			@RequestParam("idUsuario") int idUsuario) {
		oCase.envioReadyOnDraft(idSolicitud, idUsuario);
	}
	
	@PutMapping("/reasignar/{idSolicitud}/{idUsuario}")
	public void reasginar( @PathVariable("idUsuario") int idUsuario,@PathVariable("idSolicitud") int idSolicitud,@RequestParam("idUsuarioEnvio") int idUsuarioEnvio,@RequestParam(required = false) String motivo) {
		oCase.reasginar(idUsuario, motivo, idSolicitud, idUsuarioEnvio);
	}
	
	@PutMapping("/cancel-template/{idSolicitud}/{idUsuario}")
	public void cancelTamplate( @PathVariable("idUsuario") int idUsuario,@PathVariable("idSolicitud") int idSolicitud,@RequestParam("idUsuarioEnvio") int idUsuarioEnvio,@RequestParam(required = false) String motivo) {
		oCase.cancelTemplate(idUsuario, motivo, idSolicitud, idUsuarioEnvio);
	}
	
	@PutMapping("/no-show/{idSolicitud}")
	public void noShow(@PathVariable("idSolicitud") int idSolicitud,@RequestParam("idUsuarioEnvio") int idUsuarioEnvio,@RequestParam(required = false) String motivo) {
		oCase.noShow(motivo, idSolicitud, idUsuarioEnvio);
	}
	
	@PostMapping("/excel/carga")
	public String cargarExcel(@RequestParam(value = "archivo", required = false) MultipartFile archivo) {
		return oCase.cargarExcel(archivo);
	}
	
	@GetMapping("reporte-anios/{anio}")
	public ReporteComparacionAnios obtenerReporteAnios(@PathVariable("anio") int anio,@RequestParam(required = true) String filtro) {
		return oCase.reporteDeAnio(anio,filtro); 
	}
	
	@GetMapping("obtener-textos-ordenar-por/{idUsuario}")
	public List<String> obtenerTextosOrdenarPor(@PathVariable("idUsuario") int idUsuario) {
		return oCase.obtenerTextosOrdenarPor(idUsuario);
	}
	
	@GetMapping("obtener-textos-tipo-para-filtro/{idUsuario}")
	public List<String>  obtenerTextosTipoParaFiltros(@PathVariable("idUsuario") int idUsuario) {
		return oCase.obtenerTextosTipoParaFiltros(idUsuario);
	}
	
	@PutMapping("/agregar-cupon/{idSolicitud}")
	public void reasginar(@PathVariable("idSolicitud") int idSolicitud,@RequestParam("idUsuario") int idUsuario) {
		oCase.actualizarSolicitudConCupon(idSolicitud, idUsuario);
	}
	
	@PutMapping("/actualizar-emails-abo-sel/{idSolicitud}")
	public void actualizarEmailsAboSel(@PathVariable("idSolicitud") int idSolicitud,@RequestParam("emails") String emailsAbogado) {
		oCase.actualizarEmailAbo(emailsAbogado, idSolicitud);
	}
	

}
