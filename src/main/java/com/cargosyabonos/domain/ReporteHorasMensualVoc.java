package com.cargosyabonos.domain;

import java.math.BigDecimal;

public class ReporteHorasMensualVoc {

	private int idTerapeuta;
	private String nombreTerapeuta;
	private String cliente;
	private String numeroDeCaso;
	private String fechaNacimiento;
	private String telefono;
	private String direccion;
	private int idSolicitud;
	private int horasAprobadas;
	private String fechasCitasMes;
	private String duracionSesionesMes;
	private BigDecimal numSesionesMes;
	private BigDecimal horasDelMes;
	private BigDecimal totalHorasSolicitud;
	private BigDecimal horasRestantes;
	private String sexo;

	public int getIdTerapeuta() {
		return idTerapeuta;
	}

	public void setIdTerapeuta(int idTerapeuta) {
		this.idTerapeuta = idTerapeuta;
	}

	public String getNombreTerapeuta() {
		return nombreTerapeuta;
	}

	public void setNombreTerapeuta(String nombreTerapeuta) {
		this.nombreTerapeuta = nombreTerapeuta;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getNumeroDeCaso() {
		return numeroDeCaso;
	}

	public void setNumeroDeCaso(String numeroDeCaso) {
		this.numeroDeCaso = numeroDeCaso;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
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

	public BigDecimal getNumSesionesMes() {
		return numSesionesMes;
	}

	public void setNumSesionesMes(BigDecimal numSesionesMes) {
		this.numSesionesMes = numSesionesMes;
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

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

}
