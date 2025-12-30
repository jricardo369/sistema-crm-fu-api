package com.cargosyabonos.application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	Logger log = LoggerFactory.getLogger(DisponibilidadUsuarioService.class);

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
	public List<DisponibilidadUsuario> obtenerDisponibilidadUsuarioPorFecha(String fecha,int rol,boolean fechaAnterior,int idSolicitud,boolean clinician) {
		
		List<DisponibilidadUsuario> salida = null;
		
		salida = duPort.obtenerDisponibilidadUsuarioPorFecha(fecha,rol,idSolicitud,fechaAnterior,clinician);
		
		return salida;
	}
	
	public DisponibilidadUsuarioEntity convertirDUtoEntity(DisponibilidadUsuario du){
		DisponibilidadUsuarioEntity due = new DisponibilidadUsuarioEntity();
		try {
			due.setFecha(UtilidadesAdapter.cadenaAFecha(du.getFecha()));
		} catch (ParseException e) {
			e.printStackTrace();
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
				
				for (Row row : sheet) {
					
					DisponibilidadUsuario d = new DisponibilidadUsuario();

					// if (index++ == 0) continue;

					System.out.println("row:" + index);

					if (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.STRING) {

						if (row.getCell(0).getStringCellValue().equals("HORA")) {
							fecha = row.getCell(2).getStringCellValue();
							System.out.println("----------------Fecha:" + fecha);
							seLlenoFecha = true;
						}
					}

					if (!"".equals(fecha)) {
						System.out.println("cargando fecha:" + fecha);
						// f = UtilidadesAdapter.cadenaAFecha(fecha);
						f = fecha;
					}

					if (fecha != "") {
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
						int existe = duPort.obtenerDisponibilidadPorTodo(d.getFecha(), d.getHora(), d.getTipo(),
								idUsuario);
						// int existe = 1;
						System.out.println("existe:" + existe);

						if (existe == 0) {

							System.out.println("Fecha:" + d.getFecha() + "|Hora:" + d.getHora() + "|Tipo:" + d.getTipo()
									+ "|Us:" + d.getIdUsuario());
							if (d.getFecha() != null) {
								duPort.crearDisponibilidadUsuario(convertirDUtoEntity(d));

							}
							cargados++;

						} else {

							System.out.println("Ya existe");
							sb.append(d.getHora() + " " + d.getTipo() + ",");

						}

					}

					index++;

				}

				
				if(!seLlenoFecha){
					sb.append("El formato del excel es el incorrecto, puede descargarlo dendro del portal ");
				} 
				
			if (sb.length() > 0) {
				if (!seLlenoFecha) {
					salidaL[0] = sb.toString().substring(0, sb.length() - 1);
				} else {
					salidaL[0] = cargados + " records were registered, duplication error in: "
							+ sb.toString().substring(0, sb.length() - 1);
				}
			}

			//});
	        
	        //System.out.println("SalidaF:"+salidaL[0]);
	        salida = salidaL[0];
	        //System.out.println("Salida:"+salida);
	        
	        if(salida != null){
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

	@Override
	public List<DisponibilidadTodoDeUsuario> obtenerDisponibilidadTodoDeUsuario(int idUsuario) {
		return duPort.obtenerDisponibilidadTodoDeUsuario(idUsuario);
	}
	
	
	
		
}
