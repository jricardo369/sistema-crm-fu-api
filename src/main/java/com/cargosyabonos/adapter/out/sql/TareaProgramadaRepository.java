package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.TareaProgramadaPort;
import com.cargosyabonos.application.port.out.jpa.TareaProgramadaJpa;
import com.cargosyabonos.domain.TareaProgramadaEntity;

@Service
public class TareaProgramadaRepository implements TareaProgramadaPort {

	@Autowired
	TareaProgramadaJpa tJpa;

	@Override
	public List<TareaProgramadaEntity> obtenerTareasProgramadas() {
		return tJpa.findAll();
	}

	@Override
	public TareaProgramadaEntity obtenerPorCodigo(String codigo) {
		return tJpa.findByCodigo(codigo);
	}

	@Override
	public void actualizarTareaProgramada(TareaProgramadaEntity tp) {
		tJpa.save(tp);
	}

	@Override
	public void eliminarDisponibilidadesAnteriores() {
		tJpa.eliminarDisponibilidadesAnteriores();
	}
	
	
}
