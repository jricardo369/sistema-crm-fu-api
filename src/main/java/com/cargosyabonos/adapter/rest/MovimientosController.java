package com.cargosyabonos.adapter.rest;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.adapter.out.file.PdfPagos;
import com.cargosyabonos.application.port.in.MovimientosUseCase;
import com.cargosyabonos.domain.Adeudo;
import com.cargosyabonos.domain.DetalleMovimientos;
import com.cargosyabonos.domain.Movimiento;
import com.cargosyabonos.domain.MovimientosReporte;
import com.itextpdf.text.DocumentException;

@RequestMapping("/movimientos")
@RestController
public class MovimientosController {

	Logger log = LoggerFactory.getLogger(MovimientosController.class);

	@Autowired
	MovimientosUseCase movUseCase;
	
	@Autowired
	PdfPagos pdfPagos;


	@GetMapping("reporte")
	public MovimientosReporte obtenerMovimientosDeClientePorFecha(@RequestParam("fechai") Date fechai,
			@RequestParam("fechaf") Date fechaf,@RequestParam(required = false) String cliente) {
	
		if(cliente == null){
			cliente = "";
		}
		if(cliente.equals("null")){
			cliente = "";
		}
		
		
		return movUseCase.obtenerMovimientosDeClientePorFecha(cliente, fechai, fechaf);
	}
	
	@GetMapping("reporte-pagos")
	public MovimientosReporte obtenerAdeudosDeClientePorFecha(@RequestParam("fechai") String fechai,
			@RequestParam("fechaf") String fechaf,@RequestParam(required = false) String idUsuario,@RequestParam(required = false) String campo,@RequestParam(required = false) String valor, @RequestParam(required = false) String tipo) {
	
		int idUs = 0;
		if (idUsuario != null) {
			if (!"".equals(idUsuario)) {
				idUs = Integer.valueOf(idUsuario);
			}
		}
		
		UtilidadesAdapter.pintarLog("fechai:"+fechai+"|fechaf:"+fechaf+"|idUsuario:"+idUs+"|campo:"+campo+"|valor:"+valor+"|tipo:"+tipo);
		return movUseCase.obtenerAdeudosDeClientePorFecha(fechai, fechaf, idUs, campo, valor,tipo);
	}

	@GetMapping("{idCliente}/adeudo")
	public Adeudo obtenerAdeudoCliente(@PathVariable("idCliente") int idCliente) {
		return movUseCase.obtenerAdeudo(idCliente);
	}
	
	@GetMapping("{idSolicitud}")
	public List<Movimiento> obtenerMovimiento(@PathVariable("idSolicitud") int idSolicitud) {
		return movUseCase.obtenerMovimientos(idSolicitud);
	}
	
	@GetMapping("detalle/{idSolicitud}")
	public DetalleMovimientos obtenerDetalleMovimiento(@PathVariable("idSolicitud") int idSolicitud) {
		return movUseCase.obtenerDetalleMovimientos(idSolicitud);
	}

	@PostMapping
	public void crearMovimiento(@RequestBody Movimiento mov,@RequestParam("envioNotificacion") boolean envioNotificacion,@RequestParam("idUsuario") int idUsuario) {
		movUseCase.crearMovimiento(mov,envioNotificacion,idUsuario); 
	}

	@PutMapping
	public void actualizarMovimiento(@RequestBody Movimiento mov) {
		movUseCase.actualizarMovimiento(mov);
	}

	@DeleteMapping("{idMovimiento}")
	public void eliminarMovimiento(@PathVariable("idMovimiento") int idMovimiento,@RequestParam("idUsuario") int idUsuario) {
		//UtilidadesAdapter.pintarLog("idMov:"+idMovimiento+"|idUsuario:"+idUsuario);
		movUseCase.eliminarMovimiento(idMovimiento,idUsuario);
	}
	
	@GetMapping("movs-pdf-sol/{idSolicitud}")
	public byte[] obtenerReportePDFMovimientosSols(@PathVariable("idSolicitud") int idSolicitud) {
		byte[] s = null;
		try {
			s =  pdfPagos.generarPdf(idSolicitud);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return s;
	}

}
