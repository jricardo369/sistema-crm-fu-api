package com.cargosyabonos.domain;

import java.math.BigDecimal;

public interface AdeudoSolicitudes {

	int getid_solicitud();
	String getcliente();
	String getemail();
	String gettelefono();
    BigDecimal getamount();
    BigDecimal getdeuda();
    String gettipo();
    
}
