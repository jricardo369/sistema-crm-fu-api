package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.EmailAbogadoEntity;

public interface EmailAbogadoUseCase {
	
	public List<EmailAbogadoEntity> obtenerEmailsDeAbogado(int idAbogado); 
	public EmailAbogadoEntity obtenerEmailAbogado(String emailAbogado); 
	public void crearEmailAbogado(EmailAbogadoEntity ab);
	public void actualizarEmailAbogado(EmailAbogadoEntity ab);
	public void eliminarEmailAbogado(int idEmailAbogado);

}