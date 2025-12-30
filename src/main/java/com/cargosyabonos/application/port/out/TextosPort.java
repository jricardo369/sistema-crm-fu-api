package com.cargosyabonos.application.port.out;

import java.math.BigDecimal;

public interface TextosPort {
	
	public String textoEnvioRecordatorio(String cliente,String fecha,String hora,String idioma);
	public String textoEnvioPayment(String cliente,BigDecimal monto,String tipo,String idioma);

}
