package com.cargosyabonos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "prospecto_abogado")
public class ProspectoAbogadoEntity {

     @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_prospecto_abogado")
    private int idProspectoAbogado;

    @Column(name = "fecha_alta", length = 10)
    private String fechaAlta;

    @Column(name = "firma", length = 60)
    private String firma;
    
    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "telefono", length = 16)
    private String telefono;

    @Column(name = "id_estatus_prospecto", length = 100)
    private int idEstatusProspecto;

    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "estado")
    public String estado;

    private String direccion;

    @Column(name = "envio_correo")
    private boolean envioCorreo;

    @Column(name = "fecha_envio_correo", length = 10)
    private String fechaEnvioCorreo;

    private boolean porcentaje1;

    @Column(name = "fecha_porcentaje1", length = 10)
    private String fechaPorcentaje1;
    
    private boolean porcentaje2;

    @Column(name = "fecha_porcentaje2", length = 10)
    private String fechaPorcentaje2;

    @Column(name = "id_abogado_relacionado")
    private int idAbogadoRelacionado;

    @Column(name = "fecha_envio_cliente", length = 10)
    private String fechaEnvioCliente;

    @Column(name = "mail_package_received")
    private boolean mailPackageReceived;

    @Column(name = "fecha_mail_package_received", length = 10)
    private String fechaMailPackageReceived;
    
    @Column(name = "solicitud_relacionada")
    private int solicitudRelacionada;

     @Column(name = "fecha_recordatorio_liaison", length = 10)
    private String fechaRecordatorioLiaison;

    private String fuente;

    public int getIdProspectoAbogado() {
        return idProspectoAbogado;
    }

    public void setIdProspectoAbogado(int idProspectoAbogado) {
        this.idProspectoAbogado = idProspectoAbogado;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdEstatusProspecto() {
        return idEstatusProspecto;
    }

    public void setIdEstatusProspecto(int idEstatusProspecto) {
        this.idEstatusProspecto = idEstatusProspecto;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isEnvioCorreo() {
        return envioCorreo;
    }

    public void setEnvioCorreo(boolean envioCorreo) {
        this.envioCorreo = envioCorreo;
    }

    public String getFechaEnvioCorreo() {
        return fechaEnvioCorreo;
    }

    public void setFechaEnvioCorreo(String fechaEnvioCorreo) {
        this.fechaEnvioCorreo = fechaEnvioCorreo;
    }

    public boolean isPorcentaje1() {
        return porcentaje1;
    }
    public void setPorcentaje1(boolean porcentaje1) {
        this.porcentaje1 = porcentaje1;
    }
    public String getFechaPorcentaje1() {
        return fechaPorcentaje1;
    }
    public void setFechaPorcentaje1(String fechaPorcentaje1) {
        this.fechaPorcentaje1 = fechaPorcentaje1;
    }
    public boolean isPorcentaje2() {
        return porcentaje2;
    }
    public void setPorcentaje2(boolean porcentaje2) {
        this.porcentaje2 = porcentaje2;
    }
    public String getFechaPorcentaje2() {
        return fechaPorcentaje2;
    }
    public void setFechaPorcentaje2(String fechaPorcentaje2) {
        this.fechaPorcentaje2 = fechaPorcentaje2;
    }

    public int getIdAbogadoRelacionado() {
        return idAbogadoRelacionado;
    }

    public void setIdAbogadoRelacionado(int idAbogadoRelacionado) {
        this.idAbogadoRelacionado = idAbogadoRelacionado;
    }

    public String getFechaEnvioCliente() {
        return fechaEnvioCliente;
    }

    public void setFechaEnvioCliente(String fechaEnvioCliente) {
        this.fechaEnvioCliente = fechaEnvioCliente;
    }

    public boolean isMailPackageReceived() {
        return mailPackageReceived;
    }

    public void setMailPackageReceived(boolean mailPackageReceived) {
        this.mailPackageReceived = mailPackageReceived;
    }

    public String getFechaMailPackageReceived() {
        return fechaMailPackageReceived;
    }

    public void setFechaMailPackageReceived(String fechaMailPackageReceived) {
        this.fechaMailPackageReceived = fechaMailPackageReceived;
    }

    public int getSolicitudRelacionada() {
        return solicitudRelacionada;
    }

    public void setSolicitudRelacionada(int solicitudRelacionada) {
        this.solicitudRelacionada = solicitudRelacionada;
    }

    public String getFechaRecordatorioLiaison() {
        return fechaRecordatorioLiaison;
    }

    public void setFechaRecordatorioLiaison(String fechaRecordatorioLiaison) {
        this.fechaRecordatorioLiaison = fechaRecordatorioLiaison;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }
    
    
    
    
}
