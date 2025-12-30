package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.EmailAbogadoPort;
import com.cargosyabonos.application.port.out.jpa.EmailAbogadoJpa;
import com.cargosyabonos.domain.EmailAbogadoEntity;

@Service
public class EmailAbogadoRepository implements EmailAbogadoPort {

	@Autowired
	EmailAbogadoJpa emailAboJpa;

	@Override
	public List<EmailAbogadoEntity> obtenerEmailsDeAbogado(int idAbogado) {
		return emailAboJpa.obtenerEmailsAbogado(idAbogado);
	}

	@Override
	public EmailAbogadoEntity obtenerEmailAbogado(String emailAbogado) {
		return emailAboJpa.obtenerEmailAbogado(emailAbogado);
	}

	@Override
	public void crearEmailAbogado(EmailAbogadoEntity ab) {
		emailAboJpa.save(ab);
	}

	@Override
	public void actualizarEmailAbogado(EmailAbogadoEntity ab) {
		emailAboJpa.save(ab);
	}

	@Override
	public void eliminarEmailAbogado(EmailAbogadoEntity ab) {
		emailAboJpa.delete(ab);
	}

	@Override
	public EmailAbogadoEntity obtenerPorIdAbogado(int idEmailAbogado) {
		return emailAboJpa.findByIdEmailAbo(idEmailAbogado);
	}

	@Override
	public void eliminarEmailAbogadoByIdAbogado(int idAbogado) {
		 emailAboJpa.eliminarEmailByIdAbogado(idAbogado);
	}

}
