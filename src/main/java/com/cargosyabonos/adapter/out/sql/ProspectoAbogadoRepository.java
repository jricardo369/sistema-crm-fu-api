package com.cargosyabonos.adapter.out.sql;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.ProspectoAbogadoPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.application.port.out.jpa.ProspectoAbogadoJpa;
import com.cargosyabonos.domain.ProspectoAbogado;
import com.cargosyabonos.domain.ProspectoAbogadoEntity;

@Service
public class ProspectoAbogadoRepository implements ProspectoAbogadoPort{

	Logger log = LoggerFactory.getLogger(ProspectoAbogadoRepository.class);

    @Autowired
	ProspectoAbogadoJpa prosJpa;

    @PersistenceContext
	private EntityManager entityManager;

    @Autowired
    private UsuariosPort usuarioPort;

    @Override
    public List<ProspectoAbogadoEntity> obtenerProspectosAbogado() {
        return prosJpa.findAll();
    }

    @Override
	public List<ProspectoAbogado> obtenerProspectosAbogado(int idProspectoAbogado,String id, String fechai, String fechaf,int idUsuario, String estatus,boolean soloUno,String estado,String telefono,String lawFirmOrContactName,String email,
		String mailPackageReceived,String emailSent,String porcentaje20,String followUp20Porcent,String prospectSentClient) {
		
		List<Object[]> rows = null;
	    rows = obtenerProspectosAbogadoSQL(idProspectoAbogado,id, fechai, fechaf, idUsuario, estatus, soloUno,estado,telefono,lawFirmOrContactName,email,mailPackageReceived,emailSent,porcentaje20,followUp20Porcent,prospectSentClient);
		
	    List<ProspectoAbogado> result = null;

	    result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirAProspectoAbogado(row));
		}
		
		return result;
	}

    private ProspectoAbogado convertirAProspectoAbogado(Object[] row) {
		
		ProspectoAbogado o = new ProspectoAbogado();
		Integer id = (Integer) row[0];
		o.setIdProspectoAbogado(id != null ? id : 0);
        String fecha = (String) row[1];
		o.setFechaAlta(fecha);
        String f = (String) row[2];
		o.setFirma(f);
		String nomb = (String) row[3];
		o.setNombre(nomb);
        String tel = (String) row[4];
		o.setTelefono(tel);
        Integer idE = (Integer) row[5];
        o.setIdEstatusProspecto(idE);
        String desc = (String) row[6];
        o.setEstatusProspecto(desc);
		Integer idUsuario = (Integer) row[7];
		o.setIdUsuario(idUsuario != null ? idUsuario : 0);
        String estado = (String) row[8];
        o.setEstado(estado);
        String direccion = (String) row[9];
        o.setDireccion(direccion);
        o.setEnvioCorreo(Integer.valueOf(row[10].toString()) == 0 ? false : true);
        String fechaEnvioCorreo = (String) row[11];
        o.setFechaEnvioCorreo(fechaEnvioCorreo);
		String emails = (String) row[12];
        o.setEmails(emails);

		o.setPorcentaje1(Integer.valueOf(row[13].toString()) == 0 ? false : true);
        String fechaPorcentaje1 = (String) row[14];
        o.setFechaPorcentaje1(fechaPorcentaje1);
		o.setPorcentaje2(Integer.valueOf(row[15].toString()) == 0 ? false : true);
        String fechaPorcentaje2 = (String) row[16];
        o.setFechaPorcentaje2(fechaPorcentaje2);
		Integer idAbogadoRelacionado = (Integer) row[17];
		o.setIdAbogadoRelacionado(idAbogadoRelacionado != null ? idAbogadoRelacionado : 0);
		String fechaEnvioCliente = (String) row[18];
		o.setFechaEnvioCliente(fechaEnvioCliente);
		o.setMailPackageReceived(Integer.valueOf(row[19].toString()) == 0 ? false : true);
        String fechaMailPackageReceived = (String) row[20];
        o.setFechaMailPackageReceived(fechaMailPackageReceived);
		Integer solicitud_relacionada = (Integer) row[21];
		o.setSolicitudRelacionada(solicitud_relacionada != null ? solicitud_relacionada : 0);
		String fechaRecordatorioLiaison = (String) row[22];
		o.setFechaRecordatorioLiaison(fechaRecordatorioLiaison);
		String fuente = (String) row[23];
		o.setFuente(fuente);

		return o;
	}

	private ProspectoAbogado convertirAProspectoAbogadoParaBusqueda(Object[] row) {
		
		ProspectoAbogado o = new ProspectoAbogado();
		Integer id = (Integer) row[0];
		o.setIdProspectoAbogado(id != null ? id : 0);
        
        String f = (String) row[1];
		o.setFirma(f);
		String nomb = (String) row[2];
		o.setNombre(nomb);
        String tel = (String) row[3];
		o.setTelefono(tel);
		  String email = (String) row[4];
        o.setEmails(email);
		 String estado = (String) row[5];
        o.setEstado(estado);

        String direccion = (String) row[6];
        o.setDireccion(direccion);
        

		return o;
	}


    @SuppressWarnings("unchecked")
	public List<Object[]> obtenerProspectosAbogadoSQL(int idProspectoAbogado,String id,String fechai, String fechaf, int idUsuario, String estatus, boolean soloUno,String estado,String telefono,String lawFirmOrContactName,String email,
		String mailPackageReceived,String emailSent,String porcentaje20,String followUp20Porcent,String prospectSentClient) {

		log.info("obtenerProspectosAbogadoSQL idProspectoAbogado:"+idProspectoAbogado+" fechai:"+fechai+" fechaf:"+fechaf+" idUsuario:"+idUsuario+" estatus:"+estatus+" soloUno:"+soloUno);
		StringBuilder sb = new StringBuilder();
		StringBuilder sbW = new StringBuilder();
		
		String queryS = "SELECT p.id_prospecto_abogado,p.fecha_alta,p.firma,p.nombre,p.telefono,p.id_estatus_prospecto, e.descripcion,p.id_usuario,p.estado,p.direccion,p.envio_correo,p.fecha_envio_correo, " + 
		" COALESCE(mails.emails, '') AS emails, porcentaje1,fecha_porcentaje1,porcentaje2,fecha_porcentaje2, " +
		" p.id_abogado_relacionado,p.fecha_envio_cliente, p.mail_package_received, p.fecha_mail_package_received, p.solicitud_relacionada,p.fecha_recordatorio_liaison,fuente " +
        "FROM prospecto_abogado p " + 
        "LEFT JOIN estatus_prospecto_abogado e ON e.id_estatus_prospecto_abogado = p.id_estatus_prospecto " +
		"LEFT JOIN ( " +
        "SELECT " +
        "em.id_pros_abogado, " +
        "GROUP_CONCAT(DISTINCT em.email ORDER BY em.email SEPARATOR ', ') AS emails " +
        "FROM email_prospecto_abogado em " +
        "GROUP BY em.id_pros_abogado " +
        ") mails ON p.id_prospecto_abogado = mails.id_pros_abogado ";
		sb.append(queryS);

        if(idProspectoAbogado != 0){ 
			if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" p.id_prospecto_abogado = :idProspectoAbogado ");
        }

		String rol = "";
		if(!soloUno){

			rol = usuarioPort.buscarPorId(idUsuario).getRol();
			

			if(!"".equals(estatus)){ 
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" p.id_estatus_prospecto = :estatus ");
			}

			if(rol.equals("12")){
				if (!estatus.equals("INTERESTED") && !estatus.equals("SUCESSFUL") && !estatus.equals("CLOSED")) {
					if(idUsuario != 0){ 
						if (sbW.length() != 0)
							sbW.append(" AND ");
						sbW.append(" p.id_usuario = :idUsuario ");
					}
				}
			}

			if(!fechai.isEmpty()){ 
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" p.fecha_alta BETWEEN :fechai AND :fechaf ");
			}

			if(!id.isEmpty()){ 
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" id_prospecto_abogado LIKE '%"+id+"%' ");
			}

			if(!estado.isEmpty()){ 
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" estado = :estado ");
			}

			if(!telefono.isEmpty()){ 
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" telefono LIKE '%"+telefono+"%' ");
			}

			if(!lawFirmOrContactName.isEmpty()){ 
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" (firma  LIKE '%"+lawFirmOrContactName+"%' OR nombre LIKE '%"+lawFirmOrContactName+"%') ");
			}

			if(!email.isEmpty()){ 
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" emails LIKE '%"+email+"%' ");
			}

			if(mailPackageReceived != null && !mailPackageReceived.isEmpty()){
				if("Yes".equals(mailPackageReceived)){
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.envio_correo = 1 ");
				}
				if("No".equals(mailPackageReceived)){
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.envio_correo = 0 ");
				}
			}

			if(emailSent != null && !emailSent.isEmpty()){
				if("Yes".equals(emailSent)){
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" p.mail_package_received = 1 ");
				}
				if("No".equals(emailSent)){
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" p.mail_package_received = 0 ");
				}
			}

			if(porcentaje20 != null && !porcentaje20.isEmpty()){
				if("Yes".equals(porcentaje20)){
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" p.porcentaje1 = 1 ");
				}
				if("No".equals(porcentaje20)){
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" p.porcentaje1 = 0 ");
				}
			}

			if(followUp20Porcent != null && !followUp20Porcent.isEmpty()){
				if("Yes".equals(followUp20Porcent)){
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" p.porcentaje2 = 1 ");
				}
				if("No".equals(followUp20Porcent)){
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" p.porcentaje2 = 0 ");
				}
			}

			if(prospectSentClient != null && !prospectSentClient.isEmpty()){
				if("Yes".equals(prospectSentClient)){
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" p.id_abogado_relacionado != 0 ");
				}
				if("No".equals(prospectSentClient)){
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" p.id_abogado_relacionado = 0 ");
				}
			}
			

		}

		if (sbW.length() != 0) {
			sb.append(" WHERE " + sbW.toString());
		}

		UtilidadesAdapter.pintarLog("query:" + sb.toString().toString().replaceAll("fechai", fechai).replaceAll("fechaf", fechaf).replaceAll("idProspectoAbogado", String.valueOf(idProspectoAbogado)).replaceAll("idUsuario", String.valueOf(idUsuario)));

		Query query = entityManager.createNativeQuery(sb.toString());
       

        if(idProspectoAbogado != 0){ 
            query.setParameter("idProspectoAbogado", idProspectoAbogado);
        }
		if (!soloUno) {
			if(!"".equals(estatus)){ 
				String estatusF = estatus.equals("PROSPECT") ? "1" : estatus.equals("INTERESTED") ? "2" : estatus.equals("NOT INTERESTED") ? "3" : estatus.equals("SUCESSFUL") ? "4" : estatus.equals("CLOSED") ? "5" : estatus.equals("COLD") ? "6" : "";
				System.out.println("estatusF:"+estatusF);
				query.setParameter("estatus", estatusF);
			}
			if(rol.equals("12")){
				if (!estatus.equals("INTERESTED") && !estatus.equals("SUCESSFUL") && !estatus.equals("CLOSED")) {
					if (idUsuario != 0) {
						query.setParameter("idUsuario", idUsuario);
					}
				}
			}
			if (!fechai.isEmpty()) {
				query.setParameter("fechai", fechai);
				query.setParameter("fechaf", fechaf);
			}
			if (!estado.isEmpty()) {
				query.setParameter("estado", estado);
			}
		}


		List<Object[]> rows = query.getResultList();
		return rows;
	}

	@Override
	public ProspectoAbogadoEntity crearProspectoAbogado(ProspectoAbogadoEntity o) {
		return prosJpa.save(o);
	}

	@Override
	public void actualizarProspectoAbogado(ProspectoAbogadoEntity o) {
		prosJpa.save(o);
	}

	@Override
	public ProspectoAbogadoEntity findByIdProspectoAbogado(int idProspectoAbogado) {
		return prosJpa.findByIdProspectoAbogado(idProspectoAbogado);
	}

	@Override
	public List<ProspectoAbogado> obtenerProspectosAbogadosConMail(String valorBusqueda) {
		
		List<Object[]> rows = null;	
		rows = obtenerAbogadosConMailsSql(valorBusqueda);			
		List<ProspectoAbogado> result = null;

		result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirAProspectoAbogadoParaBusqueda(row));
		}
		
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerAbogadosConMailsSql(String valorBusqueda) {

		UtilidadesAdapter.pintarLog("prospectos abogado por valor:"+valorBusqueda);
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT a.id_prospecto_abogado,a.firma,a.nombre,a.telefono,e.email, a.estado, a.direccion " +
						"FROM prospecto_abogado a " + 
						"LEFT JOIN email_prospecto_abogado e ON e.id_pros_abogado = a.id_prospecto_abogado " + 
						"WHERE a.id_abogado_relacionado = 0 AND  a.id_estatus_prospecto = 2 AND (a.firma LIKE '%"+valorBusqueda+"%' OR a.nombre LIKE '%"+valorBusqueda+"%' OR e.email LIKE '%"+valorBusqueda+"%')");

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

		List<Object[]> rows = query.getResultList();
		return rows;
	}

	@Override
	public void actualizarIdProspectoAbogado(int idProspectoAbogado, int idAbogadoRelacionado, String fechaEnvioCliente,int idSolicitud) {
		prosJpa.actualizarIdProspectoAbogado(idProspectoAbogado, idAbogadoRelacionado, fechaEnvioCliente,idSolicitud);
	}

	public List<ProspectoAbogadoEntity> obtenerProspectosAbogadosParaReminderLiaison(){
		List<ProspectoAbogadoEntity> prospectos = prosJpa.obtenerProspectosAbogadosParaReminderLiaison();
		return prospectos;
	}

	@Override
	public void actualizarFechaRecordatorioLiaison(int idProspectoAbogado, String  fechaRecordatorioLiaison){
		prosJpa.actualizarFechaRecordatorioLiaison(idProspectoAbogado, fechaRecordatorioLiaison);
	}

	@Override
	public void actualizarSolicitud(int idProspectoAbogado,int crmFile,String fecha) {
		prosJpa.actualizarSolicitud(crmFile, fecha,idProspectoAbogado);
	}

	@Override
	public ProspectoAbogadoEntity findBySolicitudRelacionada(int solicitudRelacionada) {
		return prosJpa.findBySolicitudRelacionada(solicitudRelacionada);
	}

}
