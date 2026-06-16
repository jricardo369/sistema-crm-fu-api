package com.cargosyabonos.application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.DisponibilidadUsuarioUseCase;
import com.cargosyabonos.application.port.out.DisponibilidadUsuarioPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.DisponibilidadTodoDeUsuario;
import com.cargosyabonos.domain.DisponibilidadUsuario;
import com.cargosyabonos.domain.DisponibilidadUsuarioEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@SuppressWarnings("deprecation")
@Service
public class DisponibilidadUsuarioService implements DisponibilidadUsuarioUseCase {

	@Autowired
	private DisponibilidadUsuarioPort duPort;
	
	@Autowired
	private UsuariosPort usPort;
	
	@Autowired
	private EventoSolicitudPort evPort;

	@Override
	public List<DisponibilidadUsuarioEntity> obtenerDisponibilidadUsuario(int idUsuario) {
		return duPort.obtenerDisponibilidadUsuario(idUsuario);
	}

	@Override
	public void crearDisponibilidadUsuario(DisponibilidadUsuario du) {
		
		DisponibilidadUsuarioEntity due = convertirDUtoEntity(du);
		//System.out.println("F:" + du.getFecha());

			int existe = duPort.obtenerDisponibilidadPorTodo(du.getFecha(), du.getHora(), du.getTipo(),
					du.getIdUsuario());
			//System.out.println("existe:" + existe);
			if (existe > 0) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Availability already loaded previously");
			} else {
				String[] parts = du.getHora().split(":");
				String h1 = parts[0]; 
				String h2 = parts[1]; 
				if(h1.length() == 1){
					String hf = "0"+h1+":"+h2;
					due.setHora(hf);
					duPort.crearDisponibilidadUsuario(due);
				}else{
					duPort.crearDisponibilidadUsuario(due);
				}
			}

		
		
	}

	@Override
	public void actualizarDisponibilidadUsuario(DisponibilidadUsuarioEntity es) {
		duPort.actualizarDisponibilidadUsuario(es);
	}

	@Override
	public void eliminarDisponibilidadUsuario(int idDisponibilidad) {
		
		DisponibilidadUsuarioEntity du = duPort.obtenerDisponibilidadPorId(idDisponibilidad);
		int existeEvento = evPort.existeEventoSchedule(UtilidadesAdapter.formatearFecha(du.getFecha()), du.getHora(), du.getTipo(), du.getUsuario().getIdUsuario(),"1");
		System.out.println("existeEvento:" + existeEvento);
		
		if (existeEvento == 0) {
			duPort.eliminarDisponibilidadUsuario(idDisponibilidad);
		}else{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"The date and time already have an appointment assigned");
		}	
	}

	@Override
	public List<DisponibilidadUsuario> obtenerDisponibilidadUsuarioPorFecha(String fecha,int rol,boolean fechaAnterior,int idSolicitud,String estado,int idUsuarioTraductor) {
		
		List<DisponibilidadUsuario> salida = null;
		
		salida = duPort.obtenerDisponibilidadUsuarioPorFecha(fecha,rol,idSolicitud,fechaAnterior,estado,idUsuarioTraductor);
		
		return salida;
	}
	
	@Override
	public List<DisponibilidadUsuario> obtenerDisponibilidadUsuarioVocPorFecha(String fecha,int idUsuario) {

		List<DisponibilidadUsuario> salida = null;	
		salida = duPort.obtenerDisponibilidadUsuarioVocPorFecha(fecha,idUsuario);
		return salida;
	}
	
	public DisponibilidadUsuarioEntity convertirDUtoEntity(DisponibilidadUsuario du){
		DisponibilidadUsuarioEntity due = new DisponibilidadUsuarioEntity();
		if (du.getFecha() != null && !du.getFecha().trim().isEmpty()) {
			try {
				due.setFecha(UtilidadesAdapter.cadenaAFecha(du.getFecha()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		due.setHora(du.getHora());
		UsuarioEntity us = usPort.buscarPorId(du.getIdUsuario());
		due.setUsuario(us);
		due.setTipo(du.getTipo());
		due.setZonaHoraria(du.getZonaHoraria());
		return due;
	}

	@Override
	public String cargarExcel(MultipartFile archivo, int idUsuario) {
		Workbook workbook = null;
		String salida = "";
		String[] salidaL = new String[1];

		try {

			InputStream is = new ByteArrayInputStream(archivo.getBytes());
			workbook = WorkbookFactory.create(is);
			System.out.println("Number of sheetss: " + workbook.getNumberOfSheets());

			StringBuilder sb = new StringBuilder();
			StringBuilder sbErroresFecha = new StringBuilder();
			StringBuilder sbInsertados = new StringBuilder();
			//workbook.forEach(sheet -> {
			Sheet sheet = workbook.getSheetAt(0);
			
				// System.out.println("Sheet: ");
				int index = 0;
				int cargados = 0;
				String fecha = "";
				String f = "";
				String ho = "";
				boolean hora = false;
				boolean seLlenoFecha = false;
				int renglonesvaciosSeguidos = 0;
				
				for (Row row : sheet) {
					
					DisponibilidadUsuario d = new DisponibilidadUsuario();

					// if (index++ == 0) continue;

					System.out.println("row:" + index);

					boolean col0Vacia = row.getCell(0) == null || row.getCell(0).getCellType() == CellType.BLANK;
					boolean col1Vacia = row.getCell(1) == null || row.getCell(1).getCellType() == CellType.BLANK;
					boolean col2Vacia = row.getCell(2) == null || row.getCell(2).getCellType() == CellType.BLANK;

					if (col0Vacia && col1Vacia && col2Vacia) {
						renglonesvaciosSeguidos++;
						if (renglonesvaciosSeguidos >= 5) {
							System.out.println("5 consecutive empty rows, stopping.");
							break;
						}
						index++;
						continue;
					} else {
						renglonesvaciosSeguidos = 0;
					}

					if (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.STRING) {

						if (row.getCell(0).getStringCellValue().equals("HORA")) {
							try {
								fecha = obtenerFechaValidaDesdeCelda(row.getCell(2));
								System.out.println("----------------Fecha:" + fecha);
								seLlenoFecha = true;
							} catch (ResponseStatusException ex) {
								seLlenoFecha = false;
								fecha = "";
								f = "";
								sbErroresFecha.append("fila ").append(row.getRowNum() + 1).append(" (")
										.append(ex.getReason()).append("),");
								continue;
							}
						}
					}

					if (!"".equals(fecha)) {
						System.out.println("cargando fecha:" + fecha);
						// f = UtilidadesAdapter.cadenaAFecha(fecha);
						f = fecha;
					}

					if (!"".equals(fecha)) {
						index++;
						fecha = "";
						continue;
					}

					if (seLlenoFecha) {

						if (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.NUMERIC) {

							System.out.println("hora:" + row.getCell(0).getNumericCellValue());

							if (HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
								System.out.println(
										"Row No.: " + row.getRowNum() + " " + row.getCell(0).getDateCellValue());
								String h = UtilidadesAdapter.formatearHora(row.getCell(0).getDateCellValue());
								System.out.println("Formatear hora:"
										+ UtilidadesAdapter.formatearHora(row.getCell(0).getDateCellValue()));
								d.setHora(h);
							} else {
								ho = String.valueOf(Double.valueOf(row.getCell(0).getNumericCellValue()).intValue());
								if(ho.length()==1){
									d.setHora("0"+ho+":00");
								}else{
									d.setHora(ho+":00");
								}
								//d.setHora(String.valueOf(Double.valueOf(row.getCell(0).getNumericCellValue()).intValue()));
							}
							hora = true;

						} else {
							hora = false;
						}

						if (!hora) {
							if (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.STRING) {

								ho = String.valueOf(row.getCell(0).getStringCellValue());
								System.out.println("horaString:" + ho);
								if(ho.contains(":")){
								d.setHora(ho);
								}else{
									if(ho.length()==1){
										d.setHora("0"+ho+":00");
									}else{
										d.setHora(ho+":00");
									}
								}
								hora = true;

							} else {
								hora = false;
							}
						}

						if (row.getCell(1) != null && row.getCell(1).getCellType() == CellType.STRING) {
							// System.out.println("tipo:"+row.getCell(1).getStringCellValue());
							d.setTipo(row.getCell(1).getStringCellValue());
						}
						
						if (row.getCell(2) != null && row.getCell(2).getCellType() == CellType.STRING) {
							// System.out.println("tipo:"+row.getCell(1).getStringCellValue());
							d.setZonaHoraria(row.getCell(2).getStringCellValue());
						}

						if (hora) {
							d.setFecha(f);
							// System.out.println("valor:"+d.getFecha()+"
							// "+d.getHora()+" "+d.getTipo());
						}

						d.setIdUsuario(idUsuario);

						if (d.getFecha() == null || d.getFecha().trim().isEmpty()) {
							index++;
							continue;
						}

						int existe = duPort.obtenerDisponibilidadPorTodo(d.getFecha(), d.getHora(), d.getTipo(),
								idUsuario);
						// int existe = 1;
						System.out.println("existe:" + existe);

						if (existe == 0) {

							System.out.println("Fecha:" + d.getFecha() + "|Hora:" + d.getHora() + "|Tipo:" + d.getTipo()
									+ "|Us:" + d.getIdUsuario());
							duPort.crearDisponibilidadUsuario(convertirDUtoEntity(d));
							cargados++;
							sbInsertados.append("[")
									.append(d.getFecha()).append("|")
									.append(d.getHora()).append("|")
									.append(d.getTipo()).append("],");

						} else {

							System.out.println("Ya existe");
							sb.append(d.getFecha() + " " + d.getHora() + " " + d.getTipo() + ",");

						}

					}

					index++;

				}

				
				if(!seLlenoFecha && sbErroresFecha.length() == 0){
					sb.append("The Excel format is incorrect, you can download it from the portal ");
				} 
				
			boolean hayErrores = sb.length() > 0 || sbErroresFecha.length() > 0;
			if (sb.length() > 0 || sbErroresFecha.length() > 0 || sbInsertados.length() > 0) {
				StringBuilder resumen = new StringBuilder();
				resumen.append(cargados).append(" records were registered");
				if (sbInsertados.length() > 0) {
					resumen.append(", inserted: ")
							.append(sbInsertados.toString().substring(0, sbInsertados.length() - 1));
				}
				if (sb.length() > 0) {
					resumen.append(", duplication error in: ")
							.append(sb.toString().substring(0, sb.length() - 1));
				}
				if (sbErroresFecha.length() > 0) {
					resumen.append(", date format error: ")
							.append(sbErroresFecha.toString().substring(0, sbErroresFecha.length() - 1));
				}
				salidaL[0] = resumen.toString();
			}

			//});
	        
	        //System.out.println("SalidaF:"+salidaL[0]);
	        salida = salidaL[0];
	        //System.out.println("Salida:"+salida);
	        
	        if(hayErrores && salida != null){
	        	 throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, salida);
	        }
	       
	    } catch (EncryptedDocumentException | IOException e) {
	    	System.out.println("Error:"+ e.getMessage());
	    	 throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cargar el archivo, revisar si el formato es el correcto");
	    } finally {
	        try {
	            if (workbook != null) workbook.close();
	        } catch (IOException e) {
	        	System.out.println(e.getMessage());
	        	
	        }
	    }
	    return salida;
	}

	private String obtenerFechaValidaDesdeCelda(Cell celdaFecha) {
		if (celdaFecha == null) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"The date format is incorrect, it must be YYYY-MM-DD, date: [empty]");
		}

		String fechaCandidata;
		if (celdaFecha.getCellType() == CellType.STRING) {
			fechaCandidata = celdaFecha.getStringCellValue();
		} else if (celdaFecha.getCellType() == CellType.NUMERIC && HSSFDateUtil.isCellDateFormatted(celdaFecha)) {
			LocalDate localDate = celdaFecha.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			fechaCandidata = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
		} else {
			fechaCandidata = celdaFecha.toString();
		}

		if (fechaCandidata != null) {
			fechaCandidata = fechaCandidata.trim();
		}

		try {
			LocalDate.parse(fechaCandidata, DateTimeFormatter.ISO_LOCAL_DATE);
			return fechaCandidata;
		} catch (DateTimeParseException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"The date format is incorrect, it must be YYYY-MM-DD, date: " + fechaCandidata);
		}
	}

	@Override
	public List<DisponibilidadTodoDeUsuario> obtenerDisponibilidadTodoDeUsuario(int idUsuario) {
		return duPort.obtenerDisponibilidadTodoDeUsuario(idUsuario);
	}
	
	
	
		
}
