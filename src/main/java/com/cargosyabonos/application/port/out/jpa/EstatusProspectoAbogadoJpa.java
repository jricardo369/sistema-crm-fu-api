package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import com.cargosyabonos.domain.EstatusProspectoAbogadoEntity;

@Repository
public interface EstatusProspectoAbogadoJpa extends CrudRepository<EstatusProspectoAbogadoEntity, Serializable>{

    public List<EstatusProspectoAbogadoEntity> findAll();

}
