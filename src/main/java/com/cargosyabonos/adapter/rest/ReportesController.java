package com.cargosyabonos.adapter.rest;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.adapter.out.file.ExcelFilesFirmas;
import com.cargosyabonos.adapter.out.file.ExcelFilesFirmasA;
import com.cargosyabonos.adapter.out.file.PdfPagos;
import com.cargosyabonos.application.port.in.EventoSolicitudUseCase;
import com.cargosyabonos.application.port.in.SolicitudUseCase;
import com.cargosyabonos.domain.CitasDeUsuario;
import com.cargosyabonos.domain.DetalleSolsPorFecha;
import com.cargosyabonos.domain.EventoSolicitud;
import com.cargosyabonos.domain.FilesFirmaAbogadoObj;
import com.cargosyabonos.domain.NumFilesAbogados;
import com.cargosyabonos.domain.ReporteContador;
import com.cargosyabonos.domain.ReporteDash;
import com.cargosyabonos.domain.ReporteSolsDeUsuarioObj;
import com.cargosyabonos.domain.Solicitud;
import com.itextpdf.text.DocumentException;


@RequestMapping("/reportes")
@RestController
public class ReportesController {
	
	@Autowired
	private SolicitudUseCase oCase;
	
	@Autowired
	private EventoSolicitudUseCase evCase;
	
	@Autowired
	private PdfPagos pdfPagos;
	
	@GetMapping("solicitudes-de-usuarios/{idUsuario}")
	public List<ReporteContador> solicitudesDeUsuarios(@PathVariable("idUsuario") int idUsuario,@RequestParam("fechai") Date fechai,
			@RequestParam("fechaf") Date fechaf) { 
		return oCase.obtenerReporteSolicitudesDeUsuarios(idUsuario,fechai, fechaf);
	}
	
	@GetMapping("solicitudes-de-usuario/{idUsuario}")
	public List<ReporteSolsDeUsuarioObj> solicitudesDeUsuario(@PathVariable("idUsuario") int idUsuario,@RequestParam("fechai") Date fechai,
			@RequestParam("fechaf") Date fechaf) { 
		return oCase.obtenerReporteSolicitudesDeUsuario(fechai, fechaf, idUsuario);
	}
	
	@GetMapping("correos-a-abogados")
	public List<NumFilesAbogados> correosDeAbogados(@RequestParam("fechai") String fechai,
			@RequestParam("fechaf") String fechaf) { 
		//return oCase.obtenerReporteMailsAbogados(fechai, fechaf);
		return oCase.obtenerFirmasAbogadosyNumFiles(fechai, fechaf);
	}
	
	@GetMapping("files-de-firma-abogado")
	public List<FilesFirmaAbogadoObj> filesFirmaAbogado(@RequestParam("fechai") String fechai,
			@RequestParam("fechaf") String fechaf,@RequestParam("firmaAbogado") String firmaAbogado,@RequestParam("idAbogado") int idAbogado) { 
		return oCase.obtenerSolsDeFirmaYFechas(fechai, fechaf, firmaAbogado,idAbogado,false);
	}
	
	@GetMapping("files-de-firma-abogado-excel")
	public byte[] filesFirmaAbogadoExcel(@RequestParam("fechai") String fechai,
			@RequestParam("fechaf") String fechaf,@RequestParam("firmaAbogado") String firmaAbogado,@RequestParam("idAbogado") int idAbogado) { 
		List<FilesFirmaAbogadoObj> l = oCase.obtenerSolsDeFirmaYFechas(fechai, fechaf, firmaAbogado,idAbogado,true);
		byte[] s = null;
		try {
			s = ExcelFilesFirmas.generarExcelFirmas(l,firmaAbogado);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	@GetMapping("files-de-firma-abogados-excel")
	public ResponseEntity<byte[]> filesFirmaAbogadosExcel(@RequestParam("fechai") String fechai,
			@RequestParam("fechaf") String fechaf) { 
		
		List<NumFilesAbogados> la = oCase.obtenerFirmasAbogadosyNumFiles(fechai, fechaf);
		List<FilesFirmaAbogadoObj> l = oCase.obtenerSolsDeFirmaYFechas(fechai, fechaf, "",1,true);
		List<FilesFirmaAbogadoObj> l2 = oCase.obtenerSolsDeFirmaYFechas(fechai, fechaf, "",0,true);
		List<FilesFirmaAbogadoObj> l3 = oCase.obtenerSolsDeFirmaYFechas(fechai, fechaf, "all",0,true);

		
		 byte[] contenidoExcel;
		    try {
		        contenidoExcel = ExcelFilesFirmasA.generarExcelFirmas(la,l,l2,l3);
		    } catch (IOException e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		    }

		    String nombreArchivo = "firmas_files_" + fechai + "_a_" + fechaf + ".xlsx";

		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo);

		    return new ResponseEntity<>(contenidoExcel, headers, HttpStatus.OK);
		    
	}
	
	@GetMapping("/citas-usuario-entrevistador/{idUsuario}")
	public List<CitasDeUsuario> obtenerCitasUsuario(@PathVariable("idUsuario") int idUsuario,@RequestParam("fechai") Date fechai,
			@RequestParam("fechaf") Date fechaf) { 
		return evCase.citasDeUsuarioEntrevistador(idUsuario,fechai,fechaf);
	}
	
	@GetMapping("/w9/{idSolicitud}")
	public byte[] obtenerW9(@PathVariable("idSolicitud") int idSolicitud,@RequestParam("idUsuario") String idUsuario) { 
		System.out.println("idSolicitud:"+idSolicitud+"|idUsuario:"+idUsuario);
		byte [] salida = null;
		try {
			salida =  pdfPagos.generarPdf(idSolicitud);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return salida;
	}
	
	@GetMapping("reporte-cabecera-dashboard/{idUsuario}")
	public ReporteDash reporteCabeceraDashboard(@PathVariable("idUsuario") int idUsuario,@RequestParam("fechai") String fechai,
			@RequestParam("fechaf") String fechaf,@RequestParam("usuario") int usuario) {
		return oCase.reporteDash(fechai, fechaf,usuario);
	}
	
	@GetMapping("eventos-dashboard/{idUsuario}")
	public List<EventoSolicitud> eventosDashboard(@PathVariable("idUsuario") int idUsuario,@RequestParam("fechai") String fechai,
			@RequestParam("fechaf") String fechaf,@RequestParam("usuario") int usuario,@RequestParam("tileDash") int tileDash) {
		return evCase.obtenerEventosDash(fechai, fechaf, tileDash, idUsuario, usuario);
		
	}
	
	@GetMapping("detalle-solicitudes-dashboard/{idUsuario}")
	public List<Solicitud> obtenerDetalleSolicitudesDeUsuario(@PathVariable("idUsuario") int idUsuario,
			@RequestParam(required = false) String fechai,@RequestParam(required = false) String fechaf,@RequestParam(required = false) int tileDash,@RequestParam(required = false) int usuario) {	
		return oCase.obtenerSolicitudesDetalleReporteDash(tileDash, fechai, fechaf, usuario);
		
	}
	
	@GetMapping("detalle-solicitudes-all/{idUsuario}")
	public List<DetalleSolsPorFecha> reporteDetalleSolsFecha(@PathVariable("idUsuario") int idUsuario,
			@RequestParam(required = false) String fechai,@RequestParam(required = false) String fechaf,@RequestParam(required = false) int usuario) {	
		return oCase.reporteDetalleSolsFecha(fechai, fechaf, usuario);
		
	}

}
