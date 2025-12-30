package com.cargosyabonos.adapter.out.file;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.domain.SolicitudVoc;

public class ExcelSolicitudesVOC {
	


	public static byte[] generarExcelSolicitudesVOC(List<SolicitudVoc> solicitudes) throws IOException {
		boolean local=false;
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Solicitudes VOC");
            
            CellStyle cs = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short)8);
            font.setFontName("Cambria");
            font.setItalic(false);
            font.setBold(true);
            cs.setFont(font);
            
            CellStyle csn = workbook.createCellStyle();
            Font font2 = workbook.createFont();
            font2.setFontHeightInPoints((short)8);
            font2.setFontName("Cambria");
            font2.setItalic(false);
            csn.setFont(font2);

            // Cabecera
            String[] headers = {"File", "Case number", "Creation Date", "Customer Name", "Sessions","Schedules","Pending Sessions","Therapist","Email","Treatment plan","Payment Status","File Status","Gender"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                
                cell.setCellStyle(cs);
                cell.setCellValue(headers[i]);
            }

            // Contenido
            int rowIdx = 1;
            for (SolicitudVoc s : solicitudes) {
            	
                Row row = sheet.createRow(rowIdx++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(s.getIdSolicitud());
                cell0.setCellStyle(csn);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(s.getNumeroDeCaso());
                cell1.setCellStyle(csn);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(s.getFechaInicio());
                cell2.setCellStyle(csn);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(s.getNombreClienteCompleto());
                cell3.setCellStyle(csn);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(s.getNumSesiones());
                cell4.setCellStyle(csn);
                
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(s.getNumSchedules());
                cell5.setCellStyle(csn);
                
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(s.getSesionesPendientes());
                cell6.setCellStyle(csn);
                
                
                
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(s.getNombreTerapeuta());
                cell7.setCellStyle(csn);

                Cell cell8 = row.createCell(8);
                cell8.setCellValue(s.getEmail());
                cell8.setCellStyle(csn);

                Cell cell9 = row.createCell(9);
                cell9.setCellValue(s.getPurposeTreatament());
                cell9.setCellStyle(csn);
                
                Cell cell10 = row.createCell(10);
                cell10.setCellValue(s.getEstatusSolicitud());
                cell10.setCellStyle(csn);
                
                Cell cell11 = row.createCell(11);
                cell11.setCellValue(s.getEstatusSolicitud());
                cell11.setCellStyle(csn);
                
                Cell cell12 = row.createCell(12);
                cell12.setCellValue(s.getSexo());
                cell12.setCellStyle(csn);
                
                
                
            }
            if(local){
            	UtilidadesAdapter.pintarLog("Sera local");
            	FileOutputStream fo = new FileOutputStream("/Users/joser.vazquez/Desktop/test.xlsx");
            	workbook.write(fo);
            	fo.close();
            	return null;
            }else{
	            workbook.write(out);
	            return out.toByteArray();
            }
        }catch(Exception e){
        	UtilidadesAdapter.pintarLog("Error:"+e.getMessage());
        	return null;
        }
    }
	
}
