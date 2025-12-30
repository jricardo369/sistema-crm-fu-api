package com.cargosyabonos.adapter.out.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cargosyabonos.application.port.out.ArchivosPort;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class FileSystemAdapter implements ArchivosPort {

	@Value("${rutaArchivos}")
	private String rutaArchivos;

	@Value("${rutaArchivos.qas}")
	private String rutaArchivosQAS;

	@Value("${rutaArchivos.pro}")
	private String rutaArchivosPRO;

	@Value("${ambiente}")
	private String ambiente;

	Logger log = LoggerFactory.getLogger(FileSystemAdapter.class);

	@Override
	public boolean guardarArchivo(String rutaArchivo, byte[] bytes, String nombre) {

		System.out.println("///////////////////ambiente:-" + ambiente + "-");
		System.out.println("rutaArchivosQAS:" + rutaArchivosQAS);
		System.out.println("rutaArchivosPRO:" + rutaArchivosPRO);
		System.out.println("rutaArchivos:" + rutaArchivos);
		String rutaArchivosFinal = "";

		switch (ambiente) {
		case "qas":
			System.out.println("Es QAS");
			rutaArchivosFinal = rutaArchivosQAS + rutaArchivo;
			break;
		case "pro":
			System.out.println("Es PRO");
			rutaArchivosFinal = rutaArchivosPRO + rutaArchivo;
			break;
		case "test":
			System.out.println("Es local");
			rutaArchivosFinal = rutaArchivos + rutaArchivo;
			break;
		default:
			System.out.println("No encontro match");
		}

		// rutaArchivosFinal = rutaArchivosQAS + rutaArchivo;

		System.out.println("Rutafinal:" + rutaArchivosFinal);
		return almacenArchivo(rutaArchivosFinal, bytes, rutaArchivosFinal + nombre);
	}

	private boolean almacenArchivo(String ruta, byte[] bytes, String nombre) {

		log.info("Se gardará en : " + ruta);
		new File(ruta).mkdirs();

		boolean respuesta = false;
		OutputStream os = null;
		OutputStream osClone = null;

		try {

			log.info("ruta completa:" + nombre);
			File f = new File(nombre);
			f.createNewFile();

			os = new FileOutputStream(f, false);
			os.write(bytes);
			os.flush();
			os.close();

			respuesta = true;
		} catch (IOException e) {
			e.printStackTrace();
			log.error("ERROR:" + e.getLocalizedMessage());
			respuesta = false;
		} finally {

			try {

				if (os != null)
					os.close();
				if (osClone != null)
					osClone.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

		return respuesta;
	}

	@Override
	public boolean eliminarArchivo(String ruta) {
		boolean respuesta = false;
		File archivo = new File(ruta);
		respuesta = eliminarArchivoYSubArchivos(archivo);
		log.info("Archivo " + archivo.getName() + " borrado: " + respuesta);
		return respuesta;
	}

	private boolean eliminarArchivoYSubArchivos(File f) {
		if (f.isDirectory())
			for (File child : f.listFiles())
				eliminarArchivoYSubArchivos(child);
		return f.delete();
	}

	public File obtenerArchivoDesdeRuta(String rutaRelativa) {
		String rutaArchivosFinal = "";
		switch (ambiente) {
		case "qas":
			rutaArchivosFinal = rutaArchivosQAS;
			break;
		case "pro":
			rutaArchivosFinal = rutaArchivosPRO;
			break;
		case "local":
			rutaArchivosFinal = rutaArchivos;
			break;
		}
		System.out.println("Obtener archivo de:"+rutaArchivosFinal + "/" + rutaRelativa);
		File f = null;

		f = new File(rutaArchivosFinal + "/" + rutaRelativa);
		
		return f;
	}

	@Override
	public byte[] obtenerArchivo(String rutaRelativa) {

		File file = obtenerArchivoDesdeRuta(rutaRelativa);

		if (!file.exists()) {
			System.out.println(rutaRelativa + " not found");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"File not found: "+rutaRelativa);
		}

		FileInputStream fis = null;
		try {

			fis = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fis.read(bytes);
			return bytes;

		} catch (IOException e) {

			e.printStackTrace();
			return null;

		} finally {

			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	public String generaRutaArchivo(String anio, String usuario, String numeroSolicitud) {
		return "/" + anio + "/" + usuario + "/" + numeroSolicitud;
	}

	public ByteArrayOutputStream generarZip(List<String> srcFiles) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ZipOutputStream zipOut = new ZipOutputStream(bos);
			for (String srcFile : srcFiles) {
				File fileToZip = new File(srcFile);
				FileInputStream fis = new FileInputStream(fileToZip);
				ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
				zipOut.putNextEntry(zipEntry);

				byte[] bytes = new byte[1024];
				int length;
				while ((length = fis.read(bytes)) >= 0) {
					zipOut.write(bytes, 0, length);
				}
				fis.close();
			}
			zipOut.close();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bos;
	}

	public static void main(String args[]) {
		String ambiente = "qas";
		// if ("qas".equals(ambiente)) {
		// System.out.println("Es QAS");
		// } else if ("pro".equals(ambiente)) {
		// System.out.println("Es PRO");
		// } else {
		// System.out.println("Es local");
		// }
		String rutaArchivosFinal = "";
		switch (ambiente) {
		case "qas":
			System.out.println("Es QAS");
			rutaArchivosFinal = "QAS";
			break;
		case "pro":
			System.out.println("Es PRO");
			rutaArchivosFinal = "PRO";
			break;
		case "test":
			System.out.println("Es local");
			rutaArchivosFinal = "LOCAL";
			break;
		}
		System.out.println("rutafinal:" + rutaArchivosFinal);
	}

}
