package com.cargosyabonos.domain;

public class ReporteNotasCitasTerapeutasDetalleVoc {

	private int idNota;
	private String fechaCreacionNota;
	private int idCitaAsociado;
	private String fechaCita;
	private int diasDeRetraso;

	public int getIdNota() {
		return idNota;
	}

	public void setIdNota(int idNota) {
		this.idNota = idNota;
	}

	public String getFechaCreacionNota() {
		return fechaCreacionNota;
	}

	public void setFechaCreacionNota(String fechaCreacionNota) {
		this.fechaCreacionNota = fechaCreacionNota;
	}

	public int getIdCitaAsociado() {
		return idCitaAsociado;
	}

	public void setIdCitaAsociado(int idCitaAsociado) {
		this.idCitaAsociado = idCitaAsociado;
	}

	public String getFechaCita() {
		return fechaCita;
	}

	public void setFechaCita(String fechaCita) {
		this.fechaCita = fechaCita;
	}

	public int getDiasDeRetraso() {
		return diasDeRetraso;
	}

	public void setDiasDeRetraso(int diasDeRetraso) {
		this.diasDeRetraso = diasDeRetraso;
	}

}
