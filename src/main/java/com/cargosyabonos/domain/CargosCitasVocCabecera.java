package com.cargosyabonos.domain;

import java.math.BigDecimal;
import java.util.List;

public class CargosCitasVocCabecera {

    BigDecimal totalPagado;
    BigDecimal totalNoPagado;
    BigDecimal balance;
    List<CargosCitasVoc> CargoVoc;
    public BigDecimal getTotalPagado() {
        return totalPagado;
    }
    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }
    public BigDecimal getTotalNoPagado() {
        return totalNoPagado;
    }
    public void setTotalNoPagado(BigDecimal totalNoPagado) {
        this.totalNoPagado = totalNoPagado;
    }
    public List<CargosCitasVoc> getCargoVoc() {
        return CargoVoc;
    }
    public void setCargoVoc(List<CargosCitasVoc> cargoVoc) {
        CargoVoc = cargoVoc;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    

}
