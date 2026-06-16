package com.cargosyabonos.application;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cargosyabonos.application.port.in.ReportesAbogadoUseCase;
import com.cargosyabonos.application.port.out.ReportesAbogadoPort;
import com.cargosyabonos.domain.AbogadoEstado;
import com.cargosyabonos.domain.ClientesPorAbogado;
import com.cargosyabonos.domain.OficinaAnioYMes;

@Service
public class ReportesAbogadoService implements ReportesAbogadoUseCase {

    @Autowired
    private ReportesAbogadoPort reportesAbogadoPort;

    @Override
    public List<AbogadoEstado> obtenerAbogadosPorEstadoPorFecha(String fechaInicio, String fechaFin) {
        return reportesAbogadoPort.obtenerAbogadosPorEstadoPorFecha(fechaInicio, fechaFin);
    }

    @Override
    public List<ClientesPorAbogado> obtenerClientesPorFirmaYFechas(String fechaInicio, String fechaFin) {
        return reportesAbogadoPort.obtenerClientesPorFirmaYFechas(fechaInicio, fechaFin);
    }

    @Override
    public List<OficinaAnioYMes> obtenerTotalFirmasAnioYMes(String fechaInicio, String fechaFin) {
        return reportesAbogadoPort.obtenerTotalFirmasAnioYMes(fechaInicio, fechaFin);
    }
}
