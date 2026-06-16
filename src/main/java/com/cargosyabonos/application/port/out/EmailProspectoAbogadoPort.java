package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.EmailProspectoAbogadoEntity;

public interface EmailProspectoAbogadoPort {
	public List<EmailProspectoAbogadoEntity> obtenerEmailsDeProspectoAbogado(int idProspectoAbogado); 
	public EmailProspectoAbogadoEntity obtenerEmailProspectoAbogado(String emailProspectoAbogado); 
	public EmailProspectoAbogadoEntity obtenerPorIdProspectoAbogado(int idEmailProspectoAbogado); 
	public void crearEmailProspectoAbogado(EmailProspectoAbogadoEntity ab);
	public void actualizarEmailProspectoAbogado(EmailProspectoAbogadoEntity ab);
	public void eliminarEmailProspectoAbogado(EmailProspectoAbogadoEntity ab);
	public void eliminarEmailProspectoAbogadoByIdProspectoAbogado(int idProspectoAbogado);
}