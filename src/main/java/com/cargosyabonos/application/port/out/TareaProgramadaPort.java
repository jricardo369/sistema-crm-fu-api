package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.TareaProgramadaEntity;

public interface TareaProgramadaPort {
	
	public List<TareaProgramadaEntity> obtenerTareasProgramadas();
	public TareaProgramadaEntity obtenerPorCodigo(String codigo);
	public void actualizarTareaProgramada(TareaProgramadaEntity tp);
	public void eliminarDisponibilidadesAnteriores();

}
