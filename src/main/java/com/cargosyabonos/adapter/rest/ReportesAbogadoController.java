package com.cargosyabonos.adapter.rest;

import com.cargosyabonos.application.port.in.ReportesAbogadoUseCase;
import com.cargosyabonos.domain.AbogadoEstado;
import com.cargosyabonos.domain.ClientesPorAbogado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.cargosyabonos.domain.OficinaAnioYMes;

@RestController
@RequestMapping("/reportes-abogado")
public class ReportesAbogadoController {

    @Autowired
    private ReportesAbogadoUseCase reportesAbogadoUseCase;

    @GetMapping("/por-estado")
    public List<AbogadoEstado> obtenerAbogadosPorEstadoPorFecha(
            @RequestParam("fechaInicio") String fechaInicio,
            @RequestParam("fechaFin") String fechaFin) {
        return reportesAbogadoUseCase.obtenerAbogadosPorEstadoPorFecha(fechaInicio, fechaFin);
    }

    @GetMapping("/clientes-por-firma")
    public List<ClientesPorAbogado> obtenerClientesPorFirmaYFechas(
            @RequestParam("fechaInicio") String fechaInicio,
            @RequestParam("fechaFin") String fechaFin) {
        return reportesAbogadoUseCase.obtenerClientesPorFirmaYFechas(fechaInicio, fechaFin);
    }

    @GetMapping("/total-firmas-anio-mes")
    public List<OficinaAnioYMes> obtenerTotalFirmasAnioYMes(
            @RequestParam("fechaInicio") String fechaInicio,
            @RequestParam("fechaFin") String fechaFin) {
        return reportesAbogadoUseCase.obtenerTotalFirmasAnioYMes(fechaInicio, fechaFin);
    }
}
