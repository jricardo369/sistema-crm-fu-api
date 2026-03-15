package com.cargosyabonos.application;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.cargosyabonos.application.port.out.CorreosPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.MovimientosPort;
import com.cargosyabonos.application.port.out.MsgPort;
import com.cargosyabonos.application.port.out.TextosPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.AdeudoSolicitudes;
import com.cargosyabonos.domain.ConfiguracionEntity;
import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
@PropertySource(value = "classpath:configuraciones-global.properties")
public class CorreoElectronicoService implements CorreoElectronicoUseCase {

	Logger logger = Logger.getLogger(CorreoElectronicoService.class.getName());
	
	@Value("${ruta.host}")
	private String hostLocal;
	
	@Value("${ruta.host.qas}")
	private String hostQas;
	
	@Value("${ruta.host.pro}")
	private String hostPro;
	
	@Value("${ambiente}")
	private String ambiente;

	@Autowired
	private UsuariosPort usPort;	
	
	@Autowired
	private MovimientosPort movPort;
	
	@Autowired
	private TextosPort textosPort;

	@Autowired
	private CorreosPort correosPort;
	
	@Autowired
	private EventoSolicitudPort evPort;
	
	@Autowired
	private MsgPort msgPort;

	@Autowired
	private ConfiguracionPort confPort; 

	@Override
	public void enviarCorreoNuevaSolicitudCliente(int idUsuario, SolicitudEntity solicitud,boolean isMensaje,boolean isMail) {

		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}",  solicitud.obtenerCliente());
		if(isMail){
			correosPort.enviarCorreoDeLayout(solicitud.getEmail(), "Welcome to FamiliasUnidas", params, "email-bienvenida");
		}
		if (isMensaje) {
			msgPort.envioMensaje(solicitud.getTelefono(), "Thank you for your call! Please watch this video. We will call you soon! https://drive.google.com/file/d/1GVAXTSvIj9FQRnBJlSg1Y_NpOujM1eZv/view , ¡Gracias por tu llamada! Mira este video. ¡Te llamaremos pronto! https://drive.google.com/file/d/1qSIK1T1AMPL_fzVAR8mCNRJ8qPMJ7RK0/view?usp=sharing",
					solicitud, false);
		}

	}

	@Override
	public void enviarCorreoNotifNuevaSolAAbogado(int idUsuario, SolicitudEntity solicitud,boolean isMensaje,boolean isMail) {

		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}",  solicitud.obtenerCliente()+" "+solicitud.getApellidosSinNull());
		params.put("${abogado}", solicitud.getAbogado());
		if(isMail){
			correosPort.enviarCorreoDeLayout(solicitud.getEmail_abogado(), "Appreciation for the Business Referral and Update on Client Evaluation", params, "email-notif-nueva-sol-a-abogado");
		}
		if (isMensaje) {
			msgPort.envioMensaje(solicitud.getTelefono(), "Dear "+solicitud.getAbogado()+", We hope this email finds you well. We wanted to extend our sincere gratitude for referring "
                            +solicitud.obtenerCliente()+" "+solicitud.getApellidosSinNull()+" to our psychological evaluation services. Your trust and confidence in our"
                            +"expertise are greatly appreciated.",
					solicitud, false);
		}

	}
	
	@Override
	public void enviarCorreoCita(String nombre,String fecha,String hora,String tipo,String email,int idSolicitud,String formatoFecha,String entrevistador,String uidSchedule) {

		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}",  nombre);
		params.put("${fecha}", fecha);
		params.put("${hora}", hora);
		params.put("${tipo}", tipo);
		params.put("${entrevistador}", entrevistador);

		correosPort.enviarCorreoDeLayoutCalendarWithInvite("INVITACION",email, "Appointment with Familias Unidas for file "+idSolicitud, params, "email-cita",uidSchedule);

	}

	@Override
	public void enviarCorreoCitaCancelacion(String nombre, String fecha, String hora, String tipo, String email,
			String estado, int idSolicitud, String formatoFecha, String zonaHoraria,String entrevistador,String uidSchedule,boolean isCliente) {

		Date d = null;
		try {
			boolean formatoUs = fecha != null && fecha.contains("/");
			if(formatoUs){
				d = UtilidadesAdapter.cadenaAFechaFormatUS(fecha);
			}else{
				d = UtilidadesAdapter.cadenaAFecha(fecha);
			}
				
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String fechaF = UtilidadesAdapter.formatearFechaUS(d);

		// Convertir a horario del State seleccionado en solicitud, si viene vacio dejar la hora noramal
		boolean horaConvertidaFlag = false;
		String horaConvertida = "";
		String tipoConvertido = "";
		if (estado != null) {

			if (!estado.equals("")) {

				horaConvertidaFlag = true;
				horaConvertida = UtilidadesAdapter.convertirAHoraEstado(fechaF, hora, tipo, estado, zonaHoraria);

				logger.info("Se convirtio hora a estado " + estado + " la hora " + horaConvertida);
				if ("".equals(horaConvertida)) {
					horaConvertidaFlag = false;
				} else {
					String[] valores = horaConvertida.split(" ");
					horaConvertida = valores[0];
					tipoConvertido = valores[1];
				}

			}
		}

		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}", nombre);
		params.put("${fecha}", fechaF);
		params.put("${hora}", horaConvertidaFlag ? horaConvertida : hora);
		params.put("${tipo}", tipoConvertido.isEmpty() ? tipo : tipoConvertido);
		params.put("${entrevistador}", entrevistador);

		String subject = "";
		if(isCliente){
			subject = "Cancellation of Interview with Familias Unidas – File " + idSolicitud;
		}else{
			subject = "Interview cancellation with Familias Unidas for file " + idSolicitud;
		}

		correosPort.enviarCorreoDeLayoutCalendarWithInvite("CANCELACION",email,
				subject, params,
				"email-cita-cancelacion", uidSchedule);

	}

	@Override
	public void enviarCorreoCitaVoc(String nombre,String fecha,String hora,String tipo,String email,int idSolicitud,String uidSchedule) {

		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}",  nombre);
		params.put("${fecha}", fecha);
		params.put("${hora}", hora);
		params.put("${tipo}", tipo);
		correosPort.enviarCorreoDeLayoutCalendarWithInvite("INVITACION",email, "Appointment with Familias Unidas for file VOC "+idSolicitud, params, "email-cita", uidSchedule);

	}

	@Override
	public void enviarCorreoAAbogadoPorFinSolicitud(SolicitudEntity solicitud) {

		Map<String, Object> params = new HashMap<>();
		String abogado = "";
		if("".equals(solicitud.getAbogado())){
			abogado = solicitud.getFirmaAbogados();
		}else{
			abogado = solicitud.getAbogado();
		}
		if(abogado == null){
			abogado = "user";
		}
		params.put("${cliente}",  solicitud.obtenerCliente());
		params.put("${abogado}", abogado);
		if(solicitud.getEmail_abogado() == null){
			solicitud.setEmail_abogado("");
		}
		if("".equals(solicitud.getEmail_abogado())){
			evPort.ingresarEventoDeSolicitud("Info","Email for document completion for lawyer was not sent due to lack of email", "Info", "Admin", solicitud);
		}else{
			correosPort.enviarCorreoDeLayout(solicitud.getEmail_abogado(), "Finalized document for the customer " + solicitud.getCliente() +" of service " + solicitud.getTipoSolicitud().getNombre(), params, "email-finalizo-doc-solicitud");
		}

	}
	
	@Override
	public void enviarCorreoAsignacion(SolicitudEntity solicitud,String nombre,String email) {

		Map<String, Object> params = new HashMap<>();
		params.put("${usuario}",  nombre);
		params.put("${solicitud}", solicitud.getIdSolicitud());
		params.put("${tipo}",  solicitud.getTipoSolicitud().getNombre());
		params.put("${host}",  obtenerHost());
		correosPort.enviarCorreoDeLayout(email, "Assignment in FAMILIAS UNIDAS system of file " + solicitud.getIdSolicitud(), params, "email-asignacion");

	}
	
	@Override
	public void enviarCorreoAMasterPorFinSolicitud(SolicitudEntity solicitud){

		UsuarioEntity usAmanda = usPort.buscarPorUsuario("amanda");
		if(usAmanda !=  null){
			Map<String, Object> params = new HashMap<>();
			params.put("${tipo}",  solicitud.getTipoSolicitud().getIdTipoSolicitud());
			params.put("${cliente}", solicitud.getNombreClienteCompleto());
			if(!"".equals(usAmanda.getCorreoElectronico())){
				correosPort.enviarCorreoDeLayout(usAmanda.getCorreoElectronico(), "Finalized document for the customer " + solicitud.getCliente() +" of service " + solicitud.getTipoSolicitud().getNombre(), params, "email-fin-solicitud");
			}
		}

	}

	@Override
	public void enviarCorreoReject(String emailRevisor,String emailEntrevistador,String nombreRevisor,String nombreEntrevistado,SolicitudEntity solicitud,EventoSolicitudEntity e,String motivo) {

		Map<String, Object> params = new HashMap<>();

		
		params.put("${cliente}",  solicitud.obtenerCliente());
		params.put("${solicitud}", solicitud.getIdSolicitud());

		params.put("${fecha}", e.getFechaSchedule());
		params.put("${hora}",  e.getHoraSchedule());
		params.put("${tipo}", e.getTipoSchedule());
		params.put("${motivo}", motivo);

		System.out.println("ev:"+e);

		if(e != null){

			params.put("${fecha}", UtilidadesAdapter.formatearFecha(e.getFechaSchedule()) + " "+e.getHoraSchedule()+" "+e.getTipoSchedule());

			params.put("${usuario}", nombreRevisor);
			correosPort.enviarCorreoDeLayout(emailRevisor, "Interview declined for file "+solicitud.getIdSolicitud(), params, "email-reject");
			params.put("${usuario}", nombreEntrevistado);
			correosPort.enviarCorreoDeLayoutCalendarWithInvite("CANCELACION",emailEntrevistador, "Interview declined for file "+solicitud.getIdSolicitud(), params, "email-reject", UtilidadesAdapter.formarUidEvento(e));

		}

	}

	@Override
	public void enviarCorreoNoShow(String emailRevisor,String emailEntrevisador,String nombreRevisor,String nombreEntrevistador,SolicitudEntity solicitud,EventoSolicitudEntity e,String motivo) {

		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}",  solicitud.obtenerCliente());
		params.put("${solicitud}", solicitud.getIdSolicitud());
		params.put("${motivo}", motivo);

		params.put("${fecha}", e.getFechaSchedule());
		params.put("${hora}",  e.getHoraSchedule());
		params.put("${tipo}", e.getTipoSchedule());

		if(e != null){

			params.put("${usuario}", nombreRevisor);
			correosPort.enviarCorreoDeLayout(emailRevisor, "Client no show to interview at request "+solicitud.getIdSolicitud(), params, "email-no-show");
			params.put("${usuario}", nombreEntrevistador);
			correosPort.enviarCorreoDeLayoutCalendarWithInvite("CANCELACION",emailEntrevisador, "Interview declined for file "+solicitud.getIdSolicitud(), params, "email-no-show", UtilidadesAdapter.formarUidEvento(e));

		}

	}
	

	@Override
	public void enviarCorreoSuicidio(SolicitudEntity solicitud,String email,String nombreUsuario) {

		Map<String, Object> params = new HashMap<>();
		params.put("${usuario}", nombreUsuario);
		params.put("${cliente}", solicitud.obtenerCliente());
		params.put("${solicitud}", solicitud.getIdSolicitud());
		correosPort.enviarCorreoDeLayout(email, "Customer with suicidal signs in request "+solicitud.getIdSolicitud(), params, "email-suicidio");
		
	}

	@Override
	public void enviarCorreoRecordatorio(String usuario,String email,String fecha,String hora,EventoSolicitudEntity evento,SolicitudEntity solicitud) {

		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}", usuario);
		params.put("${fecha}", fecha);
		params.put("${hora}", hora);
		params.put("${servicio}", solicitud.getTipoSolicitud().getNombre());
		List<ConfiguracionEntity> confjs = confPort.obtenerConfiguraciones();
		ConfiguracionEntity cn = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-NOMBRE")).findAny().orElse(null);
		ConfiguracionEntity ci = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-INFO")).findAny().orElse(null);
		ConfiguracionEntity co = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-ORG")).findAny().orElse(null);
		params.put("${nombreContacto}", cn.getValor());
		params.put("${infoContacto}", ci.getValor());
		params.put("${suOrganizacion}", co.getValor());
		correosPort.enviarCorreoDeLayout(email, "Reminder session in request "+solicitud.getIdSolicitud() +" with FamiliasUnidas", params, "email-recordatorio-cita");	

	}
	
	@Override
	public void enviarCorreoFileVOCEndingSessions(String usuario,String email,Map<String, Object> params){

		List<ConfiguracionEntity> confjs = confPort.obtenerConfiguraciones();
		ConfiguracionEntity cn = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-NOMBRE")).findAny().orElse(null);
		ConfiguracionEntity ci = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-INFO")).findAny().orElse(null);
		ConfiguracionEntity co = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-ORG")).findAny().orElse(null);

		params.put("${nombreContacto}", cn.getValor());
		params.put("${infoContacto}", ci.getValor());
		params.put("${suOrganizacion}", co.getValor());
		correosPort.enviarCorreoDeLayout(email, "Reminder of appointments approved to be completed in file VOC "+params.get("${file-voc}")+" with FamiliasUnidas", params, "email-file-voc-ending-sessions");

	}
	
	@Override
	public void enviarCorreoPrimeraCita(String nombre,String fecha,String hora,String email,SolicitudEntity solicitud){

		Map<String, Object> params = new HashMap<>();
		params.put("${fecha}", fecha);
		params.put("${hora}", hora);
		params.put("${tipo}", solicitud.getTipoSolicitud().getNombre());
		params.put("${entrevistador}", nombre);
		correosPort.enviarCorreoDeLayout(email, "First interview for file ("+solicitud.getIdSolicitud() + ") "+solicitud.getTipoSolicitud().getNombre(), params, "email-primera-cita");

	}

	@Override
	public void enviarCorreoSaldoVencido(SolicitudEntity solicitud,boolean isMensaje,boolean isMail) {

		AdeudoSolicitudes a = movPort.obtenerDeudasSolicitud(solicitud.getIdSolicitud());
		if(a != null){

			System.out.println("Idsol:" + a.getid_solicitud() + "|email:" + a.getemail() + "|deuda:" + a.getdeuda()+ "|amount:" + a.getamount());

			if (a.getamount() != null) {
				Map<String, Object> paramsP = new HashMap<>();
				String clt = "";
				if(a.getcliente()==null||"".equals(a.getcliente())){
					clt = "";
				}else{
					clt = a.getcliente();
				}
				paramsP.put("${cliente}", clt);
				if (a.getdeuda() != null) {
					if (a.getdeuda().compareTo(BigDecimal.ZERO) != 0) {

						paramsP.put("${monto}", a.getdeuda());
						paramsP.put("${tipo-servicio}", solicitud.getTipoSolicitud().getNombre());
						paramsP.put("${solicitud}", solicitud.getIdSolicitud());

						if(isMail){
							correosPort.enviarCorreoSaldoVencido(a.getemail(), paramsP);
						}
						if(isMensaje){
							msgPort.envioMensaje(solicitud.getTelefono(),textosPort.textoEnvioPayment(null, a.getdeuda(), solicitud.getTipoSolicitud().getNombre(), "US"),
									solicitud, false);
						}

					}
				}
				
			}

		}

	}
	
	public String obtenerHost(){

		String hostFinal = "";
		if("qas".equals(ambiente)){
			hostFinal = hostLocal;
		}else if("pro".equals(ambiente)){
			hostFinal =  hostPro;
		}else{
			hostFinal =  hostQas;
		}
		return hostFinal;

	}

	@Override
	public void enviarCorreoInvoice(String email, SolicitudEntity solicitud) {

		if(email == null){
			email = "";
		}
		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}", solicitud.getCliente());
		params.put("${tipo}", solicitud.getTipoSolicitud().getNombre());
		params.put("${file}", solicitud.getIdSolicitud());
		System.out.println("enviar correo invoce a cliente:"+email);
		if("".equals(email)){
			evPort.ingresarEventoDeSolicitud("Info","Invoice email not sent due to lack of customer email", "Info", "Admin", solicitud);
		}else{
			correosPort.enviarCorreoInvoice(email, params, solicitud.getIdSolicitud());
		}

	}

	@Override
	public void enviarCorreoNotificacion(String nombre, String cuerpo,String email, String titulo, byte[] archivo,String extension,boolean ingles) {

		Map<String, Object> params = new HashMap<>();
		params.put("${cuerpo}", cuerpo);
		params.put("${cliente}", nombre);

		String template = "";
			
			if(ingles){
				template = "email-template-en";
			}else{
				template = "email-template";
			}
			
		correosPort.enviarCorreoNotificacionLayout(email, titulo, params, null, extension,template);

	}

	@Override
	public void enviarCorreoNotificacionVoc(String nombreUsuario, String titulo, String cuerpo, String email) {

		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}", nombreUsuario);
		params.put("${cuerpo}", cuerpo);
		String template = "email-template-us";
			
		correosPort.enviarCorreoNotificacionLayout(email, titulo, params, null, "",template);

	}

	@Override
	public void enviarResetPassword(String email,String url) {

		Map<String, Object> params = new HashMap<>();
		params.put("${password-reset-url}", url);

		correosPort.enviarResetPassword(email, params);

	}

	@Override
	public void enviarCorreoRetrasoSolicitudes(String email, int idSolicitud,String nombreUsuario,int dias) {

		Map<String, Object> params = new HashMap<>();
							
		params.put("${usuario}", nombreUsuario);
		params.put("${num-solicitud}", idSolicitud);
		params.put("${days}", dias);

		correosPort.enviarCorreoRetrasoSolicitudes(email,  "Request attention delay " + idSolicitud + " in application FamiliasUnidas", params);

	}

	@Override
	public void enviarCorreoMovimiento(String email,String usuario,BigDecimal monto,String tipo) {
		
		Map<String, Object> params = new HashMap<>();
							
		params.put("${usuario}", usuario);
		params.put("${monto}", monto);
		params.put("${tipo}", tipo);

		correosPort.enviarCorreoMovimiento(email, params);
	}

}
