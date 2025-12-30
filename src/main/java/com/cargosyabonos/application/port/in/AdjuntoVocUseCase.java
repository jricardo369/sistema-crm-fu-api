package com.cargosyabonos.application.port.in;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.cargosyabonos.domain.ArchivoVocEntity;

public interface AdjuntoVocUseCase {
	
	public List<ArchivoVocEntity> obtenerAdjuntos(); 
	public List<ArchivoVocEntity> buscarPorIdSolicitud(int idSolicitud);
	public void crearAdjunto(int idSolicitud, MultipartFile archivo,int idUsuario);
	public void actualizarAdjunto(ArchivoVocEntity a);
	public void eliminarAdjunto(int idAdjunto,int idUsuario);

}
