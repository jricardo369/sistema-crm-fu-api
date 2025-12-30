package com.cargosyabonos.adapter.out.file;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cargosyabonos.domain.AbogadoEntity;

public class ExcelAbogados {
	


	public static byte[] generarExcelFirmas(List<AbogadoEntity> abogados) throws IOException {
		boolean local=false;
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Lawyers firms");
            
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
            String[] headers = {"Creation Date","Law Firm", "Name", "Email", "Phone", "Synonyms","Coupon"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                
                cell.setCellStyle(cs);
                cell.setCellValue(headers[i]);
            }

            // Contenido
            int rowIdx = 1;
            for (AbogadoEntity f : abogados) {
            	
                Row row = sheet.createRow(rowIdx++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(f.getFechaCreacion());
                cell0.setCellStyle(csn);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(f.getFirma());
                cell1.setCellStyle(csn);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(f.getNombre());
                cell2.setCellStyle(csn);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue("");
                cell3.setCellStyle(csn);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(f.getTelefono());
                cell4.setCellStyle(csn);
                
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(f.getSinonimos());
                cell5.setCellStyle(csn);
                
                if(f.isCupon()){
	                Cell cell6 = row.createCell(6);
	                cell6.setCellValue("Yes - "+f.getFechaCupon());
	                cell6.setCellStyle(csn);
                }
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
		List<AbogadoEntity> l = new ArrayList<>();
		AbogadoEntity o = new AbogadoEntity();
		o.setFirma("firma");
		o.setIdAbogado(1);
		o.setNombre("nobre ");
		o.setTelefono("tel");
		l.add(o);
		try {
			generarExcelFirmas(l);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
