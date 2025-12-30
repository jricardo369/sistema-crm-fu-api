package com.cargosyabonos.application.port.in;

import com.cargosyabonos.domain.Notificacion;

public interface NotificacionUseCase {
	
	public void enviarNotificacion(Notificacion n,byte[] archivo,String extension);
	public void enviarNotificacionAbogados(Notificacion n,byte[] archivo,String extension);

}