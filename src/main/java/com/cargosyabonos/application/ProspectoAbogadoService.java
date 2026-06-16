package com.cargosyabonos.application;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.adapter.out.sql.AbogadoRepository;
import com.cargosyabonos.adapter.out.sql.NotaProspectoAbogadoRepository;
import com.cargosyabonos.application.port.in.ProspectoAbogadoUseCase;
import com.cargosyabonos.application.port.out.ProspectoAbogadoPort;
import com.cargosyabonos.domain.Abogado;
import com.cargosyabonos.domain.EmailProspectoAbogadoEntity;
import com.cargosyabonos.domain.NotaProspectoAbogadoEntity;
import com.cargosyabonos.domain.ProspectoAbogado;
import com.cargosyabonos.domain.ProspectoAbogadoEntity;

@Service
public class ProspectoAbogadoService implements ProspectoAbogadoUseCase {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern FORMULA_CELL_REF_PATTERN = Pattern.compile("(?:'[^']+'!)?\\$?([A-Z]+)\\$?(\\d+)");

    Logger log = LoggerFactory.getLogger(ProspectoAbogadoService.class);

    @Autowired
    private ProspectoAbogadoPort prosAbPort;

    @Autowired
    private AbogadoRepository aboPort;

    @Autowired
    private NotaProspectoAbogadoRepository notaAbogadoPort;

    @Autowired
    private EmailProspectoAbogadoService emProsAboPort;

    @Override
    public List<ProspectoAbogado> obtenerProspectosAbogado(String id, String fechai, String fechaf, int idUsuario,
            String estatus, String estado, String telefono, String lawFirmOrContactName, String email,
            String mailPackageReceived, String emailSent, String porcentaje20, String followUp20Porcent,
            String prospectSentClient) {
        return prosAbPort.obtenerProspectosAbogado(0, id, fechai, fechaf, idUsuario, estatus, false, estado, telefono,
                lawFirmOrContactName, email, mailPackageReceived, emailSent, porcentaje20, followUp20Porcent,
                prospectSentClient);
    }

    @Override
    public ProspectoAbogado obtenerProspectoAbogado(int idProspectoAbogado) {

        ProspectoAbogado result = null;
        if (idProspectoAbogado != 0) {
            result = prosAbPort.obtenerProspectosAbogado(idProspectoAbogado, "", "", "", 0, "", true, "", "", "", "",
                    "", "", "", "", "").get(0);
        }

        return result;

    }

    @Override
    public ProspectoAbogado crearProspectoAbogado(ProspectoAbogado o, String emailsAbogado) {
        // Al menos uno de los dos debe estar lleno: nombre o firma
        boolean nombreVacio = (o.getNombre() == null || o.getNombre().trim().isEmpty());
        boolean firmaVacia = (o.getFirma() == null || o.getFirma().trim().isEmpty());
        if (nombreVacio && firmaVacia) {
            throw new RuntimeException("At least one of 'name' or 'firm' is required");
        }
        if (o.getTelefono() == null || o.getTelefono().isEmpty()) {
            throw new RuntimeException("The phone of the lawyer is required");
        }
        Abogado abogado = aboPort.obtenerSiExisteNombreAbogado(o.getNombre());
        if (abogado != null) {
            log.info("Ya existe el abogado con nombre:" + o.getNombre());
            throw new RuntimeException(
                    "The lawyer's name (" + o.getNombre() + ") is already in the active lawyers system");
        }

        if (emailsAbogado != null && !emailsAbogado.trim().isEmpty()) {
            String[] emailsArray = emailsAbogado.split(",");
            for (String email : emailsArray) {
                EmailProspectoAbogadoEntity correoExistente = emProsAboPort.obtenerEmailProspectoAbogado(email);
                if (correoExistente != null) {
                    throw new RuntimeException("The email (" + email + ") is already registered");
                }
            }
        }

        o.setIdEstatusProspecto(1);
        ProspectoAbogadoEntity oE = convertirARequestEntity(o, true);
        ProspectoAbogadoEntity prospectoNuevo = prosAbPort.crearProspectoAbogado(oE);

        if (emailsAbogado != null && !emailsAbogado.trim().isEmpty()) {
            String[] emailsArray = emailsAbogado.split(",");
            for (String email : emailsArray) {
                UtilidadesAdapter.pintarLog("Email procesado: " + email);
                EmailProspectoAbogadoEntity eae = new EmailProspectoAbogadoEntity();
                eae.setIdProspectoAbogado(oE.getIdProspectoAbogado());
                eae.setEmail(email);
                emProsAboPort.crearEmailProspectoAbogado(eae);
            }
        }

        ProspectoAbogado p = prosAbPort.obtenerProspectosAbogado(prospectoNuevo.getIdProspectoAbogado(), "", "", "", 0,
                "", true, "", "", "", "", "", "", "", "", "").get(0);
        return p;

    }

    @Override
    public void actualizarProspectoAbogado(ProspectoAbogado o, int idUsuario, String motivo) {

        ProspectoAbogadoEntity existingEntity = prosAbPort.findByIdProspectoAbogado(o.getIdProspectoAbogado());
        if (existingEntity != null) {

            if (existingEntity.isEnvioCorreo() == false && o.isEnvioCorreo() == true) {
                try {
                    String fecha = UtilidadesAdapter.generarFecha(true, false, false, "", 0, null);
                    o.setFechaEnvioCorreo(fecha);
                    NotaProspectoAbogadoEntity nota = new NotaProspectoAbogadoEntity();
                    nota.setIdProspectoAbogado(o.getIdProspectoAbogado());
                    nota.setDescripcion("Email sent to prospect");
                    nota.setFechaCreacion(fecha);
                    String hora = UtilidadesAdapter.generarFecha(false, true, false, "", 0, null).substring(0, 5);
                    nota.setHora(hora);
                    nota.setIdUsuario(idUsuario);
                    notaAbogadoPort.crearNotaProspecto(nota);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                if (o.isEnvioCorreo() == false) {
                    o.setFechaEnvioCorreo("");
                }
            }

            // log.info("existingEntity.isMailPackageReceived():"+existingEntity.isMailPackageReceived());
            // log.info("o.isMailPackageReceived():"+o.isMailPackageReceived());
            if (existingEntity.isMailPackageReceived() == false && o.isMailPackageReceived() == true) {
                try {
                    String fecha = UtilidadesAdapter.generarFecha(true, false, false, "", 0, null);
                    o.setFechaEnvioCorreo(fecha);
                    NotaProspectoAbogadoEntity nota = new NotaProspectoAbogadoEntity();
                    nota.setIdProspectoAbogado(o.getIdProspectoAbogado());
                    nota.setDescripcion("Mail package received");
                    nota.setFechaCreacion(fecha);
                    String hora = UtilidadesAdapter.generarFecha(false, true, false, "", 0, null).substring(0, 5);
                    nota.setHora(hora);
                    nota.setIdUsuario(idUsuario);
                    notaAbogadoPort.crearNotaProspecto(nota);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                if (o.isMailPackageReceived() == false) {
                    o.setFechaEnvioCorreo("");
                }
            }

            // log.info("existingEntity.isPorcentaje1():"+existingEntity.isPorcentaje1());
            // log.info("o.isPorcentaje1():"+o.isPorcentaje1());
            if (existingEntity.isPorcentaje1() == false && o.isPorcentaje1() == true) {
                try {
                    String fecha = UtilidadesAdapter.generarFecha(true, false, false, "", 0, null);
                    o.setFechaPorcentaje1(fecha);
                    NotaProspectoAbogadoEntity nota = new NotaProspectoAbogadoEntity();
                    nota.setIdProspectoAbogado(o.getIdProspectoAbogado());
                    nota.setDescripcion("Percentage 20% checked");
                    nota.setFechaCreacion(fecha);
                    String hora = UtilidadesAdapter.generarFecha(false, true, false, "", 0, null).substring(0, 5);
                    nota.setHora(hora);
                    nota.setIdUsuario(idUsuario);
                    notaAbogadoPort.crearNotaProspecto(nota);
                    if (existingEntity.getFechaRecordatorioLiaison() == null
                            || existingEntity.getFechaRecordatorioLiaison().isEmpty()) {
                        Date fechaRecordatorio = UtilidadesAdapter.sumarDiasAFecha(UtilidadesAdapter.fechaActualDate(),
                                15);
                        o.setFechaRecordatorioLiaison(UtilidadesAdapter.formatearFecha(fechaRecordatorio));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                if (o.isPorcentaje1() == false) {
                    o.setFechaPorcentaje1("");
                    o.setFechaRecordatorioLiaison("");
                }
            }

            if (existingEntity.isPorcentaje2() == false && o.isPorcentaje2() == true) {
                try {
                    String fecha = UtilidadesAdapter.generarFecha(true, false, false, "", 0, null);
                    o.setFechaPorcentaje2(fecha);
                    NotaProspectoAbogadoEntity nota = new NotaProspectoAbogadoEntity();
                    nota.setIdProspectoAbogado(o.getIdProspectoAbogado());
                    nota.setDescripcion("Fall out percentage 20% checked");
                    nota.setFechaCreacion(fecha);
                    String hora = UtilidadesAdapter.generarFecha(false, true, false, "", 0, null).substring(0, 5);
                    nota.setHora(hora);
                    nota.setIdUsuario(idUsuario);
                    notaAbogadoPort.crearNotaProspecto(nota);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                if (o.isPorcentaje2() == false) {
                    o.setFechaPorcentaje2("");
                }
            }

        }

        if (o.getIdEstatusProspecto() == 3) {
            log.info("Es estatus not interested");
            if (motivo == null || motivo.isEmpty()) {
                throw new RuntimeException("The reason for rejecting the prospect is required");
            }
            NotaProspectoAbogadoEntity nota = new NotaProspectoAbogadoEntity();
            nota.setIdProspectoAbogado(o.getIdProspectoAbogado());
            nota.setDescripcion("Prospect rejected. Reason: " + motivo);
            try {
                String fecha = UtilidadesAdapter.generarFecha(true, false, false, "", 0, null);
                nota.setFechaCreacion(fecha);
                String hora = UtilidadesAdapter.generarFecha(false, true, false, "", 0, null).substring(0, 5);
                nota.setHora(hora);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            nota.setIdUsuario(idUsuario);
            log.info("Se inserta nota por rechazo de prospecto, motivo:" + motivo);
            notaAbogadoPort.crearNotaProspecto(nota);
        }

        if (o.getIdEstatusProspecto() == 5) {
            log.info("Es estatus closed");
            if (motivo == null || motivo.isEmpty()) {
                throw new RuntimeException("The reason for closing the prospect is required");
            }
            NotaProspectoAbogadoEntity nota = new NotaProspectoAbogadoEntity();
            nota.setIdProspectoAbogado(o.getIdProspectoAbogado());
            nota.setDescripcion("Prospect closed, Reason: " + motivo);
            try {
                String fecha = UtilidadesAdapter.generarFecha(true, false, false, "", 0, null);
                nota.setFechaCreacion(fecha);
                String hora = UtilidadesAdapter.generarFecha(false, true, false, "", 0, null).substring(0, 5);
                nota.setHora(hora);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            nota.setIdUsuario(idUsuario);
            log.info("Se inserta nota por rechazo de prospecto, motivo:" + motivo);
            notaAbogadoPort.crearNotaProspecto(nota);
        }

        ProspectoAbogadoEntity oE = convertirARequestEntity(o, false);
        log.info("fecha alta:" + oE.getFechaAlta());

        prosAbPort.actualizarProspectoAbogado(oE);
    }

    public List<ProspectoAbogado> obtenerProspectosAbogadosConMail(String valorBusqueda) {
        return prosAbPort.obtenerProspectosAbogadosConMail(valorBusqueda);
    }

    private ProspectoAbogadoEntity convertirARequestEntity(ProspectoAbogado r, boolean nuevo) {

        ProspectoAbogadoEntity o = new ProspectoAbogadoEntity();

        if (nuevo) {
            try {
                o.setFechaAlta(UtilidadesAdapter.generarFecha(true, false, false, "", 0, null));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            o.setIdProspectoAbogado(r.getIdProspectoAbogado());
            o.setFechaAlta(r.getFechaAlta());
        }
        o.setFirma(r.getFirma());
        o.setNombre(r.getNombre());
        o.setTelefono(r.getTelefono());
        o.setIdEstatusProspecto(r.getIdEstatusProspecto());
        o.setIdUsuario(r.getIdUsuario());
        o.setEstado(r.getEstado());
        o.setDireccion(r.getDireccion());
        o.setEnvioCorreo(r.isEnvioCorreo());
        o.setFechaEnvioCorreo(r.getFechaEnvioCorreo());
        o.setPorcentaje1(r.isPorcentaje1());
        o.setFechaPorcentaje1(r.getFechaPorcentaje1());
        o.setPorcentaje2(r.isPorcentaje2());
        o.setFechaPorcentaje2(r.getFechaPorcentaje2());
        o.setMailPackageReceived(r.isMailPackageReceived());
        o.setFechaMailPackageReceived(r.getFechaMailPackageReceived());
        o.setSolicitudRelacionada(r.getSolicitudRelacionada());
        o.setIdAbogadoRelacionado(r.getIdAbogadoRelacionado());
        o.setFechaRecordatorioLiaison(r.getFechaRecordatorioLiaison());
        o.setFuente(r.getFuente());

        return o;
    }

    @Override
    public void actualizarIdProspectoAbogado(int idProspectoAbogado, int idAbogadoRelacionado, String fechaEnvioCliente,
            int idSolicitud) {
        prosAbPort.actualizarIdProspectoAbogado(idProspectoAbogado, idAbogadoRelacionado, fechaEnvioCliente,
                idSolicitud);
    }

    @Override
    public String cargarExcel(MultipartFile archivo) {
        Workbook workbook = null;
        String salida = "";
        StringBuilder sb = new StringBuilder();

        try {

            // workbook = WorkbookFactory.create(new
            // File("/Users/joser.vazquez/ExcelUnif.xlsx"));
            InputStream is = new ByteArrayInputStream(archivo.getBytes());
            workbook = WorkbookFactory.create(is);

            UtilidadesAdapter.pintarLog("Number of sheetss: " + workbook.getNumberOfSheets());

            UtilidadesAdapter.pintarLog("Comenzara a leer sheets");

            int numRegistros = leerSheet(sb, workbook);

            salida = "Cargados " + numRegistros;

            UtilidadesAdapter.pintarLog(salida);

        } catch (EncryptedDocumentException | IOException e) {
            UtilidadesAdapter.pintarLog(e.getMessage());
        } finally {
            try {
                if (workbook != null)
                    workbook.close();
            } catch (IOException e) {
                UtilidadesAdapter.pintarLog(e.getMessage());
            }
            UtilidadesAdapter.pintarLog(sb.toString());
        }
        return salida;
    }

    public int leerSheet(StringBuilder sb, Workbook workbook) {

        String sheetName = "";

        String telefono = "";
        String fechaAlta = "";
        String fuente = "";
        String firma = "";
        String nombre = "";
        String estado = "";
        String direccion = "";
        String ciuidad = "";
        String zip = "";
        String direccionFinal = "";
        String estatusProspecto = "";
        String reasonsClosed = "";
        String fechaEnvioCorreo = "";
        String fechaPorcentaje1 = "";
        String fechaPorcentaje2 = "";
        String correoElectronico = "";
        String nota = "";

        // Obtener la hoja por nombre 'Interested Lawyers'
        Sheet sheet = workbook.getSheet("Interested Lawyers");
        if (sheet == null) {
            throw new RuntimeException("No se encontró la hoja 'Interested Lawyers' en el archivo Excel");
        }
        sheetName = sheet.getSheetName();
        UtilidadesAdapter.pintarLog("Leyendo sheet:" + sheetName);
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        DataFormatter dataFormatter = new DataFormatter(Locale.US);

        // UtilidadesAdapter.pintarLog("Sheet:" + i);

        int cargados = 0;
        int filasVaciasConsecutivas = 0;

        for (Row row : sheet) {

            if (row.getRowNum() > 0 && filaVacia(row)) {
                filasVaciasConsecutivas++;
                if (filasVaciasConsecutivas > 3) {
                    pintarLog("Se detiene la lectura: se encontraron mas de tres filas vacias consecutivas", sb);
                    break;
                }
                continue;
            }

            filasVaciasConsecutivas = 0;
            List<String> correos = null;

            if (row.getRowNum() > 0) {

                ProspectoAbogadoEntity r = new ProspectoAbogadoEntity();

                if (row.getCell(7) != null && !"".equals(row.getCell(7))) {

                    NotaProspectoAbogadoEntity notaEntity = null;

                    pintarLog("## ", sb);

                    if (row.getCell(0) != null) {
                        fuente = row.getCell(0).getStringCellValue();
                        pintarLog("fuente:" + fuente, sb);
                        r.setFuente(fuente);
                    }

                    if (row.getCell(1) != null) {
                        firma = row.getCell(1).getStringCellValue();
                        pintarLog("firma:" + firma, sb);
                        r.setFirma(firma);
                    }

                    if (row.getCell(2) != null) {
                        nombre = row.getCell(2).getStringCellValue();
                        pintarLog("nombre:" + nombre, sb);
                        r.setNombre(nombre);
                    }

                    if (row.getCell(3) != null) {
                        direccion = row.getCell(3).getStringCellValue();
                        ciuidad = UtilidadesAdapter.getCellStringValue(row.getCell(4));
                        zip = UtilidadesAdapter.getCellStringValue(row.getCell(6));
                        direccionFinal = direccion + ", " + ciuidad + ", " + zip;
                        r.setDireccion(direccionFinal);
                        pintarLog("direccion:" + direccionFinal, sb);

                    }

                    if (row.getCell(5) != null) {
                        estado = row.getCell(5).getStringCellValue();
                        r.setEstado(estado);
                        pintarLog("estado:" + estado, sb);

                    }

                    if (row.getCell(7) != null) {
                        telefono = row.getCell(7).getStringCellValue();
                        pintarLog("telefono:" + telefono, sb);
                        if (!telefono.isEmpty()) {
                            telefono = telefono.replaceAll("-", "");
                        }
                        r.setTelefono(telefono);
                    }

                    if (row.getCell(8) != null) {
                        fechaAlta = UtilidadesAdapter.formatearFechaMDY_a_ISO(row.getCell(8).getStringCellValue());
                        pintarLog("fecha_alta:" + fechaAlta, sb);
                        r.setFechaAlta(fechaAlta);
                    }

                    if (row.getCell(12) != null) {
                        estatusProspecto = row.getCell(12).getStringCellValue();
                        pintarLog("estatusProspecto:" + estatusProspecto, sb);
                        if (estatusProspecto.equalsIgnoreCase("Warm")) {
                            r.setIdEstatusProspecto(2);
                        } else if (estatusProspecto.equalsIgnoreCase("CLOSED")) {
                            r.setIdEstatusProspecto(5);

                        } else if (estatusProspecto.equalsIgnoreCase("Successful")) {
                            r.setIdEstatusProspecto(4);
                        }
                    }

                    if (row.getCell(13) != null) {

                        reasonsClosed = row.getCell(13).getStringCellValue();
                         pintarLog("reasonsClosed:" + reasonsClosed, sb);

                    }

                    if (row.getCell(14) != null) {
                        fechaEnvioCorreo = UtilidadesAdapter
                                .formatearFechaMDY_a_ISO(row.getCell(14).getStringCellValue());
                        pintarLog("fechaEnvioCorreo:" + fechaEnvioCorreo, sb);
                        r.setFechaEnvioCorreo(fechaEnvioCorreo);
                        r.setEnvioCorreo(true);
                    }

                    if (row.getCell(15) != null) {
                        fechaPorcentaje1 = UtilidadesAdapter
                                .formatearFechaMDY_a_ISO(row.getCell(15).getStringCellValue());
                        pintarLog("fechaPorcentaje1:" + fechaPorcentaje1, sb);
                        r.setFechaPorcentaje1(fechaPorcentaje1);
                        r.setPorcentaje1(true);
                        r.setIdUsuario(103);
                        Date fechaRecordatorio = UtilidadesAdapter.sumarDiasAFecha(UtilidadesAdapter.fechaActualDate(),
                                15);
                        r.setFechaRecordatorioLiaison(UtilidadesAdapter.formatearFecha(fechaRecordatorio));
                    } else {
                        r.setIdUsuario(102);
                    }

                    if (row.getCell(16) != null) {
                        fechaPorcentaje2 = UtilidadesAdapter
                                .formatearFechaMDY_a_ISO(row.getCell(16).getStringCellValue());
                        pintarLog("fechaPorcentaje2:" + fechaPorcentaje2, sb);
                        r.setFechaPorcentaje2(fechaPorcentaje2);
                        r.setPorcentaje2(true);
                    }

                    if (row.getCell(19) != null) {
                        correoElectronico = valorCeldaComoTexto(row.getCell(19), dataFormatter, formulaEvaluator);
                        if (correoElectronico == null || correoElectronico.trim().isEmpty()
                                || "#NAME?".equalsIgnoreCase(correoElectronico.trim())) {
                            correoElectronico = extraerCorreoDesdeTexto(nota);
                        }
                        pintarLog("correoElectronico:" + correoElectronico, sb);
                        if (correoElectronico.contains("/")) {
                            correos = obtenerCorreos(correoElectronico);
                            pintarLog("Correos encontrados en la celda: " + correos, sb);
                        }
                    }

                    if (row.getCell(20) != null) {
                        nota = valorCeldaComoTexto(row.getCell(20), dataFormatter, formulaEvaluator);
                        pintarLog("nota:" + nota, sb);
                    }

                    EmailProspectoAbogadoEntity correoExistente = emProsAboPort
                            .obtenerEmailProspectoAbogado(correoElectronico);

                    if (correoExistente == null) {

                        ProspectoAbogadoEntity pCreado = prosAbPort.crearProspectoAbogado(r);
                        pintarLog("idProspectoAbogado creado:" + pCreado.getIdProspectoAbogado(), sb);

                        if (correos != null && correos.size() > 0) {
                            for (String c : correos) {
                                EmailProspectoAbogadoEntity eae = new EmailProspectoAbogadoEntity();
                                eae.setIdProspectoAbogado(pCreado.getIdProspectoAbogado());
                                eae.setEmail(c);
                                emProsAboPort.crearEmailProspectoAbogado(eae);
                            }
                        } else {
                            EmailProspectoAbogadoEntity eae = new EmailProspectoAbogadoEntity();
                            eae.setIdProspectoAbogado(pCreado.getIdProspectoAbogado());
                            eae.setEmail(correoElectronico);
                            emProsAboPort.crearEmailProspectoAbogado(eae);
                        }

                        if(!"".equals(nota)) {
                            notaEntity = null;
                            notaEntity = new NotaProspectoAbogadoEntity();
                            notaEntity.setIdProspectoAbogado(pCreado.getIdProspectoAbogado());
                            notaEntity.setDescripcion(nota);
                            notaEntity.setFechaCreacion(r.getFechaAlta());
                            notaEntity.setHora("");
                            notaEntity.setIdUsuario(1);
                            notaAbogadoPort.crearNotaProspecto(notaEntity);
                        }

                        notaEntity = null;
                        if (!"".equals(reasonsClosed)) {
                            notaEntity = new NotaProspectoAbogadoEntity();
                            notaEntity.setIdProspectoAbogado(pCreado.getIdProspectoAbogado());
                            notaEntity.setDescripcion("Prospect closed, Reason: " + reasonsClosed);
                            notaEntity.setFechaCreacion(r.getFechaAlta());
                            notaEntity.setHora("");
                            notaEntity.setIdUsuario(1);
                            notaAbogadoPort.crearNotaProspecto(notaEntity);
                        }

                        cargados++;

                    } else {
                        pintarLog(
                                "############################## El correo ya existe para otro prospecto abogado, no se crea el prospecto ni se asigna el correo, correo:"
                                        + correoElectronico,
                                sb);
                    }

                    pintarLog("\n", sb);
                    correos = null;

                }
            }

            telefono = "";
            fechaAlta = "";
            firma = "";
            nombre = "";
            estado = "";
            direccion = "";
            ciuidad = "";
            zip = "";
            direccionFinal = "";
            estatusProspecto = "";
            reasonsClosed = "";
            fechaEnvioCorreo = "";
            fechaPorcentaje1 = "";
            fechaPorcentaje2 = "";
            correoElectronico = "";
            nota = "";

        }

        pintarLog("cargados:" + cargados, sb);
        return cargados;
    }

    public List<String> obtenerCorreos(String texto) {
        List<String> correos = new ArrayList<>();

        if (texto == null || texto.trim().isEmpty()) {
            return correos;
        }

        Pattern pattern = Pattern.compile(
                "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(texto);
        while (matcher.find()) {
            correos.add(matcher.group());
        }

        return correos;
    }

    private String valorCeldaComoTexto(Cell cell, DataFormatter dataFormatter, FormulaEvaluator formulaEvaluator) {
        if (cell == null) {
            return "";
        }

        if (cell.getCellType() == CellType.FORMULA) {
            String formula = cell.getCellFormula();
            String valorFormulaManual = resolverFormulaTextoEspecial(cell, formula, dataFormatter, formulaEvaluator);
            if (!valorFormulaManual.isEmpty()) {
                return valorFormulaManual;
            }
        }

        try {
            return dataFormatter.formatCellValue(cell, formulaEvaluator).trim();
        } catch (Exception ex) {
            log.warn("No se pudo evaluar la celda {} con formula '{}': {}", cell.getAddress(), cell.getCellFormula(),
                    ex.getMessage());
            return dataFormatter.formatCellValue(cell).trim();
        }
    }

    private String resolverFormulaTextoEspecial(Cell formulaCell, String formula, DataFormatter dataFormatter,
            FormulaEvaluator formulaEvaluator) {
        if (formula == null) {
            return "";
        }

        String formulaNormalizada = formula.toUpperCase(Locale.US);
        if (!formulaNormalizada.contains("TEXTAFTER(")) {
            return "";
        }

        Matcher matcher = FORMULA_CELL_REF_PATTERN.matcher(formula);
        if (!matcher.find()) {
            return "";
        }

        String columna = matcher.group(1);
        int fila = Integer.parseInt(matcher.group(2));
        int indiceColumna = CellReference.convertColStringToIndex(columna);
        Row sourceRow = formulaCell.getSheet().getRow(fila - 1);
        if (sourceRow == null) {
            return "";
        }

        Cell sourceCell = sourceRow.getCell(indiceColumna);
        String textoOrigen = sourceCell == null ? ""
                : dataFormatter.formatCellValue(sourceCell, formulaEvaluator).trim();
        if (textoOrigen.isEmpty()) {
            return "";
        }

        String correo = extraerCorreoDesdeTexto(textoOrigen);
        if (!correo.isEmpty()) {
            return correo;
        }

        String[] partes = textoOrigen.trim().split("\\s+");
        return partes.length == 0 ? "" : partes[partes.length - 1].trim();
    }

    private String extraerCorreoDesdeTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }

        Matcher matcher = EMAIL_PATTERN.matcher(texto);
        String ultimoCorreo = "";
        while (matcher.find()) {
            ultimoCorreo = matcher.group();
        }

        if (!ultimoCorreo.isEmpty()) {
            return ultimoCorreo;
        }

        String[] partes = texto.trim().split("\\s+");
        if (partes.length == 0) {
            return "";
        }

        return partes[partes.length - 1].replaceAll("^[^A-Z0-9]+|[^A-Z0-9.]+$", "");
    }

    private boolean filaVacia(Row row) {
        if (row == null) {
            return true;
        }

        short ultimaCelda = row.getLastCellNum();
        if (ultimaCelda < 0) {
            return true;
        }

        for (int indice = 0; indice < ultimaCelda; indice++) {
            Cell cell = row.getCell(indice);
            if (cell == null) {
                continue;
            }

            if (cell.getCellType() == CellType.BLANK) {
                continue;
            }

            if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().trim().isEmpty()) {
                return false;
            }

            if (cell.getCellType() == CellType.FORMULA) {
                CellType cachedType = cell.getCachedFormulaResultType();
                if (cachedType == CellType.STRING && !cell.getStringCellValue().trim().isEmpty()) {
                    return false;
                }
                if (cachedType == CellType.NUMERIC || cachedType == CellType.BOOLEAN) {
                    return false;
                }
                continue;
            }

            return false;
        }

        return true;
    }

    public void actualizarSolicitud(int idProspectoAbogado, int crmFile, String fecha) {

        ProspectoAbogadoEntity pa = prosAbPort.findByIdProspectoAbogado(idProspectoAbogado);

        if (pa == null) {
            throw new RuntimeException("The crm file " + idProspectoAbogado + " does not exist ");
        }

        ProspectoAbogadoEntity paBySolRel = prosAbPort.findBySolicitudRelacionada(crmFile);
        if (paBySolRel != null) {
            throw new RuntimeException(
                    "CRM file " + crmFile + " is already linked to the prospect " + paBySolRel.getIdProspectoAbogado());
        }

        prosAbPort.actualizarSolicitud(idProspectoAbogado, crmFile, fecha);

    }

    public void pintarLog(String mensaje, StringBuilder sb) {
        // if(false){
        // UtilidadesAdapter.pintarLog(mensaje);
        sb.append(mensaje + " | ");
    }

}
