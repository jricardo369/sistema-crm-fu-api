package com.cargosyabonos.adapter.rest;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.cargosyabonos.adapter.out.file.ExcelAbogados;
import com.cargosyabonos.application.port.in.AbogadoUseCase;
import com.cargosyabonos.domain.Abogado;
import com.cargosyabonos.domain.AbogadoEntity;

@RequestMapping("/abogados")
@RestController
public class AbogadoController {

	Logger log = LoggerFactory.getLogger(AbogadoController.class);

	@Autowired
	AbogadoUseCase aboUseCase;

	@GetMapping
	public List<AbogadoEntity> obtenerAbogados() {
		return aboUseCase.obtenerAbogados();
	}
	
	@GetMapping("/por-nombre/{nombre}")
	public List<AbogadoEntity> obtenerAbogadoPorNombre(@PathVariable("nombre") String nombre) {
		return aboUseCase.obtenerAbogadosPorNombreYSinonimo(nombre);
	}
	
	@GetMapping("/mails-por-nombre/{nombre}")
	public List<Abogado> obtenerAbogadosMailsPorNombre(@PathVariable("nombre") String nombre) {
		return aboUseCase.obtenerAbogadosConMail(nombre);
	}
	
	@GetMapping("{idAbogado}")
	public AbogadoEntity obtenerAbogadoPorId(@PathVariable("idAbogado") int idAbogado) {
		return aboUseCase.obtenerAbogadoPorId(idAbogado);
	}

	@PostMapping
	public void crearAbogado(@RequestBody AbogadoEntity a,@RequestParam("idUsuario") int idUsuario,@RequestParam("emailsAbogado") String emailsAbogado) {
		UtilidadesAdapter.pintarLog("emailsAbogado:"+emailsAbogado);
		aboUseCase.crearAbogado(a,emailsAbogado);
	}
	
	@PutMapping
	public void actualizarAbogado(@RequestBody AbogadoEntity a) {
		aboUseCase.actualizarAbogado(a);
	}
	
	@DeleteMapping(("{idAbogado}"))
	public void eliminarAbogado(@PathVariable("idAbogado") int idAbogado) {
		aboUseCase.eliminarAbogado(idAbogado);
	}
	

	@GetMapping("excel")
	public ResponseEntity<byte[]> filesFirmaAbogadosExcel() { 
		
		List<AbogadoEntity> abos = aboUseCase.obtenerAbogados();
		 byte[] contenidoExcel;
		    try {
		        contenidoExcel = ExcelAbogados.generarExcelFirmas(abos);
		    } catch (IOException e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		    }

		    String nombreArchivo = "abogados.xlsx";

		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo);

		    return new ResponseEntity<>(contenidoExcel, headers, HttpStatus.OK);
		    
	}

}
