package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.Abogado;
import com.cargosyabonos.domain.AbogadoEntity;
import com.cargosyabonos.domain.FuenteDeReferenciaEntity;

public interface AbogadoUseCase {
	
	public List<AbogadoEntity> obtenerAbogados(); 
	public AbogadoEntity obtenerAbogadoPorId(int idAbogado);
	public List<AbogadoEntity> obtenerAbogadosPorNombreYSinonimo(String nombre);
	public void crearAbogado(Abogado a,int idUsuario,int idProspectoAbogado,int idSolicitud);
	public void actualizarAbogado(AbogadoEntity a);
	public void eliminarAbogado(int idAbogado);
	public List<Abogado> obtenerAbogadosConMail(String valorBusqueda);
	public List<FuenteDeReferenciaEntity> obtenerFuentesDeReferencia();

}
