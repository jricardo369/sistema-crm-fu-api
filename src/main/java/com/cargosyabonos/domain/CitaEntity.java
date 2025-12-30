package com.cargosyabonos.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cita")
public class CitaEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cita")
	private int idCita;
	
	@Column(name = "comentario", length = 250)
	private String comentario;
	
	@Column(name = "fecha", length = 10)
	private String fecha;
	
	@Column(name = "hora", length = 5)
	private String hora;
	
	@Column(name = "tipo", length = 5)
	private String tipo;
	
	@Column(name = "dos_citas")
	private boolean dosCitas;
	
	@Column(name = "id_solicitud")
	private int idSolicitud;
	
	@Column(name = "no_show")
	private boolean noShow;
	
	@Column(name = "id_usuario")
	private int idUsuario;
	
	private BigDecimal amount;
	
	private boolean pagado;
	
	@Column(name = "fecha_pagado")
	private String fechaPagado;

	public int getIdCita() {
		return idCita;
	}

	public void setIdCita(int idCita) {
		this.idCita = idCita;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isDosCitas() {
		return dosCitas;
	}

	public void setDosCitas(boolean dosCitas) {
		this.dosCitas = dosCitas;
	}

	public int getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public boolean isNoShow() {
		return noShow;
	}

	public void setNoShow(boolean noShow) {
		this.noShow = noShow;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean isPagado() {
		return pagado;
	}

	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}

	public String getFechaPagado() {
		return fechaPagado;
	}

	public void setFechaPagado(String fechaPagado) {
		this.fechaPagado = fechaPagado;
	}

}
