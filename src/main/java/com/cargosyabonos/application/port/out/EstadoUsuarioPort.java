package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.EstadoUsuarioEntity;

public interface EstadoUsuarioPort {

    public EstadoUsuarioEntity obtenerEstadoUsuario(int idEstadoUsuario);
    public List<EstadoUsuarioEntity> obtenerEstadosUsuario(int idUsuario);
    public EstadoUsuarioEntity crearEstadoUsuario(EstadoUsuarioEntity estadoUsuario);
    public void actualizarEstadoUsuario(EstadoUsuarioEntity estadoUsuario);
    public void eliminarEstadoUsuario(EstadoUsuarioEntity estadoUsuario);

}
