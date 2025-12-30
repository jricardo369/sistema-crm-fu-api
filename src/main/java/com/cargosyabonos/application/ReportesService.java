package com.cargosyabonos.application;

import org.springframework.stereotype.Service;

@Service
public class ReportesService  {
/*
	Logger log = LoggerFactory.getLogger(ReportesService.class);

	@Autowired
	CitaJpa citaJpa;
	
	@Autowired
	MovimientoJpa movJpa;
	
	@Autowired
	CitasUseCase citaUseCase;

	@Override
	public String ultimaCitaPaciente(int idPaciente) {
		String salida = "";
		List<CitaEntity> c = citaJpa.ultimaCitaPaciente(idPaciente);
		if (!c.isEmpty()) {
			CitaEntity e = c.get(0);
			salida =  "Su proxima cita es "+UtilidadesAdapter.fechaEnLetra(e.getFecha())
			+" de " +UtilidadesAdapter.formatearHora(e.getHoraInicio())
			+" a " + UtilidadesAdapter.formatearHora(e.getHoraFin());
		}else{
			salida = "No tiene cita agendada";
		}
		return salida;
	}
	
	@Override
	public ReporteInicio reporteInicio() {
		ReporteInicio r = new ReporteInicio();
		int numeroCitas = citaJpa.numeroDeCitasDelDía();
		r.setCitasPendientes(numeroCitas);
		BigDecimal cargos = movJpa.obtenerSumaTipoMovimiento(1) == null ? BigDecimal.ZERO : movJpa.obtenerSumaTipoMovimiento(1);
		BigDecimal abonos = movJpa.obtenerSumaTipoMovimiento(2) == null ? BigDecimal.ZERO : movJpa.obtenerSumaTipoMovimiento(2);
		r.setTotalAdeudos(cargos);
		r.setTotalIngreso(abonos);
		return r;
	}
	
	@Override
	public ReporteMovimientos reporteMovDePaciente(int idPaciente,Date fechai, Date fechaf){
		ReporteMovimientos r = new ReporteMovimientos();
		BigDecimal cargos = movJpa.obtenerSumaTipoMovimientoYPaciente(1, idPaciente,fechai,fechaf) == null ? BigDecimal.ZERO : movJpa.obtenerSumaTipoMovimientoYPaciente(1, idPaciente,fechai,fechaf);
		BigDecimal abonos = movJpa.obtenerSumaTipoMovimientoYPaciente(2, idPaciente,fechai,fechaf) == null ? BigDecimal.ZERO : movJpa.obtenerSumaTipoMovimientoYPaciente(2, idPaciente,fechai,fechaf);
		BigDecimal saldo = cargos.subtract(abonos);
		r.setSaldo(saldo);
		r.setTotalAdeudos(cargos);
		r.setTotalIngreso(abonos);
		return r;
	}
	
	@Override
	public ReporteMovimientos reporteMovGeneral(int idSociedad){
		ReporteMovimientos r = new ReporteMovimientos();
		BigDecimal cargos = movJpa.obtenerSumaTipoMovimientoYSociedad(1,idSociedad) == null ? BigDecimal.ZERO : movJpa.obtenerSumaTipoMovimiento(1);
		BigDecimal abonos = movJpa.obtenerSumaTipoMovimientoYSociedad(2,idSociedad) == null ? BigDecimal.ZERO : movJpa.obtenerSumaTipoMovimiento(2);
		BigDecimal saldo = cargos.subtract(abonos);
		r.setSaldo(saldo);
		r.setTotalAdeudos(cargos);
		r.setTotalIngreso(abonos);
		return r;
	}
*/
}
