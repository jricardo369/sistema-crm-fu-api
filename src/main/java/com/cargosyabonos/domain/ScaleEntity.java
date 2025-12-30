package com.cargosyabonos.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "scale")
public class ScaleEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_scale")
	private int idScale;
	
	@Column(name = "scale", length = 45)
	private String scale;

	@JoinColumn(name = "id_solicitud", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	private SolicitudEntity solicitud;

	public int getIdScale() {
		return idScale;
	}

	public void setIdScale(int idScale) {
		this.idScale = idScale;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public SolicitudEntity getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(SolicitudEntity solicitud) {
		this.solicitud = solicitud;
	}

}
