package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.NotaCitaPort;
import com.cargosyabonos.application.port.out.jpa.NotaCitaJpa;
import com.cargosyabonos.domain.NotaCitaEntity;

@Service
public class NotaCitaRepository implements NotaCitaPort{

	@Autowired
	NotaCitaJpa ncJpa;

	@Override
	public List<NotaCitaEntity> obtenerNotasCitas(int idCita) {
		return ncJpa.obtenerNotasCitas(idCita);
	}

	@Override
	public NotaCitaEntity obtenerNotaCita(int idNota) {
		return ncJpa.obtenerPorId(idNota);
	}

	@Override
	public void crearNotaCita(NotaCitaEntity a) {
		ncJpa.save(a);
	}

	@Override
	public void actualizarNotaCita(NotaCitaEntity a) {
		ncJpa.save(a);
	}

	@Override
	public void eliminarNotaCita(NotaCitaEntity a) {
		ncJpa.delete(a);
	}

	@Override
	public NotaCitaEntity obtenerNotaDeCita(int idCita) {
		return ncJpa.obtenerNotaDeCita(idCita);
	}

	@Override
	public int obtenerIdSolByIdNota(int idNota) {
		return ncJpa.obtenerIdSolByIdNota(idNota);
	}

}
