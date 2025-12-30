package com.cargosyabonos.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "estatus_pago")
public class EstatusPagoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estatus_pago")
	private int idEstatusPago;
	

	@Column(name = "descripcion", length = 45)
	private String descripcion;


	public int getIdEstatusPago() {
		return idEstatusPago;
	}


	public void setIdEstatusPago(int idEstatusPago) {
		this.idEstatusPago = idEstatusPago;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	

}
