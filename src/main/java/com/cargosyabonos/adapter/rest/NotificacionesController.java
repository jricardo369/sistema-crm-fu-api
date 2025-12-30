package com.cargosyabonos.adapter.rest; 

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.application.port.in.NotificacionUseCase;
import com.cargosyabonos.domain.Notificacion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@RequestMapping("/notificaciones")
@RestController
public class NotificacionesController {
	
	Logger log = LoggerFactory.getLogger(NotificacionesController.class);
	
	@Autowired
	NotificacionUseCase notifUserCase;
	
	@PostMapping
	public void crearNotificacion(@RequestParam(value = "archivo", required = false) MultipartFile archivo,@RequestParam(value = "notificacion") String notificacion) {
		System.out.println("archivo:"+archivo);
		ObjectMapper om = new ObjectMapper();
		String tipoArchivo = "";
		byte[] archivoBytes = null;
		try {
			if(archivo != null){
				if(archivo.getContentType().contains("word")){
					tipoArchivo = "docx";
				}else{
					tipoArchivo = archivo.getContentType().substring(archivo.getContentType().length() - 3,
							archivo.getContentType().length());
				}
			}
			Notificacion n = om.readValue(notificacion, Notificacion.class);
			//System.out.println("n:"+n.getFirmasAbogados());
			if(n.getFirmasAbogados() != null){
				System.out.println("Notificacion abogados");
				if(archivo != null){
					archivoBytes = archivo.getBytes();
				}
				notifUserCase.enviarNotificacionAbogados(n,archivoBytes,tipoArchivo);
			}else{
				if(archivo != null){
					archivoBytes = archivo.getBytes();
				}
				notifUserCase.enviarNotificacion(n,archivoBytes,tipoArchivo);
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
