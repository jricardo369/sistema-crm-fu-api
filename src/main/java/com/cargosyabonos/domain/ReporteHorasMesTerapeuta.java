package com.cargosyabonos.domain;

import java.math.BigDecimal;

public class ReporteHorasMesTerapeuta {

	private int terapeuta;
	private String cliente;
	private int idSolicitud;
	private int horasAprobadas;
	private String fechasCitasMes;
	private String duracionSesionesMes;
	private BigDecimal horasDelMes;
	private BigDecimal totalHorasSolicitud;
	private BigDecimal horasRestantes;

	public int getTerapeuta() {
		return terapeuta;
	}

	public void setTerapeuta(int terapeuta) {
		this.terapeuta = terapeuta;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public int getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public int getHorasAprobadas() {
		return horasAprobadas;
	}

	public void setHorasAprobadas(int horasAprobadas) {
		this.horasAprobadas = horasAprobadas;
	}

	public String getFechasCitasMes() {
		return fechasCitasMes;
	}

	public void setFechasCitasMes(String fechasCitasMes) {
		this.fechasCitasMes = fechasCitasMes;
	}

	public String getDuracionSesionesMes() {
		return duracionSesionesMes;
	}

	public void setDuracionSesionesMes(String duracionSesionesMes) {
		this.duracionSesionesMes = duracionSesionesMes;
	}

	public BigDecimal getHorasDelMes() {
		return horasDelMes;
	}

	public void setHorasDelMes(BigDecimal horasDelMes) {
		this.horasDelMes = horasDelMes;
	}

	public BigDecimal getTotalHorasSolicitud() {
		return totalHorasSolicitud;
	}

	public void setTotalHorasSolicitud(BigDecimal totalHorasSolicitud) {
		this.totalHorasSolicitud = totalHorasSolicitud;
	}

	public BigDecimal getHorasRestantes() {
		return horasRestantes;
	}

	public void setHorasRestantes(BigDecimal horasRestantes) {
		this.horasRestantes = horasRestantes;
	}

}
