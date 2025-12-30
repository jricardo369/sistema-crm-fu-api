package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.ConfiguracionEntity;

public interface ConfiguracionPort {
	
	public void crearConf(ConfiguracionEntity conf);
	public void actualizarConf(ConfiguracionEntity conf);
	public void eliminarConf(ConfiguracionEntity conf);
	public ConfiguracionEntity obtenerConfiguracionPorCodigo(String codigo);
	public List<ConfiguracionEntity> obtenerConfiguraciones();

}
