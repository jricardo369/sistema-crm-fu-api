package com.cargosyabonos.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author joser.vazquez
 *
 */
public class SolicitudQueryObj {

	private int id_solicitud;

	private Date fecha_inicio;

	private String cliente;
	
	private String telefono;

	private String apellidos;

	private String email;

	private int id_tipo_solicitud;
	
	private String tipoSolicitud;
	
	private String adicional;

	private String importante;

	private BigDecimal amount;

	private String abogado;

	String numeroDeCaso;

	private int id_estatus_pago;

	private String descEstPago;

	private int id_estatus_solicitud;

	private String descEstSol;

	private boolean Waiver;

	private boolean interview_master;

	private boolean external;

	private boolean asignacion_template;

	boolean asignacion_int_sc;

	private int idUsuarioSc;

	private String usuarioSC;

	private int idUsuarioRevisor;

	private String usuarioRevisor;
	
	private String usuarioRevisorNombre;

	private int idUsuarioRevisando;

	private String usuarioRevisando;
	
	private String usuarioRevisandoNombre;

	private int idUsuarioTemplate;

	private String usuarioTemplate;

	private int idUsuarioExternal;

	private String usuarioExternal;

	String fechaint;

	String fechascale;

	private int tieneScale;
	
	private String estado;
	
	private String referencia;

	private String idioma;
	
	private String direccion;
	
	private String docusign;
	
	private String email_abogado;
	
	private String firma_de_abogados;
	
	private String fecha_nacimiento;
	
	private String tipo_entrevista;
	
	private String parelegal_name;
	
	private String parelegal_emails;
	
	private String parelegal_telefonos;
	
	private Integer numero_entrevistas;
	
	private String dueDate;
	
	private BigDecimal pagos;

	public int getId_solicitud() {
		return id_solicitud;
	}

	public void setId_solicitud(int id_solicitud) {
		this.id_solicitud = id_solicitud;
	}

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId_tipo_solicitud() {
		return id_tipo_solicitud;
	}

	public void setId_tipo_solicitud(int id_tipo_solicitud) {
		this.id_tipo_solicitud = id_tipo_solicitud;
	}

	public String getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(String tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public String getAdicional() {
		return adicional;
	}

	public void setAdicional(String adicional) {
		this.adicional = adicional;
	}

	public String getImportante() {
		return importante;
	}

	public void setImportante(String importante) {
		this.importante = importante;
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

	public String getNumeroDeCaso() {
		return numeroDeCaso;
	}

	public void setNumeroDeCaso(String numeroDeCaso) {
		this.numeroDeCaso = numeroDeCaso;
	}

	public int getId_estatus_pago() {
		return id_estatus_pago;
	}

	public void setId_estatus_pago(int id_estatus_pago) {
		this.id_estatus_pago = id_estatus_pago;
	}

	public String getDescEstPago() {
		return descEstPago;
	}

	public void setDescEstPago(String descEstPago) {
		this.descEstPago = descEstPago;
	}

	public int getId_estatus_solicitud() {
		return id_estatus_solicitud;
	}

	public void setId_estatus_solicitud(int id_estatus_solicitud) {
		this.id_estatus_solicitud = id_estatus_solicitud;
	}

	public String getDescEstSol() {
		return descEstSol;
	}

	public void setDescEstSol(String descEstSol) {
		this.descEstSol = descEstSol;
	}

	public boolean isWaiver() {
		return Waiver;
	}

	public void setWaiver(boolean waiver) {
		Waiver = waiver;
	}

	public boolean isInterview_master() {
		return interview_master;
	}

	public void setInterview_master(boolean interview_master) {
		this.interview_master = interview_master;
	}

	public boolean isExternal() {
		return external;
	}

	public void setExternal(boolean external) {
		this.external = external;
	}

	public boolean isAsignacion_template() {
		return asignacion_template;
	}

	public void setAsignacion_template(boolean asignacion_template) {
		this.asignacion_template = asignacion_template;
	}

	public boolean isAsignacion_int_sc() {
		return asignacion_int_sc;
	}

	public void setAsignacion_int_sc(boolean asignacion_int_sc) {
		this.asignacion_int_sc = asignacion_int_sc;
	}

	public int getIdUsuarioSc() {
		return idUsuarioSc;
	}

	public void setIdUsuarioSc(int idUsuarioSc) {
		this.idUsuarioSc = idUsuarioSc;
	}

	public String getUsuarioSC() {
		return usuarioSC;
	}

	public void setUsuarioSC(String usuarioSC) {
		this.usuarioSC = usuarioSC;
	}

	public int getIdUsuarioRevisor() {
		return idUsuarioRevisor;
	}

	public void setIdUsuarioRevisor(int idUsuarioRevisor) {
		this.idUsuarioRevisor = idUsuarioRevisor;
	}

	public String getUsuarioRevisor() {
		return usuarioRevisor;
	}

	public void setUsuarioRevisor(String usuarioRevisor) {
		this.usuarioRevisor = usuarioRevisor;
	}

	public String getUsuarioRevisorNombre() {
		return usuarioRevisorNombre;
	}

	public void setUsuarioRevisorNombre(String usuarioRevisorNombre) {
		this.usuarioRevisorNombre = usuarioRevisorNombre;
	}

	public int getIdUsuarioRevisando() {
		return idUsuarioRevisando;
	}

	public void setIdUsuarioRevisando(int idUsuarioRevisando) {
		this.idUsuarioRevisando = idUsuarioRevisando;
	}

	public String getUsuarioRevisando() {
		return usuarioRevisando;
	}

	public void setUsuarioRevisando(String usuarioRevisando) {
		this.usuarioRevisando = usuarioRevisando;
	}

	public String getUsuarioRevisandoNombre() {
		return usuarioRevisandoNombre;
	}

	public void setUsuarioRevisandoNombre(String usuarioRevisandoNombre) {
		this.usuarioRevisandoNombre = usuarioRevisandoNombre;
	}

	public int getIdUsuarioTemplate() {
		return idUsuarioTemplate;
	}

	public void setIdUsuarioTemplate(int idUsuarioTemplate) {
		this.idUsuarioTemplate = idUsuarioTemplate;
	}

	public String getUsuarioTemplate() {
		return usuarioTemplate;
	}

	public void setUsuarioTemplate(String usuarioTemplate) {
		this.usuarioTemplate = usuarioTemplate;
	}

	public int getIdUsuarioExternal() {
		return idUsuarioExternal;
	}

	public void setIdUsuarioExternal(int idUsuarioExternal) {
		this.idUsuarioExternal = idUsuarioExternal;
	}

	public String getUsuarioExternal() {
		return usuarioExternal;
	}

	public void setUsuarioExternal(String usuarioExternal) {
		this.usuarioExternal = usuarioExternal;
	}

	public String getFechaint() {
		return fechaint;
	}

	public void setFechaint(String fechaint) {
		this.fechaint = fechaint;
	}

	public String getFechascale() {
		return fechascale;
	}

	public void setFechascale(String fechascale) {
		this.fechascale = fechascale;
	}

	public int getTieneScale() {
		return tieneScale;
	}

	public void setTieneScale(int tieneScale) {
		this.tieneScale = tieneScale;
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

	public String getDocusign() {
		return docusign;
	}

	public void setDocusign(String docusign) {
		this.docusign = docusign;
	}

	public String getEmail_abogado() {
		return email_abogado;
	}

	public void setEmail_abogado(String email_abogado) {
		this.email_abogado = email_abogado;
	}

	public String getFirma_de_abogados() {
		return firma_de_abogados;
	}

	public void setFirma_de_abogados(String firma_de_abogados) {
		this.firma_de_abogados = firma_de_abogados;
	}

	public String getFecha_nacimiento() {
		return fecha_nacimiento;
	}

	public void setFecha_nacimiento(String fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}

	public String getTipo_entrevista() {
		return tipo_entrevista;
	}

	public void setTipo_entrevista(String tipo_entrevista) {
		this.tipo_entrevista = tipo_entrevista;
	}

	public String getParelegal_name() {
		return parelegal_name;
	}

	public void setParelegal_name(String parelegal_name) {
		this.parelegal_name = parelegal_name;
	}

	public String getParelegal_emails() {
		return parelegal_emails;
	}

	public void setParelegal_emails(String parelegal_emails) {
		this.parelegal_emails = parelegal_emails;
	}

	public String getParelegal_telefonos() {
		return parelegal_telefonos;
	}

	public void setParelegal_telefonos(String parelegal_telefonos) {
		this.parelegal_telefonos = parelegal_telefonos;
	}

	public Integer getNumero_entrevistas() {
		return numero_entrevistas;
	}

	public void setNumero_entrevistas(Integer numero_entrevistas) {
		this.numero_entrevistas = numero_entrevistas;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getPagos() {
		return pagos;
	}

	public void setPagos(BigDecimal pagos) {
		this.pagos = pagos;
	}
	
	

}
