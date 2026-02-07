package com.cargosyabonos.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.in.EstadoUsuarioUseCase;
import com.cargosyabonos.application.port.out.EstadoUsuarioPort;
import com.cargosyabonos.domain.EstadoUsuarioEntity;

@Service
public class EstadoUsuarioService implements EstadoUsuarioUseCase {

    @Autowired
    private EstadoUsuarioPort estadoPort;

    @Override
    public List<EstadoUsuarioEntity> obtenerEstadosUsuario(int idUsuario) {
        return estadoPort.obtenerEstadosUsuario(idUsuario);
    }

    @Override
    public EstadoUsuarioEntity crearEstadoUsuario(EstadoUsuarioEntity estadoUsuario) {
        return estadoPort.crearEstadoUsuario(estadoUsuario);
    }

    @Override
    public void actualizarEstadoUsuario(EstadoUsuarioEntity estadoUsuario) {
         estadoPort.actualizarEstadoUsuario(estadoUsuario);
    }

    @Override
    public void eliminarEstadoUsuario(int idEstadoUsuario) {
        EstadoUsuarioEntity estadoUsuario = estadoPort.obtenerEstadoUsuario(idEstadoUsuario);
        estadoPort.eliminarEstadoUsuario(estadoUsuario);
    }

    @Override
    public EstadoUsuarioEntity obtenerEstadoUsuario(int idEstadoUsuario) {
        return estadoPort.obtenerEstadoUsuario(idEstadoUsuario);
    }

}
