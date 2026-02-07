package com.cargosyabonos.domain;


public class ReporteDash {
	
	private int todas;
	private int activas;
	private int completas;
	private int noShow;
	private int lost;
	private int rejectFile;
	private int cancelSchedules;
	private int ventas;
	private boolean verVentas;
	
	public boolean isVerVentas() {
		return verVentas;
	}
	public void setVerVentas(boolean verVentas) {
		this.verVentas = verVentas;
	}
	public void setVentas(int ventas) {
		this.ventas = ventas;
	}
	public int getVentas() {
		return ventas;
	}
	public int getTodas() {
		return todas;
	}
	public void setTodas(int todas) {
		this.todas = todas;
	}
	public int getActivas() {
		return activas;
	}
	public void setActivas(int activas) {
		this.activas = activas;
	}
	public int getCompletas() {
		return completas;
	}
	public void setCompletas(int completas) {
		this.completas = completas;
	}
	public int getNoShow() {
		return noShow;
	}
	public void setNoShow(int noShow) {
		this.noShow = noShow;
	}
	public int getLost() {
		return lost;
	}
	public void setLost(int lost) {
		this.lost = lost;
	}
	public int getRejectFile() {
		return rejectFile;
	}
	public void setRejectFile(int rejectFile) {
		this.rejectFile = rejectFile;
	}
	public int getCancelSchedules() {
		return cancelSchedules;
	}
	public void setCancelSchedules(int cancelSchedules) {
		this.cancelSchedules = cancelSchedules;
	}
	
	
	
}
