package com.cargosyabonos.application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.in.SolicitudVocUseCase;
import com.cargosyabonos.application.port.out.CitaPort;
import com.cargosyabonos.application.port.out.EstatusPagoPort;
import com.cargosyabonos.application.port.out.EstatusSolicitudPort;
import com.cargosyabonos.application.port.out.EventoSolicitudVocPort;
import com.cargosyabonos.application.port.out.SolicitudVocPort;
import com.cargosyabonos.application.port.out.TipoSolicitudPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.CitaEntity;
import com.cargosyabonos.domain.EstatusPagoEntity;
import com.cargosyabonos.domain.EstatusSolicitudEntity;
import com.cargosyabonos.domain.NumeroCasosSolicitudes;
import com.cargosyabonos.domain.SolicitudVoc;
import com.cargosyabonos.domain.SolicitudVocEntity;
import com.cargosyabonos.domain.TipoSolicitudEntity;
import com.cargosyabonos.domain.UltimoUsuarioRevisor;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
@PropertySource(value = "classpath:configuraciones-global.properties")
public class SolicitudVocService implements SolicitudVocUseCase {

	Logger log = LoggerFactory.getLogger(SolicitudVocService.class);

	@Value("${ambiente}")
	private String ambiente;

	@Autowired
	private SolicitudVocPort reqPort;

	@Autowired
	private EstatusSolicitudPort esPort;

	@Autowired
	private EstatusPagoPort epPort;

	@Autowired
	private EventoSolicitudVocPort evPort;

	@Autowired
	private UsuariosPort usPort;

	@Autowired
	private CorreoElectronicoUseCase correoUs;

	@Autowired
	private TipoSolicitudPort tsPort;

	@Autowired
	private CitaPort citaPort;

	@Override
	public List<SolicitudVoc> obtenerSolicitudesFiltroV2(int idUsuario, String campo, String valor, String fecha1,
			String fecha2) {
		List<SolicitudVoc> salida = new ArrayList<>();
		return salida;
	}

	@Override
	public List<SolicitudVoc> obtenerSolicitudes(int idUsuario, int estatus, int idSolicitud, String fechai,
			String fechaf, String ordenarPor, String orden, String campo, String valor, boolean myFiles,
			String cerradas) {

		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		List<SolicitudVoc> salida = new ArrayList<>();

		if (us.getRol().equals("6")) {
			salida = reqPort.obtenerTodasLasSolicitudesV2(us, estatus, idSolicitud, fechai, fechaf, ordenarPor, orden,
					campo, valor, myFiles, cerradas);
		} else if (us.getRol().equals("10")) {
			salida = reqPort.obtenerTodasLasSolicitudesV2(us, estatus, idSolicitud, fechai, fechaf, ordenarPor, orden,
					campo, valor, myFiles, cerradas);
		}

		return salida;

	}

	@Override
	public List<SolicitudVoc> obtenerSolicitudesActivasTerapeuta(int idUsuario) {
		List<SolicitudVoc> salida = new ArrayList<>();
		List<SolicitudVocEntity> list = null;
		List<UsuarioEntity> listUs = usPort.obtenerUsuarios();
		list = reqPort.obtenerSolicitudesActivasTerapueta(idUsuario);

		SolicitudVoc solSalida = null;
		if (list != null) {

			for (SolicitudVocEntity s : list) {

				solSalida = convertirASoloSolicitud(s, listUs);
				solSalida.setCliente(s.getNombreClienteCompleto());
				salida.add(solSalida);

			}
		}
		return salida;
	}

	@Override
	public SolicitudVocEntity obtenerSolicitud(int idRequest) {
		return reqPort.obtenerSolicitud(idRequest);
	}

	@Override
	public SolicitudVoc obtenerSolicitudObj(int idRequest, int idUsuario) {

		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		List<SolicitudVoc> salida = new ArrayList<>();
		SolicitudVoc solicitud = null;

		salida = reqPort.obtenerTodasLasSolicitudesV2(us, 0, idRequest, "", "", "", "", "", "", false, "OPEN");
		if (!salida.isEmpty()) {

			solicitud = salida.get(0);

		}

		return solicitud;
	}

	@Override
	public void crearSolicitud(SolicitudVoc r, int idUsuario) {

		UltimoUsuarioRevisor idUsuarioRevisando = reqPort.obtenerUsuarioConMenosRevisiones(6);
		UtilidadesAdapter.pintarLog("idUsuarioRevisando:" + idUsuarioRevisando.getUsuario());

		r.setIdEstatusPago(1);
		r.setIdEstatusSolicitud(1);
		r.setTelefono(r.getTelefono().replaceAll("\\D+", ""));
		r.setUsuarioRevisando(idUsuarioRevisando.getUsuario());

		SolicitudVocEntity s = reqPort.crearSolicitud(convertirARequestEntity(r), idUsuario, true);

		evPort.ingresarEventoDeSolicitud("Info", "File voc creation", "Info", "", s);

	}

	@Override
	public void actualizarSolicitud(SolicitudVoc r) {

		UtilidadesAdapter.pintarLog("r:" + r.getIdSolicitud());
		r.setTelefono(r.getTelefono().replaceAll("\\D+", ""));

		SolicitudVocEntity s = reqPort.obtenerSolicitud(r.getIdSolicitud());
		if (s.getNumSesiones() == 0) {
			r.setSesionesPendientes(r.getNumSesiones());
		} else {
			int spend = r.getNumSesiones() - s.getNumSchedules();
			UtilidadesAdapter.pintarLog("spend:" + spend);
			reqPort.actualizarNumSesiones(s.getNumSchedules(), spend, r.getIdSolicitud());
			r.setSesionesPendientes(spend);
		}

		reqPort.actualizarSolicitud(convertirARequestEntity(r));
	}

	@Override
	public void eliminarSolicitud(int r) {
		SolicitudVocEntity re = reqPort.obtenerSolicitud(r);
		reqPort.eliminarSolicitud(re);
	}

	private SolicitudVocEntity convertirARequestEntity(SolicitudVoc r) {

		SolicitudVocEntity s = new SolicitudVocEntity();

		s.setIdSolicitud(r.getIdSolicitud());
		s.setFechaInicio(r.getFechaInicio());
		s.setCliente(r.getCliente());
		s.setTelefono(r.getTelefono());
		s.setEmail(r.getEmail());
		s.setDocusign(r.getDocusign());
		s.setAbogado(r.getAbogado());
		s.setEmail_abogado(r.getEmail_abogado());
		s.setNumeroDeCaso(r.getNumeroDeCaso());
		s.setFirmaAbogados(r.getFirmaAbogados());
		TipoSolicitudEntity ts = tsPort.obtenerTipoSolicitudPorId(r.getIdTipoSolicitud());
		s.setTipoSolicitud(ts);
		EstatusSolicitudEntity es = esPort.obtenerEstatusSolicitudPorId(r.getIdEstatusSolicitud());
		s.setEstatusSolicitud(es);
		EstatusPagoEntity ep = epPort.obtenerEstatusPagoPorId(r.getIdEstatusPago());
		s.setEstatusPago(ep);
		s.setFechaNacimiento(r.getFechaNacimiento());
		s.setAdicional(r.getAdicional());
		s.setIdioma(r.getIdioma());
		s.setDireccion(r.getDireccion());
		s.setEstado(r.getEstado());
		s.setApellidos(r.getApellidos());
		s.setParalegalName(r.getParalegalName());
		s.setParalegalEmails(r.getParalegalEmails());
		s.setParalegalTelefonos(r.getParalegalTelefonos());
		s.setNumSesiones(r.getNumSesiones());
		s.setNumSchedules(r.getNumSchedules());
		s.setSesionesPendientes(r.getSesionesPendientes());
		s.setDocumento1(r.isDocumento1());
		s.setFechaDoc1(r.getFechaDoc1());
		s.setDocumento2(r.isDocumento2());
		s.setFechaDoc2(r.getFechaDoc2());
		s.setTerapeuta(r.getTerapeuta());
		s.setUsuarioRevisando(r.getUsuarioRevisando());
		s.setCode(r.getCode());
		s.setSexo(r.getSexo());
		s.setPurposeTreatament(r.getPurposeTreatament());
		s.setParticipation(r.getParticipation());
		s.setRecommendations(r.getRecommendations());
		s.setNombreDelPadre(r.getNombreDelPadre());
		s.setConsejera(r.getConsejera());
		s.setNombreEscuela(r.getNombreEscuela());
		s.setLenguajePreferente(r.getLenguajePreferente());

		return s;
	}

	@Override
	public void actualizarEstatusSolicitud(int idSolicitud, int idEstatus, int idUsuario, boolean closed) {

		UtilidadesAdapter.pintarLog("Closed:" + closed);
		boolean actualizar = true;

		EstatusSolicitudEntity es = esPort.obtenerEstatusSolicitudPorId(idEstatus);
		UsuarioEntity u = usPort.buscarPorId(idUsuario);
		SolicitudVocEntity s = reqPort.obtenerSolicitud(idSolicitud);

		if (actualizar) {
			evPort.ingresarEventoDeSolicitud("Update status", "Change to status " + es.getDescripcion(), "Info",
					u.getUsuario(), s);
			reqPort.actualizarEstatusSolicitud(idSolicitud, idEstatus);
		}

		if (idEstatus == 14) {
			reqPort.actualizarUsuarioTerapeuta(0, idSolicitud);
		}

	}

	public void reasginar(int idUsuario, String motivo, int idSolicitud, int idUsuarioEnvio) {

		// UsuarioEntity usEnvio = usPort.buscarPorId(idUsuarioEnvio);
		// SolicitudVocEntity s = reqPort.obtenerSolicitud(idSolicitud);

		if (motivo != null) {
			// Si esta lleno el motivo es por que rechazo la asignacion
			if (!"".equals(motivo)) {

			}

		} else {

			UsuarioEntity us = usPort.buscarPorId(idUsuario);
			Date licenciaValida = null;
			Date fechaActual = null;
			try {
				licenciaValida = UtilidadesAdapter.cadenaAFecha(us.getLicenciaValida());
				fechaActual = UtilidadesAdapter.fechaActualDate();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if(fechaActual !=  null){
				UtilidadesAdapter.pintarLog("licenciaValida?" + fechaActual.after(licenciaValida));
				if (fechaActual.after(licenciaValida)) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"The therapist " + us.getNombre() + " already has his license expired");
				} else {
					reqPort.actualizarUsuarioTerapeuta(idUsuarioEnvio, idSolicitud);
				}
			}else{
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"There was a problem while reassigning. Please contact the administrator");
			}
		}

	}

	public int ageSolicitud(String fi, String ff) {
		int df = UtilidadesAdapter.diferenciaDias(fi, ff);
		// UtilidadesAdapter.pintarLog("df:"+df);
		if (df < 0) {
			df = 0;
		}
		return df;
	}

	private SolicitudVoc convertirASoloSolicitud(SolicitudVocEntity r, List<UsuarioEntity> listUs) {

		SolicitudVoc s = new SolicitudVoc();
		s.setIdSolicitud(r.getIdSolicitud());
		s.setFechaInicio(r.getFechaInicio());
		s.setCliente(r.getCliente());
		s.setTelefono(r.getTelefono());
		s.setEmail(r.getEmail());
		s.setDocusign(r.getDocusign());
		s.setAbogado(r.getAbogado());
		s.setEmail_abogado(r.getEmail_abogado());
		s.setNumeroDeCaso(r.getNumeroDeCaso());
		s.setFirmaAbogados(r.getFirmaAbogados());
		s.setIdTipoSolicitud(r.getTipoSolicitud().getIdTipoSolicitud());
		s.setIdEstatusPago(r.getEstatusPago().getIdEstatusPago());
		s.setIdEstatusSolicitud(r.getEstatusSolicitud().getIdEstatusSolicitud());
		s.setTipoSolicitud(r.getTipoSolicitud().getNombre());
		s.setEstatusPago(r.getEstatusPago().getDescripcion());
		s.setEstatusSolicitud(r.getEstatusSolicitud().getDescripcion());
		s.setFechaNacimiento(r.getFechaNacimiento());
		s.setAdicional(r.getAdicional());
		s.setIdioma(r.getIdioma());
		s.setDireccion(r.getDireccion());
		s.setEstado(r.getEstado());
		s.setApellidos(r.getApellidos());
		s.setParalegalName(r.getParalegalName());
		s.setParalegalEmails(r.getParalegalEmails());
		s.setParalegalTelefonos(r.getParalegalTelefonos());
		s.setNumSesiones(r.getNumSesiones());
		s.setNumSchedules(r.getNumSchedules());
		s.setSesionesPendientes(r.getSesionesPendientes());
		s.setDocumento1(r.isDocumento1());
		s.setFechaDoc1(r.getFechaDoc1());
		s.setDocumento2(r.isDocumento2());
		s.setFechaDoc2(r.getFechaDoc2());
		s.setTerapeuta(r.getTerapeuta());
		s.setUsuarioRevisando(r.getUsuarioRevisando());
		s.setCode(r.getCode());
		s.setSexo(r.getSexo());
		s.setPurposeTreatament(r.getPurposeTreatament());
		s.setParticipation(r.getParticipation());
		s.setRecommendations(r.getRecommendations());

		return s;
	}

	@Override
	public void actualizarUsuarioTerapeuta(int idUsuario, int idSolicitud, int idUsuarioEntrada) {

		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		Date licenciaValida = null;
		Date fechaActual = null;
		try {
			licenciaValida = UtilidadesAdapter.cadenaAFecha(us.getLicenciaValida());
			fechaActual = UtilidadesAdapter.fechaActualDate();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if(fechaActual !=  null){
			UtilidadesAdapter.pintarLog("licenciaValida?" + fechaActual.after(licenciaValida));
			if (fechaActual.after(licenciaValida)) {
	
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"The therapist " + us.getNombre() + " already has his license expired");
	
			} else {
	
				UsuarioEntity usE = usPort.buscarPorId(idUsuarioEntrada);
				SolicitudVocEntity s = reqPort.obtenerSolicitud(idSolicitud);
				evPort.ingresarEventoDeSolicitud("Therapist assignment", "Therapist " + us.getNombre() + " was assigned",
						"Info", usE.getUsuario(), s);
				reqPort.actualizarUsuarioTerapeuta(idUsuario, idSolicitud);
				reqPort.actualizarEstatusSolicitud(idSolicitud, 14);
				correoUs.enviarCorreoNotificacionVoc(usE.getNombre(), "VOC file assignment in FamiliasUnidadas",
						"File " + s.getIdSolicitud() + " was assigned in the joint families system, with a total of "
								+ s.getNumSchedules() + " appointments.",
						usE.getCorreoElectronico());
	
			}
		}else{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"There was a problem while reassigning. Please contact the administrator");
		}
	}

	public int agregarPago(Row row, int idSolicitud, int idUsuario, StringBuilder sb, Workbook wb) {

		int cellInicial = 29;
		int s1 = generarPago(row, cellInicial, idSolicitud, idUsuario, sb, wb);
		cellInicial = cellInicial + 5;
		int s2 = generarPago(row, cellInicial, idSolicitud, idUsuario, sb, wb);
		cellInicial = cellInicial + 5;
		int s3 = generarPago(row, cellInicial, idSolicitud, idUsuario, sb, wb);
		cellInicial = cellInicial + 5;
		int s4 = generarPago(row, cellInicial, idSolicitud, idUsuario, sb, wb);
		cellInicial = cellInicial + 5;
		int s5 = generarPago(row, cellInicial, idSolicitud, idUsuario, sb, wb);

		int totalSesiones = s1 + s2 + s3 + s4 + s5;

		pintarLog("Total sesiones:" + totalSesiones, sb);
		
		return totalSesiones;

	}

	private int generarPago(Row row, int cellInicial, int idSolicitud, int idUsuario, StringBuilder sb, Workbook wb) {

		int sesion = 0;
		pintarLog("Cell inicial:" + cellInicial, sb);
		FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
		String fechaCitaP = "";
		String fechaCita = "";
		String fechaCita2 = "";
		String fechaPago = "";
		String pagoString = "";

		if (row.getCell(cellInicial) != null) {

			DataFormatter dataFormatter = new DataFormatter(Locale.US);
			fechaCita = dataFormatter.formatCellValue(row.getCell(cellInicial), formulaEvaluator);
			fechaCitaP = fechaCita;
			pintarLog("Fecha citai:" + fechaCita, sb);

			if (fechaValida(fechaCitaP)) {

				fechaCita = UtilidadesAdapter.formatearFechaStringAtipoMX(fechaCita);
				pintarLog("Fecha cita:" + fechaCita, sb);

				if (!"1899-12-31".equals(fechaCita)) {

					cellInicial = cellInicial + 1;
					pintarLog("Fecha inicial:" + cellInicial + " -"+row.getCell(cellInicial), sb);
					if (row.getCell(cellInicial) != null) {
						fechaCita2 = valorCell2(row, cellInicial, wb);
						pintarLog("Fecha cita2:" + fechaCita2, sb);
						if (fechaValida(fechaCita2)) {
							fechaCita2 = UtilidadesAdapter.formatearFechaStringAtipoMX(fechaCita2);
							pintarLog("Fecha cita2:" + fechaCita2, sb);
						}
					}

					cellInicial = cellInicial + 1;
					if (row.getCell(cellInicial) != null) {

						fechaPago = dataFormatter.formatCellValue(row.getCell(cellInicial), formulaEvaluator);

						if (fechaPago.length() >= 9) {

							fechaPago = UtilidadesAdapter.formatearFechaStringAtipoMX(fechaPago);
							pintarLog("Fecha Pago:" + fechaPago, sb);

							if ("1899-12-31".equals(fechaPago)) {

								fechaPago = "";

							} else {
								if (!"".equals(fechaPago)) {
									cellInicial = cellInicial + 1;
									if (row.getCell(cellInicial) != null) {
										pagoString = valorCell(row, cellInicial);
										pintarLog("Pago:" + pagoString, sb);
									}
								}
							}
						}
					}

					if (!"".equals(fechaCita)) {
						pintarLog("Insertar cita 1:" + fechaCita, sb);
						generarCita(pagoString, fechaCita, fechaPago, idUsuario, idSolicitud);
						sesion = 1;
					}

					if (!"".equals(fechaCita2)) {
						pintarLog("Insertar cita 2:" + fechaCita2, sb);
						generarCita(pagoString, fechaCita2, fechaPago, idUsuario, idSolicitud);
						sesion = 2;
					}

				}

			}
		}
		return sesion;
	}

	private String valorCell(Row row, int cell) {
		
		String salida = "";
		if (row.getCell(cell).getCellType() == CellType.BLANK)
			salida = row.getCell(cell).getStringCellValue();
		if (row.getCell(cell).getCellType() == CellType.NUMERIC)
			salida = row.getCell(cell).getNumericCellValue() + "";
		if (row.getCell(cell).getCellType() == CellType.STRING)
			salida = row.getCell(cell).getStringCellValue();
		if (row.getCell(cell).getCellType() == CellType.FORMULA)
			salida = row.getCell(cell).getStringCellValue();

		return salida;
	}

	private String valorCell2(Row row, int cell, Workbook wb) {
		String salida = "";
		FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
		DataFormatter dataFormatter = new DataFormatter(Locale.US);
		salida = dataFormatter.formatCellValue(row.getCell(cell), formulaEvaluator);
		return salida;
	}

	private CitaEntity generarCita(String amount, String fechaCita, String fechaPago, int idUsuario, int idSolicitud) {
		CitaEntity ce = new CitaEntity();
		if ("".equals(amount)) {
			ce.setAmount(BigDecimal.ZERO);
		} else {
			ce.setAmount(new BigDecimal(amount));
		}
		ce.setAmount(new BigDecimal("100.0"));
		ce.setFecha(fechaCita);
		ce.setFechaPagado(fechaPago);
		ce.setHora("");
		ce.setTipo("");
		ce.setIdUsuario(idUsuario);
		ce.setIdSolicitud(idSolicitud);
		ce.setPagado(fechaPago.equals("") ? false : true);
		ce.setDosCitas(false);
		citaPort.crearCita(ce);
		return ce;
	}

	public void pintarLog(String mensaje, StringBuilder sb) {
		if (ambiente.equals("local")) {
			// if(false){
			// UtilidadesAdapter.pintarLog(mensaje);
			sb.append(mensaje + "\n");
		}

	}

	@Override
	public SolicitudVocEntity crearSolicitudMasivo(SolicitudVoc r, int usuario) {
		if (!"".equals(r.getTelefono())) {
			r.setTelefono(r.getTelefono().replaceAll("\\D+", ""));
		}
		return reqPort.crearSolicitud(convertirARequestEntity(r), usuario, false);
	}

	private boolean fechaValida(String fechaCita) {
		boolean salida = true;
		if (fechaCita.length() < 8) {
			salida = false;
		}
		if (fechaCita.matches(".*[a-z].*")) {
			salida = false;
		}
		if (fechaCita.matches(".*[A-Z].*")) {
			salida = false;
		}
		if (fechaCita.contains("&")) {
			salida = false;
		}
		if (fechaCita.contains(",")) {
			salida = false;
		}

		return salida;
	}

	public static void main(String args[]) {
		SolicitudVocService o = new SolicitudVocService();
		UtilidadesAdapter.pintarLog(o.fechaValida("12/3/2025s")+"");
	}

	@Override
	public List<NumeroCasosSolicitudes> obtenerNumerosCaso(String numeroCaso) {
		return reqPort.obtenerNumerosCaso(numeroCaso);
	}

	@Override
	public void syncNumSesiones(int idSolicitud, int idUsuario) {
		SolicitudVocEntity s = reqPort.obtenerSolicitud(idSolicitud);
		int numSesiones = reqPort.obtenerSesionesDeSolicitud(idSolicitud);
		int spend = s.getNumSesiones() - numSesiones;
		reqPort.actualizarNumSesiones(numSesiones, spend, idSolicitud);
	}

}
