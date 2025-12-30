package com.cargosyabonos.application.port.out;

public interface ArchivosPort {
	
	public boolean guardarArchivo(String rutaArchivos,byte[] bytes, String nombre);
	public boolean eliminarArchivo(String ruta);
	public byte[] obtenerArchivo(String rutaRelativa);
	public String generaRutaArchivo(String anio,String usuario,String numeroSolicitud);

}
