package com.cargosyabonos.domain;

import java.math.BigDecimal;

public class ReporteInicio {
	
	public int citasPendientes;
	public BigDecimal totalIngreso;
	public BigDecimal totalAdeudos;
	public int getCitasPendientes() {
		return citasPendientes;
	}
	public void setCitasPendientes(int citasPendientes) {
		this.citasPendientes = citasPendientes;
	}
	public BigDecimal getTotalIngreso() {
		return totalIngreso;
	}
	public void setTotalIngreso(BigDecimal totalIngreso) {
		this.totalIngreso = totalIngreso;
	}
	public BigDecimal getTotalAdeudos() {
		return totalAdeudos;
	}
	public void setTotalAdeudos(BigDecimal totalAdeudos) {
		this.totalAdeudos = totalAdeudos;
	}

}
