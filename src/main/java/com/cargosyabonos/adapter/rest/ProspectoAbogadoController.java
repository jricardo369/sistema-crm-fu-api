package com.cargosyabonos.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.application.port.in.ProspectoAbogadoUseCase;
import com.cargosyabonos.domain.ProspectoAbogado;
import com.cargosyabonos.domain.ProspectoAbogadoEntity;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.OPTIONS})
@RequestMapping("/prospecto-abogado")
@RestController
public class ProspectoAbogadoController {

     @Autowired
	private ProspectoAbogadoUseCase prosAboService;

	@GetMapping()
	public List<ProspectoAbogado> obtenerEstadosUsuario(@RequestParam(required = false) String id,
	@RequestParam(required = false) String fechai,@RequestParam(required = false) String fechaf,
	@RequestParam(required = false) int idUsuario,@RequestParam(required = false) String estatus,
	@RequestParam(required = false) String estado,@RequestParam(required = false) String telefono,
	@RequestParam(required = false) String lawFirmOrContactName,@RequestParam(required = false) String email,
	@RequestParam(required = false) String mailPackageReceived,@RequestParam(required = false) String emailSent,
	@RequestParam(required = false) String porcentaje20,@RequestParam(required = false) String followUp20Porcent,
	@RequestParam(required = false) String prospectSentClient
) { 
		return prosAboService.obtenerProspectosAbogado(id,fechai, fechaf, idUsuario, estatus, estado, telefono, lawFirmOrContactName, email, mailPackageReceived, emailSent, porcentaje20, followUp20Porcent, prospectSentClient);
	}

    @GetMapping("{idProspectoAbogado}")
	public ProspectoAbogado obtenerProspectoAbogadoSoloUno(@PathVariable("idProspectoAbogado") int idProspectoAbogado) { 
		return prosAboService.obtenerProspectoAbogado(idProspectoAbogado);
	}

	@PostMapping()
	public ProspectoAbogado crearSolicitud(@RequestBody ProspectoAbogado o, @RequestParam("idUsuario") int idUsuario,@RequestParam("emailsAbogado") String emailsAbogado) {
		return prosAboService.crearProspectoAbogado(o,emailsAbogado);
	}

	@PutMapping()
	public void actualizarSolicitud(@RequestBody ProspectoAbogado o,@RequestParam( required = false) int idUsuario,@RequestParam( required = false) String motivo) {
		prosAboService.actualizarProspectoAbogado(o,idUsuario,motivo);
	}

	@GetMapping("/mails-por-nombre/{nombre}")
	public List<ProspectoAbogado> obtenerProspectosAbogadosConMail(@PathVariable("nombre") String nombre) {
		return prosAboService.obtenerProspectosAbogadosConMail(nombre);
	}

	@PostMapping("/excel/carga")
	public String cargarExcel(@RequestParam(value = "archivo", required = false) MultipartFile archivo) {
		return prosAboService.cargarExcel(archivo);
	}

	@PutMapping("actualizar-crm-file/{idProspectoAbogado}")
	public void actualizarSolicitud(@PathVariable("idProspectoAbogado") int idProspectoAbogado,@RequestParam( required = false) int crmFile,@RequestParam( required = false) String fecha ) {
		prosAboService.actualizarSolicitud(idProspectoAbogado, crmFile, fecha);
	} 
	

}
