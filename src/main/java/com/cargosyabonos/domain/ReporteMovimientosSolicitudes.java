package com.cargosyabonos.domain;

import java.math.BigDecimal;
import java.util.Date;

public interface ReporteMovimientosSolicitudes {

	int getid_movimiento();
	String getfolio();
	String getcliente();
    String gettipo_pago();
    int getid_tipo_pago();
    String gettipo();
    Date getfecha();
	BigDecimal getmonto();
	BigDecimal getdescuento();
    String getdescripcion();
    int getid_solicitud();
    
}
