package com.cargosyabonos.adapter.out.sql;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.ConfiguracionPort;
import com.cargosyabonos.application.port.out.TextosPort;
import com.cargosyabonos.domain.ConfiguracionEntity;


@Service
public class TextosRepository implements TextosPort{
	
	@Autowired
	private ConfiguracionPort confPort;

	@Override
	public String textoEnvioRecordatorio(String cliente, String fecha, String hora, String idioma) {
		List<ConfiguracionEntity> confjs = confPort.obtenerConfiguraciones();
		ConfiguracionEntity cn = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-NOMBRE")).findAny().orElse(null);
		ConfiguracionEntity ci = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-INFO")).findAny().orElse(null);
		ConfiguracionEntity co = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-ORG")).findAny().orElse(null);
		ConfiguracionEntity tm = confjs.stream().filter(a -> a.getCodigo().equals("TXT-MSJ-RECORD-"+idioma)).findAny().orElse(null);
		
		String mensaje = tm.getValor();
		mensaje = mensaje.replace("${clt}", cliente);
		mensaje = mensaje.replace("${fecha}", fecha);
		mensaje = mensaje.replace("${hora}", hora);
		mensaje = mensaje.replace("${nContacto}", cn.getValor());
		mensaje = mensaje.replace("${iContacto}", ci.getValor());
		mensaje = mensaje.replace("${sOrganizacion}", co.getValor());	
		
		return mensaje;
	}

	@Override
	public String textoEnvioPayment(String cliente,BigDecimal monto, String tipo, String idioma) {
		ConfiguracionEntity ce = confPort.obtenerConfiguracionPorCodigo("TXT-MSJ-PAYMENT-"+idioma);
		String mensaje = ce.getValor();
		if(cliente != null){
			mensaje = mensaje.replace("${cliente}", cliente);
		}
		mensaje = mensaje.replace("${monto}", monto.toString());
		mensaje = mensaje.replace("${tipo}", tipo);
		return mensaje;
	}

	
}
