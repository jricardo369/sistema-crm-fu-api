package com.cargosyabonos.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cargosyabonos.application.port.in.EmailAbogadoUseCase;
import com.cargosyabonos.application.port.out.EmailAbogadoPort;
import com.cargosyabonos.domain.EmailAbogadoEntity;

@Service
public class EmailAbogadoService implements EmailAbogadoUseCase {

	Logger log = LoggerFactory.getLogger(EmailAbogadoService.class);
	
	@Autowired
	private EmailAbogadoPort emailAboPort;

	@Override
	public List<EmailAbogadoEntity> obtenerEmailsDeAbogado(int idAbogado) {
		return emailAboPort.obtenerEmailsDeAbogado(idAbogado);
	}

	@Override
	public EmailAbogadoEntity obtenerEmailAbogado(String emailAbogado) {
		return emailAboPort.obtenerEmailAbogado(emailAbogado);
	}

	@Override
	public void crearEmailAbogado(EmailAbogadoEntity ab) {

		EmailAbogadoEntity eae = emailAboPort.obtenerEmailAbogado(ab.getEmail());

		if (eae != null) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "This email is already registered");
		}

		emailAboPort.crearEmailAbogado(ab);
	}

	@Override
	public void actualizarEmailAbogado(EmailAbogadoEntity ab) {
		emailAboPort.actualizarEmailAbogado(ab);
	}

	@Override
	public void eliminarEmailAbogado(int idEmailAbogado) {
		EmailAbogadoEntity ab = emailAboPort.obtenerPorIdAbogado(idEmailAbogado);
		emailAboPort.eliminarEmailAbogado(ab);
	}

	
	
}
