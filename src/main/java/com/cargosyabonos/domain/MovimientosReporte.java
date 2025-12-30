package com.cargosyabonos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MovimientosReporte {
	
	private BigDecimal cargos;
	private BigDecimal abonos;
	private BigDecimal balance;
	private List<Movimiento> movimientos;
	private List<Adeudo> adeudos;
	
	public BigDecimal getCargos() {
		return cargos;
	}
	public void setCargos(BigDecimal cargos) {
		this.cargos = cargos;
	}
	public BigDecimal getAbonos() {
		return abonos;
	}
	public void setAbonos(BigDecimal abonos) {
		this.abonos = abonos;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public List<Movimiento> getMovimientos() {
		return movimientos;
	}
	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}
	public List<Adeudo> getAdeudos() {
		return adeudos;
	}
	public void setAdeudos(List<Adeudo> adeudos) {
		this.adeudos = adeudos;
	}
	
	
	
	
	
	
}
