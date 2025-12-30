package com.cargosyabonos.adapter.out.file;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cargosyabonos.domain.CargosCitasVoc;

public class ExcelVOCCharges {
	


	public static byte[] generarExcelVOCCharges(List<CargosCitasVoc> lista) throws IOException {
		
		boolean local=false;
		
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("VOC Charges");
            
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
            String[] headers = {"File", "Id Schedule", "Date", "Case number","Customer","Gender","DOB","Address" ,"Phone","Email","Amount","Therapist","Paid","Date paid"};
            Row headerRow = sheet.createRow(0);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellStyle(cs);
                cell.setCellValue(headers[i]);
            }

            // Contenido
            int rowIdx = 1;
            for (CargosCitasVoc o : lista) {
            	
                Row row = sheet.createRow(rowIdx++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(o.getIdFile());
                cell0.setCellStyle(csn);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(o.getIdCita());
                cell1.setCellStyle(csn);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(o.getFecha());
                cell2.setCellStyle(csn);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(o.getCaseNumber());
                cell3.setCellStyle(csn);
                
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(o.getCliente());
                cell4.setCellStyle(csn);
                
                Cell cellGender = row.createCell(5);
                cellGender.setCellValue(o.getSexo());
                cellGender.setCellStyle(csn);
                
                Cell cell5 = row.createCell(6);
                cell5.setCellValue(o.getAnioNacimiento());
                cell5.setCellStyle(csn);
                
                Cell cell6 = row.createCell(7);
                cell6.setCellValue(o.getDireccion());
                cell6.setCellStyle(csn);

                Cell cell7 = row.createCell(8);
                cell7.setCellValue(o.getTelefono());
                cell7.setCellStyle(csn);
                
                Cell cell8 = row.createCell(9);
                cell8.setCellValue(o.getEmail());
                cell8.setCellStyle(csn);
                
                Cell cell9 = row.createCell(10);
                cell9.setCellValue(o.getAmount() == null? "":o.getAmount().toString());
                cell9.setCellStyle(csn);
                
                Cell cell10 = row.createCell(11);
                cell10.setCellValue(o.getTerapeuta());
                cell10.setCellStyle(csn);
                
                Cell cell11 = row.createCell(12);
                cell11.setCellValue(o.isPagado() == true ? "Yes":"No");
                cell11.setCellStyle(csn);
                
                Cell cell12 = row.createCell(13);
                cell12.setCellValue(o.getFechaPagado());
                cell12.setCellStyle(csn);
                
            }
            if(local){
            	
            	FileOutputStream fo = new FileOutputStream("/Users/joser.vazquez/Desktop/test.xlsx");
            	workbook.write(fo);
            	fo.close();
            	return null;
            	
            }else{
            	
	            workbook.write(out);
	            return out.toByteArray();
	            
            }
        }
        
    }
	
	public static void main(String args[]){
		List<CargosCitasVoc> l = new ArrayList<>();
		CargosCitasVoc o = new CargosCitasVoc(1, 121, "A23233", "2025-05-16", "Juan", "3521007318", "na", new BigDecimal("100"), false, "", "", "Gaby");
				
		l.add(o);
		try {
			generarExcelVOCCharges(l);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
