package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.EmailAbogadoEntity;

public interface EmailAbogadoPort {
	
	public List<EmailAbogadoEntity> obtenerEmailsDeAbogado(int idAbogado); 
	public EmailAbogadoEntity obtenerEmailAbogado(String emailAbogado); 
	public EmailAbogadoEntity obtenerPorIdAbogado(int idEmailAbogado); 
	public void crearEmailAbogado(EmailAbogadoEntity ab);
	public void actualizarEmailAbogado(EmailAbogadoEntity ab);
	public void eliminarEmailAbogado(EmailAbogadoEntity ab);
	public void eliminarEmailAbogadoByIdAbogado(int idAbogado);

}