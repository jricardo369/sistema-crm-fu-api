package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.ConfiguracionEntity;

public interface ConfiguracionUseCase {
	
	public void crearConf(ConfiguracionEntity conf);
	public void actualizarConf(ConfiguracionEntity conf);
	public void eliminarConf(ConfiguracionEntity conf);
	public ConfiguracionEntity obtenerConfiguracionPorCodigo(String codigo);
	public List<ConfiguracionEntity> obtenerConfiguraciones();

}
