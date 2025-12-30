package com.cargosyabonos.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.in.ConfiguracionUseCase;
import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.cargosyabonos.domain.ConfiguracionEntity;

@Service
public class ConfiguracionesService implements ConfiguracionUseCase {

	Logger log = LoggerFactory.getLogger(ConfiguracionesService.class);

	@Autowired
	private ConfiguracionPort confPort;

	@Override
	public void crearConf(ConfiguracionEntity conf) {
		confPort.crearConf(conf);
	}

	@Override
	public void actualizarConf(ConfiguracionEntity conf) {
		if(conf.getCodigo().equals("TEL-ENVIO")){
			conf.setValor(conf.getValor().replaceAll("\\D+",""));
		}
		confPort.actualizarConf(conf);
	}

	@Override
	public void eliminarConf(ConfiguracionEntity conf) {
		confPort.eliminarConf(conf);
	}

	@Override
	public ConfiguracionEntity obtenerConfiguracionPorCodigo(String codigo) {
		return confPort.obtenerConfiguracionPorCodigo(codigo);
	}

	@Override
	public List<ConfiguracionEntity> obtenerConfiguraciones() {
		return confPort.obtenerConfiguraciones();
	}

	 

}
