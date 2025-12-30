package com.cargosyabonos.adapter.out.file;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.CitaPort;
import com.cargosyabonos.application.port.out.SolicitudVocPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.CitaSql;
import com.cargosyabonos.domain.SolicitudVocEntity;
import com.cargosyabonos.domain.UsuarioEntity;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class PdfProgressLetterV2 {
	
	boolean local = false;
	
	@Value("${ruta.host}")
	private String rutaHost;

	@Value("${ruta.host.qas}")
	private String rutaHostQas;

	@Value("${ruta.host.pro}")
	private String rutaHostPro;

	static Logger log = LoggerFactory.getLogger(PdfProgressLetterV2.class);

	private BigDecimal TotalMenosNoAplica;

	@Autowired
	private CitaPort citasPort;
	
	@Autowired
	private SolicitudVocPort solVocPort;
	
	@Autowired
	private UsuariosPort usPort;

	@Autowired
	private PdfUtilidad pdfUtil;

	@Value("${pdf.font}")
	private static String rutaFont;

	@Value("${rutaArchivos}")
	private String rutaArchivos;

	@Value("${rutaArchivos.qas}")
	private String rutaArchivosQAS;

	@Value("${rutaArchivos.pro}")
	private String rutaArchivosPRO;
	
	@Value("${pdf.logo}")
	private String logo;

	@Value("${pdf.logoQas}")
	private String logoQAS;

	@Value("${pdf.logoPro}")
	private String logoPRO;

	@Value("${ambiente}")
	private String ambiente;

	private String[] colores;

	public String[] getColores() {
		return colores;
	}

	public void setColores(String[] colores) {
		this.colores = colores;
	}

	public static void main(String args[]) {

		PdfProgressLetterV2 p = new PdfProgressLetterV2();
		try {
			try {

				p.generarPdf(1);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public byte[] generarPdf(int idSolicitud)
			throws FileNotFoundException, DocumentException {

		SolicitudVocEntity s = solVocPort.obtenerSolicitud(idSolicitud);
		List<CitaSql> citas = null;
		citas = citasPort.obtenerCitasPorSolicitudSinNoShow(idSolicitud);
		System.out.println("Citas:"+citas.size());
	
		String coloresRGB = "94,44,126";
		String[] colors = coloresRGB.split(",");
		setColores(colors);
		

		byte[] pdfBytes = null;
		final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LETTER);
		document.setMargins(12, 12, 30, 20);
		document.setMarginMirroring(true);

		PdfWriter writer = null;
		if(local){
			String nmb = "/Users/joser.vazquez/Downloads/";
			writer = PdfWriter.getInstance(document, new FileOutputStream(nmb));
		}else{
			writer = PdfWriter.getInstance(document, byteStream);
		}
		
		// Agregar evento de página personalizada
        writer.setPageEvent(new Footer());

		document.open();
		generaDocumentoGeneral(document, s, citas);

		document.close();

		pdfBytes = byteStream.toByteArray();
		return pdfBytes;
		
	}

	public void generaDocumentoGeneral(Document document, SolicitudVocEntity solicitud,List<CitaSql> citas) throws FileNotFoundException, DocumentException {		
		crearTablaLogo(document, solicitud);
		fecha(document);
		espacio(document, 20f);
		cabecera(document,solicitud);
		espacio(document, 20f);
		datos(document,solicitud,citas);
		espacio(document, 20f);
		espacio(document, 20f);
		espacio(document, 100f);
	}

	public void crearTablaLogo(Document document,SolicitudVocEntity s)
			throws DocumentException {

		log.info("generando sección de logo de viatico");

		//String l = "assets/img/main-logo.png";
		String r = rutaLogo()+"/logo.png";
		
		try {

			PdfPTable tabla = new PdfPTable(2);
			tabla.setWidthPercentage(100);
			tabla.setWidths(new int[] { 95,5 });
			PdfPCell cell = null;
			//System.out.println("ruta logo:" + rutaServer() + l);
			//Image img = Image.getInstance(rutaServer() + l);
			Image img = Image.getInstance(r);
			img.setWidthPercentage(40);

			cell = new PdfPCell(img, true);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			cell.setFixedHeight(50f);
			tabla.addCell(cell);
			
			tabla.addCell(PdfUtilidad.cell("",pdfUtil.obtenerFont(getColores(), "tituloPdf"), 0, "izquierda", "","negro"));
			
			document.add(tabla);

			/*PdfPTable tabla2 = new PdfPTable(1);
			tabla2.setWidthPercentage(90);
			tabla2.setWidths(new int[] { 10 });
			cell = null;
			tabla2.addCell(PdfUtilidad.cell("PROGRESS LETTER",pdfUtil.obtenerFont(getColores(), "tituloPdf"), 0, "izquierda", "","negro"));

			document.add(tabla2);*/

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public PdfPTable cabecera(Document document, SolicitudVocEntity s)
			throws FileNotFoundException, DocumentException {

		log.info("generando sección de cabecera");

		BigDecimal totalT = new BigDecimal("0.00");
		BigDecimal noAplicaT = new BigDecimal("0.00");

		totalT = totalT.subtract(noAplicaT);
		setTotalMenosNoAplica(totalT);

		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(90);
		table.setWidths(new int[] { 10});	
		
		table.addCell(PdfUtilidad.cell("Department Victm of a Crime",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell("Re: "+s.getNombreClienteCompleto(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		String dob = s.getFechaNacimiento() == null ? "" : UtilidadesAdapter.formatearFechaStringAtipoUS(s.getFechaNacimiento());
		table.addCell(PdfUtilidad.cell("DOB: " +dob,pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell("In compliance with VOC referral, "+s.getNombreClienteCompleto()+" receives services at Familias Unidas. "
				+ "He enrolled for services on "+ UtilidadesAdapter.formatearFechaUS(s.getFechaInicio())+".",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));

		document.add(table);

		return table;
	}
	
	public PdfPTable fecha(Document document)
			throws FileNotFoundException, DocumentException {
		
		BigDecimal totalT = new BigDecimal("0.00");
		BigDecimal noAplicaT = new BigDecimal("0.00");

		totalT = totalT.subtract(noAplicaT);
		setTotalMenosNoAplica(totalT);

		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(90);
		table.setWidths(new int[] { 10});
		
		table.addCell(PdfUtilidad.cell(UtilidadesAdapter.fechaEnLetraEnghish(UtilidadesAdapter.fechaActual()),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));

		document.add(table);

		return table;
	}
	
	
	
	
	public PdfPTable datos(Document document, SolicitudVocEntity s,List<CitaSql> citas)
			throws FileNotFoundException, DocumentException {

		log.info("generando sección de cabecera");

		BigDecimal totalT = new BigDecimal("0.00");
		BigDecimal noAplicaT = new BigDecimal("0.00");

		totalT = totalT.subtract(noAplicaT);
		setTotalMenosNoAplica(totalT);

		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(90);
		table.setWidths(new int[] { 10});
		
		DateTimeFormatter entrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter salida = DateTimeFormatter.ofPattern("MM/dd/yyyy");

     // Concatenar fechas formateadas separadas por espacio
        String resultado = citas.stream()
                .map(p -> {
                    LocalDate fecha = LocalDate.parse(p.getfecha(), entrada);
                    return fecha.format(salida);
                })
                .collect(Collectors.joining(", "));
		
		table.addCell(PdfUtilidad.cell("SERVICES & ATTENDANCE:",pdfUtil.obtenerFont(getColores(), "negritaSubrayada"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell("Individual therapy and has completed "+citas.size()+" up to date: "+resultado,pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		document.add(table);
		espacio(document, 20f);
		
		PdfPTable table2 = new PdfPTable(1);
		table2.setWidthPercentage(90);
		table2.setWidths(new int[] { 10});
		table2.addCell(PdfUtilidad.cell("PURPOSE OF TREATMENT (SYMPTOMS, TOPICS, & GOALS):",pdfUtil.obtenerFont(getColores(), "negritaSubrayada"), 0, "izquierda", "","negro"));
		table2.addCell(PdfUtilidad.cell(s.getPurposeTreatament() == null ?  "N/A" : s.getPurposeTreatament(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		document.add(table2);
		espacio(document, 20f);
		
		PdfPTable table3 = new PdfPTable(1);
		table3.setWidthPercentage(90);
		table3.setWidths(new int[] { 10});
		table3.addCell(PdfUtilidad.cell("PARTICIPATION & ENGAGEMENT:",pdfUtil.obtenerFont(getColores(), "negritaSubrayada"), 0, "izquierda", "","negro"));
		table3.addCell(PdfUtilidad.cell(s.getParticipation() == null ?  "N/A" : s.getParticipation(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		document.add(table3);
		espacio(document, 20f);
		
		PdfPTable table4 = new PdfPTable(1);
		table4.setWidthPercentage(90);
		table4.setWidths(new int[] { 10});
		table4.addCell(PdfUtilidad.cell("RECOMMENDATIONS:",pdfUtil.obtenerFont(getColores(), "negritaSubrayada"), 0, "izquierda", "","negro"));
		table4.addCell(PdfUtilidad.cell(s.getRecommendations() == null ?  "N/A" : s.getRecommendations(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		document.add(table4);
		espacio(document, 20f);
		
		PdfPTable table5 = new PdfPTable(1);
		table5.setWidthPercentage(90);
		table5.setWidths(new int[] { 10});
		table5.addCell(PdfUtilidad.cell("Please do not hesitate to contact me, or our License Clinical Supervisor if further clarification is needed.",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		document.add(table5);
		espacio(document, 20f);
		
		PdfPTable table6 = new PdfPTable(1);
		table6.setWidthPercentage(90);
		table6.setWidths(new int[] { 10});
		table6.addCell(PdfUtilidad.cell("Best Regards,",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		document.add(table6);
		espacio(document, 20f);
		
		UsuarioEntity u = usPort.buscarPorId(s.getTerapeuta());
		
		if (u.isWithSupervision()) {
			
			if (s.getTerapeuta() > 0) {
				
				espacio(document, 20f);
				PdfPTable table7 = new PdfPTable(1);
				table7.setWidthPercentage(90);
				table7.setWidths(new int[] { 10 });

				table7.addCell(PdfUtilidad.cell(u.getNombre() == null ? "" : u.getNombre(),pdfUtil.obtenerFont(getColores(), "cursiva"), 0, "izquierda", "", "negro"));
				table7.addCell(PdfUtilidad.cell(u.getNombre() == null ? "" : u.getNombre()+", AMFT",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
				table7.addCell(PdfUtilidad.cell("Associate Marriage and Family Therapist", pdfUtil.obtenerFont(getColores(), "normal"),0, "izquierda", "", "negro"));
				document.add(table7);
				
			}
			
			espacio(document, 20f);
			espacio(document, 20f);

			PdfPTable table8 = new PdfPTable(1);
			table8.setWidthPercentage(90);
			table8.setWidths(new int[] { 10 });
			table8.addCell(PdfUtilidad.cell("__________________________________",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
			table8.addCell(PdfUtilidad.cell(u.getSupervisor()+", LMFT", pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
			table8.addCell(PdfUtilidad.cell("Licensed Marriage and Family Therapist", pdfUtil.obtenerFont(getColores(), "normal"),0, "izquierda", "", "negro"));
			document.add(table8);
		}else{
			
			espacio(document, 20f);
			PdfPTable table7 = new PdfPTable(1);
			table7.setWidthPercentage(90);
			table7.setWidths(new int[] { 10 });

			table7.addCell(PdfUtilidad.cell(u.getNombre() == null ? "" : u.getNombre(),pdfUtil.obtenerFont(getColores(), "cursiva"), 0, "izquierda", "", "negro"));
			table7.addCell(PdfUtilidad.cell(u.getNombre() == null ? "" : u.getNombre()+", LMFT",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
			table7.addCell(PdfUtilidad.cell("Licensed Marriage and Family Therapist", pdfUtil.obtenerFont(getColores(), "normal"),0, "izquierda", "", "negro"));
			document.add(table7);
			
		}
		
		return table;
	}

	
	
	

	public void titulo(Document document, String titulo, int tamaño) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(tamaño);
		table.setWidths(new int[] { 2, 1, 1, 1, });
		table.addCell(
				PdfUtilidad.cell(titulo, pdfUtil.obtenerFont(getColores(), "negritaTitulos"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell("", pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell("", pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell("", pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "","negro"));
		document.add(table);
	}

	public static void espacio(Document document, float altura) throws FileNotFoundException, DocumentException {

		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		table.setWidths(new int[] { 20 });

		PdfPCell cell = new PdfPCell();
		cell.setFixedHeight(altura);
		cell.setBorder(0);
		table.addCell(cell);

		document.add(table);
	}

	public BigDecimal getTotalMenosNoAplica() {
		return TotalMenosNoAplica;
	}

	public void setTotalMenosNoAplica(BigDecimal totalMenosNoAplica) {
		TotalMenosNoAplica = totalMenosNoAplica;
	}
	
	public String rutaServer() {
		String rutaHostFinal = "";
		switch (ambiente) {
		case "qas":
			rutaHostFinal = rutaHostQas;
			break;
		case "pro":
			rutaHostFinal = rutaHostPro;
			break;
		case "test":
			rutaHostFinal = rutaHost;
			break;
		}
		return rutaHostFinal;
	}
	
	public String rutaLogo() {
		String rutaLogoFinal = "";
		switch (ambiente) {
		case "qas":
			rutaLogoFinal = logoQAS;
			break;
		case "pro":
			rutaLogoFinal = logoPRO;
			break;
		case "test":
			rutaLogoFinal = logo;
			break;
		}
		return rutaLogoFinal;
	}
	
	static class Footer extends PdfPageEventHelper {
		PdfUtilidad a = new PdfUtilidad();
		
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
        	Font f = a.obtenerFont(null, "pequenia");
        	//Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL );
            PdfContentByte cb = writer.getDirectContent();
            
            float x = (document.right() + document.left()) / 2;
            float y = document.bottom() + 15;  // Más arriba que el default (20 o menos)

            // Línea 1: texto personalizado
            Phrase line1 = new Phrase("5717 E. Beverly Blvd Los Angeles, CA 90022 | Office: 323-430-4200 Fax: 323-430-4222", f);
            // Línea 2: número de página
            Phrase line2 = new Phrase("www.FamiliasUnidasLA.com |  info@FamiliasUnidasLA.com" + writer.getPageNumber(), f);

            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, line1, x, y + 10, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, line2, x, y, 0);
        }
	}

}
