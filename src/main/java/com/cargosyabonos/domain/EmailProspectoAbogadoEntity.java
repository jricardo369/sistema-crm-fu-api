package com.cargosyabonos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "email_prospecto_abogado")
public class EmailProspectoAbogadoEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_email_pros_abo")
	private int idEmailProsAbo;


	@Column(name = "id_pros_abogado")
	private int idProsAbogado;

	@Column(name = "email", length = 150)
	private String email;

	public int getIdEmailProsAbo() {
		return idEmailProsAbo;
	}

	public void setIdEmailProsAbo(int idEmailProsAbo) {
		this.idEmailProsAbo = idEmailProsAbo;
	}

	public int getIdProsAbogado() {
		return idProsAbogado;
	}

	public void setIdProspectoAbogado(int idProsAbogado) {
		this.idProsAbogado = idProsAbogado;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
