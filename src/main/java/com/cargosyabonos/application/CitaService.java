package com.cargosyabonos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.CitaUseCase;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.out.CitaPort;
import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.cargosyabonos.application.port.out.DisponibilidadUsuarioPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.EventoSolicitudVocPort;
import com.cargosyabonos.application.port.out.SolicitudVocPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.CargosCitasVoc;
import com.cargosyabonos.domain.Cita;
import com.cargosyabonos.domain.CitaEntity;
import com.cargosyabonos.domain.CitaSql;
import com.cargosyabonos.domain.ConfiguracionEntity;
import com.cargosyabonos.domain.EventoSolicitudEntity;
import com.cargosyabonos.domain.SolicitudVocEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class CitaService implements CitaUseCase {

	private static final Logger logger = LoggerFactory.getLogger(CitaService.class);

	@Autowired
	private CitaPort citaPort;
	
	@Autowired
	private CitaUseCase citaUseCase;
	
	@Autowired
	private SolicitudVocPort solPort;
	
	@Autowired
	private UsuariosPort usPort;
	
	@Autowired
	private EventoSolicitudPort evPort;
	
	@Autowired
	private EventoSolicitudVocPort evVocPort;
	
	@Autowired
	private DisponibilidadUsuarioPort dispPort;
	
	@Autowired
	private CorreoElectronicoUseCase correoUs;
	
	@Autowired
	private ConfiguracionPort confPort;
	
	@Override
	public List<Cita> obtenerTodasLasCitasSemana(String fecha){
		List<CitaEntity> se =  citaPort.obtenerTodasLasCitasSemana(fecha,0);
		List<Cita> sce = new ArrayList<>();
		for (CitaEntity ce : se) {
			Cita c = convertirACita(ce);
			sce.add(c);
		}
		return sce;
	}
	
	@Override
	public List<Cita> obtenerTodasCitasPorFecha(String fecha) {
		List<CitaEntity> se =  citaPort.obtenerTodasCitasPorFecha(fecha);
		List<Cita> sce = new ArrayList<>();
		for (CitaEntity ce : se) {
			Cita c = convertirACita(ce);
			sce.add(c);
		}
		return sce;
	}

	@Override
	public List<Cita> obtenerCitasPorSolicitud(int idSolicitud) {
		
		List<CitaSql> cSql = citaPort.obtenerCitasPorSolicitud(idSolicitud);
		List<Cita> sce = new ArrayList<>();
		for (CitaSql ce : cSql) {
			Cita c = convertirCitaSqlACita(ce);
			sce.add(c);
		}
		return sce;
	}
	
	private Cita convertirCitaSqlACita(CitaSql c){

		Cita cs = new Cita();
		cs.setIdCita(c.getidCita());
		cs.setComentario(c.getcomentario());
		cs.setFecha(c.getfecha());
		cs.setHora(c.gethora());
		cs.setTipo(c.gettipo());
		cs.setDosCitas(c.getdosCitas());
		//ds = c.getdosCitas() == null ? "0" : c.getdosCitas();
		//cs.setDosCitas(ds.equals("0") ? false : true);
		cs.setIdSolicitud(c.getidSolicitud());
		cs.setNoShow(c.getnoShow());
		cs.setIdUsuario(c.getidUsuario());
		cs.setAmount(c.getamount());
		cs.setPagado(c.getpagado());
		cs.setNombreUsuario(c.getnombreUsuario());
		cs.setFechaPagado(c.getfechaPagado());
		cs.setColor(c.getcolor());
		cs.setDescEst(c.getdescEst());
		String tns = c.gettieneNota() == null ? "0" : c.gettieneNota();
		cs.setTieneNota(tns.equals("0") ? false : true);
		cs.setCaseNumber(c.getcasenumber());
		cs.setCliente(c.getcliente());
		cs.setNombreUsuario(c.getnombreUsuario());
		cs.setImportante("");
		cs.setFechaCreacion(c.getfechaCreacion() != null ? c.getfechaCreacion().substring(0, 10) : "");
		
		return cs;
	}
	

	@Override
	public List<Cita> obtenerCitasDeUsuarioPorFecha(int idUsuario, String fecha) {
		List<CitaEntity> se =  citaPort.obtenerCitasDeUsuarioPorFecha(idUsuario, fecha);
		List<Cita> sce = new ArrayList<>();
		for (CitaEntity ce : se) {
			Cita c = convertirACita(ce);
			sce.add(c);
		}
		return sce;
	}
	

	@Override
	public List<Cita> obtenerCitasDeUsuarioPorSemana(int idUsuario, String fecha,String filtro,boolean disponibilidad,
		String idRol, String estatusCita,String estado) {
		
		UsuarioEntity ue = usPort.buscarPorId(idUsuario);
		List<Cita> sce = new ArrayList<>();
		
		if (disponibilidad) {

			if (ue.getRol().equals("2") || ue.getRol().equals("3") || ue.getRol().equals("4")) {
				if(idRol != null && !idRol.equals("")){
					if(idRol.equals("0")){
						idRol = "5,8,11";
					}	
				}
				if (!"".equals(filtro)) {
					sce = dispPort.obtenerDisponibilidadesTodosUsuarios(fecha, Integer.valueOf(filtro),idRol,estado);
				} else {
					sce = dispPort.obtenerDisponibilidadesTodosUsuarios(fecha, 0,idRol,estado);
				}
			} else if (ue.getRol().equals("5") || ue.getRol().equals("8") || ue.getRol().equals("11")) {
				sce = dispPort.obtenerDisponibilidadesTodosUsuarios(fecha, idUsuario,idRol,estado);
			}

		}else{
		
			if(ue.getRol().equals("2")||ue.getRol().equals("3")|| ue.getRol().equals("4")){
				if(!"".equals(filtro)){
					sce =  evPort.obtenerCitasInterviewer(fecha, Integer.valueOf(filtro),estatusCita, estado);
				}else{
					sce =  evPort.obtenerCitasInterviewer(fecha,0,estatusCita, estado);
				}
				
			}else if(ue.getRol().equals("5")||ue.getRol().equals("8")|| ue.getRol().equals("11")){
				sce =  evPort.obtenerCitasInterviewer(fecha, idUsuario,estatusCita, estado);
			}else{
				List<Cita> se =  citaUseCase.obtenerCitasDeUsuarioPorSemana(fecha, idUsuario, 0);
				sce.addAll(se);
			}
		}
		return sce;
	}

	@Override
	public void crearCita(CitaEntity a) {
		
		UsuarioEntity u = usPort.buscarPorId(a.getIdUsuario());
		if (!u.getRol().equals("10")) {
			SolicitudVocEntity v = solPort.obtenerSolicitud(a.getIdSolicitud());
			logger.info("Terapeuta:" + v.getTerapeuta());
			if (v.getTerapeuta() != 0) {
				a.setIdUsuario(v.getTerapeuta());
			} else {
				List<Cita> lc = obtenerCitasPorSolicitud(a.getIdSolicitud());
				if (!lc.isEmpty()) {
					a.setIdUsuario(lc.get(0).getIdUsuario());
				}
			}

			evVocPort.ingresarEventoDeSolicitud("Add schedule",
					"The user " + u.getNombre() + " add new schedule in date " + a.getFecha(), "Info", u.getNombre(),
					v);
		}

		BigDecimal amount = BigDecimal.ZERO;
		String rate = u.getRate() == null ? "" : u.getRate();
		if ("".equals(rate)) {
			ConfiguracionEntity confj = confPort.obtenerConfiguracionPorCodigo("THER-AMOUNT-DEF");
			logger.info("amount default terapeuta" + confj.getValor());
			amount = new BigDecimal(confj.getValor());
		} else {
			amount = new BigDecimal(u.getRate());
		}
		
		SolicitudVocEntity s = solPort.obtenerSolicitud(a.getIdSolicitud());
		int nsesions = 0;
		if (a.isDosCitas()) {
			a.setAmount(amount.multiply(new BigDecimal(2.0)));
			nsesions = s.getNumSchedules() + 2;
		} else {
			a.setAmount(amount);
			nsesions = s.getNumSchedules() + 1;
		}

		int spend = s.getNumSesiones() - nsesions;
		logger.info("nsesions:" + nsesions + "|spend:" + spend);

		solPort.actualizarNumSesiones(nsesions, spend, a.getIdSolicitud());
		citaPort.crearCita(a);
		// Enviar notificacion al usuario
		correoUs.enviarCorreoCitaVoc(s.getCliente(), a.getFecha(), a.getHora(), a.getTipo(), s.getEmail(),
				s.getIdSolicitud());

	}

	@Override
	public void actualizarCita(CitaEntity a) {
		citaPort.actualizarCita(a);
	}

	@Override
	public void eliminarCita(int idCita,int idUsuario) {
		
		UsuarioEntity u = usPort.buscarPorId(idUsuario);
		CitaEntity c = citaPort.obtenerCita(idCita);
		
		SolicitudVocEntity s = solPort.obtenerSolicitud(c.getIdSolicitud());
		evVocPort.ingresarEventoDeSolicitud("Info", "Schedule " + idCita + " with the date "+c.getFecha()+"("+c.getHora()+c.getTipo()+") was deleted", "Info", u.getNombre(), s);
		
		int nsesions = 0;
		int nsesionss = 0;
		if(c.isDosCitas()){
			nsesions = s.getNumSchedules()-2;
			nsesionss = 2;
		}else{
			nsesions = s.getNumSchedules()-1;
			nsesionss = 1;
		}	
		int spend = s.getSesionesPendientes() + nsesionss;
		solPort.actualizarNumSesiones(nsesions, spend,s.getIdSolicitud());
		
		
		citaPort.eliminarCita(c);
	}

	@Override
	public void actualizarNoShow(int idCita,int idUsuario,String motivo) {
		CitaEntity c = citaPort.obtenerCita(idCita);
		SolicitudVocEntity s = solPort.obtenerSolicitud(c.getIdSolicitud());
		UsuarioEntity u = usPort.buscarPorId(idUsuario);
		int nsesions = 0;
		int nsesionss = 0;
		if(c.isDosCitas()){
			nsesions = s.getNumSchedules()-2;
			nsesionss = 2;
		}else{
			nsesions = s.getNumSchedules()-1;
			nsesionss = 1;
		}	
		int spend = s.getSesionesPendientes() + nsesionss;
		solPort.actualizarNumSesiones(nsesions, spend,s.getIdSolicitud());
		citaPort.actualizarNoShow(true,idCita);
		//Agregar motivo no show a evento voc
		evVocPort.ingresarEventoDeSolicitud("Update", "Schedule ("+c.getFecha()+") no show - reason: "+motivo, "Info", u.getUsuario(), s);
	}

	@Override
	public List<CargosCitasVoc> obtenerCargosPendientes(String fechai, String fechaf, int idUsuario, String campo, String valor,
			String tipo) {

		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		List<CargosCitasVoc> lista = null;
			lista = citaPort.obtenerCargosPendientesFiltro( fechai,  fechaf,  idUsuario,  campo,  valor,
					 tipo,  us.getRol());
		return lista;
		
	}

	private Cita convertirACita(CitaEntity ce){
		Cita c = new Cita();
		c.setAmount(ce.getAmount());
		c.setComentario(ce.getComentario());
		c.setDosCitas(ce.isDosCitas());
		c.setFecha(ce.getFecha());
		c.setHora(ce.getHora());
		c.setIdCita(ce.getIdCita());
		c.setIdSolicitud(ce.getIdSolicitud());
		c.setIdUsuario(ce.getIdUsuario());
		UsuarioEntity u = usPort.buscarPorId(ce.getIdUsuario());
		if(u != null){
			c.setNombreUsuario(u.getNombre());
			c.setColor(u.getColor());
		}
		c.setNoShow(ce.isDosCitas());
		c.setPagado(ce.isPagado());
		c.setTipo(ce.getTipo());
		c.setFechaPagado(ce.getFechaPagado());
		SolicitudVocEntity s = solPort.obtenerSolicitud(ce.getIdSolicitud());
		c.setCaseNumber(s.getNumeroDeCaso());
		c.setImportante("");
		
		return c;
	}

	@Override
	public void actualizarPagado(boolean pagado, int idCita,int idUsuario) {
		String fp = "";
		if(pagado){
			fp = UtilidadesAdapter.fechaActual();
		}else{
			UsuarioEntity u = usPort.buscarPorId(idUsuario);
			CitaEntity c = citaPort.obtenerCita(idCita);
			SolicitudVocEntity se = solPort.obtenerSolicitud(c.getIdSolicitud());
			evVocPort.ingresarEventoDeSolicitud("Update", "The payment for appointment "+idCita+" to file VOC "+se.getIdSolicitud()+" was cancelled", "Info", u.getUsuario(), se);
		}
		citaPort.actualizarPagado(pagado, fp,idCita);
	}

	@Override
	public List<Cita> obtenerCitasDeUsuarioPorSemana(String fecha, int idUsuario, int noShow) {
		List<CitaSql> cSql = citaPort.obtenerCitasDeUsuarioPorSemana(fecha, idUsuario, noShow);
		List<Cita> sce = new ArrayList<>();
		for (CitaSql ce : cSql) {
			Cita c = convertirCitaSqlACita(ce);
			sce.add(c);
		}
		return sce;
	}

	@Override
	public boolean envioRecordatorio(int idEvento){

		EventoSolicitudEntity e = evPort.findByIdEvento(idEvento);

		if(e != null){
			UsuarioEntity u = usPort.buscarPorId(Integer.valueOf(e.getUsuarioSchedule()));
			correoUs.enviarCorreoCita(u.getNombre(), UtilidadesAdapter.formatearFecha(e.getFechaSchedule()), e.getHoraSchedule(), e.getTipoSchedule(), u.getCorreoElectronico(), e.getSolicitud().getIdSolicitud(), "US");
		}

		return true;
	}

	@Override
	public boolean envioRecordatorios(String fecha,int idUsuario){

		correoUs.enviarCorreoRecordatoriosCitas(fecha, idUsuario, "US");

		return true;
	}
	

}
