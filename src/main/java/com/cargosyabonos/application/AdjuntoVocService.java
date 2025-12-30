package com.cargosyabonos.application;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.AdjuntoVocUseCase;
import com.cargosyabonos.application.port.out.AdjuntoVocPort;
import com.cargosyabonos.application.port.out.ArchivosPort;
import com.cargosyabonos.application.port.out.EventoSolicitudVocPort;
import com.cargosyabonos.application.port.out.SolicitudVocPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.ArchivoVocEntity;
import com.cargosyabonos.domain.SolicitudVocEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class AdjuntoVocService implements AdjuntoVocUseCase {

	@Value("${ruta.servidor}")
	private String rutaServidor;

	@Value("${ruta.servidor.qas}")
	private String rutaServidorQas;

	@Value("${ruta.servidor.pro}")
	private String rutaServidorPro;

	@Value("${ambiente}")
	private String ambiente;
	
	@Value("${rutaArchivos}")
	private String rutaArchivos;

	@Value("${rutaArchivos.qas}")
	private String rutaArchivosQAS;

	@Value("${rutaArchivos.pro}")
	private String rutaArchivosPRO;

	Logger log = LoggerFactory.getLogger(AdjuntoVocService.class);
	
	@Autowired
	private EventoSolicitudVocPort evPort;

	@Autowired
	private AdjuntoVocPort adjPort;

	@Autowired
	private ArchivosPort arcPort;
	
	@Autowired
	private SolicitudVocPort solPort;
	
	@Autowired
	private UsuariosPort usPort;

	@Override
	public List<ArchivoVocEntity> obtenerAdjuntos() {
		List<ArchivoVocEntity> al = adjPort.obtenerAdjuntos();
		return al;
	}

	@Override
	public List<ArchivoVocEntity> buscarPorIdSolicitud(int idSolicitud) {

		String rutaServidorFinal = "";
		
		switch (ambiente) {
		case "qas":
			rutaServidorFinal = rutaServidorQas;
			break;
		case "pro":
			rutaServidorFinal = rutaServidorPro;
			break;
		case "test":
			rutaServidorFinal = rutaServidor;
			break;
		}
		
		List<ArchivoVocEntity> al = adjPort.buscarPorIdSolicitud(idSolicitud);
		for (ArchivoVocEntity a : al) {
			a.setUrlImagen(rutaServidorFinal + a.getUrlImagen());
		}
		return al;
	}

	public void crearAdjunto(int idSolicitud, MultipartFile archivo,int idUsuario) {

		String nombreOriginal = archivo.getOriginalFilename();
		System.out.println("ContentType:"+archivo.getContentType());
		String tipoArchivo = "";
		
		if(archivo.getContentType().contains("word")){
			tipoArchivo = "docx";
		}else{
			tipoArchivo = archivo.getContentType().substring(archivo.getContentType().length() - 3,
					archivo.getContentType().length());
		}
		
		String rutaArchivos = UtilidadesAdapter.generarRutaArchivoVoc(idSolicitud);
		String nombre = "/" + UUID.randomUUID() + "." + tipoArchivo;
		
		byte[] archivoB = null;

		try {
			archivoB = archivo.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("rutaArchios:"+rutaArchivos);
		System.out.println("bytes:"+archivoB);
		System.out.println("nombre:"+nombre);
		System.out.println("original nombre:"+nombreOriginal);
		
		arcPort.guardarArchivo(rutaArchivos, archivoB, nombre);
		SolicitudVocEntity r = solPort.obtenerSolicitud(idSolicitud);
		ArchivoVocEntity a = new ArchivoVocEntity();
		a.setSolicitd(r);
		a.setUrlImagen(rutaArchivos + nombre);
		a.setNombre(nombreOriginal);
		adjPort.crearAdjunto(a);
		
		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		
		evPort.ingresarEventoDeSolicitud("File upload", "A file was uploaded "+ nombreOriginal, "Info", us.getNombre(), r);
		
	}

	@Override
	public void actualizarAdjunto(ArchivoVocEntity a) {
		adjPort.actualizarAdjunto(a);
	}

	@Override
	public void eliminarAdjunto(int idAdjunto,int idUsuario) {
		ArchivoVocEntity a = adjPort.buscarPorId(idAdjunto);
		adjPort.eliminarAdjunto(a);
		System.out.println("url img:"+a.getUrlImagen());
		
		System.out.println("rutaArchivosQAS:" + rutaArchivosQAS);
		System.out.println("rutaArchivosPRO:" + rutaArchivosPRO);
		System.out.println("rutaArchivos:" + rutaArchivos);
		String rutaArchivosFinal = "";

		switch (ambiente) {
		case "qas":
			System.out.println("Es QAS");
			rutaArchivosFinal = rutaArchivosQAS + a.getUrlImagen();
			break;
		case "pro":
			System.out.println("Es PRO");
			rutaArchivosFinal = rutaArchivosPRO + a.getUrlImagen();
			break;
		case "test":
			System.out.println("Es local");
			rutaArchivosFinal = rutaArchivos + a.getUrlImagen();
			break;
		default:
			System.out.println("No encontro match");
		}
		System.out.println("rutaArchivosFinal:" + rutaArchivosFinal);
		
		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		SolicitudVocEntity r = solPort.obtenerSolicitud(a.getSolicitd().getIdSolicitud());
		evPort.ingresarEventoDeSolicitud("File was deleted", "The file was deleted "+ a.getNombre(), "Info", us.getNombre(), r);
		
		//Eliminar de filesystem
		arcPort.eliminarArchivo(rutaArchivosFinal);
		
		
	}

}
