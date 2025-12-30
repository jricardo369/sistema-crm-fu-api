package com.cargosyabonos.application.port.in;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.domain.NumeroCasosSolicitudes;
import com.cargosyabonos.domain.SolicitudVoc;
import com.cargosyabonos.domain.SolicitudVocEntity;

public interface SolicitudVocUseCase {
	
	//public List<SolicitudVoc> obtenerSolicitudesDeUsuarioPorFechayEstatus(int idUsuario,Date fechai,Date fechaf,String estatus);	
	//public List<SolicitudVoc> obtenerSolicitudesFiltro(int idUsuario,String campo,String valor,String fecha1,String fecha2);
	public List<SolicitudVoc> obtenerSolicitudes(int idUsuario, int estatus,int idSolicitud,String fechai,String fechaf,String ordenarPor,String orden,String campo,String valor,boolean myFiles,String cerradas);
	public List<SolicitudVoc> obtenerSolicitudesActivasTerapeuta(int idUsuario);
	public SolicitudVocEntity obtenerSolicitud(int idRequest);
	public SolicitudVoc obtenerSolicitudObj(int idRequest,int idUsuario);
	public void crearSolicitud(SolicitudVoc r,int usuario);
	public SolicitudVocEntity crearSolicitudMasivo(SolicitudVoc r,int usuario);
	public void actualizarSolicitud(SolicitudVoc r);
	public void eliminarSolicitud(int r);
	public void actualizarEstatusSolicitud(int idSolicitud, int idEstatus,int idUsuario,boolean closed);
	public void reasginar(int idUsuario,String motivo,int idSolicitud,int idUsuarioEnvio);
	public void actualizarUsuarioTerapeuta(int idUsuario,int idSolicitud,int idUsuarioEntrada);
	public List<SolicitudVoc> obtenerSolicitudesFiltroV2(int idUsuario, String campo,String valor,String fecha1,String fecha2);
	public String cargarExcel(MultipartFile archivo);
	public List<NumeroCasosSolicitudes> obtenerNumerosCaso(String numeroCaso);
	public void syncNumSesiones(int idSolicitud, int idUsuario);

}
