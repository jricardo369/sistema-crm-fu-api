package com.cargosyabonos.application;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.AdjuntoUseCase;
import com.cargosyabonos.application.port.out.AdjuntoPort;
import com.cargosyabonos.application.port.out.ArchivosPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.ArchivoEntity;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class AdjuntoService implements AdjuntoUseCase {

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

	Logger log = LoggerFactory.getLogger(AdjuntoService.class);
	
	@Autowired
	private EventoSolicitudPort evPort;

	@Autowired
	private AdjuntoPort adjPort;

	@Autowired
	private ArchivosPort arcPort;
	
	@Autowired
	private SolicitudPort solPort;
	
	@Autowired
	private UsuariosPort usPort;

	@Override
	public List<ArchivoEntity> obtenerAdjuntos() {
		List<ArchivoEntity> al = adjPort.obtenerAdjuntos();
		return al;
	}

	@Override
	public List<ArchivoEntity> buscarPorIdSolicitud(int idSolicitud) {

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
		
		List<ArchivoEntity> al = adjPort.buscarPorIdSolicitud(idSolicitud);
		for (ArchivoEntity a : al) {
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
		
		String rutaArchivos = UtilidadesAdapter.generarRutaArchivo(idSolicitud);
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
		SolicitudEntity r = solPort.obtenerSolicitud(idSolicitud);
		ArchivoEntity a = new ArchivoEntity();
		a.setIdUsuarioCargo(idUsuario);
		a.setSolicitd(r);
		a.setUrlImagen(rutaArchivos + nombre);
		a.setNombre(nombreOriginal);
		if(a.getNombre().length() > 100){
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"The file name exceeds the limit of 95");	
		}
		adjPort.crearAdjunto(a);
		
		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		
		evPort.ingresarEventoDeSolicitud("File upload", "A file was uploaded "+ nombreOriginal, "Info", us.getNombre(), r);
		
	}

	@Override
	public void actualizarAdjunto(ArchivoEntity a) {
		adjPort.actualizarAdjunto(a);
	}

	@Override
	public void eliminarAdjunto(int idAdjunto,int idUsuario) {
		
		ArchivoEntity a = adjPort.buscarPorId(idAdjunto);
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
		SolicitudEntity r = solPort.obtenerSolicitud(a.getSolicitd().getIdSolicitud());
		evPort.ingresarEventoDeSolicitud("File was deleted", "The file was deleted "+ a.getNombre(), "Info", us.getNombre(), r);
		
		//Eliminar de filesystem
		arcPort.eliminarArchivo(rutaArchivosFinal);		
		
	}

}
