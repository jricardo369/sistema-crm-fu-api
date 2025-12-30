package com.cargosyabonos.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author joser.vazquez
 *
 */
public class SolicitudVoc {
	
	private int idSolicitud;
	private Date fechaInicio;
	private String cliente;
	private String telefono;
	private String email;
	private String docusign;
	private BigDecimal amount;
	private String abogado;
	private String email_abogado;
	private String numeroDeCaso;
	private int idTipoSolicitud;
	private int idEstatusSolicitud;
	private int idEstatusPago;
	private String firmaAbogados;
	private String tipoSolicitud;
	private String estatusSolicitud;
	private String estatusPago;
	private int age;
	
	private String importante;
	private String comentario;
	private String fechaNacimiento;
	private String adicional;
	
	
	private String idioma;
	private String tipoEntrevista;
	private String direccion;
	private String estado;
	private String referencia;
	private String apellidos;
	
	private String paralegalName;
	private String paralegalEmails;
	private String paralegalTelefonos;
	
	private int numSesiones;
	private int numSchedules;
	private int sesionesPendientes;
	private boolean  documento1;
	private String fechaDoc1;
	private boolean documento2;
	private String fechaDoc2;
	private int terapeuta;
	private int usuarioRevisando;
	private String nombreTerapeuta;
	private String code;
	private String info;
	private String sexo;
	
	private String purposeTreatament;
	private String participation;
	private String recommendations;

	private int numSesionesSinNota;
	
	public int getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
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
	public String getDocusign() {
		return docusign;
	}
	public void setDocusign(String docusign) {
		this.docusign = docusign;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getAbogado() {
		return abogado;
	}
	public void setAbogado(String abogado) {
		this.abogado = abogado;
	}
	public String getEmail_abogado() {
		return email_abogado;
	}
	public void setEmail_abogado(String email_abogado) {
		this.email_abogado = email_abogado;
	}
	public String getNumeroDeCaso() {
		return numeroDeCaso;
	}
	public void setNumeroDeCaso(String numeroDeCaso) {
		this.numeroDeCaso = numeroDeCaso;
	}
	public int getIdTipoSolicitud() {
		return idTipoSolicitud;
	}
	public void setIdTipoSolicitud(int idTipoSolicitud) {
		this.idTipoSolicitud = idTipoSolicitud;
	}
	public int getIdEstatusSolicitud() {
		return idEstatusSolicitud;
	}
	public void setIdEstatusSolicitud(int idEstatusSolicitud) {
		this.idEstatusSolicitud = idEstatusSolicitud;
	}
	public int getIdEstatusPago() {
		return idEstatusPago;
	}
	public void setIdEstatusPago(int idEstatusPago) {
		this.idEstatusPago = idEstatusPago;
	}
	public String getFirmaAbogados() {
		return firmaAbogados;
	}
	public void setFirmaAbogados(String firmaAbogados) {
		this.firmaAbogados = firmaAbogados;
	}
	public String getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(String tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	public String getEstatusSolicitud() {
		return estatusSolicitud;
	}
	public void setEstatusSolicitud(String estatusSolicitud) {
		this.estatusSolicitud = estatusSolicitud;
	}
	public String getEstatusPago() {
		return estatusPago;
	}
	public void setEstatusPago(String estatusPago) {
		this.estatusPago = estatusPago;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getImportante() {
		return importante;
	}
	public void setImportante(String importante) {
		this.importante = importante;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getAdicional() {
		return adicional;
	}
	public void setAdicional(String adicional) {
		this.adicional = adicional;
	}

	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	public String getTipoEntrevista() {
		return tipoEntrevista;
	}
	public void setTipoEntrevista(String tipoEntrevista) {
		this.tipoEntrevista = tipoEntrevista;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public String getApellidosSinNull(){
		String salida = "";
		if(this.apellidos == null){
			salida = "";
		}else{
			salida = this.apellidos;
		}
		return salida;
	}
	public String getParalegalName() {
		return paralegalName;
	}
	public void setParalegalName(String paralegalName) {
		this.paralegalName = paralegalName;
	}
	public String getParalegalEmails() {
		return paralegalEmails;
	}
	public void setParalegalEmails(String paralegalEmails) {
		this.paralegalEmails = paralegalEmails;
	}
	public String getParalegalTelefonos() {
		return paralegalTelefonos;
	}
	public void setParalegalTelefonos(String paralegalTelefonos) {
		this.paralegalTelefonos = paralegalTelefonos;
	}
	public int getNumSesiones() {
		return numSesiones;
	}
	public void setNumSesiones(int numSesiones) {
		this.numSesiones = numSesiones;
	}
	public int getNumSchedules() {
		return numSchedules;
	}
	public void setNumSchedules(int numSchedules) {
		this.numSchedules = numSchedules;
	}
	public int getSesionesPendientes() {
		return sesionesPendientes;
	}
	public void setSesionesPendientes(int sesionesPendientes) {
		this.sesionesPendientes = sesionesPendientes;
	}
	public boolean isDocumento1() {
		return documento1;
	}
	public void setDocumento1(boolean documento1) {
		this.documento1 = documento1;
	}
	public String getFechaDoc1() {
		return fechaDoc1;
	}
	public void setFechaDoc1(String fechaDoc1) {
		this.fechaDoc1 = fechaDoc1;
	}
	public boolean isDocumento2() {
		return documento2;
	}
	public void setDocumento2(boolean documento2) {
		this.documento2 = documento2;
	}
	public String getFechaDoc2() {
		return fechaDoc2;
	}
	public void setFechaDoc2(String fechaDoc2) {
		this.fechaDoc2 = fechaDoc2;
	}
	public int getTerapeuta() {
		return terapeuta;
	}
	public void setTerapeuta(int terapeuta) {
		this.terapeuta = terapeuta;
	}
	public int getUsuarioRevisando() {
		return usuarioRevisando;
	}
	public void setUsuarioRevisando(int usuarioRevisando) {
		this.usuarioRevisando = usuarioRevisando;
	}
	public String getNombreTerapeuta() {
		return nombreTerapeuta;
	}
	public void setNombreTerapeuta(String nombreTerapeuta) {
		this.nombreTerapeuta = nombreTerapeuta;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getNombreClienteCompleto() {
		return this.cliente+" "+this.getApellidosSinNull();
	}
	
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getPurposeTreatament() {
		return purposeTreatament;
	}
	public void setPurposeTreatament(String purposeTreatament) {
		this.purposeTreatament = purposeTreatament;
	}
	public String getParticipation() {
		return participation;
	}
	public void setParticipation(String participation) {
		this.participation = participation;
	}
	
	public String getRecommendations() {
		return recommendations;
	}
	public void setRecommendations(String recommendations) {
		this.recommendations = recommendations;
	}
	
	public int getNumSesionesSinNota() {
		return numSesionesSinNota;
	}
	public void setNumSesionesSinNota(int numSesionesSinNota) {
		this.numSesionesSinNota = numSesionesSinNota;
	}
	@Override
	public String toString() {
		return "SolicitudVoc [idSolicitud=" + idSolicitud + ", fechaInicio=" + fechaInicio + ", cliente=" + cliente
				+ ", telefono=" + telefono + ", email=" + email + ", docusign=" + docusign + ", amount=" + amount
				+ ", abogado=" + abogado + ", email_abogado=" + email_abogado + ", numeroDeCaso=" + numeroDeCaso
				+ ", idTipoSolicitud=" + idTipoSolicitud + ", idEstatusSolicitud=" + idEstatusSolicitud
				+ ", idEstatusPago=" + idEstatusPago + ", firmaAbogados=" + firmaAbogados + ", tipoSolicitud="
				+ tipoSolicitud + ", estatusSolicitud=" + estatusSolicitud + ", estatusPago=" + estatusPago + ", age="
				+ age + ", importante=" + importante + ", comentario=" + comentario + ", fechaNacimiento="
				+ fechaNacimiento + ", adicional=" + adicional + ", idioma=" + idioma + ", tipoEntrevista="
				+ tipoEntrevista + ", direccion=" + direccion + ", estado=" + estado + ", referencia=" + referencia
				+ ", apellidos=" + apellidos + ", paralegalName=" + paralegalName + ", paralegalEmails="
				+ paralegalEmails + ", paralegalTelefonos=" + paralegalTelefonos + ", numSesiones=" + numSesiones
				+ ", numSchedules=" + numSchedules + ", sesionesPendientes=" + sesionesPendientes + ", documento1="
				+ documento1 + ", fechaDoc1=" + fechaDoc1 + ", documento2=" + documento2 + ", fechaDoc2=" + fechaDoc2
				+ ", terapeuta=" + terapeuta + ", usuarioRevisando=" + usuarioRevisando + ", nombreTerapeuta="
				+ nombreTerapeuta + ", code=" + code + ", info=" + info + "]";
	}
	
	
	
}
