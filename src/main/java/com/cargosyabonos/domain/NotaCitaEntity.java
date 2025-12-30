package com.cargosyabonos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nota_cita")
public class NotaCitaEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_nota")
	private int idNota;
	
	@Column(name = "id_cita")
	private int idCita;
	
	@Column(name = "descripcion", length = 200)
	private String descripcion;
	
	@Column(name = "fecha_creacion", length = 10)
	private String fechaCreacion;
	
	@Column(name = "hora", length = 10)
	private String hora;
	
	@Column(name = "tipo", length = 50)
	private String tipo;
	
	@Column(name = "referencia", length = 45)
	private String referencia;
	
	@Column(name = "tiempo_sesion", length = 20)
	private String tiempoSesion;
	
	@Column(name = "enfoque_principal", length = 200)
	private String enfoquePrincipal;
	
	@Column(name = "si_hi_asignado", length = 1)
	private boolean SiHiAsignado;
	
	@Column(name = "locacion_verificada", length = 1)
	private boolean locacionVerificada;
	
	@Column(name = "sintoma_comportamiento1", length = 45)
	private String sintomaComportamiento1;
	
	@Column(name = "rating1", length = 1)
	private String rating1;
	
	@Column(name = "sintoma_comportamiento2", length = 45)
	private String sintomaComportamiento2;
	
	@Column(name = "rating2", length = 1)
	private String rating2;
	
	@Column(name = "sintoma_comportamiento3", length = 45)
	private String sintomaComportamiento3;
	
	@Column(name = "rating3", length = 1)
	private String rating3;
	
	@Column(name = "tipo_contenido_sesion", length = 200)
	private String tipoContenidoSesion;
	
	@Column(name = "comentarios", length = 200)
	private String comentarios;
	
	@Column(name = "respuesta_de_intervension", length = 4)
	private String respuestaDeIntervencion;
	
	@Column(name = "resumen_evaluacion", length = 200)
	private String resumenEvaluacion;
	
	@Column(name = "plan_futuro", length = 200)
	private String planFuturo;

	public int getIdNota() {
		return idNota;
	}

	public void setIdNota(int idNota) {
		this.idNota = idNota;
	}

	public int getIdCita() {
		return idCita;
	}

	public void setIdCita(int idCita) {
		this.idCita = idCita;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getTiempoSesion() {
		return tiempoSesion;
	}

	public void setTiempoSesion(String tiempoSesion) {
		this.tiempoSesion = tiempoSesion;
	}

	public String getEnfoquePrincipal() {
		return enfoquePrincipal;
	}

	public void setEnfoquePrincipal(String enfoquePrincipal) {
		this.enfoquePrincipal = enfoquePrincipal;
	}

	

	public String getSintomaComportamiento1() {
		return sintomaComportamiento1;
	}

	public void setSintomaComportamiento1(String sintomaComportamiento1) {
		this.sintomaComportamiento1 = sintomaComportamiento1;
	}

	public String getRating1() {
		return rating1;
	}

	public void setRating1(String rating1) {
		this.rating1 = rating1;
	}

	public String getSintomaComportamiento2() {
		return sintomaComportamiento2;
	}

	public void setSintomaComportamiento2(String sintomaComportamiento2) {
		this.sintomaComportamiento2 = sintomaComportamiento2;
	}

	public String getRating2() {
		return rating2;
	}

	public void setRating2(String rating2) {
		this.rating2 = rating2;
	}

	public String getSintomaComportamiento3() {
		return sintomaComportamiento3;
	}

	public void setSintomaComportamiento3(String sintomaComportamiento3) {
		this.sintomaComportamiento3 = sintomaComportamiento3;
	}

	public String getRating3() {
		return rating3;
	}

	public void setRating3(String rating3) {
		this.rating3 = rating3;
	}

	public String getTipoContenidoSesion() {
		return tipoContenidoSesion;
	}

	public void setTipoContenidoSesion(String tipoContenidoSesion) {
		this.tipoContenidoSesion = tipoContenidoSesion;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getRespuestaDeIntervencion() {
		return respuestaDeIntervencion;
	}

	public void setRespuestaDeIntervencion(String respuestaDeIntervencion) {
		this.respuestaDeIntervencion = respuestaDeIntervencion;
	}

	public String getResumenEvaluacion() {
		return resumenEvaluacion;
	}

	public void setResumenEvaluacion(String resumenEvaluacion) {
		this.resumenEvaluacion = resumenEvaluacion;
	}

	public String getPlanFuturo() {
		return planFuturo;
	}

	public void setPlanFuturo(String planFuturo) {
		this.planFuturo = planFuturo;
	}

	public boolean isSiHiAsignado() {
		return SiHiAsignado;
	}

	public void setSiHiAsignado(boolean siHiAsignado) {
		SiHiAsignado = siHiAsignado;
	}

	public boolean isLocacionVerificada() {
		return locacionVerificada;
	}

	public void setLocacionVerificada(boolean locacionVerificada) {
		this.locacionVerificada = locacionVerificada;
	}

	@Override
	public String toString() {
		return "NotaCitaEntity [idNota=" + idNota + ", idCita=" + idCita + ", descripcion=" + descripcion
				+ ", fechaCreacion=" + fechaCreacion + ", hora=" + hora + ", tipo=" + tipo + ", referencia="
				+ referencia + ", tiempoSesion=" + tiempoSesion + ", enfoquePrincipal=" + enfoquePrincipal
				+ ", SiHiAsignado=" + SiHiAsignado + ", locacionVerificada=" + locacionVerificada
				+ ", sintomaComportamiento1=" + sintomaComportamiento1 + ", rating1=" + rating1
				+ ", sintomaComportamiento2=" + sintomaComportamiento2 + ", rating2=" + rating2
				+ ", sintomaComportamiento3=" + sintomaComportamiento3 + ", rating3=" + rating3
				+ ", tipoContenidoSesion=" + tipoContenidoSesion + ", comentarios=" + comentarios
				+ ", respuestaDeIntervencion=" + respuestaDeIntervencion + ", resumenEvaluacion=" + resumenEvaluacion
				+ ", planFuturo=" + planFuturo + "]";
	}

}
