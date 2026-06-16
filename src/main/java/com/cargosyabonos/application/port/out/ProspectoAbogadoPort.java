package com.cargosyabonos.application.port.out;

import java.util.List;

import com.cargosyabonos.domain.ProspectoAbogado;
import com.cargosyabonos.domain.ProspectoAbogadoEntity;

public interface ProspectoAbogadoPort {

    public ProspectoAbogadoEntity findByIdProspectoAbogado(int idProspectoAbogado);

    public List<ProspectoAbogadoEntity> obtenerProspectosAbogado();

    public List<ProspectoAbogado> obtenerProspectosAbogado(int idProspectoAbogado,String id,String fechai, String fechaf,int idUsuario, String estatus,boolean soloUno,String estado,String telefono,String lawFirmOrContactName,String email,String mailPackageReceived,String emailSent,String porcentaje20,String followUp20Porcent,String prospectSentClient);

     public ProspectoAbogadoEntity crearProspectoAbogado(ProspectoAbogadoEntity o);

      public void actualizarProspectoAbogado(ProspectoAbogadoEntity o);

     public List<ProspectoAbogado> obtenerProspectosAbogadosConMail(String valorBusqueda);

     public void actualizarIdProspectoAbogado(int idProspectoAbogado, int idAbogadoRelacionado, String fechaEnvioCliente,int idSolicitud);

    public List<ProspectoAbogadoEntity> obtenerProspectosAbogadosParaReminderLiaison();

    public void actualizarFechaRecordatorioLiaison(int idProspectoAbogado, String  fechaRecordatorioLiaison);

    public void actualizarSolicitud(int idProspectoAbogado,int crmFile,String fecha);

    public ProspectoAbogadoEntity findBySolicitudRelacionada(int solicitudRelacionada);

}
