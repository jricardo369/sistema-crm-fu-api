package com.cargosyabonos.domain;


public class Notificacion {
	
	private String titulo;
	private String usuario;
	private String cuerpo;
	private boolean mail;
	private boolean mensaje;
	private String layout;
	private String archivo;
	private String firmasAbogados;
	private int idSolicitud;
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getCuerpo() {
		return cuerpo;
	}
	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
	public boolean isMail() {
		return mail;
	}
	public void setMail(boolean mail) {
		this.mail = mail;
	}
	public boolean isMensaje() {
		return mensaje;
	}
	public void setMensaje(boolean mensaje) {
		this.mensaje = mensaje;
	}
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public String getArchivo() {
		return archivo;
	}
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public int getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getFirmasAbogados() {
		return firmasAbogados;
	}
	public void setFirmasAbogados(String firmasAbogados) {
		this.firmasAbogados = firmasAbogados;
	}

}
