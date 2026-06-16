package com.cargosyabonos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "estatus_prospecto_abogado")
public class EstatusProspectoAbogadoEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estatus_prospecto_abogado")
    private int idEstatusProspectoAbogado;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    public int getIdEstatusProspectoAbogado() {
        return idEstatusProspectoAbogado;
    }
    public void setIdEstatusProspectoAbogado(int idEstatusProspectoAbogado) {
        this.idEstatusProspectoAbogado = idEstatusProspectoAbogado;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    

}
