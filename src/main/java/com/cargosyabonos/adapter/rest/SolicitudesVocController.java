package com.cargosyabonos.adapter.rest;

import java.io.FileNotFoundException;
import java.io.IOException;
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
import com.cargosyabonos.adapter.out.file.ExcelSolicitudesVOC;
import com.cargosyabonos.adapter.out.file.PdfProgressLetterV2;
import com.cargosyabonos.application.port.in.SolicitudVocUseCase;
import com.cargosyabonos.domain.NumeroCasosSolicitudes;
import com.cargosyabonos.domain.SolicitudVoc;
import com.itextpdf.text.DocumentException;

@RequestMapping("/solicitudes-voc")
@RestController
public class SolicitudesVocController {

	@Autowired
	private PdfProgressLetterV2 pdfProcessLetter;
	
	@Autowired
	private SolicitudVocUseCase oCase;
	
	@GetMapping("reporte-solicitudes-filters")
	public List<SolicitudVoc> obtenerSolicitudesFiltros(@RequestParam(required = false) String idUsuario,@RequestParam(required = false) String valor,@RequestParam(required = false) String campo,
			@RequestParam(required = false) String fecha1, @RequestParam(required = false) String fecha2, @RequestParam(required = false) boolean myFiles) {
		
		UtilidadesAdapter.pintarLog("idUsuario:"+idUsuario+"|valor:"+valor+"|campo:"+campo+"|fecha1:"+fecha1+"|fecha2:"+fecha2+"|myFiles:"+myFiles);
		int idUs = 0;
		if (idUsuario != null) {
			if (!"".equals(idUsuario)) {
				idUs = Integer.valueOf(idUsuario);
			}
		}
		if (campo == null) {
			campo = "";
			if ("null".equals(campo)) {
				campo = "";
			}
		}
		UtilidadesAdapter.pintarLog("campo:"+campo);
		return oCase.obtenerSolicitudesFiltroV2(idUs, campo, valor, fecha1, fecha2);
	}

	@GetMapping("solicitudes-de-usuario/{idUsuario}")
	public List<SolicitudVoc> obtenerSolicitudesDeUsuario(@PathVariable("idUsuario") int idUsuario,@RequestParam(required = false) String estatus,
			@RequestParam(required = false) String fechai,@RequestParam(required = false) String fechaf,@RequestParam(required = false) String ordenarPor,
			@RequestParam(required = false) String orden,@RequestParam(required = false) String valor,@RequestParam(required = false) String campo, @RequestParam(required = false) boolean myFiles,
			@RequestParam(required = false) String cerradas) {
		
		int e = 0;
		fechai = fechai == null ? "" : fechai;
		fechaf = fechaf == null ? "" : fechaf;
		ordenarPor = ordenarPor == null ? "" : ordenarPor;
		orden = orden == null ? "" : orden;
		valor = valor == null ? "" : valor;
		orden = orden == null ? "" : orden;
		campo = campo == null ? "" : campo;
		e = estatus != null ? e = Integer.valueOf(estatus) : 0;
		
		UtilidadesAdapter.pintarLog("usuario:" + idUsuario
				+"\n" +"estatus" + estatus
				+"\n" +"fechai" + fechai
				+"\n" +"fechaf" + fechaf
				+"\n" +"ordenarPor" + ordenarPor
				+"\n" +"orden" + orden
				+"\n" +"valor" + valor
				+"\n" +"campo" + campo
				+"\n" +"myFiles" + myFiles
				+"\n" +"cerradas" + cerradas);

		return oCase.obtenerSolicitudes(idUsuario, e, 0, fechai, fechaf, ordenarPor, orden, campo, valor, myFiles, cerradas);
	}
	
	@GetMapping("solicitudes-de-usuario-excel/{idUsuario}")
	public  byte[] obtenerSolicitudesDeUsuarioExcel(@PathVariable("idUsuario") int idUsuario,@RequestParam(required = false) String estatus,
			@RequestParam(required = false) String fechai,@RequestParam(required = false) String fechaf,@RequestParam(required = false) String ordenarPor,
			@RequestParam(required = false) String orden,@RequestParam(required = false) String valor,@RequestParam(required = false) String campo, @RequestParam(required = false) boolean myFiles,
			@RequestParam(required = false) String cerradas) {
		
		int e = 0;
		fechai = fechai == null ? "" : fechai;
		fechaf = fechaf == null ? "" : fechaf;
		ordenarPor = ordenarPor == null ? "" : ordenarPor;
		orden = orden == null ? "" : orden;
		valor = valor == null ? "" : valor;
		orden = orden == null ? "" : orden;
		campo = campo == null ? "" : campo;
		e = estatus != null ? e = Integer.valueOf(estatus) : 0;

		List<SolicitudVoc> l =  oCase.obtenerSolicitudes(idUsuario, e, 0, fechai, fechaf, ordenarPor, orden, campo, valor, myFiles, cerradas);
		byte[] s = null;
		try {
			s = ExcelSolicitudesVOC.generarExcelSolicitudesVOC(l);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return s;
	}
	
	@GetMapping("solicitudes-activas-de-usuario/{idUsuario}")
	public List<SolicitudVoc> obtenerSolicitudesActivasDeUsuario(@PathVariable("idUsuario") int idUsuario) {
		return oCase.obtenerSolicitudesActivasTerapeuta(idUsuario);
	}

	@GetMapping("solicitud-por-id/{idSolicitud}")
	public SolicitudVoc obtenerSolicitudObj(@PathVariable("idSolicitud") int idSolicitud,
			@RequestParam(required = false) String idUsuario) {
		UtilidadesAdapter.pintarLog("idUsuario:" + idUsuario);
		int idUs = 0;
		if (idUsuario != null) {
			if (!"".equals(idUsuario)) {
				idUs = Integer.valueOf(idUsuario);
			}
		}
		return oCase.obtenerSolicitudObj(idSolicitud, idUs);
	}
	
	@GetMapping("solicitudes-de-numerocasos/{numeroCaso}")
	public List<NumeroCasosSolicitudes> obtenerNumerosCaso(@PathVariable("numeroCaso") String numeroCaso) {
		return oCase.obtenerNumerosCaso(numeroCaso);
	}

	@PostMapping()
	public void crearSolicitudVoc(@RequestBody SolicitudVoc o, @RequestParam("idUsuario") int idUsuario) {
		oCase.crearSolicitud(o, idUsuario);
		UtilidadesAdapter.pintarLog("Se creo file voc");
	}

	@PutMapping()
	public void actualizarSolicitud(@RequestBody SolicitudVoc o) {
		UtilidadesAdapter.pintarLog("-------------------ACT VOC");
		oCase.actualizarSolicitud(o);
	}

	@PutMapping("asignar-terapeuta/{idSolicitud}/{idUsuario}")
	public void asignarTerapueta(@PathVariable("idSolicitud") int idSolicitud,
			@PathVariable("idUsuario") int idUsuario, @RequestParam("idUsuarioEntrada") int idUsuarioEntrada) {		
		oCase.actualizarUsuarioTerapeuta(idUsuario, idSolicitud, idUsuarioEntrada);
	}
	
	@PutMapping("actualizar-estatus-solicitud/{idSolicitud}/{idEstatus}")
	public void actualizarEstatusSolicitud(@PathVariable("idSolicitud") int idSolicitud,
			@PathVariable("idEstatus") int idEstatus, @RequestParam("idUsuario") int idUsuario,@RequestParam(required = false) boolean closed) {
		
		oCase.actualizarEstatusSolicitud(idSolicitud, idEstatus, idUsuario,closed);
	}

	@DeleteMapping("{idSolicitud}")
	public void eliminarSolicitud(@PathVariable("idSolicitud") int idSolicitud) {
		oCase.eliminarSolicitud(idSolicitud);
	}
	
	@PutMapping("/reasignar/{idSolicitud}/{idUsuario}")
	public void reasginar( @PathVariable("idUsuario") int idUsuario,@PathVariable("idSolicitud") int idSolicitud,@RequestParam("idUsuarioEnvio") int idUsuarioEnvio,@RequestParam(required = false) String motivo) {
		oCase.reasginar(idUsuario, motivo, idSolicitud, idUsuarioEnvio);
	}
	
	@GetMapping("generar-process-letter/{idSolicitudVoc}")
	public byte[] generarProcessLetter(@PathVariable("idSolicitudVoc") int idSolicitudVoc,@RequestParam("idUsuario") String idUsuario) {
		UtilidadesAdapter.pintarLog("idSolicitudVOC:"+idSolicitudVoc+"|idUsuario:"+idUsuario);
		byte [] salida = null;
		try {
			salida =  pdfProcessLetter.generarPdf(idSolicitudVoc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return salida;
	}
	
	@PostMapping("/excel/carga")
	public String cargarExcel(@RequestParam(value = "archivo", required = false) MultipartFile archivo) {
		return oCase.cargarExcel(archivo);
	}
	
	@PutMapping("/syncronizar-sesiones/{idSolicitud}/{idUsuario}")
	public void syncSesiones( @PathVariable("idUsuario") int idUsuario,@PathVariable("idSolicitud") int idSolicitud) {
		oCase.syncNumSesiones(idSolicitud, idUsuario);
	}

}
