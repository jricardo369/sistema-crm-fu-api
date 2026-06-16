package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cargosyabonos.domain.FuenteDeReferenciaEntity;

public interface FuenteDeReferenciaJpa extends CrudRepository<FuenteDeReferenciaEntity, Serializable>{

    public List<FuenteDeReferenciaEntity> findAll();

}
