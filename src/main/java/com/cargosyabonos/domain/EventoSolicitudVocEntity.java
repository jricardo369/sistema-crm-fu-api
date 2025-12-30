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
@Table(name = "evento_solicitud_voc")
public class EventoSolicitudVocEntity {
	
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
	private SolicitudVocEntity solicitud;

	@Column(name = "usuario", length = 100)
	private String usuario;
	
	

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

	public SolicitudVocEntity getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(SolicitudVocEntity solicitud) {
		this.solicitud = solicitud;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	

}
