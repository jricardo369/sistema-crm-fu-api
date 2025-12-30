package com.cargosyabonos.adapter.rest;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import com.cargosyabonos.adapter.out.file.ExcelVOCCharges;
import com.cargosyabonos.adapter.out.file.PdfCita;
import com.cargosyabonos.application.port.in.CitaUseCase;
import com.cargosyabonos.domain.CargosCitasVoc;
import com.cargosyabonos.domain.Cita;
import com.cargosyabonos.domain.CitaEntity;
import com.itextpdf.text.DocumentException;

@RequestMapping("/citas")
@RestController
public class CitaController {

	Logger log = LoggerFactory.getLogger(CitaController.class);

	@Autowired
	CitaUseCase citaUseCase;
	
	@Autowired
	PdfCita pdfCita;
	
	@GetMapping("/semana")
	public List<Cita> obtenerTodasLasCitasSemana(@RequestParam("fecha") String fecha) {
		return citaUseCase.obtenerTodasLasCitasSemana(fecha);
	}
	
	@GetMapping("/dia")
	public List<Cita> obtenerTodasLasCitasDia(@RequestParam("fecha") String fecha) {
		return citaUseCase.obtenerTodasCitasPorFecha(fecha);
	}
	
	@GetMapping("/citas-de-solicitud/{idSolicitud}")
	public List<Cita> obtenerCitasDeSolicitud(@PathVariable("idSolicitud") int idSolicitud) {
		return citaUseCase.obtenerCitasPorSolicitud(idSolicitud);
	}
	
	@GetMapping("/citas-de-usuario/{idUsuario}")
	public List<Cita> obtenerCitasDeUsuarioPorFecha(@PathVariable("idUsuario") String idUsuario,@RequestParam("fecha") String fecha) {
		int idUs = 0;
		if (idUsuario != null) {
			if (!"".equals(idUsuario)) {
				idUs = Integer.valueOf(idUsuario);
			}
		}
		UtilidadesAdapter.pintarLog("idUsuario:"+idUsuario+"|fecha:"+fecha);
		return citaUseCase.obtenerCitasDeUsuarioPorFecha(idUs, fecha);
	}
	
	@GetMapping("/citas-de-usuario-semana/{idUsuario}")
	public List<Cita> obtenerCitasDeUsuarioPorSemana(@PathVariable("idUsuario") String idUsuario,@RequestParam("fecha") String fecha,@RequestParam(required = false) String filtro,
			@RequestParam(required = false) boolean disponibilidad,@RequestParam(required = false) String idRol,@RequestParam(required = false) String estatusCita) {
		
		int idUs = 0;
		if (idUsuario != null) {
			if (!"".equals(idUsuario)) {
				idUs = Integer.valueOf(idUsuario);
			}
		}
		if(filtro == null){
			filtro = "";
		}
		UtilidadesAdapter.pintarLog("idUsuario:"+idUsuario+"|fecha:"+fecha+"|filtro:"+filtro+"|disponibilidad:"+disponibilidad+"|idRol:"+idRol+"|estatusCita:"+estatusCita);
		return citaUseCase.obtenerCitasDeUsuarioPorSemana(idUs, fecha,filtro,disponibilidad,idRol,estatusCita);
	}
	
	@PutMapping("/no-show/{idCita}")
	public void noShowCita(@PathVariable("idCita") int idCita,@RequestParam("idUsuario") int idUsuario,@RequestParam("motivo") String motivo) {
		UtilidadesAdapter.pintarLog("idCita:"+idCita+"|idUsuario:"+idUsuario+"|motivo:"+motivo);
		citaUseCase.actualizarNoShow(idCita,idUsuario,motivo);
	}
	
	@GetMapping("/reporte/{idCita}")
	public byte[] reporteCita(@PathVariable("idCita") int idCita,@RequestParam("idUsuario") int idUsuario) {
		UtilidadesAdapter.pintarLog("idCita:"+idCita+"idUsuario:"+idUsuario);
		byte[] salida = null;
		try {
			salida = pdfCita.generarPdf(idCita,idUsuario);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return salida;
	}
	
	@GetMapping("/cargos-pendientes")
	public List<CargosCitasVoc> cargosPendientes(@RequestParam("idUsuario") int idUsuario,@RequestParam("fechai") String fechai,@RequestParam("fechaf") String fechaf,
			@RequestParam(required = false) String valor,@RequestParam(required = false) String campo,@RequestParam(required = false) String tipo) {
		UtilidadesAdapter.pintarLog("idUsuario:"+idUsuario+"/fechai:"+fechai+"/fechaf:"+fechaf+"/campo:"+campo+"/valor:"+valor+"/tipo:"+tipo);
		return citaUseCase.obtenerCargosPendientes( fechai,  fechaf,  idUsuario,  campo,  valor,
				 tipo);
	}
	
	@GetMapping("/cargos-pendientes-excel")
	public byte[] cargosPendientesExcel(@RequestParam("idUsuario") int idUsuario,@RequestParam("fechai") String fechai,@RequestParam("fechaf") String fechaf,
			@RequestParam(required = false) String valor,@RequestParam(required = false) String campo,@RequestParam(required = false) String tipo) {
		
		UtilidadesAdapter.pintarLog("idUsuario:"+idUsuario+"/fechai:"+fechai+"/fechaf:"+fechaf+"/campo:"+campo+"/valor:"+valor+"/tipo:"+tipo);
		List<CargosCitasVoc> l = citaUseCase.obtenerCargosPendientes( fechai,  fechaf,  idUsuario,  campo,  valor,
				 tipo);
		UtilidadesAdapter.pintarLog("size:"+l.size());
		byte[] s = null;
		try {
			s = ExcelVOCCharges.generarExcelVOCCharges(l);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	@PostMapping()
	public void crearCita(@RequestBody CitaEntity c) {
		citaUseCase.crearCita(c);
	}
	
	@PutMapping
	public void actualizarCita(@RequestBody CitaEntity c) {
		citaUseCase.actualizarCita(c);
	}
	
	@PutMapping("pagado/{idCita}/{pagado}")
	public void actualizarPagado(@PathVariable("idCita") int idCita,@PathVariable("pagado") boolean pagado,@RequestParam("idUsuario") int idUsuario) {
		citaUseCase.actualizarPagado(pagado, idCita,idUsuario);
	}
	
	@DeleteMapping("{idCita}")
	public void eliminarCita(@PathVariable("idCita") int idCita,@RequestParam("idUsuario") int idUsuario) {
		citaUseCase.eliminarCita(idCita,idUsuario);
	}

}
