package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.cargosyabonos.application.port.out.jpa.ConfiguracionJpa;
import com.cargosyabonos.domain.ConfiguracionEntity;

@Service
public class ConfiguracionRepository implements ConfiguracionPort {

	@Autowired
	ConfiguracionJpa confJpa;

	@Override
	public void crearConf(ConfiguracionEntity conf) {
		confJpa.save(conf);
	}

	@Override
	public void actualizarConf(ConfiguracionEntity conf) {
		confJpa.save(conf);
	}

	@Override
	public void eliminarConf(ConfiguracionEntity conf) {
		confJpa.delete(conf);
	}

	@Override
	public ConfiguracionEntity obtenerConfiguracionPorCodigo(String codigo) {
		return confJpa.obtenerConfiguracionPorCodigo(codigo);
	}

	@Override
	public List<ConfiguracionEntity> obtenerConfiguraciones() {
		return confJpa.findAll();
	}

	
	
}
