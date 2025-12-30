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

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.MovimientosPort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.domain.MovimientoEntity;
import com.cargosyabonos.domain.SolicitudEntity;
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
public class PdfPagos {
	
	boolean local = false;
	
	@Value("${ruta.host}")
	private String rutaHost;

	@Value("${ruta.host.qas}")
	private String rutaHostQas;

	@Value("${ruta.host.pro}")
	private String rutaHostPro;

	static Logger log = LoggerFactory.getLogger(PdfPagos.class);

	private BigDecimal TotalMenosNoAplica;

	@Autowired
	private MovimientosPort movPort;
	
	@Autowired
	private SolicitudPort solPort;

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

		PdfPagos p = new PdfPagos();
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

		SolicitudEntity s = solPort.obtenerSolicitud(idSolicitud);
		List<MovimientoEntity> movs = null;
			movs = movPort.obtenerMovimientos(idSolicitud);
			System.out.println("Movs:"+movs.size());
		
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
		generaDocumentoGeneral(document, s, movs);

		document.close();

		pdfBytes = byteStream.toByteArray();
		return pdfBytes;
		
	}

	public void generaDocumentoGeneral(Document document, SolicitudEntity solicitud,List<MovimientoEntity> movimientos) throws FileNotFoundException, DocumentException {
		crearTablaLogo(document, solicitud);
		//espacio(document, 20f);
		cabecera(document,solicitud);
		
		espacio(document, 20f);
		datosInvoice(document,solicitud);
		espacio(document, 20f);
		datosClt(document,solicitud);
		espacio(document, 20f);
		if (movimientos != null) {
			log.info("Tamaño movimientos:" + movimientos.size());
			if (!movimientos.isEmpty()) {
				datosDetalle(document, movimientos);
			}
		} else {
				titulo(document, "No se han cargado movimientos aun", 90);
			
			espacio(document, 5f);
		}
		espacio(document, 20f);

		espacio(document, 5f);
		espacio(document, 100f);
	}

	public void crearTablaLogo(Document document,SolicitudEntity s)
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
			tabla2.addCell(PdfUtilidad.cell("INVOICE",pdfUtil.obtenerFont(getColores(), "tituloPdf"), 0, "izquierda", "","negro"));
			

			document.add(tabla2);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public PdfPTable cabecera(Document document, SolicitudEntity s)
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
	
	public PdfPTable datosInvoice(Document document, SolicitudEntity s)
			throws FileNotFoundException, DocumentException {

		log.info("generando sección de cabecera");

		BigDecimal totalT = new BigDecimal("0.00");
		BigDecimal noAplicaT = new BigDecimal("0.00");

		totalT = totalT.subtract(noAplicaT);
		setTotalMenosNoAplica(totalT);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(90);
		table.setWidths(new int[] { 1, 1});

		table.addCell(PdfUtilidad.cell("INVOICE #:" + s.getIdSolicitud(),pdfUtil.obtenerFont(getColores(), "normal"), 3, "izquierda", "","negro"));
		
		table.addCell(PdfUtilidad.cell("INVOICE DATE:"+UtilidadesAdapter.fechaActual(),pdfUtil.obtenerFont(getColores(), "normal"), 3, "izquierda", "","negro"));

		document.add(table);

		return table;
	}
	
	public PdfPTable datosClt(Document document, SolicitudEntity s)
			throws FileNotFoundException, DocumentException {

		log.info("generando sección de cabecera");

		BigDecimal totalT = new BigDecimal("0.00");
		BigDecimal noAplicaT = new BigDecimal("0.00");

		totalT = totalT.subtract(noAplicaT);
		setTotalMenosNoAplica(totalT);

		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(90);
		table.setWidths(new int[] { 10});

		table.addCell(PdfUtilidad.cell("SHIP TO:",pdfUtil.obtenerFont(getColores(), "negrita"), -2, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell(s.getNombreClienteCompleto(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell(s.getDireccion()+","+s.getEstado(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell(s.getTelefono(),pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "","negro"));
		table.addCell(PdfUtilidad.cell(s.getEmail() == null ? "" : s.getEmail(),pdfUtil.obtenerFont(getColores(), "normal"), -1, "izquierda", "","negro"));


		document.add(table);

		return table;
	}

	public void datosDetalle(Document document, List<MovimientoEntity> movimientos)
			throws FileNotFoundException, DocumentException {

		log.info("Generando sección de datos detalle de pago");

		espacio(document, 20f);
		//titulo(document, "Billings", 90);

		espacio(document, 3f);
		PdfPTable table;

		table = new PdfPTable(5);
		table.setWidthPercentage(90);
		table.setWidths(new int[] { 1, 1, 1, 1, 1 });
		
		table.addCell(PdfUtilidad.cell("FOLIO", pdfUtil.obtenerFont(getColores(), "tituloTablaBlanco"), 5, "izquierda", "negro","negro"));
		table.addCell(PdfUtilidad.cell("DATE", pdfUtil.obtenerFont(getColores(), "tituloTablaBlanco"), 5, "izquierda","negro", "negro"));
		table.addCell(PdfUtilidad.cell("DESCRIPTION", pdfUtil.obtenerFont(getColores(), "tituloTablaBlanco"), 5, "izquierda","negro", "negro"));
		table.addCell(PdfUtilidad.cell("PAYMENT METHOD", pdfUtil.obtenerFont(getColores(), "tituloTablaBlanco"), 5, "izquierda", "negro","negro"));
		table.addCell(PdfUtilidad.cell("AMOUNT", pdfUtil.obtenerFont(getColores(), "tituloTablaBlanco"), 5, "izquierda","negro", "negro"));

		BigDecimal total = BigDecimal.ZERO;
		if (movimientos != null) {
			
			if (!movimientos.isEmpty()) {
				
				for (MovimientoEntity m : movimientos) {
					
					table.addCell(PdfUtilidad.cell(m.getFolio() != null ? m.getFolio() : "", pdfUtil.obtenerFont(getColores(), "normal"), 5,"izquierda", "", "negro"));
					table.addCell(PdfUtilidad.cell(UtilidadesAdapter.formatearFecha(m.getFecha()),pdfUtil.obtenerFont(getColores(), "normal"), 5, "izquierda", "", "negro"));
					table.addCell(PdfUtilidad.cell(m.getDescripcion() != null ? m.getDescripcion() : "",pdfUtil.obtenerFont(getColores(), "normal"), 5, "izquierda", "", "negro"));
					table.addCell(PdfUtilidad.cell(m.getTipoPago().getNombre(),pdfUtil.obtenerFont(getColores(), "normal"), 5, "izquierda", "", "negro"));
					table.addCell(PdfUtilidad.cell(m.getMonto(),pdfUtil.obtenerFont(getColores(), "normal"), 5, "izquierda", "", "negro"));
					total = total.add(m.getMonto());

				}
				table.addCell(PdfUtilidad.cell("", pdfUtil.obtenerFont(getColores(), "normal"), 0,"izquierda", "", "negro"));
				table.addCell(PdfUtilidad.cell("",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
				table.addCell(PdfUtilidad.cell("",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
				table.addCell(PdfUtilidad.cell("",pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
				table.addCell(PdfUtilidad.cell("Total:"+total,pdfUtil.obtenerFont(getColores(), "normal"), 0, "izquierda", "", "negro"));
			}
		}
		

		document.add(table);
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

}
