package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cargosyabonos.domain.EstadoUsuarioEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoUsuarioJpa extends CrudRepository<EstadoUsuarioEntity, Serializable> {

    public EstadoUsuarioEntity findByIdEstadoUsuario(int idEstadoUsuario);

    @Query(value = "SELECT * FROM request_evaluation.estado_usuario WHERE id_usuario = ?1", nativeQuery = true)
	public List<EstadoUsuarioEntity> obEstadosDeUsuario(int idUsuario);

}
