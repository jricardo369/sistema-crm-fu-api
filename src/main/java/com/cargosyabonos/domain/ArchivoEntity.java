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
@Table(name = "adjuntos")
public class ArchivoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_imagen")
	private int idImagen;

	@Column(name = "url_imagen", length = 200)
	private String urlImagen;
	
	@Column(name = "nombre", length = 100)
	private String nombre;	
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_solicitud", nullable =  false)
	@JsonIgnore
    private SolicitudEntity solicitd;

	@Column(name = "id_usuario_cargo")
	private int idUsuarioCargo;	
	
	public int getIdImagen() {
		return idImagen;
	}

	public void setIdImagen(int idImagen) {
		this.idImagen = idImagen;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public SolicitudEntity getSolicitd() {
		return solicitd;
	}

	public void setSolicitd(SolicitudEntity solicitd) {
		this.solicitd = solicitd;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getIdUsuarioCargo() {
		return idUsuarioCargo;
	}

	public void setIdUsuarioCargo(int idUsuarioCargo) {
		this.idUsuarioCargo = idUsuarioCargo;
	}

}
