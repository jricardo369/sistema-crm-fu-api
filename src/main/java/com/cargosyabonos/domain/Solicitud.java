package com.cargosyabonos.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author joser.vazquez
 *
 */
public class Solicitud {
	
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
	private String fecha_schedule;
	private String fecha_schedule_scales;
	private String fechaClinicianAppo;
	private int idUsuarioRevisor;
	private int idUusuarioRevisando;
	private String usuarioRevisor;
	private String usuarioRevisorNombre;
	private String usuarioRevisando;
	private String usuarioRevisandoNombre;
	private String comentario;
	private String fechaNacimiento;
	private String adicional;
	private String adeudo;
	
	private String idioma;
	private String tipoEntrevista;
	private String direccion;
	private String estado;
	private String referencia;
	private String apellidos;
	
	private boolean waiver;
	private String paralegalName;
	private String paralegalEmails;
	private String paralegalTelefonos;
	private boolean asignacionTemplate;
	private int numeroEntrevistas;
	private int idUsuarioTemplate;
	private String usuarioTemplate;
	private boolean external;
	private int idUsuarioExternal;
	private int usuarioExternal;
	private boolean asignacionIntSc;
	private int usuarioIntSc;
	private String usuarioIntScNombre;
	private int usuarioInterview;
	private String usuarioInterviewNombre;
	private boolean finIntSc;
	private boolean interviewMaster;
	private boolean tieneScale;
	private String dueDate;
	private boolean finIntIni;
	private int assignedClinician;
	private int idAbogado;
	private boolean finAsgTmp;
	private boolean noShow;
	private boolean lostFile;
	private boolean rejectFile;
	private BigDecimal pagos;
	private String emailAboSel;
	private String motivo;
	private String sexo;
	
	private String usuarioCreacion;
	
	private boolean conCupon;
	private String fechaCupon;
	
	private String usuarioClinicianNombre;
	private int idRolUsInt;
	
	private boolean finAsgClnc;

	private String importantNotes;

	private boolean signedClnc;

	public boolean isSignedClnc() {
		return signedClnc;
	}
	public void setSignedClnc(boolean signedClnc) {
		this.signedClnc = signedClnc;
	}
	public String getImportantNotes() {
		return importantNotes;
	}
	public void setImportantNotes(String importantNotes) {
		this.importantNotes = importantNotes;
	}
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
	public String getFecha_schedule() {
		return fecha_schedule;
	}
	public void setFecha_schedule(String fecha_schedule) {
		this.fecha_schedule = fecha_schedule;
	}
	public String getUsuarioRevisor() {
		return usuarioRevisor;
	}
	public void setUsuarioRevisor(String usuarioRevisor) {
		this.usuarioRevisor = usuarioRevisor;
	}
	public String getUsuarioRevisando() {
		return usuarioRevisando;
	}
	public void setUsuarioRevisando(String usuarioRevisando) {
		this.usuarioRevisando = usuarioRevisando;
	}
	public int getIdUsuarioRevisor() {
		return idUsuarioRevisor;
	}
	public void setIdUsuarioRevisor(int idUsuarioRevisor) {
		this.idUsuarioRevisor = idUsuarioRevisor;
	}
	public int getIdUsuarioRevisando() {
		return idUusuarioRevisando;
	}
	public void setIdUsuarioRevisando(int idUusuarioRevisando) {
		this.idUusuarioRevisando = idUusuarioRevisando;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public int getIdUusuarioRevisando() {
		return idUusuarioRevisando;
	}
	public void setIdUusuarioRevisando(int idUusuarioRevisando) {
		this.idUusuarioRevisando = idUusuarioRevisando;
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
	public String getAdeudo() {
		return adeudo;
	}
	public void setAdeudo(String adeudo) {
		this.adeudo = adeudo;
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
	public String getUsuarioRevisandoNombre() {
		return usuarioRevisandoNombre;
	}
	public void setUsuarioRevisandoNombre(String usuarioRevisandoNombre) {
		this.usuarioRevisandoNombre = usuarioRevisandoNombre;
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
	public boolean isWaiver() {
		return waiver;
	}
	public void setWaiver(boolean waiver) {
		this.waiver = waiver;
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
	public String getUsuarioIntScNombre() {
		return usuarioIntScNombre;
	}
	public void setUsuarioIntScNombre(String usuarioIntScNombre) {
		this.usuarioIntScNombre = usuarioIntScNombre;
	}
	public boolean isFinIntSc() {
		return finIntSc;
	}
	public void setFinIntSc(boolean finIntSc) {
		this.finIntSc = finIntSc;
	}
	public String getFecha_schedule_scales() {
		return fecha_schedule_scales;
	}
	public void setFecha_schedule_scales(String fecha_schedule_scales) {
		this.fecha_schedule_scales = fecha_schedule_scales;
	}
	public boolean isInterviewMaster() {
		return interviewMaster;
	}
	public void setInterviewMaster(boolean interviewMaster) {
		this.interviewMaster = interviewMaster;
	}
	public boolean isTieneScale() {
		return tieneScale;
	}
	public void setTieneScale(boolean tieneScale) {
		this.tieneScale = tieneScale;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public int getIdUsuarioTemplate() {
		return idUsuarioTemplate;
	}
	public void setIdUsuarioTemplate(int idUsuarioTemplate) {
		this.idUsuarioTemplate = idUsuarioTemplate;
	}
	public int getIdUsuarioExternal() {
		return idUsuarioExternal;
	}
	public void setIdUsuarioExternal(int idUsuarioExternal) {
		this.idUsuarioExternal = idUsuarioExternal;
	}
	public String getUsuarioTemplate() {
		return usuarioTemplate;
	}
	public void setUsuarioTemplate(String usuarioTemplate) {
		this.usuarioTemplate = usuarioTemplate;
	}
	public String getUsuarioRevisorNombre() {
		return usuarioRevisorNombre;
	}
	public void setUsuarioRevisorNombre(String usuarioRevisorNombre) {
		this.usuarioRevisorNombre = usuarioRevisorNombre;
	}
	public int getUsuarioInterview() {
		return usuarioInterview;
	}
	public void setUsuarioInterview(int usuarioInterview) {
		this.usuarioInterview = usuarioInterview;
	}
	public String getUsuarioInterviewNombre() {
		return usuarioInterviewNombre;
	}
	public void setUsuarioInterviewNombre(String usuarioInterviewNombre) {
		this.usuarioInterviewNombre = usuarioInterviewNombre;
	}
	public boolean isFinIntIni() {
		return finIntIni;
	}
	public void setFinIntIni(boolean finIntIni) {
		this.finIntIni = finIntIni;
	}
	public int getAssignedClinician() {
		return assignedClinician;
	}
	public void setAssignedClinician(int assignedClinician) {
		this.assignedClinician = assignedClinician;
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
	public boolean isNoShow() {
		return noShow;
	}
	public void setNoShow(boolean noShow) {
		this.noShow = noShow;
	}
	public boolean isLostFile() {
		return lostFile;
	}
	public void setLostFile(boolean lostFile) {
		this.lostFile = lostFile;
	}
	public boolean isRejectFile() {
		return rejectFile;
	}
	public void setRejectFile(boolean rejectFile) {
		this.rejectFile = rejectFile;
	}
	public BigDecimal getPagos() {
		return pagos;
	}
	public void setPagos(BigDecimal pagos) {
		this.pagos = pagos;
	}
	public String getEmailAboSel() {
		return emailAboSel;
	}
	public void setEmailAboSel(String emailAboSel) {
		this.emailAboSel = emailAboSel;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public boolean isConCupon() {
		return conCupon;
	}
	public void setConCupon(boolean conCupon) {
		this.conCupon = conCupon;
	}
	public String getFechaCupon() {
		return fechaCupon;
	}
	public void setFechaCupon(String fechaCupon) {
		this.fechaCupon = fechaCupon;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getUsuarioClinicianNombre() {
		return usuarioClinicianNombre;
	}
	public void setUsuarioClinicianNombre(String usuarioClinicianNombre) {
		this.usuarioClinicianNombre = usuarioClinicianNombre;
	}
	public int getIdRolUsInt() {
		return idRolUsInt;
	}
	public void setIdRolUsInt(int idRolUsInt) {
		this.idRolUsInt = idRolUsInt;
	}
	public boolean isFinAsgClnc() {
		return finAsgClnc;
	}
	public void setFinAsgClnc(boolean finAsgClnc) {
		this.finAsgClnc = finAsgClnc;
	}
	public String getFechaClinicianAppo() {
		return fechaClinicianAppo;
	}
	public void setFechaClinicianAppo(String fechaClinicianAppo) {
		this.fechaClinicianAppo = fechaClinicianAppo;
	}
	
}
