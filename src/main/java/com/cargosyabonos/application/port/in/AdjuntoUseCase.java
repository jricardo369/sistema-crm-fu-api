package com.cargosyabonos.application.port.in;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.cargosyabonos.domain.ArchivoEntity;

public interface AdjuntoUseCase {
	
	public List<ArchivoEntity> obtenerAdjuntos(); 
	public List<ArchivoEntity> buscarPorIdSolicitud(int idSolicitud);
	public void crearAdjunto(int idSolicitud, MultipartFile archivo,int idUsuario);
	public void actualizarAdjunto(ArchivoEntity a);
	public void eliminarAdjunto(int idAdjunto,int idUsuario);

}
