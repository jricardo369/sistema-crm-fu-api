package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.application.port.out.EstadoUsuarioPort;
import com.cargosyabonos.application.port.out.jpa.EstadoUsuarioJpa;
import com.cargosyabonos.domain.EstadoUsuarioEntity;

@Repository
public class EstadoUsuarioRepository implements EstadoUsuarioPort {

    @Autowired
    EstadoUsuarioJpa estUsJpa;

    @Override
    public List<EstadoUsuarioEntity> obtenerEstadosUsuario(int idUsuario) {
        return estUsJpa.obEstadosDeUsuario(idUsuario);
    }

    @Override
    public EstadoUsuarioEntity crearEstadoUsuario(EstadoUsuarioEntity estadoUsuario) {
        EstadoUsuarioEntity e = estUsJpa.save(estadoUsuario);
        return e;   
    }

    @Override
    public void actualizarEstadoUsuario(EstadoUsuarioEntity estadoUsuario) {
        estUsJpa.save(estadoUsuario);
    }

    @Override
    public void eliminarEstadoUsuario(EstadoUsuarioEntity estadoUsuario) {
        estUsJpa.delete(estadoUsuario);
    }

    @Override
    public EstadoUsuarioEntity obtenerEstadoUsuario(int idEstadoUsuario) {
        return estUsJpa.findByIdEstadoUsuario(idEstadoUsuario);
    }

}
