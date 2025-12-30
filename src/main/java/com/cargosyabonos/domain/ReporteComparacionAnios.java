package com.cargosyabonos.domain;

import java.math.BigDecimal;
import java.util.List;

public class ReporteComparacionAnios {
	
	public int anioActual;
	public List<ReporteAnios> mesesAnioActual;
	public int anioAnterior;
	public List<ReporteAnios> mesesAnioAnterior;
	public BigDecimal montoTotalAnioActual;
	public BigDecimal montoTotalAnioAnterior;
	
	public int getAnioActual() {
		return anioActual;
	}
	public void setAnioActual(int anioActual) {
		this.anioActual = anioActual;
	}
	public List<ReporteAnios> getMesesAnioActual() {
		return mesesAnioActual;
	}
	public void setMesesAnioActual(List<ReporteAnios> mesesAnioActual) {
		this.mesesAnioActual = mesesAnioActual;
	}
	public int getAnioAnterior() {
		return anioAnterior;
	}
	public void setAnioAnterior(int anioAnterior) {
		this.anioAnterior = anioAnterior;
	}
	public List<ReporteAnios> getMesesAnioAnterior() {
		return mesesAnioAnterior;
	}
	public void setMesesAnioAnterior(List<ReporteAnios> mesesAnioAnterior) {
		this.mesesAnioAnterior = mesesAnioAnterior;
	}
	public BigDecimal getMontoTotalAnioActual() {
		return montoTotalAnioActual;
	}
	public void setMontoTotalAnioActual(BigDecimal montoTotalAnioActual) {
		this.montoTotalAnioActual = montoTotalAnioActual;
	}
	public BigDecimal getMontoTotalAnioAnterior() {
		return montoTotalAnioAnterior;
	}
	public void setMontoTotalAnioAnterior(BigDecimal montoTotalAnioAnterior) {
		this.montoTotalAnioAnterior = montoTotalAnioAnterior;
	}
	
	
}
