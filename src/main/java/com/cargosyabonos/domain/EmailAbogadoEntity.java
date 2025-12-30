package com.cargosyabonos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "email_abogado")
public class EmailAbogadoEntity {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_email_abo")
	private int idEmailAbo;


	@Column(name = "id_abogado")
	private int idAbogado;

	@Column(name = "email", length = 150)
	private String email;

	public int getIdEmailAbo() {
		return idEmailAbo;
	}

	public void setIdEmailAbo(int idEmailAbo) {
		this.idEmailAbo = idEmailAbo;
	}

	public int getIdAbogado() {
		return idAbogado;
	}

	public void setIdAbogado(int idAbogado) {
		this.idAbogado = idAbogado;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
