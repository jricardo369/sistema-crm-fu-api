package com.cargosyabonos.domain;

import java.math.BigDecimal;
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

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "solicitud")
public class SolicitudEntity {
	
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
	
	@Column(name = "amount")
	private BigDecimal amount;
	
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
	
	@Column(name = "usuario_revisor")
	private int usuarioRevisor;
	
	@Column(name = "usuario_revisando")
	private int usuarioRevisando;
	
	@Column(name = "adicional", length = 45)
	private String adicional;
	
	@Column(name = "idioma", length = 10)
	private String idioma;
	
	@Column(name = "tipo_entrevista", length = 20)
	private String tipoEntrevista;
	
	@Column(name = "direccion", length = 45)
	private String direccion;
	
	@Column(name = "estado", length = 45)
	private String estado;
	
	@Column(name = "referencia", length = 45)
	private String referencia;
	
	@Column(name = "apellidos", length = 150)
	private String apellidos;

	@Column(name = "waiver", length = 1)
	private boolean waiver;
	
	@Column(name = "parelegal_name", length = 150)
	private String paralegalName;
	
	@Column(name = "parelegal_emails", length = 200)
	private String paralegalEmails;
	
	@Column(name = "parelegal_telefonos", length = 200)
	private String paralegalTelefonos;
	
	@Column(name = "asignacion_template", length = 1)
	private boolean asignacionTemplate;
	
	@Column(name = "numero_entrevistas")
	private int numeroEntrevistas;
	
	private boolean external;
	
	@Column(name = "usuario_external")
	private int usuarioExternal;
	
	private String importante;
	
	@Column(name = "asignacion_int_sc")
	private boolean asignacionIntSc;
	
	@Column(name = "usuario_int_sc")
	private int usuarioIntSc;
	
	@Column(name = "fin_int_sc")
	private boolean finIntSc;
	
	@Column(name = "interview_master")
	private boolean interviewMaster;
	
	@Column(name = "due_date", length = 10)
	private String dueDate;

	@Column(name = "assigned_clinician")
	private int assignedClinician;
	
	@Column(name = "usuario_interview")
	private int usuarioInterview;
	
	@Column(name = "fin_int_ini")
	private boolean finIntIni;
	
	@Column(name = "id_abogado")
	private int idAbogado;
	
	@Column(name = "fin_asg_tmp")
	private boolean finAsgTmp;
	
	@Column(name = "email_abo_sel")
	private String emailAboSel;
	
	private String sexo;
	
	@Column(name = "fin_asg_clnc")
	private boolean finAsgClnc;
	
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

	public int getUsuarioRevisor() {
		return usuarioRevisor;
	}

	public void setUsuarioRevisor(int usuarioRevisor) {
		this.usuarioRevisor = usuarioRevisor;
	}

	public int getUsuarioRevisando() {
		return usuarioRevisando;
	}

	public void setUsuarioRevisando(int usuarioRevisando) {
		this.usuarioRevisando = usuarioRevisando;
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
	
	public boolean datosClienteEEmailVacios(){
		boolean salida = false;
		if(this.getCliente()==null||"".equals(this.getCliente())||this.getEmail()==null||"".equals(this.getEmail())){
			salida = true;
		}
		return salida;
	}
	
	public boolean datosClienteVacios(){
		boolean salida = false;
		if(this.getCliente()==null||"".equals(this.getCliente())){
			salida = true;
		}
		return salida;
	}
	
	public boolean datosEmailVacios(){
		boolean salida = false;
		if(this.getEmail()==null||"".equals(this.getEmail())){
			salida = true;
		}
		return salida;
	}
	
	public boolean datosTelefonoVacios(){
		boolean salida = false;
		if(this.getTelefono()==null||"".equals(this.getTelefono())){
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
	
	public boolean isWaiver() {
		return waiver;
	}
	public void setWaiver(boolean waiver) {
		this.waiver = waiver;
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

	public boolean isAsignacionTemplate() {
		return asignacionTemplate;
	}
	public void setAsignacionTemplate(boolean asignacionTemplate) {
		this.asignacionTemplate = asignacionTemplate;
	}

	public int getNumeroEntrevistas() {
		return numeroEntrevistas;
	}

	public void setNumeroEntrevistas(int numeroEntrevistas) {
		this.numeroEntrevistas = numeroEntrevistas;
	}

	public boolean isExternal() {
		return external;
	}

	public void setExternal(boolean external) {
		this.external = external;
	}

	public int getUsuarioExternal() {
		return usuarioExternal;
	}

	public void setUsuarioExternal(int usuarioExternal) {
		this.usuarioExternal = usuarioExternal;
	}

	public String getImportante() {
		return importante;
	}

	public void setImportante(String importante) {
		this.importante = importante;
	}

	public boolean isAsignacionIntSc() {
		return asignacionIntSc;
	}
	public void setAsignacionIntSc(boolean asignacionIntSc) {
		this.asignacionIntSc = asignacionIntSc;
	}
	
	public int getUsuarioIntSc() {
		return usuarioIntSc;
	}
	public void setUsuarioIntSc(int usuarioIntSc) {
		this.usuarioIntSc = usuarioIntSc;
	}
	
	public boolean isFinIntSc() {
		return finIntSc;
	}
	public void setFinIntSc(boolean finIntSc) {
		this.finIntSc = finIntSc;
	}

	public boolean isInterviewMaster() {
		return interviewMaster;
	}

	public void setInterviewMaster(boolean interviewMaster) {
		this.interviewMaster = interviewMaster;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public int getAssignedClinician() {
		return assignedClinician;
	}

	public void setAssignedClinician(int assignedClinician) {
		this.assignedClinician = assignedClinician;
	}

	public int getUsuarioInterview() {
		return usuarioInterview;
	}

	public void setUsuarioInterview(int usuarioInterview) {
		this.usuarioInterview = usuarioInterview;
	}

	public boolean isFinIntIni() {
		return finIntIni;
	}

	public void setFinIntIni(boolean finIntIni) {
		this.finIntIni = finIntIni;
	}

	public int getIdAbogado() {
		return idAbogado;
	}

	public void setIdAbogado(int idAbogado) {
		this.idAbogado = idAbogado;
	}

	public boolean isFinAsgTmp() {
		return finAsgTmp;
	}

	public void setFinAsgTmp(boolean finAsgTmp) {
		this.finAsgTmp = finAsgTmp;
	}

	public String getEmailAboSel() {
		return emailAboSel;
	}

	public void setEmailAboSel(String emailAboSel) {
		this.emailAboSel = emailAboSel;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public boolean isFinAsgClnc() {
		return finAsgClnc;
	}

	public void setFinAsgClnc(boolean finAsgClnc) {
		this.finAsgClnc = finAsgClnc;
	}

}
