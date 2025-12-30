package com.cargosyabonos.domain;

import java.math.BigDecimal;

public class ReporteAnios {
	
	private String mes;
	private int numSolicitudes;
	private int mesNum;
	private BigDecimal monto;
	private int anio;
	
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public int getNumSolicitudes() {
		return numSolicitudes;
	}
	public void setNumSolicitudes(int numSolicitudes) {
		this.numSolicitudes = numSolicitudes;
	}
	public int getMesNum() {
		return mesNum;
	}
	public void setMesNum(int mesNum) {
		this.mesNum = mesNum;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	
	
}
