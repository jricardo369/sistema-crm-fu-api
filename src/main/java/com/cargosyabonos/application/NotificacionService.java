package com.cargosyabonos.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cargosyabonos.application.port.in.NotificacionUseCase;
import com.cargosyabonos.application.port.out.EnviarCorreoPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.MsgPort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.CorreosFirmas;
import com.cargosyabonos.domain.Notificacion;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
public class NotificacionService implements NotificacionUseCase {

	@Autowired
	private SolicitudPort solPort;

	@Autowired
	private MsgPort msgPort;

	@Autowired
	private UsuariosPort usPort;

	@Autowired
	private EventoSolicitudPort evPort;

	@Autowired
	private EnviarCorreoPort envCorrPort;

	@Autowired
	private CorreoElectronicoService corrElecServ;
	
	

	@Override
	public void enviarNotificacion(Notificacion n,byte[] archivo,String extension) {

		boolean enviarLayout = false;
		SolicitudEntity s = solPort.obtenerSolicitud(n.getIdSolicitud());
		UsuarioEntity us = usPort.buscarPorId(Integer.valueOf(n.getUsuario()));

		if (n.getLayout() != null) {
			if (!"".equals(n.getLayout())) {
				enviarLayout = true;
			}
		}

		System.out.println("Enviar layout:" + enviarLayout);

		if (!enviarLayout) {

			Map<String, Object> params = new HashMap<>();
			params.put("${cuerpo}", n.getCuerpo());
			params.put("${cliente}", s.obtenerCliente());

			if (n.isMensaje()) {
				boolean envioMensaje = msgPort.envioMensaje(s.getTelefono(), n.getTitulo() + " - " + n.getCuerpo(), s,
						false);
				if (envioMensaje) {
					evPort.ingresarEventoDeSolicitud("Notification", "Notification message was sent to client",
							"Notification", us.getUsuario(), s);
				}
			}
			if (n.isMail()) {
				envCorrPort.enviarCorreoNotificacion(s.getEmail(), n.getTitulo(), params,archivo,extension,true);
			}
			
			evPort.ingresarEventoDeSolicitud("Notification", "Notification mail was sent to client", "Notification",
					us.getUsuario(), s);

		} else {

			if (n.getLayout().equals("Payment")) {
				System.out.println("Enviar layout Payment");
				if (s.datosClienteVacios()) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Customer email and name are mandatory to send notification");
				} else {
					
					if(n.isMail()){
						if(s.datosEmailVacios()){
								throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
										"Customer email is mandatory to send notification mail");
						}
					}
					if(n.isMensaje()){
						if(s.datosTelefonoVacios()){
								throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
										"Customer phone is mandatory to send notification");
						}
					}
					
					corrElecServ.enviarCorreoSaldoVencido(s, n.isMensaje(),n.isMail());
				}
			}
			if (n.getLayout().equals("Welcome Message")) {
				System.out.println("Enviar layout Welcome Message");
				if (s.datosClienteVacios()) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Customer email and name are mandatory to send notification");
				} else {
					corrElecServ.enviarCorreoNuevaSolicitudCliente(us.getIdUsuario(), s, n.isMensaje(),n.isMail());
				}
			}
			if (n.getLayout().equals("Thanks referral client")) {
				System.out.println("Enviar layout Thanks referral client al abogado");
				if (s.datosAbogadoVacios()) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Lawyer email and name are mandatory to send notification");
				} else {
					if (s.datosNombreVacio()) {
						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"Customer name or last name are mandatory to send notification");
					} else {
						corrElecServ.enviarCorreoNotifNuevaSolAAbogado(us.getIdUsuario(), s, n.isMensaje(),n.isMail());
					}
				}
			}

		}

	}

	@Override
	public void enviarNotificacionAbogados(Notificacion n,byte[] archivo,String extension) {
		
		System.out.println("Firmas:"+n.getFirmasAbogados());
		List<CorreosFirmas> listCorreos = solPort.obtenerCorreosAbogados(n.getFirmasAbogados());
		//System.out.println("num correos:"+listCorreos.size());
		String correos = "";
		
		StringBuilder s = new StringBuilder();
		int count = 0;
		for(CorreosFirmas c:listCorreos){
			s.append(""+c.getemail()+",");
			count++;
		}
		
		if(s.length() != 0){
			if(count > 1){
				correos = s.toString().substring(0,s.length()-1);
			}else{
				correos = s.toString();
			}
		}
		
		System.out.println("correos:"+correos);
		
		Map<String, Object> params = new HashMap<>();
		params.put("${cuerpo}", n.getCuerpo());
		params.put("${cliente}", "");
		envCorrPort.enviarCorreoNotificacion(correos, n.getTitulo(), params,archivo,extension,true);
		
	}

}
