package com.cargosyabonos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fuente_de_referencia")
public class FuenteDeReferenciaEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_fuente")
    private int fuenteDeReferenciaId;
	
	private String descripcion;

    public int getFuenteDeReferenciaId() {
        return fuenteDeReferenciaId;
    }

    public void setFuenteDeReferenciaId(int fuenteDeReferenciaId) {
        this.fuenteDeReferenciaId = fuenteDeReferenciaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
