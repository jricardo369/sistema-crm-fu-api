package com.cargosyabonos.application.port.in;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.domain.ProspectoAbogado;

public interface ProspectoAbogadoUseCase {

    public List<ProspectoAbogado> obtenerProspectosAbogado(String id,String fechai, String fechaf,int idUsuario, String estatus,String estado,String telefono,String lawFirmOrContactName,String email,String mailPackageReceived,String emailSent,String porcentaje20,String followUp20Porcent,String prospectSentClient);

    public ProspectoAbogado obtenerProspectoAbogado(int idProspectoAbogado);

    public ProspectoAbogado crearProspectoAbogado(ProspectoAbogado o,String emailsAbogado);

     public void actualizarProspectoAbogado(ProspectoAbogado o,int idUsuario,String motivo);

     public List<ProspectoAbogado> obtenerProspectosAbogadosConMail(String valorBusqueda);

      public void actualizarIdProspectoAbogado(int idProspectoAbogado, int idAbogadoRelacionado, String fechaEnvioCliente,int idSolicitud);

      public String cargarExcel(MultipartFile archivo);

      public void actualizarSolicitud(int idProspectoAbogado,int crmFile,String fecha);
}
