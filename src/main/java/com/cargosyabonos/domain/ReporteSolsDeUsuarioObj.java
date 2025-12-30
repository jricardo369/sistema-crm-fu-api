package com.cargosyabonos.domain;

public class ReporteSolsDeUsuarioObj {

	private int idSolicitud;
	private String fecha;
	
	public int getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public ReporteSolsDeUsuarioObj(int idSolicitud, String fecha) {
		super();
		this.idSolicitud = idSolicitud;
		this.fecha = fecha;
	}
	
}
