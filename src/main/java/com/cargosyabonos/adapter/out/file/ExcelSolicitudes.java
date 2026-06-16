package com.cargosyabonos.adapter.out.file;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.domain.Solicitud;

public class ExcelSolicitudes {

    private static final Logger logger = LoggerFactory.getLogger(ExcelSolicitudes.class);

    public static HashMap<String, CellStyle> estilos(Workbook workbook){
        HashMap<String, CellStyle> estilos = new HashMap<>();
    

        // Usar XSSF para colores personalizados
        org.apache.poi.xssf.usermodel.XSSFWorkbook xssfWorkbook = (org.apache.poi.xssf.usermodel.XSSFWorkbook) workbook;

        // Letra blanca
        XSSFFont fontWhite = xssfWorkbook.createFont();
        fontWhite.setFontHeightInPoints((short)8);
        fontWhite.setFontName("Cambria");
        fontWhite.setItalic(false);
        fontWhite.setBold(true);
        fontWhite.setColor(org.apache.poi.ss.usermodel.IndexedColors.WHITE.getIndex());

        // Letra negra
        XSSFFont fontBlack = xssfWorkbook.createFont();
        fontBlack.setFontHeightInPoints((short)8);
        fontBlack.setFontName("Cambria");
        fontBlack.setItalic(false);
        fontBlack.setBold(true);
        fontBlack.setColor(org.apache.poi.ss.usermodel.IndexedColors.BLACK.getIndex());

        // Letra roja
        XSSFFont fontRoja = xssfWorkbook.createFont();
        fontRoja.setFontHeightInPoints((short)8);
        fontRoja.setFontName("Cambria");
        fontRoja.setItalic(false);
        fontRoja.setBold(true);
        fontRoja.setColor(org.apache.poi.ss.usermodel.IndexedColors.RED.getIndex());

         // Borde gris
        java.awt.Color borderGray = new java.awt.Color(180, 180, 180); // Gris claro
        org.apache.poi.xssf.usermodel.XSSFColor borderColor = new org.apache.poi.xssf.usermodel.XSSFColor(borderGray, null);

        // Titulo color
        org.apache.poi.xssf.usermodel.XSSFCellStyle tituloTabla = xssfWorkbook.createCellStyle();
        tituloTabla.setFont(fontWhite);
        tituloTabla.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(117,81,140), null)); // #75518c
        tituloTabla.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        tituloTabla.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        tituloTabla.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        tituloTabla.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        tituloTabla.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        tituloTabla.setTopBorderColor(borderColor);
        tituloTabla.setBottomBorderColor(borderColor);
        tituloTabla.setLeftBorderColor(borderColor);
        tituloTabla.setRightBorderColor(borderColor);
        estilos.put("Titulo", tituloTabla);

        // File Status colors
        // Recibido
        org.apache.poi.xssf.usermodel.XSSFCellStyle recibido = xssfWorkbook.createCellStyle();
        recibido.setFont(fontBlack);
        recibido.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(184,232,182), null)); // #B8E8B6
        recibido.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        recibido.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        recibido.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        recibido.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        recibido.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        recibido.setTopBorderColor(borderColor);
        recibido.setBottomBorderColor(borderColor);
        recibido.setLeftBorderColor(borderColor);
        recibido.setRightBorderColor(borderColor);
        estilos.put("Received", recibido);

        // Interview
        org.apache.poi.xssf.usermodel.XSSFCellStyle interview = xssfWorkbook.createCellStyle();
        interview.setFont(fontBlack);
        interview.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(255,207,232), null)); // #FFCFE8
        interview.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        interview.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        interview.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        interview.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        interview.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        interview.setTopBorderColor(borderColor);
        interview.setBottomBorderColor(borderColor);
        interview.setLeftBorderColor(borderColor);
        interview.setRightBorderColor(borderColor);
        estilos.put("Interview", interview);

        // Reviewing
        org.apache.poi.xssf.usermodel.XSSFCellStyle reviewing = xssfWorkbook.createCellStyle();
        reviewing.setFont(fontBlack);
        reviewing.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(151,224,252), null)); // #97E0FC
        reviewing.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        reviewing.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        reviewing.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        reviewing.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        reviewing.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        reviewing.setTopBorderColor(borderColor);
        reviewing.setBottomBorderColor(borderColor);
        reviewing.setLeftBorderColor(borderColor);
        reviewing.setRightBorderColor(borderColor);
        estilos.put("Reviewing", reviewing);

        // Reviewing
        org.apache.poi.xssf.usermodel.XSSFCellStyle readyOnDraft = xssfWorkbook.createCellStyle();
        readyOnDraft.setFont(fontBlack);
        readyOnDraft.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(197,192,241), null)); // #C5C0F1
        readyOnDraft.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        readyOnDraft.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        readyOnDraft.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        readyOnDraft.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        readyOnDraft.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        readyOnDraft.setTopBorderColor(borderColor);
        readyOnDraft.setBottomBorderColor(borderColor);
        readyOnDraft.setLeftBorderColor(borderColor);
        readyOnDraft.setRightBorderColor(borderColor);
        estilos.put("ReadyOnDraft", readyOnDraft);

        // Closed
        org.apache.poi.xssf.usermodel.XSSFCellStyle closed = xssfWorkbook.createCellStyle();
        closed.setFont(fontWhite);
        closed.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(175,71,210), null)); // #AF47D2
        closed.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        closed.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        closed.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        closed.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        closed.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        closed.setTopBorderColor(borderColor);
        closed.setBottomBorderColor(borderColor);
        closed.setLeftBorderColor(borderColor);
        closed.setRightBorderColor(borderColor);
        estilos.put("Closed", closed);





        // Payment Status colors
        // Closed
        org.apache.poi.xssf.usermodel.XSSFCellStyle complete = xssfWorkbook.createCellStyle();
        complete.setFont(fontBlack);
        complete.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(114,203,184), null)); // #72CBB8
        complete.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        complete.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        complete.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        complete.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        complete.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        complete.setTopBorderColor(borderColor);
        complete.setBottomBorderColor(borderColor);
        complete.setLeftBorderColor(borderColor);
        complete.setRightBorderColor(borderColor);
        estilos.put("Complete", complete);

        // Partially
        org.apache.poi.xssf.usermodel.XSSFCellStyle partially = xssfWorkbook.createCellStyle();
        partially.setFont(fontBlack);
        partially.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(250,194,251), null)); // #FAC2FB
        partially.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        partially.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        partially.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        partially.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        partially.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        partially.setTopBorderColor(borderColor);
        partially.setBottomBorderColor(borderColor);
        partially.setLeftBorderColor(borderColor);
        partially.setRightBorderColor(borderColor);
        estilos.put("Partially", partially);

        // Entrevistadores colors
        // Case Manager
        org.apache.poi.xssf.usermodel.XSSFCellStyle caseManager = xssfWorkbook.createCellStyle();
        caseManager.setFont(fontWhite);
        caseManager.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(147,64,211), null)); // #9340D3
        caseManager.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        caseManager.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        caseManager.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        caseManager.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        caseManager.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        caseManager.setTopBorderColor(borderColor);
        caseManager.setBottomBorderColor(borderColor);
        caseManager.setLeftBorderColor(borderColor);
        caseManager.setRightBorderColor(borderColor);
        estilos.put("CaseManager", caseManager);

        // Scales
        org.apache.poi.xssf.usermodel.XSSFCellStyle scales = xssfWorkbook.createCellStyle();
        scales.setFont(fontWhite);
        scales.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(64,164,211), null)); // #40A4D3
        scales.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        scales.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        scales.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        scales.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        scales.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        scales.setTopBorderColor(borderColor);
        scales.setBottomBorderColor(borderColor);
        scales.setLeftBorderColor(borderColor);
        scales.setRightBorderColor(borderColor);
        estilos.put("Scales", scales);

        // Clinician
        org.apache.poi.xssf.usermodel.XSSFCellStyle clinician = xssfWorkbook.createCellStyle();
        clinician.setFont(fontWhite);
        clinician.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(238,134,175), null)); // #ee86af
        clinician.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        clinician.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        clinician.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        clinician.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        clinician.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        clinician.setTopBorderColor(borderColor);
        clinician.setBottomBorderColor(borderColor);
        clinician.setLeftBorderColor(borderColor);
        clinician.setRightBorderColor(borderColor);
        estilos.put("Clinician", clinician);

        // Editor
        org.apache.poi.xssf.usermodel.XSSFCellStyle editor = xssfWorkbook.createCellStyle();
        editor.setFont(fontWhite);
        editor.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(new java.awt.Color(49,151,87), null)); // #319757
        editor.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        editor.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        editor.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        editor.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        editor.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        editor.setTopBorderColor(borderColor);
        editor.setBottomBorderColor(borderColor);
        editor.setLeftBorderColor(borderColor);
        editor.setRightBorderColor(borderColor);
        estilos.put("Editor", editor);




        // Important
        org.apache.poi.xssf.usermodel.XSSFCellStyle important = xssfWorkbook.createCellStyle();
        important.setFont(fontRoja);
        important.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        important.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        important.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        important.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        important.setTopBorderColor(borderColor);
        important.setBottomBorderColor(borderColor);
        important.setLeftBorderColor(borderColor);
        important.setRightBorderColor(borderColor);
        estilos.put("Important", important);


        return estilos;
    }

	public static byte[] generarExcelSolicitudes(List<Solicitud> solicitudes) throws IOException {
		boolean local=false;
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Solicitudes");
            // Obtener estilos
            HashMap<String, CellStyle> estilos = estilos(workbook);
            CellStyle cs = estilos.getOrDefault("Titulo", workbook.createCellStyle());
            CellStyle csn = workbook.createCellStyle();
            XSSFFont font2 = (XSSFFont) workbook.createFont();
            font2.setFontHeightInPoints((short)8);
            font2.setFontName("Cambria");
            font2.setItalic(false);
            csn.setFont(font2);

            // Cabecera
            String[] headers = {"File","Creation Date","Due Date", "Customer Name", "Phone","Email","Address","State","Aditional","File Type","File Status","Payment Status","Payment type(s)","Consert","Case Manager schedule","Case Manager name","Scales schedule","Scales name","Clinician schedule","Clinician name","Editor name","Important notes"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                
                if(headers[i].equals("Case Manager schedule") || headers[i].equals("Case Manager name")){
                    cell.setCellStyle(estilos.getOrDefault("CaseManager", cs));
                }else if(headers[i].equals("Scales schedule") || headers[i].equals("Scales name")){
                    cell.setCellStyle(estilos.getOrDefault("Scales", cs));
                }else if(headers[i].equals("Clinician schedule") || headers[i].equals("Clinician name")){
                    cell.setCellStyle(estilos.getOrDefault("Clinician", cs));
                }else if(headers[i].equals("Editor name")){
                    cell.setCellStyle(estilos.getOrDefault("Editor", cs));
                }else{
                 cell.setCellStyle(cs);
                }
                cell.setCellValue(headers[i]);
            }

            // Contenido
            int rowIdx = 1;
            for (Solicitud s : solicitudes) {
                Row row = sheet.createRow(rowIdx++);
                int i = 0;
                Cell cell;
                // File (ID)
                cell = row.createCell(i++);
                cell.setCellStyle(csn);
                cell.setCellValue(s.getIdSolicitud());
               
                // Creation Date
                cell = row.createCell(i++);
                cell.setCellValue(s.getFechaInicio() != null ?  UtilidadesAdapter.formatearFechaStringAtipoUS(s.getFechaInicio().toString().substring(0,10)) : "");
                cell.setCellStyle(csn);

                // Due Date
                cell = row.createCell(i++);
                cell.setCellValue(s.getDueDate() != null ? UtilidadesAdapter.formatearFechaStringAtipoUS(s.getDueDate()) : "");
                cell.setCellStyle(csn);

                // Customer Name
                cell = row.createCell(i++);
                cell.setCellValue(s.getCliente() != null ? s.getCliente() : "");
                cell.setCellStyle(csn);

                // Phone
                cell = row.createCell(i++);
                cell.setCellValue(s.getTelefono() != null ? s.getTelefono() : "");
                cell.setCellStyle(csn);

                // Email
                cell = row.createCell(i++);
                cell.setCellValue(s.getEmail() != null ? s.getEmail() : "");
                cell.setCellStyle(csn);
                // Address
                cell = row.createCell(i++);
                cell.setCellValue(s.getDireccion() != null ? s.getDireccion() : "");
                cell.setCellStyle(csn);
                // State
                cell = row.createCell(i++);
                cell.setCellValue(s.getEstado() != null ? s.getEstado() : "");
                cell.setCellStyle(csn);
                // Aditional
                cell = row.createCell(i++);
                cell.setCellValue(s.getAdicional() != null ? s.getAdicional() : "");
                cell.setCellStyle(csn);
                // File Type
                cell = row.createCell(i++);
                cell.setCellValue(s.getTipoSolicitud() != null ? s.getTipoSolicitud() : "");
                cell.setCellStyle(csn);
                // File Status
                cell = row.createCell(i++);
                cell.setCellValue(s.getEstatusSolicitud() != null ? s.getEstatusSolicitud() : "");
                if((s.getEstatusSolicitud() != null && s.getEstatusSolicitud().equalsIgnoreCase("Received"))){
                    cell.setCellStyle(estilos.getOrDefault("Received", csn));
                }else if((s.getEstatusSolicitud() != null && s.getEstatusSolicitud().equalsIgnoreCase("Interview"))){
                    cell.setCellStyle(estilos.getOrDefault("Interview", csn));
                }else if((s.getEstatusSolicitud() != null && s.getEstatusSolicitud().equalsIgnoreCase("Reviewing"))){
                    cell.setCellStyle(estilos.getOrDefault("Reviewing", csn));
                }else if((s.getEstatusSolicitud() != null && s.getEstatusSolicitud().equalsIgnoreCase("Ready on draft"))){
                    cell.setCellStyle(estilos.getOrDefault("ReadyOnDraft", csn));
                }else if((s.getEstatusSolicitud() != null && s.getEstatusSolicitud().equalsIgnoreCase("Closed"))){
                    cell.setCellStyle(estilos.getOrDefault("Closed", csn));
                }else{
                    cell.setCellStyle(csn);
                }
                // Payment Status
                cell = row.createCell(i++);
                cell.setCellValue(s.getEstatusPago() != null ? s.getEstatusPago() : "");
                if((s.getEstatusPago() != null && s.getEstatusPago().equalsIgnoreCase("Complete"))){
                    cell.setCellStyle(estilos.getOrDefault("Complete", csn));
                }else if((s.getEstatusPago() != null && s.getEstatusPago().equalsIgnoreCase("Partially"))){
                    cell.setCellStyle(estilos.getOrDefault("Partially", csn));
                }else{
                    cell.setCellStyle(csn);
                }

                // Payment type(s)
                cell = row.createCell(i++);
                cell.setCellValue(s.getTiposPagosRealizados()!= null ? s.getTiposPagosRealizados() : "");
                cell.setCellStyle(csn);

                // Consent
                cell = row.createCell(i++);
                cell.setCellValue(s.getConsentimiento() == true ? "Yes" : "No");
                cell.setCellStyle(csn);

                // Case Manager schedule
                cell = row.createCell(i++);
                cell.setCellValue( (s.getFecha_schedule()!= null && !s.getFecha_schedule().isEmpty())  ? UtilidadesAdapter.formatearFechaYHoraSeparadoUS(s.getFecha_schedule()) : "");
                cell.setCellStyle(csn);

                // Case Manager name
                cell = row.createCell(i++);
                cell.setCellValue(s.getUsuarioInterviewNombre() != null ? s.getUsuarioInterviewNombre() : "");
                cell.setCellStyle(csn);

                // Scales schedule
                cell = row.createCell(i++);
                cell.setCellValue( (s.getFecha_schedule_scales()!= null && !s.getFecha_schedule_scales().isEmpty())  ? UtilidadesAdapter.formatearFechaYHoraSeparadoUS(s.getFecha_schedule_scales()) : "");
                cell.setCellStyle(csn);

                // Scales name
                cell = row.createCell(i++);
                cell.setCellValue(s.getUsuarioIntScNombre() != null ? s.getUsuarioIntScNombre() : "");
                cell.setCellStyle(csn);

                // Clinician schedule
                cell = row.createCell(i++);
                cell.setCellValue( (s.getFechaClinicianAppo()!= null && !s.getFechaClinicianAppo().isEmpty())  ? UtilidadesAdapter.formatearFechaYHoraSeparadoUS(s.getFechaClinicianAppo()) : "");
                cell.setCellStyle(csn);

                // Clinician name
                cell = row.createCell(i++);
                cell.setCellValue(s.getUsuarioClinicianNombre() != null ? s.getUsuarioClinicianNombre() : "");
                cell.setCellStyle(csn);

                // Editor name
                cell = row.createCell(i++);
                cell.setCellValue(s.getUsuarioTemplate() != null ? s.getUsuarioTemplate() : "");
                cell.setCellStyle(csn);
                
                // Important notes
                cell = row.createCell(i++);
                cell.setCellValue(s.getImportantNotes() != null ? s.getImportantNotes() : "");
                cell.setCellStyle(estilos.getOrDefault("Important", csn));
             


            }
            if(local){
            	logger.info("Sera local");
            	FileOutputStream fo = new FileOutputStream("/Users/joser.vazquez/Desktop/test.xlsx");
            	workbook.write(fo);
            	fo.close();
            	return null;
            }else{
	            workbook.write(out);
	            return out.toByteArray();
            }
        }catch(Exception e){
        	logger.info("Error:"+e.getMessage());
        	return null;
        }
    }

}
