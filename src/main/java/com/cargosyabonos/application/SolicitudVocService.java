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
		s.setImportante(r.getImportante());
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
		s.setImportante(r.getImportante() == null ? "" : r.getImportante());
		s.setFechaNacimiento(r.getFechaNacimiento());
		s.setAdicional(r.getAdicional());
		s.setIdioma(r.getIdioma());
		s.setDireccion(r.getDireccion());
		s.setEstado(r.getEstado());
		s.setApellidos(r.getApellidos());
		s.setParalegalName(r.getParalegalName());
		s.setParalegalEmails(r.getParalegalEmails());
		s.setParalegalTelefonos(r.getParalegalTelefonos());
		s.setImportante(r.getImportante());
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

	@Override
	public String cargarExcel(MultipartFile archivo) {

		UtilidadesAdapter.pintarLog("Ambiente:" + ambiente);
		Workbook workbook = null;
		String salida = "";
		StringBuilder sb = new StringBuilder();

		try {

			// workbook = WorkbookFactory.create(new
			// File("/Users/joser.vazquez/ExcelUnif.xlsx"));
			InputStream is = new ByteArrayInputStream(archivo.getBytes());
			workbook = WorkbookFactory.create(is);

			UtilidadesAdapter.pintarLog("Number of sheetss: " + workbook.getNumberOfSheets());

			List<TipoSolicitudEntity> ls = tsPort.obtenerTiposSolicitudes();
			int countGen = 0;

			UtilidadesAdapter.pintarLog("Comenzara a leer sheets");

			int s1 = leerSheet(sb, "Mar25", workbook, ls, 27);
			int s2 = leerSheet(sb, "Feb25", workbook, ls, 26);
			int s3 = leerSheet(sb, "Jan25", workbook, ls, 26);
			int s4 = leerSheet(sb, "Dec24", workbook, ls, 26);
			int s5 = leerSheet(sb, "Nov24", workbook, ls, 26);
			int s6 = leerSheet(sb, "Oct24", workbook, ls, 26);
			int s7 = leerSheet(sb, "Sept24", workbook, ls, 26);
			int s8 = leerSheet(sb, "Aug24", workbook, ls, 27);
			int s9 = leerSheet(sb, "Jul24", workbook, ls, 27);
			int s10 = leerSheet(sb, "Jun24", workbook, ls, 29);
			int s11 = leerSheet(sb, "May24", workbook, ls, 30);
			int s12 = leerSheet(sb, "Apr24", workbook, ls, 30);
			int s13 = leerSheet(sb, "Mar24", workbook, ls, 30);
			int s14 = leerSheet(sb, "Feb24", workbook, ls, 31);
			int s15 = leerSheet(sb, "Jan24", workbook, ls, 31);

			countGen = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8 + s9 + s10 + s11 + s12 + s13 + s14 + s15;

			salida = "Cargados " + countGen;

			UtilidadesAdapter.pintarLog(salida);

		} catch (EncryptedDocumentException | IOException e) {
			UtilidadesAdapter.pintarLog(e.getMessage());
		} finally {
			try {
				if (workbook != null)
					workbook.close();
			} catch (IOException e) {
				UtilidadesAdapter.pintarLog(e.getMessage());
			}
			UtilidadesAdapter.pintarLog(sb.toString());
		}
		return salida;
	}

	public int leerSheet(StringBuilder sb, String nombre, Workbook workbook, List<TipoSolicitudEntity> ls,
			int comienza) {

		SolicitudVocEntity solEnc = null;
		String sheetName = "";
		BigDecimal pago = BigDecimal.ZERO;
		String caseNumber = "";
		String terapeuta = "";
		String denegado = "";
		String cliente = "";
		String telefono = "";
		String direccion = "";
		int idUsuarioTerapeuta = 0;
		int sesiones = 0;
		//int sesionesTomadas = 0;
		int cargados = 0;
		boolean solicitud = false;
		boolean numericTel = false;
		boolean finalSheet = false;
		boolean esDenegado = false;

		String type = "";
		int idEstatusSolicitud = 0;
		int idTipoSolicitud = 0;
		int numeroDeSesiones = 0;

		Date fecha = null;
		int mes = 0;

		Sheet sheet = workbook.getSheet(nombre);
		sheetName = sheet.getSheetName();
		pintarLog("NombreSheet:" + sheetName, sb);
		UtilidadesAdapter.pintarLog("Leyendo sheet:" + sheetName);

		// UtilidadesAdapter.pintarLog("Sheet:" + i);

		for (Row row : sheet) {
			SolicitudVoc r = new SolicitudVoc();
			// UtilidadesAdapter.pintarLog("row:" + row.getRowNum());

			if (row.getRowNum() >= comienza) {

				if (row.getCell(1) != null && !"".equals(row.getCell(1))) {

					if (row.getCell(1) != null && row.getCell(1).getCellType() == CellType.STRING) {
						pintarLog("Case number:" + row.getCell(1).getStringCellValue() + " | " + nombre, sb);
						caseNumber = row.getCell(1).getStringCellValue();
						if (caseNumber.length() > 11) {
							caseNumber = caseNumber.substring(0, 11);
						}
						r.setNumeroDeCaso(caseNumber.trim());
						solicitud = true;
						if (caseNumber.contains("COLORS")) {
							finalSheet = true;
						}

					}

					if (solicitud) {

						pintarLog("FinalSheet:" + finalSheet, sb);
						if (!finalSheet) {

							solEnc = reqPort.obtenerSolicitudByNumeroDeCaso(caseNumber);

							if (solEnc == null) {

								pintarLog("##########################", sb);
								pintarLog("··Llenando datos solicitud", sb);

								if (row.getCell(3) != null) {
									denegado = valorCell2(row, 3, workbook);
									pintarLog("Denegado:" + denegado, sb);
									if (!"".equals(denegado)) {
										if ("Denied".equals(denegado)) {
											esDenegado = true;
										}
									}
									if (!"".equals(denegado)) {
										if ("Not found".equals(denegado)) {
											esDenegado = true;
										}
									}
								}

								if (row.getCell(22) != null) {
									denegado = valorCell2(row, 22, workbook);
									pintarLog("Denegado:" + denegado, sb);
									if (!"".equals(denegado)) {
										if ("Denied".equals(denegado)) {
											esDenegado = true;
										}
									}
								}

								if (!esDenegado) {

									if (row.getCell(4) != null && row.getCell(4).getCellType() == CellType.STRING) {

										pintarLog("Terapueta:" + row.getCell(4).getStringCellValue(), sb);
										terapeuta = row.getCell(4).getStringCellValue();
										UsuarioEntity u = usPort.buscarPorNombre(terapeuta);
										pintarLog("Usuario Terapeuta:" + u, sb);
										if (u != null) {
											idUsuarioTerapeuta = u.getIdUsuario();
										} else {
											idUsuarioTerapeuta = 44;
										}
									}

									r.setTerapeuta(idUsuarioTerapeuta);

									if (row.getCell(15) != null) {
										pintarLog("Cliente:" + row.getCell(15).getStringCellValue(), sb);
										cliente = row.getCell(15).getStringCellValue();
									}

									r.setCliente(cliente);
									final String typeF = type;
									TipoSolicitudEntity se = ls.stream().filter(a -> typeF.contains(a.getNombre()))
											.findAny().orElse(null);

									if (se == null) {
										idTipoSolicitud = 1;
									} else {
										idTipoSolicitud = se.getIdTipoSolicitud();
									}

									pintarLog("Id tipo solicitud:" + idTipoSolicitud, sb);
									r.setIdTipoSolicitud(idTipoSolicitud);

									if (row.getCell(16) != null) {
										if (!numericTel) {
											try {
												telefono = row.getCell(16).getStringCellValue();
											} catch (Exception e) {
												// UtilidadesAdapter.pintarLog("e:"+e.getMessage());
												if (e.getMessage().equals(
														"Cannot get a STRING value from a NUMERIC formula cell")) {
													telefono = (int) row.getCell(16).getNumericCellValue() + "";
												}
											}
										}

										pintarLog("Telefono inicial:" + telefono, sb);
										if (telefono.length() > 12) {
											telefono = telefono.substring(0, 12);
											telefono = telefono.replaceAll("-", "");
										} else {
											telefono = telefono.replaceAll("-", "");
										}
										pintarLog("Telefono final:" + telefono, sb);
										if (ambiente.equals("test")) {
											r.setTelefono("0000000000");
										} else {
											r.setTelefono(telefono);
										}
									} else {
										r.setTelefono("");
									}
									if (row.getCell(19) != null) {
										pintarLog("Direccion:" + row.getCell(19).getStringCellValue(), sb);
										direccion = row.getCell(19).getStringCellValue();
										r.setDireccion(direccion);
									}

									if (row.getCell(22) != null) {
										pintarLog("Num sesiones:" + row.getCell(22).getNumericCellValue(), sb);
										sesiones = (int) row.getCell(22).getNumericCellValue();
										r.setNumSesiones(sesiones);
									}

									if (row.getCell(26) != null) {
										pintarLog("Num sesiones tomadas:" + row.getCell(26).getNumericCellValue(), sb);
										//sesionesTomadas = (int) row.getCell(26).getNumericCellValue();
										//r.setNumSchedules(sesionesTomadas);
										//r.setSesionesPendientes(r.getNumSesiones() - r.getNumSchedules());
									}

									// Estatus pago
									r.setIdEstatusPago(1);

									pintarLog("idEstatusSolicitud:" + idEstatusSolicitud, sb);
									if (idEstatusSolicitud == 0) {
										r.setIdEstatusSolicitud(14);
									}

									pintarLog("Pago:" + pago, sb);
									pintarLog("idEstatusSolicitud:" + idEstatusSolicitud, sb);

									try {
										mes = UtilidadesAdapter.obtenerMesSheet(sheetName.toUpperCase());
										fecha = UtilidadesAdapter.cadenaAFecha("2024-" + mes + "-15");
									} catch (ParseException e) {
										e.printStackTrace();
									}

									r.setFechaInicio(fecha);

									SolicitudVocEntity s = crearSolicitudMasivo(r, 1);
									cargados++;
									int sesionesInsertadas = agregarPago(row, s.getIdSolicitud(), idUsuarioTerapeuta, sb, workbook);
									
									pintarLog("Num Sesiones:" + s.getNumSesiones(), sb);
									pintarLog("Num Sesiones tomadas:" + s.getNumSchedules(), sb);
									numeroDeSesiones = s.getNumSchedules() + sesionesInsertadas;
									s.setNumSchedules(numeroDeSesiones);
									s.setSesionesPendientes(s.getNumSesiones() - s.getNumSchedules());
									reqPort.actualizarSolicitud(s);

								}

								pintarLog("_______________", sb);

							} else {

								if (row.getCell(4) != null && row.getCell(4).getCellType() == CellType.STRING) {

									pintarLog("Terapueta:" + row.getCell(4).getStringCellValue(), sb);
									terapeuta = row.getCell(4).getStringCellValue();
									UsuarioEntity u = usPort.buscarPorNombre(terapeuta);
									pintarLog("Usuario Terapeuta:" + u, sb);
									if (u != null) {
										idUsuarioTerapeuta = u.getIdUsuario();
									} else {
										idUsuarioTerapeuta = 44;
									}
								}

								pintarLog(
										"///////////// Se encontro y no se insertara solicitud, se actualiza pagos y sesiones",
										sb);
								int sesionesInsertadas = agregarPago(row, solEnc.getIdSolicitud(), idUsuarioTerapeuta,
										sb, workbook);

								pintarLog("Num Sesiones:" + solEnc.getNumSesiones(), sb);
								pintarLog("Num Sesiones tomadas:" + solEnc.getNumSchedules(), sb);	
								numeroDeSesiones = solEnc.getNumSchedules() + sesionesInsertadas;
								solEnc.setNumSchedules(numeroDeSesiones);
								solEnc.setSesionesPendientes(solEnc.getNumSesiones() - solEnc.getNumSchedules());
								reqPort.actualizarSolicitud(solEnc);
								  
							}

						}

						//pintarLog(r.toString(), sb);
						pintarLog("_______________", sb);

					}
					solicitud = false;
					pago = BigDecimal.ZERO;
					idEstatusSolicitud = 0;
					idTipoSolicitud = 0;
					type = "";
					esDenegado = false;
					solEnc = null;

				}
			}

			numericTel = false;
			solEnc = null;

		}

		finalSheet = false;

		pintarLog("cargados:" + cargados, sb);
		return cargados;
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
		if (ambiente.equals("test")) {
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
