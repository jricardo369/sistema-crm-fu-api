package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.TittleAbogadoEntity;

@Repository
public interface TittleAbogadoJpa extends CrudRepository<TittleAbogadoEntity, Serializable>{

    public List<TittleAbogadoEntity> findAll();

}
