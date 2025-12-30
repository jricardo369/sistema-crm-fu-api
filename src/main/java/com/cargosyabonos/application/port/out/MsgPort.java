package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.ConfiguracionEntity;
import com.cargosyabonos.domain.SolicitudEntity;

public interface MsgPort {
	
	public boolean envioMensaje(String telefono,String mensaje,SolicitudEntity s,boolean segundaVez);
	public boolean envioMensajePrueba();
	public boolean refreshToken(List<ConfiguracionEntity> confs);

}
