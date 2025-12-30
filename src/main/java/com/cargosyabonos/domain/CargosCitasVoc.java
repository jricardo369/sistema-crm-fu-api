package com.cargosyabonos.domain;

import java.math.BigDecimal;

public class CargosCitasVoc {

	private int idFile;
	private int idCita;
    private String caseNumber;
    private String fecha;
    private String cliente;
    private String telefono;
    private String email;
    private BigDecimal amount;
    private boolean pagado;
    private String comentario;
    private String fechaPagado;
    private String terapeuta;
    private String anioNacimiento;
    private String sexo;
    private String direccion;
    
	public int getIdFile() {
		return idFile;
	}
	public void setIdFile(int idFile) {
		this.idFile = idFile;
	}
	public int getIdCita() {
		return idCita;
	}
	public void setIdCita(int idCita) {
		this.idCita = idCita;
	}
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public boolean isPagado() {
		return pagado;
	}
	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getFechaPagado() {
		return fechaPagado;
	}
	public void setFechaPagado(String fechaPagado) {
		this.fechaPagado = fechaPagado;
	}
	public String getTerapeuta() {
		return terapeuta;
	}
	public void setTerapeuta(String terapeuta) {
		this.terapeuta = terapeuta;
	}
	
	public String getAnioNacimiento() {
		return anioNacimiento;
	}
	public void setAnioNacimiento(String anioNacimiento) {
		this.anioNacimiento = anioNacimiento;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public CargosCitasVoc(int idFile, int idCita, String caseNumber, String fecha, String cliente, String telefono,
			String email, BigDecimal amount, boolean pagado, String comentario, String fechaPagado, String terapeuta) {
		super();
		this.idFile = idFile;
		this.idCita = idCita;
		this.caseNumber = caseNumber;
		this.fecha = fecha;
		this.cliente = cliente;
		this.telefono = telefono;
		this.email = email;
		this.amount = amount;
		this.pagado = pagado;
		this.comentario = comentario;
		this.fechaPagado = fechaPagado;
		this.terapeuta = terapeuta;
	}
	public CargosCitasVoc() {
		super();
	}
	
    
}
