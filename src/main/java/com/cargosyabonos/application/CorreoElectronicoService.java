package com.cargosyabonos.application;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.ApiException;
import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.cargosyabonos.application.port.out.EnviarCorreoPort;
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

	Logger log = LoggerFactory.getLogger(CorreoElectronicoService.class);
	
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
	private EnviarCorreoPort envCorrPort;
	
	@Autowired
	private EventoSolicitudPort evPort;
	
	@Autowired
	private MsgPort msgPort;
	
	@Autowired
	private ConfiguracionPort confPort;

	@Override
	public void enviarCorreoRecuperacion(String usuario) {

		UsuarioEntity us = usPort.buscarPorUsuario(usuario);
		if (us != null) {

			Map<String, Object> params = new HashMap<>();
			params.put("${usuario}", us.getNombre());

			envCorrPort.enviarCorreoRecuperacion(us.getCorreoElectronico(), params);

		} else {

			throw new ApiException(500, "No se econtro tu usuario");

		}
	}

	@Override
	public void enviarCorreoMovimiento(int idCliente) {

			Map<String, Object> params = new HashMap<>();
			params.put("${usuario}", "nombre");
			params.put("${saldo}", new BigDecimal("5000"));
			params.put("${abono}", new BigDecimal("400"));
			envCorrPort.enviarCorreoMovimiento("email", params);

	}

	@Override
	public void enviarSaldoVencido(String usuario) {
			Map<String, Object> params = new HashMap<>();
			params.put("${usuario}", "nombre");
			params.put("${saldo}", new BigDecimal("5000"));
			envCorrPort.enviarCorreoSaldoVencido("mail", params);

	}

	@Override
	public void enviarCorreoNuevaSolicitudCliente(int idUsuario, SolicitudEntity solicitud,boolean isMensaje,boolean isMail) {
		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}",  solicitud.obtenerCliente());
		if(isMail){
			envCorrPort.enviarCorreoDeLayout(solicitud.getEmail(), "Welcome to FamiliasUnidas", params, "email-bienvenida");
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
			envCorrPort.enviarCorreoDeLayout(solicitud.getEmail_abogado(), "Appreciation for the Business Referral and Update on Client Evaluation", params, "email-notif-nueva-sol-a-abogado");
		}
		if (isMensaje) {
			msgPort.envioMensaje(solicitud.getTelefono(), "Dear "+solicitud.getAbogado()+", We hope this email finds you well. We wanted to extend our sincere gratitude for referring "
                            +solicitud.obtenerCliente()+" "+solicitud.getApellidosSinNull()+" to our psychological evaluation services. Your trust and confidence in our"
                            +"expertise are greatly appreciated.",
					solicitud, false);
		}
	}
	
	@Override
	public void enviarCorreoCita(String nombre,String fecha,String hora,String tipo,String email,int idSolicitud,String formatoFecha) {
		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}",  nombre);
		params.put("${fecha}", fecha);
		params.put("${hora}", hora);
		params.put("${tipo}", tipo);
		envCorrPort.enviarCorreoDeLayoutCalendarWithInvite(email, "Appointment with Familias Unidas for file "+idSolicitud, params, "email-cita");
	}

	@Override
	public void enviarCorreoRecordatoriosCitas(String fecha,int idUsuario,String formatoFecha){
		envCorrPort.enviarCorreoSincronizacionCitas("email-recordatorios-citas", fecha, idUsuario, formatoFecha);
	}

	@Override
	public void enviarCorreoCitaVoc(String nombre,String fecha,String hora,String tipo,String email,int idSolicitud) {
		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}",  nombre);
		params.put("${fecha}", fecha);
		params.put("${hora}", hora);
		params.put("${tipo}", tipo);
		envCorrPort.enviarCorreoDeLayoutCalendar(email, "Appointment with Familias Unidas for file VOC "+idSolicitud , params, "email-cita",null);
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
			envCorrPort.enviarCorreoDeLayout(solicitud.getEmail_abogado(), "Finalized document for the customer " + solicitud.getCliente() +" of service " + solicitud.getTipoSolicitud().getNombre(), params, "email-finalizo-doc-solicitud");
		}
	}
	
	@Override
	public void enviarCorreoAsignacion(SolicitudEntity solicitud,String nombre,String email) {
		Map<String, Object> params = new HashMap<>();
		params.put("${usuario}",  nombre);
		params.put("${solicitud}", solicitud.getIdSolicitud());
		params.put("${tipo}",  solicitud.getTipoSolicitud().getNombre());
		params.put("${host}",  obtenerHost());
		envCorrPort.enviarCorreoDeLayout(email, "Assignment in FAMILIAS UNIDAS system of file " + solicitud.getIdSolicitud(), params, "email-asignacion");
	}
	
	@Override
	public void enviarCorreoAMasterPorFinSolicitud(SolicitudEntity solicitud){
		UsuarioEntity usAmanda = usPort.buscarPorUsuario("amanda");
		if(usAmanda !=  null){
			Map<String, Object> params = new HashMap<>();
			params.put("${tipo}",  solicitud.getTipoSolicitud().getIdTipoSolicitud());
			params.put("${cliente}", solicitud.getNombreClienteCompleto());
			if(!"".equals(usAmanda.getCorreoElectronico())){
				envCorrPort.enviarCorreoDeLayout(usAmanda.getCorreoElectronico(), "Finalized document for the customer " + solicitud.getCliente() +" of service " + solicitud.getTipoSolicitud().getNombre(), params, "email-fin-solicitud");
			}
		}
	}

	@Override
	public void enviarCorreoNoShow(String emailRevisor,String usuario,SolicitudEntity solicitud,EventoSolicitudEntity e) {
		Map<String, Object> params = new HashMap<>();
		params.put("${usuario}", usuario);
		params.put("${cliente}",  solicitud.obtenerCliente());
		params.put("${solicitud}", solicitud.getIdSolicitud());
		System.out.println("ev:"+e);
		if(e != null){
			params.put("${fecha}", UtilidadesAdapter.formatearFecha(e.getFechaSchedule()) + " "+e.getHoraSchedule()+" "+e.getTipoSchedule());
			envCorrPort.enviarCorreoDeLayout(emailRevisor, "Client no show to interview at request "+solicitud.getIdSolicitud(), params, "email-no-show");
		}
	}

	@Override
	public void enviarCorreoSuicidio(SolicitudEntity solicitud,String email,String nombreUsuario) {
		Map<String, Object> params = new HashMap<>();
		params.put("${usuario}", nombreUsuario);
		params.put("${cliente}", solicitud.obtenerCliente());
		params.put("${solicitud}", solicitud.getIdSolicitud());
		envCorrPort.enviarCorreoDeLayout(email, "Customer with suicidal signs in request "+solicitud.getIdSolicitud(), params, "email-suicidio");
		
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
		envCorrPort.enviarCorreoDeLayout(email, "Reminder session in request "+solicitud.getIdSolicitud() +" with FamiliasUnidas", params, "email-recordatorio-cita");	
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
		envCorrPort.enviarCorreoDeLayout(email, "Reminder of appointments approved to be completed in file VOC "+params.get("${file-voc}")+" with FamiliasUnidas", params, "email-file-voc-ending-sessions");
	}
	
	@Override
	public void enviarCorreoPrimeraCita(String nombre,String fecha,String hora,String email,SolicitudEntity solicitud){
		Map<String, Object> params = new HashMap<>();
		params.put("${fecha}", fecha);
		params.put("${hora}", hora);
		params.put("${tipo}", solicitud.getTipoSolicitud().getNombre());
		params.put("${entrevistador}", nombre);
		envCorrPort.enviarCorreoDeLayout(email, "First interview for file ("+solicitud.getIdSolicitud() + ") "+solicitud.getTipoSolicitud().getNombre(), params, "email-primera-cita");
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
				//String monto = "";
				if (a.getdeuda() != null) {
					if (a.getdeuda().compareTo(BigDecimal.ZERO) != 0) {
						//monto = UtilidadesAdapter.currencyFormat(a.getdeuda());
						paramsP.put("${monto}", a.getdeuda());
						paramsP.put("${tipo-servicio}", solicitud.getTipoSolicitud().getNombre());
						paramsP.put("${solicitud}", solicitud.getIdSolicitud());
						if(isMail){
								envCorrPort.enviarCorreoSaldoVencido(a.getemail(), paramsP);
							
						}
						if(isMensaje){
							/*msgPort.envioMensaje(solicitud.getTelefono(), "You have an overdue balance of $"+a.getdeuda()+" USD for the "+solicitud.getTipoSolicitud().getNombre()
									+" Psychological evaluation with Familias Unidas. Please reach out to us to settle your balance. You can contact us at: 323-430-4200",
									solicitud, false);*/
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
			envCorrPort.enviarCorreoInvoice(email, params, solicitud.getIdSolicitud());
		}
	}

	@Override
	public void enviarCorreoNotificacionVoc(String nombreUsuario, String titulo, String cuerpo, String email) {
		Map<String, Object> params = new HashMap<>();
		params.put("${cliente}", nombreUsuario);
		params.put("${cuerpo}", cuerpo);
		envCorrPort.enviarCorreoNotificacion(email, titulo, params, null, "",false);
	}

	

}
