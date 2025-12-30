package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.Abogado;
import com.cargosyabonos.domain.AbogadoEntity;

public interface AbogadoPort {
	
	public List<AbogadoEntity> obtenerAbogados(); 
	public AbogadoEntity obtenerAbogadoPorId(int idAbogado);
	public AbogadoEntity obtenerAbogadoPorEmail(String email);
	public List<AbogadoEntity> obtenerAbogadosPorNombreYSinonimo(String nombre);
	public AbogadoEntity crearAbogado(AbogadoEntity a);
	public void actualizarAbogado(AbogadoEntity a);
	public void eliminarAbogado(AbogadoEntity a);
	public List<Abogado> obtenerAbogadosConMail(String valorBusqueda);
	public void actualizarCuponAbogado(int idAbogado);
	
}
