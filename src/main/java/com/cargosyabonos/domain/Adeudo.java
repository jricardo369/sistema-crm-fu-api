package com.cargosyabonos.domain;

import java.math.BigDecimal;
import java.sql.Date;

public class Adeudo {
	
	private int idSolicitud;
	private Date fecha;
	private String cliente;
	private String telefono;
	private String email;
	private BigDecimal amount;
	private BigDecimal adeudo;
	private BigDecimal pagado;
	
	public int getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAdeudo() {
		return adeudo;
	}
	public void setAdeudo(BigDecimal adeudo) {
		this.adeudo = adeudo;
	}
	public BigDecimal getPagado() {
		return pagado;
	}
	public void setPagado(BigDecimal pagado) {
		this.pagado = pagado;
	}

}
