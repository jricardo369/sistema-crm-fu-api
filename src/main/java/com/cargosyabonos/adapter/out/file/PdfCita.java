package com.cargosyabonos.adapter.out.file;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.in.SolicitudVocUseCase;
import com.cargosyabonos.application.port.out.CitaPort;
import com.cargosyabonos.application.port.out.NotaCitaPort;
import com.cargosyabonos.domain.CitaEntity;
import com.cargosyabonos.domain.NotaCitaEntity;
import com.cargosyabonos.domain.SolicitudVoc;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class PdfCita {
	
	boolean local = false;
	
	@Value("${ruta.host}")
	private String rutaHost;

	@Value("${ruta.host.qas}")
	private String rutaHostQas;

	@Value("${ruta.host.pro}")
	private String rutaHostPro;

	static Logger log = LoggerFactory.getLogger(PdfCita.class);

	private BigDecimal TotalMenosNoAplica;

	@Autowired
	private NotaCitaPort ntPort;
	
	@Autowired
	private CitaPort citaPort;
	
	@Autowired
	private SolicitudVocUseCase solVocUseCase;

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

		PdfCita p = new PdfCita();
		try {
			try {

				p.generarPdf(1,1);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public byte[] generarPdf(int idCita,int idUsuario)
			throws FileNotFoundException, DocumentException {

		CitaEntity c = citaPort.obtenerCita(idCita);
	    SolicitudVoc s = solVocUseCase.obtenerSolicitudObj(c.getIdSolicitud(), idUsuario);
	
		String coloresRGB = "94,44,126";
		String[] colors = coloresRGB.split(",");
		setColores(colors);

		byte[] pdfBytes = null;
		final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LETTER);
		document.setMargins(12, 12, 30, 20);
		document.setMarginMirroring(true);

		if(local){
			String nmb = "/Users/joser.vazquez/Downloads/";
			PdfWriter.getInstance(document, new FileOutputStream(nmb));
		}else{
			PdfWriter.getInstance(document, byteStream);
		}

		document.open();
		generaDocumentoGeneral(document, c ,s);

		document.close();

		pdfBytes = byteStream.toByteArray();
		return pdfBytes;
		
	}

	public void generaDocumentoGeneral(Document document, CitaEntity cita,SolicitudVoc solVoc) throws FileNotFoundException, DocumentException {
		crearTablaLogo(document, cita,solVoc);
		//cabecera(document,cita);
		espacio(document, 20f);
		datosInfo(document,cita);
		espacio(document, 15f);
		if (cita != null) {
				datosDetalle(document, cita);
			
		} else {
				titulo(document, "No se han cargado notas citas aun", 90);
			
			espacio(document, 5f);
		}
		espacio(document, 20f);
		espacio(document, 5f);
		espacio(document, 100f);
	}

	public void crearTablaLogo(Document document,CitaEntity s,SolicitudVoc svoc)
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

			PdfPTable tabla2 = new PdfPTable(1);
			tabla2.setWidthPercentage(90);
			tabla2.setWidths(new int[] { 10 });
			cell = null;
			tabla2.addCell(PdfUtilidad.cell("THERAPIST APPOINTMENT FOR "+svoc.getNombreTerapeuta().toUpperCase(),pdfUtil.obtenerFont(getColores(), "tituloPdf"), 0, "izquierda", "","negro"));
			

			document.add(tabla2);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public PdfPTable cabecera(Document document, CitaEntity s)
			throws FileNotFoundException, DocumentException {

		log.info("generando sección de cabecera");

		BigDecimal totalT = new BigDecimal("0.00");
		BigDecimal noAplicaT = new BigDecimal("0.00");

		totalT = totalT.subtract(noAplicaT);
		setTotalMenosNoAplica(totalT);

		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(90);
		table.setWidths(new int[] { 10});

		table.addCell(PdfUtilidad.cell("Familias Unidas Counseling Center",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell("info@familiasunidasla.com",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell("323-430-4200",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));

		document.add(table);

		return table;
	}
	
	public PdfPTable datosInfo(Document document, CitaEntity s)
			throws FileNotFoundException, DocumentException {

		log.info("generando sección de info");

		BigDecimal totalT = new BigDecimal("0.00");
		BigDecimal noAplicaT = new BigDecimal("0.00");

		totalT = totalT.subtract(noAplicaT);
		setTotalMenosNoAplica(totalT);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(90);
		table.setWidths(new int[] { 1, 1});

		table.addCell(PdfUtilidad.cell("APPOINTMENT NO. " + s.getIdCita()+" OF FILE VOC:"+s.getIdSolicitud(),pdfUtil.obtenerFont(getColores(), "negritaTitulos"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell("",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));	
		table.addCell(PdfUtilidad.cell("DATE "+s.getFecha() + " AT " +s.getHora()+" "+s.getTipo(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell("",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));

		document.add(table);

		return table;
	}

	public void datosDetalle(Document document, CitaEntity cita)
			throws FileNotFoundException, DocumentException {

		log.info("Generando sección de datos detalle de pago");
		
		PdfPTable table;

		table = new PdfPTable(2);
		table.setWidthPercentage(90);
		table.setWidths(new int[] { 2,8});
		
		String v = "";
		
					//table.addCell(PdfUtilidad.cell("APPOINTMENT NO. " + cita.getIdCita()+" OF FILE VOC:"+cita.getIdSolicitud(),pdfUtil.obtenerFont(getColores(), "negritaTitulos"), 0, "izquierda", "", "magenta"));
					//table.addCell(PdfUtilidad.cell(" ",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "magenta"));
					table.addCell(PdfUtilidad.cell("Comment schedule",pdfUtil.obtenerFont(getColores(), "negrita"), 4, "izquierda", "", "magenta"));
					table.addCell(PdfUtilidad.cell(cita.getComentario() != null ? cita.getComentario() : "",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "magenta"));
					
					 List<NotaCitaEntity> notasCitas = ntPort.obtenerNotasCitas(cita.getIdCita());
					 //datosDetalleDeDetalle(document, notas);
					 
					 if (notasCitas != null) {
							
							if (!notasCitas.isEmpty()) {
								
								for (NotaCitaEntity n : notasCitas) {
									
									v="";
									table.addCell(PdfUtilidad.cell(" ",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(" ",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell("NOTE", pdfUtil.obtenerFont(getColores(), "negritaTitulos"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell(" ",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(" ",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(" ",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell("Date",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(n.getFechaCreacion(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Type",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(n.getTipo(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Refered by",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(n.getReferencia(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Lentht session",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(n.getTiempoSesion(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Main focus Therapy",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(n.getEnfoquePrincipal(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Si an Hi Assegned?",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									v = n.isSiHiAsignado() == true ? "Yes" : "No";
									table.addCell(PdfUtilidad.cell(v,pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Location Verified at the beginning of session?",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									v = n.isLocacionVerificada() == true ? "Yes" : "No";
									table.addCell(PdfUtilidad.cell(v,pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell(" ",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(" ",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "magenta"));
									
									table.addCell(PdfUtilidad.cell("Symptoms/Behaivor",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell(n.getSintomaComportamiento1(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Raiting",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(n.getRating1(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									
									table.addCell(PdfUtilidad.cell("Symptoms/Behaivor",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(n.getSintomaComportamiento2(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Raiting",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(n.getRating2(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									
									table.addCell(PdfUtilidad.cell("Symptoms/Behaivor ",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(n.getSintomaComportamiento3(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Raiting",pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "magenta"));
									table.addCell(PdfUtilidad.cell(n.getRating3(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									
									table.addCell(PdfUtilidad.cell("Content of the Therapy Session", pdfUtil.obtenerFont(getColores(), "negrita"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell(n.getTipoContenidoSesion(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Comments", pdfUtil.obtenerFont(getColores(), "negrita"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell(n.getComentarios(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Response to Intervention", pdfUtil.obtenerFont(getColores(), "negrita"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell(n.getRespuestaDeIntervencion(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("Summary of your assessment of the client´s progess towards TX goals", pdfUtil.obtenerFont(getColores(), "negrita"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell(n.getResumenEvaluacion(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell("TX plan for the immediate future", pdfUtil.obtenerFont(getColores(), "negrita"), 0,"izquierda", "", "negro"));
									table.addCell(PdfUtilidad.cell(n.getPlanFuturo(), pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
									
									ntPort.obtenerNotasCitas(cita.getIdCita());

								}
							}
							
						}
					 
					 
					 if(notasCitas == null || notasCitas.isEmpty()){
							
							table.addCell(PdfUtilidad.cell("This schedule has no notes " ,pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
							table.addCell(PdfUtilidad.cell("",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
							
						}

		
		

		document.add(table);
	}

	public void titulo(Document document, String titulo, int tamaño) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(tamaño);
		table.setWidths(new int[] { 2, 1, 1, 1, });
		table.addCell(PdfUtilidad.cell(titulo, pdfUtil.obtenerFont(getColores(), "negritaTitulos"), 0, "izquierda", "","negro"));
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

}
