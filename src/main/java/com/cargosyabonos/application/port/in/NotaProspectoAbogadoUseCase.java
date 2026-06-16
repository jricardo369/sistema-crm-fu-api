package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.NotaProspectoAbogado;
import com.cargosyabonos.domain.NotaProspectoAbogadoEntity;

public interface NotaProspectoAbogadoUseCase {

     public NotaProspectoAbogadoEntity findByIdNota(int idNota);
	public List<NotaProspectoAbogado> obtenerNotasProspectosAbogadosPorIdProspecto(int idProspectoAbogado);
	public NotaProspectoAbogadoEntity crearNotaProspecto(NotaProspectoAbogadoEntity o);
	public void eliminarNotaProspecto(int idNota);
	public void actualizarNotaProspecto(NotaProspectoAbogadoEntity o);

}
