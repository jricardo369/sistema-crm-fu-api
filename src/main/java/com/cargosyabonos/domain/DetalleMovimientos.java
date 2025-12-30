package com.cargosyabonos.domain;

import java.math.BigDecimal;
import java.util.List;

public class DetalleMovimientos {
	
	private BigDecimal pendiente;
	private List<Movimiento> movimientos;
	
	public BigDecimal getPendiente() {
		return pendiente;
	}
	public void setPendiente(BigDecimal pendiente) {
		this.pendiente = pendiente;
	}
	public List<Movimiento> getMovimientos() {
		return movimientos;
	}
	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}
	
	
	
}
