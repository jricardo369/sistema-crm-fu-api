package com.cargosyabonos.domain;

import java.math.BigDecimal;


public class Cita {

	private int idCita;
	private String comentario;
	private String fecha;
	private String hora;
	private String tipo;
	private boolean dosCitas;
	private int idSolicitud;
	private boolean noShow;
	private int idUsuario;
	private BigDecimal amount;
	private boolean pagado;
	private String nombreUsuario;
	private String fechaPagado;
	private String color;
	private boolean finSchedule;
	private String descEst;
	private int idEstaSol;
	private boolean tieneNota;
	private String caseNumber;
	private String importante;
	private String cliente;
	private String fechaCreacion;
	private String zonaHoraria;
	private int idEvento;
	
	public int getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(int idEvento) {
		this.idEvento = idEvento;
	}
	public String getZonaHoraria() {
		return zonaHoraria;
	}
	public void setZonaHoraria(String zonaHoraria) {
		this.zonaHoraria = zonaHoraria;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
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
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getFechaPagado() {
		return fechaPagado;
	}
	public void setFechaPagado(String fechaPagado) {
		this.fechaPagado = fechaPagado;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isFinSchedule() {
		return finSchedule;
	}
	public void setFinSchedule(boolean finSchedule) {
		this.finSchedule = finSchedule;
	}
	public String getDescEst() {
		return descEst;
	}
	public void setDescEst(String descEst) {
		this.descEst = descEst;
	}
	public int getIdEstaSol() {
		return idEstaSol;
	}
	public void setIdEstaSol(int idEstaSol) {
		this.idEstaSol = idEstaSol;
	}
	public boolean isTieneNota() {
		return tieneNota;
	}
	public void setTieneNota(boolean tieneNota) {
		this.tieneNota = tieneNota;
	}
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getImportante() {
		return importante;
	}
	public void setImportante(String importante) {
		this.importante = importante;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	

}
