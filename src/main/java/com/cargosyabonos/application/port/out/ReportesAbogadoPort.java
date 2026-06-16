package com.cargosyabonos.application.port.out;

import java.util.List;
import com.cargosyabonos.domain.AbogadoEstado;
import com.cargosyabonos.domain.ClientesPorAbogado;
import com.cargosyabonos.domain.OficinaAnioYMes;

public interface ReportesAbogadoPort {
   List<AbogadoEstado> obtenerAbogadosPorEstadoPorFecha(String fechaInicio, String fechaFin);
   List<ClientesPorAbogado> obtenerClientesPorFirmaYFechas(String fechaInicio, String fechaFin);
   List<OficinaAnioYMes> obtenerTotalFirmasAnioYMes(String fechaInicio, String fechaFin);
}
