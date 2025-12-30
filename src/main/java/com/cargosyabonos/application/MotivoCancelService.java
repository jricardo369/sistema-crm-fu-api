package com.cargosyabonos.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.cargosyabonos.application.port.in.MotivoCancelUseCase;
import com.cargosyabonos.application.port.out.MotivoCancelPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.MotivoCancelEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
public class MotivoCancelService implements MotivoCancelUseCase {

	
	@Autowired
	private MotivoCancelPort mcPort;
	
	@Autowired
	private UsuariosPort usPort;

	@Override
	public List<MotivoCancelEntity> obtenerMotivosPorTipo(String tipo,int idUsuario) {
		
		String rol ="";
		UsuarioEntity usEnt = usPort.buscarPorId(idUsuario); 
		if(usEnt.getRol().equals("5")||usEnt.getRol().equals("8")){
			rol = "INT";
		}else if(usEnt.getRol().equals("7")){
			rol = "TMP";
		}else if(usEnt.getRol().equals("11")){
			rol = "GEN";
		}else{
			rol = "GEN";
		}
		
		return mcPort.obtenerMotivosPorTipo(tipo,rol);
	}


}
