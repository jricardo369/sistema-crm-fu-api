package com.cargosyabonos.domain;

public class ReporteNotasCitasTerapeutasVoc {

	private String nombre;
	private int totalCitas;
	private int totalNotas;
	private int notasConRetraso;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTotalCitas() {
		return totalCitas;
	}

	public void setTotalCitas(int totalCitas) {
		this.totalCitas = totalCitas;
	}

	public int getTotalNotas() {
		return totalNotas;
	}

	public void setTotalNotas(int totalNotas) {
		this.totalNotas = totalNotas;
	}

	public int getNotasConRetraso() {
		return notasConRetraso;
	}

	public void setNotasConRetraso(int notasConRetraso) {
		this.notasConRetraso = notasConRetraso;
	}

}
