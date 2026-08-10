package com.cargosyabonos.adapter.out.file;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.in.SolicitudVocUseCase;
import com.cargosyabonos.domain.ReporteNotasCitasVoc;
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
public class ReporteNotasCitasVocPdf {

	boolean local = false;

	static Logger log = LoggerFactory.getLogger(ReporteNotasCitasVocPdf.class);

	@Autowired
	private SolicitudVocUseCase solVocUseCase;

	@Autowired
	private PdfUtilidad pdfUtil;

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

		ReporteNotasCitasVocPdf p = new ReporteNotasCitasVocPdf();
		try {
			try {
				p.generarPdf("2026-07-01", "2026-07-31");
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public byte[] generarPdf(String fechai, String fechaf)
			throws FileNotFoundException, DocumentException {

		List<ReporteNotasCitasVoc> reporte = solVocUseCase.obtenerReporteNotasCitasRangoFechas(fechai, fechaf);
		System.out.println("ReporteNotasCitasVoc:" + reporte.size());

		String coloresRGB = "94,44,126";
		String[] colors = coloresRGB.split(",");
		setColores(colors);

		byte[] pdfBytes = null;
		final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LETTER);
		document.setMargins(12, 12, 30, 20);
		document.setMarginMirroring(true);

		if (local) {
			String nmb = "/Users/joser.vazquez/Downloads/";
			PdfWriter.getInstance(document, new FileOutputStream(nmb));
		} else {
			PdfWriter.getInstance(document, byteStream);
		}

		document.open();
		generaDocumentoGeneral(document, reporte, fechai, fechaf);

		document.close();

		pdfBytes = byteStream.toByteArray();
		return pdfBytes;

	}

	public void generaDocumentoGeneral(Document document, List<ReporteNotasCitasVoc> reporte, String fechai, String fechaf)
			throws FileNotFoundException, DocumentException {
		crearTablaLogo(document);
		espacio(document, 20f);
		titulo(document, "Reporte notas citas del " + fechai + " al " + fechaf, 90);
		if (reporte != null) {
			log.info("Tamaño reporte:" + reporte.size());
			if (!reporte.isEmpty()) {
				datosDetalle(document, reporte);
			}
		} else {
			titulo(document, "No se encontraron datos", 90);
			espacio(document, 5f);
		}
		espacio(document, 20f);
		espacio(document, 5f);
		espacio(document, 100f);
	}

	public void crearTablaLogo(Document document)
			throws DocumentException {

		log.info("generando sección de logo de notas citas");

		String r = rutaLogo() + "/logo.png";

		try {

			PdfPTable tabla = new PdfPTable(2);
			tabla.setWidthPercentage(100);
			tabla.setWidths(new int[] { 95, 5 });
			PdfPCell cell = null;
			Image img = Image.getInstance(r);
			img.setWidthPercentage(40);

			cell = new PdfPCell(img, true);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			cell.setFixedHeight(50f);
			tabla.addCell(cell);

			tabla.addCell(
					PdfUtilidad.cell("", pdfUtil.obtenerFont(getColores(), "tituloPdf"), 0, "izquierda", "", "negro"));

			document.add(tabla);

			PdfPTable tabla2 = new PdfPTable(1);
			tabla2.setWidthPercentage(90);
			tabla2.setWidths(new int[] { 10 });
			cell = null;
			tabla2.addCell(PdfUtilidad.cell("Notes and Appointments Report", pdfUtil.obtenerFont(getColores(), "tituloPdf"),
					0, "izquierda", "", "negro"));

			document.add(tabla2);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void datosDetalle(Document document, List<ReporteNotasCitasVoc> reporte)
			throws FileNotFoundException, DocumentException {

		log.info("Generando sección de datos detalle de notas citas");

		espacio(document, 20f);

		espacio(document, 3f);
		PdfPTable table;

		table = new PdfPTable(2);
		table.setWidthPercentage(90);
		table.setWidths(new int[] { 3, 1 });

		table.addCell(PdfUtilidad.cell("Total notas citas", pdfUtil.obtenerFont(getColores(), "negrita"), 5, "izquierda",
				"gris", "negro"));
		table.addCell(PdfUtilidad.cell("Total notas descarga", pdfUtil.obtenerFont(getColores(), "negrita"), 5,
				"izquierda", "gris", "negro"));

		if (reporte != null) {

			if (!reporte.isEmpty()) {

				for (ReporteNotasCitasVoc r : reporte) {

					table.addCell(PdfUtilidad.cell(r.getTotalNotasCitas(),
							pdfUtil.obtenerFont(getColores(), "negrita"), 5, "izquierda", "", "negro"));
					table.addCell(PdfUtilidad.cell(r.getTotalNotasDescarga(),
							pdfUtil.obtenerFont(getColores(), "negrita"), 5, "izquierda", "", "negro"));

				}
			}
		}

		document.add(table);
	}

	public void titulo(Document document, String titulo, int tamaño) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(tamaño);
		table.setWidths(new int[] { 2, 1, 1, 1, });
		table.addCell(
				PdfUtilidad.cell(titulo, pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "",
						"negro"));
		table.addCell(PdfUtilidad.cell("", pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "negro"));
		table.addCell(PdfUtilidad.cell("", pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "negro"));
		table.addCell(PdfUtilidad.cell("", pdfUtil.obtenerFont(getColores(), "negrita"), 0, "izquierda", "", "negro"));
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

	public String rutaLogo() {
		String rutaLogoFinal = "";
		switch (ambiente) {
			case "qas":
				rutaLogoFinal = logoQAS;
				break;
			case "pro":
				rutaLogoFinal = logoPRO;
				break;
			case "local":
				rutaLogoFinal = logo;
				break;
		}
		return rutaLogoFinal;
	}

}
