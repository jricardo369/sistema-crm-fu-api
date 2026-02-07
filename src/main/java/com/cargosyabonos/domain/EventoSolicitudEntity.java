package com.cargosyabonos.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "evento_solicitud")
public class EventoSolicitudEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_evento")
	private int idEvento;
	
	@Column(name = "fecha", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date fecha;
	
	@Column(name = "evento", length = 45)
	private String evento;
	
	@Column(name = "descripcion", length = 45)
	private String descripcion;
	
	@Column(name = "tipo", length = 45)
	private String tipo;

	@JsonIgnore
	@JoinColumn(name = "id_solicitud", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private SolicitudEntity solicitud;

	@Column(name = "usuario", length = 100)
	private String usuario;
	
	@Column(name = "fecha_schedule", columnDefinition = "DATE")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date fechaSchedule;
	
	@Column(name = "hora_schedule")
	private String horaSchedule;
	
	@Column(name = "tipo_schedule", length = 5)
	private String tipoSchedule;
	
	@Column(name = "usuario_schedule", length = 5)
	private String usuarioSchedule;
	
	@Column(name = "estatus_schedule", length = 1)
	private String estatusSchedule;
	
	@Column(name = "timezone_schedule", length = 3)
	private String timeZoneSchedule;

	@Column(name = "fin_schedule")
	private boolean finSchedule;

	public boolean isFinSchedule() {
		return finSchedule;
	}

	public void setFinSchedule(boolean finSchedule) {
		this.finSchedule = finSchedule;
	}

	public int getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(int idEvento) {
		this.idEvento = idEvento;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public SolicitudEntity getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(SolicitudEntity solicitud) {
		this.solicitud = solicitud;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getHoraSchedule() {
		return horaSchedule;
	}

	public void setHoraSchedule(String horaSchedule) {
		this.horaSchedule = horaSchedule;
	}

	public String getTipoSchedule() {
		return tipoSchedule;
	}

	public void setTipoSchedule(String tipoSchedule) {
		this.tipoSchedule = tipoSchedule;
	}
	
	public Date getFechaSchedule() {
		return fechaSchedule;
	}

	public void setFechaSchedule(Date fechaSchedule) {
		this.fechaSchedule = fechaSchedule;
	}
	
	public String getUsuarioSchedule() {
		return usuarioSchedule;
	}

	public void setUsuarioSchedule(String usuarioSchedule) {
		this.usuarioSchedule = usuarioSchedule;
	}
	
	public String getEstatusSchedule() {
		return estatusSchedule;
	}

	public void setEstatusSchedule(String estatusSchedule) {
		this.estatusSchedule = estatusSchedule;
	}

	public String getTimeZoneSchedule() {
		return timeZoneSchedule;
	}

	public void setTimeZoneSchedule(String timeZoneSchedule) {
		this.timeZoneSchedule = timeZoneSchedule;
	}

}
