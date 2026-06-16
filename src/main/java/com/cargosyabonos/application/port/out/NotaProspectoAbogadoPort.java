package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.NotaProspectoAbogado;
import com.cargosyabonos.domain.NotaProspectoAbogadoEntity;

public interface NotaProspectoAbogadoPort {

    public NotaProspectoAbogadoEntity findByIdNota(int idNota);
	public List<NotaProspectoAbogado> obtenerNotasProspectosAbogadosPorIdProspecto(int idProspectoAbogado);
	public NotaProspectoAbogadoEntity crearNotaProspecto(NotaProspectoAbogadoEntity o);
	public void eliminarNotaProspecto(NotaProspectoAbogadoEntity o);
	public void actualizarNotaProspecto(NotaProspectoAbogadoEntity o);

}
