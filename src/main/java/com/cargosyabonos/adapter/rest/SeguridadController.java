package com.cargosyabonos.adapter.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.application.port.in.SeguridadUseCase;
import com.cargosyabonos.domain.CambiosUsuario;

@RequestMapping("/seguridad")
@RestController
public class SeguridadController {

	Logger log = LoggerFactory.getLogger(SeguridadController.class);

	@Autowired
	private SeguridadUseCase segUse;

	@PutMapping("cambio-password")
	public void cambioPassword(@RequestBody CambiosUsuario cambiosUsuario) {
		segUse.cambioPassword(cambiosUsuario);
	}
	
	@PutMapping("cambio-email")
	public void cambioEmail(@RequestBody CambiosUsuario cambiosUsuario) {
		segUse.cambioEmail(cambiosUsuario);
	}
	
	@GetMapping("reset-password-request/{email}")
	public void resetPasswordRequest(@PathVariable("email") String email) {
		segUse.enviarCorreoResetPassword(email);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("reset-password/{uuid}/{usuario}")
	public ResponseEntity resetPassword(@PathVariable("uuid") String uuid,@PathVariable("usuario") int usuario) {
		return ResponseEntity.ok(segUse.obtenerFormReset(uuid,usuario));
		
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("reset-password-request/{uuid}/{usuario}")
	public ResponseEntity resetPassword(@PathVariable("uuid") String uuid,@RequestPart String password,
			@RequestPart String passwordRepeat,@PathVariable("usuario") int usuario) {
		return ResponseEntity.ok(segUse.enviarForRest(uuid, password, passwordRepeat,usuario));
	}

}
