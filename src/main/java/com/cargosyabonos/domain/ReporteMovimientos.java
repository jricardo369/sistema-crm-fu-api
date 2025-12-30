package com.cargosyabonos.domain;

import java.math.BigDecimal;

public class ReporteMovimientos {
	
	public BigDecimal saldo;
	public BigDecimal totalIngreso;
	public BigDecimal totalAdeudos;
	
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
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
