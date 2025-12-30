package com.cargosyabonos.domain;

import java.util.Date;

public class DetalleSolsPorFecha {
	
	public Date fecha;
	public int numero;
	public int activas;
	public int cerradas;
	public int lost;
	public int noshow;
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public int getActivas() {
		return activas;
	}
	public void setActivas(int activas) {
		this.activas = activas;
	}
	public int getCerradas() {
		return cerradas;
	}
	public void setCerradas(int cerradas) {
		this.cerradas = cerradas;
	}
	public int getLost() {
		return lost;
	}
	public void setLost(int lost) {
		this.lost = lost;
	}
	public int getNoshow() {
		return noshow;
	}
	public void setNoshow(int noshow) {
		this.noshow = noshow;
	}
	

}
