package com.cargosyabonos.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author joser.vazquez
 *
 */
public interface SolicitudQuery {

	int getid_solicitud();

	Date getfecha_inicio();

	String getcliente();
	
	String gettelefono();

	String getapellidos();

	String getemail();

	int getid_tipo_solicitud();
	
	String gettipoSolicitud();
	
	String getadicional();

	String getimportante();

	BigDecimal getamount();

	String getabogado();

	String getnumeroDeCaso();

	int getid_estatus_pago();

	String getdescEstPago();

	int getid_estatus_solicitud();

	String getdescEstSol();

	boolean getWaiver();

	boolean getinterview_master();

	boolean getexternal();

	boolean getasignacion_template();

	boolean getasignacion_int_sc();

	int getidUsuarioSc();

	String getusuarioSC();

	int getidUsuarioRevisor();

	String getusuarioRevisor();
	
	String getusuarioRevisorNombre();

	int getidUsuarioRevisando();

	String getusuarioRevisando();
	
	String getusuarioRevisandoNombre();

	int getidUsuarioTemplate();

	String getusuarioTemplate();

	int getidUsuarioExternal();

	String getusuarioExternal();

	String getfechaint();

	String getfechascale();

	int gettieneScale();
	
	String getestado();
	
	String getreferencia();

	String getidioma();
	
	String getdireccion();
	
	String getdocusign();
	
	String getemail_abogado();
	
	String getfirma_de_abogados();
	
	String getfecha_nacimiento();
	
	String gettipo_entrevista();
	
	String getparelegal_name();
	
	String getparelegal_emails();
	
	String getparelegal_telefonos();
	
	Integer getnumero_entrevistas();
	
	String getdueDate();
	
	int getAssignedClinician();
	
	BigDecimal getpagos();

}
