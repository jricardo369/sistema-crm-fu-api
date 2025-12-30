package com.cargosyabonos.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.ApiException;
import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.GenerarTokenDeAutenticacionUseCase;
import com.cargosyabonos.application.port.out.GeneraTokenDeAutenticacionPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
@PropertySource(value = "classpath:configuraciones-global.properties")
public class AutenticationService implements GenerarTokenDeAutenticacionUseCase {

	Logger log = LoggerFactory.getLogger(AutenticationService.class);

	@Value("${intentos-inicio-sesion}")
	private int intentosInicioSesion;

	@Autowired
	private GeneraTokenDeAutenticacionPort genTokenAutPort;

	@Autowired
	private UsuariosPort usPort;

	@Override
	public String autenticarUsuario(String usuario, String passwd) throws ApiException {

		String token = "";
		UsuarioEntity usuarioBD = usPort.buscarSoloPorUsuario(usuario);
		UtilidadesAdapter.pintarLog("Usuario:"+usuario);
		UtilidadesAdapter.pintarLog("Password:"+passwd);

		if (usuarioBD == null) {

			UtilidadesAdapter.pintarLog("no se encontro usuario");
			throw new ApiException(500, "Usuario y/o Contraseña incorrectos");

		} else {
			
			UtilidadesAdapter.pintarLog("Password sha256 bd:"+usuarioBD.getContrasenia());
			UtilidadesAdapter.pintarLog("Password sha256 login:"+UtilidadesAdapter.sha256(usuario, passwd));

			if (!usuarioBD.getContrasenia().equals(UtilidadesAdapter.sha256(usuario, passwd))) {

				int intentos = usuarioBD.getIntentos() + 1;
				usuarioBD.setIntentos(intentos);
				if (intentosInicioSesion == intentos) {
					usuarioBD.setEstatus("3");
				}
				usPort.actualizarUsuario(usuarioBD);
				throw new ApiException(500, "Usuario y/o Contraseña incorrectos");

			} else {
				//UtilidadesAdapter.pintarLog(usuarioBD.getNombre());
				if (usuarioBD.getEstatus().equals("2")) {
					throw new ApiException(500, "The user is inactive, please contact the Administrator");
				}
				if (usuarioBD.getEstatus().equals("3")) {
					throw new ApiException(500, "The user is inactive due to failed attempts");
				}
				if (usuarioBD.getEstatus().equals("4")) {
					throw new ApiException(500, "Your user has been deleted");
				}
				token = genTokenAutPort.generarTokenDeAutenticacion(usuario);
			}

		}

		return token;
	}

}
