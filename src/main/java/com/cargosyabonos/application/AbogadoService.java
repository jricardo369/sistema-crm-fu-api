package com.cargosyabonos.application;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.AbogadoUseCase;
import com.cargosyabonos.application.port.out.AbogadoPort;
import com.cargosyabonos.application.port.out.EmailAbogadoPort;
import com.cargosyabonos.domain.Abogado;
import com.cargosyabonos.domain.AbogadoEntity;
import com.cargosyabonos.domain.EmailAbogadoEntity;

@Service
public class AbogadoService implements AbogadoUseCase {

	@Autowired
	private AbogadoPort aboPort;
	
	@Autowired
	private EmailAbogadoPort emAboPort;

	@Override
	public List<AbogadoEntity> obtenerAbogados() {
		return aboPort.obtenerAbogados();
	}

	@Override
	public AbogadoEntity obtenerAbogadoPorId(int idAbogado) {
		return aboPort.obtenerAbogadoPorId(idAbogado);
	}

	@Override
	public List<AbogadoEntity> obtenerAbogadosPorNombreYSinonimo(String nombre) {
		return aboPort.obtenerAbogadosPorNombreYSinonimo(nombre);
	}

	@Override
	public void crearAbogado(AbogadoEntity a,String emailsAbogado) {
		a.setFechaCreacion(null);
		//a.setEmail("t");
		
		/*List<AbogadoEntity> l = aboPort.obtenerAbogadoPorEmail(a.getEmail());
		
		if(!l.isEmpty()){
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"This email is already registered");
		}*/
		
		AbogadoEntity ae = aboPort.crearAbogado(a);
		
		String[] emailsArray = emailsAbogado.split(",");
		
	    for (String email : emailsArray) {
	        UtilidadesAdapter.pintarLog("Email procesado: " + email);
	        EmailAbogadoEntity eae = new EmailAbogadoEntity();
	        eae.setIdAbogado(ae.getIdAbogado());
	        eae.setEmail(email);
	        emAboPort.crearEmailAbogado(eae);
	    }
	
		
	}
	

	@Override
	public void actualizarAbogado(AbogadoEntity a) {
		aboPort.actualizarAbogado(a);
	}

	@Override
	public void eliminarAbogado(int idAbogado) {
		AbogadoEntity a = aboPort.obtenerAbogadoPorId(idAbogado);
		emAboPort.eliminarEmailAbogadoByIdAbogado(a.getIdAbogado());
		aboPort.eliminarAbogado(a);
	}

	@Override
	public List<Abogado> obtenerAbogadosConMail(String valorBusqueda) {
		return aboPort.obtenerAbogadosConMail(valorBusqueda);
	}


	

}
