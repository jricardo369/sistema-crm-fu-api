package com.cargosyabonos.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "solicitud_voc")
public class SolicitudVocEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_solicitud")
	private int idSolicitud;
	
	@Column(name = "fecha_inicio", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date fechaInicio;
	
	@Column(name = "cliente", length = 100)
	private String cliente;

	@Column(name = "telefono", length = 45)
	private String telefono;
	
	@Column(name = "email", length = 200)
	private String email;
	
	@Column(name = "docusign", length = 200)
	private String docusign;
	
	@Column(name = "abogado", length = 45)
	private String abogado;
	
	@Column(name = "email_abogado", length = 150)
	private String email_abogado;
	
	@Column(name = "firma_de_abogados", length = 150)
	private String firmaAbogados;
	
	@Column(name = "numero_de_caso", length = 20)
	private String numeroDeCaso;
	
	@JoinColumn(name = "id_tipo_solicitud", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private TipoSolicitudEntity tipoSolicitud;
	
	@JoinColumn(name = "id_estatus_solicitud", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private EstatusSolicitudEntity estatusSolicitud;
	
	@JoinColumn(name = "id_estatus_pago", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private EstatusPagoEntity estatusPago;
	
	@Column(name = "fecha_nacimiento", length = 10)
	private String fechaNacimiento;

	
	@Column(name = "adicional", length = 45)
	private String adicional;
	
	@Column(name = "idioma", length = 10)
	private String idioma;
	
	//@NotBlank(message = "The address is required")
    @Size(max = 50, message = "The address cannot exceed 150 characters")
	@Column(name = "direccion", length = 150)
	private String direccion;
	
	@Column(name = "estado", length = 45)
	private String estado;
	
	@Column(name = "apellidos", length = 150)
	private String apellidos;
	
	@Column(name = "parelegal_name", length = 150)
	private String paralegalName;
	
	@Column(name = "parelegal_emails", length = 200)
	private String paralegalEmails;
	
	@Column(name = "parelegal_telefonos", length = 200)
	private String paralegalTelefonos;
	
	private String importante;
	
	@Column(name = "num_sesiones")
	private int numSesiones;
	
	@Column(name = "num_schedules")
	private int numSchedules;
	
	@Column(name = "sesiones_pendientes")
	private int sesionesPendientes;
	
	@Column(name = "documento_1", length = 1)
	private boolean  documento1;
	
	@Column(name = "fecha_doc_1", length = 10)
	private String fechaDoc1;
	
	@Column(name = "documento_2", length = 1)
	private boolean documento2;
	
	@Column(name = "fecha_doc_2", length = 10)
	private String fechaDoc2;
	
	private int terapeuta;
	
	@Column(name = "code", length = 20)
	private String code;
	
	@Column(name = "usuario_revisando")
	private int usuarioRevisando;
	
	private String sexo;
	
	@Column(name = "purpose_treatament", length = 100)
	private String purposeTreatament;
	
	private String participation;
	
	private String recommendations;
	
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

	public TipoSolicitudEntity getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(TipoSolicitudEntity tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public EstatusSolicitudEntity getEstatusSolicitud() {
		return estatusSolicitud;
	}

	public void setEstatusSolicitud(EstatusSolicitudEntity estatusSolicitud) {
		this.estatusSolicitud = estatusSolicitud;
	}

	public EstatusPagoEntity getEstatusPago() {
		return estatusPago;
	}

	public void setEstatusPago(EstatusPagoEntity estatusPago) {
		this.estatusPago = estatusPago;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getFirmaAbogados() {
		return firmaAbogados;
	}

	public void setFirmaAbogados(String firmaAbogados) {
		this.firmaAbogados = firmaAbogados;
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

	public String obtenerCliente(){
		String clt = this.getCliente();
		if(clt==null||"".equals(clt)){
			clt = "";
		}
		//System.out.println("clt:"+clt);
		return clt;
	}
	
	public String obtenerClienteParaMail(){
		String clt = "";
		if(this.getCliente()==null||"".equals(this.getCliente())){
			clt = "User";
		}else{
			clt = this.getCliente();
		}
		//System.out.println("clt:"+clt);
		return clt;
	}
	
	public boolean datosClienteVacios(){
		boolean salida = false;
		if(this.getCliente()==null||"".equals(this.getCliente())||this.getEmail()==null||"".equals(this.getEmail())){
			salida = true;
		}
		return salida;
	}
	
	public boolean datosNombreVacio(){
		boolean salida = true;
		if(this.getCliente()!=null&&!"".equals(this.getCliente())){
			salida = false;
		}
		if(!"".equals(this.getApellidosSinNull())){
			salida = false;
		}
		return salida;
	}
	
	public boolean datosAbogadoVacios(){
		boolean salida = false;
		if(this.getAbogado()==null||"".equals(this.getAbogado())||this.getEmail_abogado()==null||"".equals(this.getEmail_abogado())){
			salida = true;
		}
		return salida;
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
	
	public String getNombreClienteCompleto() {
		return this.cliente+" "+this.getApellidosSinNull();
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

	public String getImportante() {
		return importante;
	}

	public void setImportante(String importante) {
		this.importante = importante;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

}
