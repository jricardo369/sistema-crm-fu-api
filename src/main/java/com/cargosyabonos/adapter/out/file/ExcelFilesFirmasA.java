package com.cargosyabonos.adapter.out.file;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cargosyabonos.domain.FilesFirmaAbogadoObj;
import com.cargosyabonos.domain.NumFilesAbogados;

public class ExcelFilesFirmasA {

	private static final Logger logger = LoggerFactory.getLogger(ExcelFilesFirmasA.class);

	public static byte[] generarExcelFirmas( List<NumFilesAbogados> listRepCont, List<FilesFirmaAbogadoObj> l1, List<FilesFirmaAbogadoObj> l2,List<FilesFirmaAbogadoObj> l3)
			throws IOException {
		
		logger.info("l1:"+l1.size());
		logger.info("l2:"+l2.size());
		logger.info("l3:"+l3.size());
		
		boolean local = false;
		
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			
			Sheet sheet = workbook.createSheet("FilesLayers");

			CellStyle cs = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8);
			font.setFontName("Cambria");
			font.setItalic(false);
			font.setBold(true);
			cs.setFont(font);
			
			CellStyle csb = workbook.createCellStyle();
			Font fontb = workbook.createFont();
			fontb.setFontHeightInPoints((short) 8);
			fontb.setFontName("Cambria");
			fontb.setItalic(false);
			fontb.setBold(true);
			fontb.setColor(IndexedColors.BLUE.getIndex());
			csb.setFont(fontb);

			CellStyle csn = workbook.createCellStyle();
			Font font2 = workbook.createFont();
			font2.setFontHeightInPoints((short) 8);
			font2.setFontName("Cambria");
			font2.setItalic(false);
			csn.setFont(font2);

			int rowIdx = 0;

			for (NumFilesAbogados rep : listRepCont) {

				// Título principal: nombre y número
				Row row = sheet.createRow(rowIdx++);
				Cell cell0 = row.createCell(0);
				cell0.setCellValue("Law firm:");
				cell0.setCellStyle(cs);

				cell0 = row.createCell(1);
				cell0.setCellValue(rep.getFirma());
				cell0.setCellStyle(csn);
				
				cell0 = row.createCell(2);
				cell0.setCellValue("Lawyer name:");
				cell0.setCellStyle(cs);
				
				cell0 = row.createCell(3);
				cell0.setCellValue(rep.getNombre());
				
				if(rep.getNombre() != null){
				if(rep.getNombre().equals("Without asociated lawyer")){
					cell0.setCellStyle(csb);
				}else{
					cell0.setCellStyle(csn);
				}
				}else{
					cell0.setCellStyle(csn);
				}

				cell0 = row.createCell(4);
				cell0.setCellValue("Emails lawyer:");
				cell0.setCellStyle(cs);
				
				cell0 = row.createCell(5);
				cell0.setCellValue(rep.getEmails());
				cell0.setCellStyle(csn);
				
				cell0 = row.createCell(6);
				cell0.setCellValue("Number of files:");
				cell0.setCellStyle(cs);

				cell0 = row.createCell(7);
				cell0.setCellValue(rep.getNumero());
				cell0.setCellStyle(csn);

				
				// Encabezados de detalle
				Row header = sheet.createRow(rowIdx++);

				Cell cellH = header.createCell(0);
				cellH.setCellValue("File");
				cellH.setCellStyle(cs);
				cellH = header.createCell(1);
				cellH.setCellValue("Email");
				cellH.setCellStyle(cs);
				cellH = header.createCell(2);
				cellH.setCellValue("Customer");
				cellH.setCellStyle(cs);
				cellH = header.createCell(3);
				cellH.setCellValue("Lawyer");
				cellH.setCellStyle(cs);
				cellH = header.createCell(4);
				cellH.setCellValue("Lawyer email");
				cellH.setCellStyle(cs);
				cellH = header.createCell(5);
				cellH.setCellValue("State");
				cellH.setCellStyle(cs);

				String firma = rep.getFirma() == null ? "" : rep.getFirma();

				List<FilesFirmaAbogadoObj> coincidencias = null;
				if (rep.getIdAbogado() > 0) {
					coincidencias = l1.stream().filter(f -> f.getIdAbogado() == rep.getIdAbogado()).collect(Collectors.toList());
					//logger.info("conabo:"+coincidencias.size());
				} 
				else if (firma.equals("")) {
					
					coincidencias = l2.stream().filter(f -> f.getFirma().equals(rep.getFirma())).collect(Collectors.toList());
				} 
				else if (!firma.equals("")) {
					//logger.info("rep.getFirma():"+rep.getFirma());
					coincidencias = l3.stream().filter(f -> f.getFirma().equals(rep.getFirma()))
							.collect(Collectors.toList());
				}

				if(coincidencias != null){
					Cell cellHD = null;
					for (FilesFirmaAbogadoObj f : coincidencias) {
						
						Row dataRow = sheet.createRow(rowIdx++);
						cellHD = dataRow.createCell(0);
						cellHD.setCellValue(f.getIdSolicitud());
						cellHD.setCellStyle(csn);
						cellHD = dataRow.createCell(1);
						cellHD.setCellValue(f.getEmail());
						cellHD.setCellStyle(csn);
						cellHD = dataRow.createCell(2);
						cellHD.setCellValue(f.getCliente());
						cellHD.setCellStyle(csn);
						cellHD = dataRow.createCell(3);
						cellHD.setCellValue(f.getAbogado());
						cellHD.setCellStyle(csn);
						cellHD = dataRow.createCell(4);
						cellHD.setCellValue(f.getEmailAbogado());
						cellHD.setCellStyle(csn);
						cellHD = dataRow.createCell(5);
						cellHD.setCellValue(f.getEstado());
						cellHD.setCellStyle(csn);
						
					}
				}
				

				rowIdx++; // Espacio entre secciones
			}

			// Autosize columnas
			for (int i = 0; i < 6; i++) {
				sheet.autoSizeColumn(i);
			}
			if (local) {
				FileOutputStream fo = new FileOutputStream("/Users/joser.vazquez/Desktop/test.xlsx");
				workbook.write(fo);
				fo.close();
				return null;
			} else {
				workbook.write(out);
				return out.toByteArray();
			}
			
		}
	}

	public static void main(String args[]) {
		List<FilesFirmaAbogadoObj> l = new ArrayList<>();
		FilesFirmaAbogadoObj o = new FilesFirmaAbogadoObj(1, "email", "cliente", "luis", "emailAbo", "TX", "",0);
		l.add(o);

		List<NumFilesAbogados> lr = new ArrayList<>();
		NumFilesAbogados or = new NumFilesAbogados();
		or.setNombre("luis");
		or.setNumero(34);
		lr.add(or);
		NumFilesAbogados or2 = new NumFilesAbogados();
		or2.setNombre("jose");
		or2.setNumero(34);
		lr.add(or2);
		try {
			generarExcelFirmas(lr,l,l,l);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
