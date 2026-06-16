package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.EmailProspectoAbogadoPort;
import com.cargosyabonos.application.port.out.jpa.EmailProspectoAbogadoJpa;
import com.cargosyabonos.domain.EmailProspectoAbogadoEntity;

@Service
public class EmailProspectoAbogadoRepository implements EmailProspectoAbogadoPort {

	@Autowired
	EmailProspectoAbogadoJpa emailProsAboJpa;

	@Override
	public List<EmailProspectoAbogadoEntity> obtenerEmailsDeProspectoAbogado(int idProspectoAbogado) {
		return emailProsAboJpa.obtenerEmailsProspectoAbogado(idProspectoAbogado);
	}

	@Override
	public EmailProspectoAbogadoEntity obtenerEmailProspectoAbogado(String emailProspectoAbogado) {
		return emailProsAboJpa.obtenerEmailProspectoAbogado(emailProspectoAbogado);
	}

	@Override
	public void crearEmailProspectoAbogado(EmailProspectoAbogadoEntity ab) {
		emailProsAboJpa.save(ab);
	}

	@Override
	public void actualizarEmailProspectoAbogado(EmailProspectoAbogadoEntity ab) {
		emailProsAboJpa.save(ab);
	}

	@Override
	public void eliminarEmailProspectoAbogado(EmailProspectoAbogadoEntity ab) {
		emailProsAboJpa.delete(ab);
	}

	@Override
	public EmailProspectoAbogadoEntity obtenerPorIdProspectoAbogado(int idEmailProspectoAbogado) {
		return emailProsAboJpa.findByIdEmailProsAbo(idEmailProspectoAbogado);
	}

	@Override
	public void eliminarEmailProspectoAbogadoByIdProspectoAbogado(int idProspectoAbogado) {
		emailProsAboJpa.eliminarEmailByIdProspectoAbogado(idProspectoAbogado);
	}

}