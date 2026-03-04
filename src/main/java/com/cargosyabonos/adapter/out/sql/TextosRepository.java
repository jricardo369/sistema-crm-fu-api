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
		ConfiguracionEntity co = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-PHONE")).findAny().orElse(null);
		ConfiguracionEntity tm = confjs.stream().filter(a -> a.getCodigo().equals("TXT-MSJ-RECORD-"+idioma)).findAny().orElse(null);
		
		String mensaje = tm.getValor();

		mensaje = mensaje.replace("${clt}", cliente);
		mensaje = mensaje.replace("${date}", fecha);
		mensaje = mensaje.replace("${hour}", hora);

		mensaje = mensaje.replace("${contactName}", cn.getValor());
		mensaje = mensaje.replace("${contactInfo}", ci.getValor());
		mensaje = mensaje.replace("${phone}", co.getValor());	
		
		return mensaje;
	}

	@Override
	public String textoEnvioPayment(String cliente,BigDecimal monto, String tipo, String idioma) {

		List<ConfiguracionEntity> confjs = confPort.obtenerConfiguraciones();
		ConfiguracionEntity cn = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-NOMBRE")).findAny().orElse(null);
		ConfiguracionEntity ci = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-INFO")).findAny().orElse(null);
		ConfiguracionEntity co = confjs.stream().filter(a -> a.getCodigo().equals("CONTACT-PHONE")).findAny().orElse(null);
		ConfiguracionEntity mp = confjs.stream().filter(a -> a.getCodigo().equals("TXT-MSJ-PAYMENT-"+idioma)).findAny().orElse(null);

		String mensaje = mp.getValor();

		if(cliente != null){
			mensaje = mensaje.replace("${clt}", cliente);
		}

		mensaje = mensaje.replace("${amount}", monto.toString());
		mensaje = mensaje.replace("${type}", tipo);

		mensaje = mensaje.replace("${contactName}", cn.getValor());
		mensaje = mensaje.replace("${contactInfo}", ci.getValor());
		mensaje = mensaje.replace("${phone}", co.getValor());

		return mensaje;
	}

	
}
