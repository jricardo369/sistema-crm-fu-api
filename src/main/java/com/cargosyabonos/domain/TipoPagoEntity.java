package com.cargosyabonos.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "tipo_pago")
public class TipoPagoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tipo_pago")
	private int idTipoPago;
	

	@Column(name = "nombre", length = 50)
	private String nombre;


	public int getIdTipoPago() {
		return idTipoPago;
	}


	public void setIdTipoPago(int idTipoPago) {
		this.idTipoPago = idTipoPago;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	

}
