package com.cargosyabonos.domain;

import java.math.BigDecimal;


public interface CitaSql {

	 int getidCita();
	 String getcomentario();
	 String getfecha();
	 String gethora();
	 String gettipo();
	 boolean getdosCitas();
	 int getidSolicitud();
	 boolean getnoShow();
	 int getidUsuario();
	 BigDecimal getamount();
	 boolean getpagado();
	 String getnombreUsuario();
	 String getfechaPagado();
	 String getcolor();
	 boolean finSchedule();
	 String getdescEst();
	 int getidEstaSol();
	 String gettieneNota();
	 String getcasenumber();
	 String getcliente();
	 String getfechaCreacion();
	 String getCodigoRecurrencia();
	 String gettieneFirma();
	 String getsupervisor();
	 int getrechazada();
	 String getfechaAprobacion();
	 String gethoraNota();
	 String getfechacreacionnota();
	 String getemailterapeuta();
	 String getnombreterapeuta();

}
