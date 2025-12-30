package com.cargosyabonos.application.port.in;

import java.sql.Date;
import java.util.List;

import com.cargosyabonos.domain.TareaProgramadaEntity;

public interface TareaProgramadaUseCase {
	
	public List<TareaProgramadaEntity> obtenerTareasProgramadas();
	public TareaProgramadaEntity obtenerPorCodigo(String codigo);
	public void actualizarTareaProgramada(TareaProgramadaEntity tp);
	public void ejecutarTareaProgramada(String codigoTarea,Date fechai,Date fechaf,int tipoPayment);
	public void limpiezaDisponibilidad();
	public void validacionMsms();
	public void solicitudesVocEndingSessions();

}
