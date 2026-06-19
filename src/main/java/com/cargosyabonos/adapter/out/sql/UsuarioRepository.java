package com.cargosyabonos.adapter.out.sql;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.application.port.out.jpa.UsuarioJpa;
import com.cargosyabonos.domain.UsuarioEntity;
import com.cargosyabonos.domain.UsuarioObj;

@Repository
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class UsuarioRepository implements UsuariosPort {

	@Autowired
	UsuarioJpa usJpa;

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${ambiente}")
	private String ambiente;
	
	@Value("${ruta.servidor}")
	private String rutaServidor;

	@Value("${ruta.servidor.qas}")
	private String rutaServidorQas;

	@Value("${ruta.servidor.pro}")
	private String rutaServidorPro;

	@Override
	public List<UsuarioEntity> obtenerUsuarios() {
		return usJpa.obtenerTodosUsuariosActivos();
	}

	@Override
	public UsuarioEntity buscarPorUsuario(String usuario) {
		return usJpa.findByUsuario(usuario);
	}
	
	@Override
	public UsuarioEntity buscarPorNombre(String nombre){
		return usJpa.findByNombre(nombre);
	}
	
	@Override
	public UsuarioEntity encontrarPorNombre(String nombre){
		return usJpa.encontrarPorNombre(nombre);
	}
	
	@Override
	public UsuarioEntity buscarPorCorreo(String correo) {
		return usJpa.findBycorreoElectronico(correo);
	}

	@Override
	public UsuarioEntity buscarPorCorreoWithLike(String correo) {
		return usJpa.findBycorreoElectronicoWithLike(correo);
	}

	@Override
	public UsuarioEntity crearUsuario(UsuarioEntity usuario) {
		UsuarioEntity u = usJpa.save(usuario);
		return u;
	}

	@Override
	public void actualizarUsuario(UsuarioEntity usuario) {
		usJpa.save(usuario);
	}

	@Override
	public void eliminarUsuario(UsuarioEntity usuario) {
		usJpa.delete(usuario);
	}

	@Override
	public UsuarioEntity buscarSoloPorUsuario(String usuario) {
		return usJpa.encontrarUsuario(usuario);
	}

	@Override
	public UsuarioEntity buscarPorId(int idUsuario) {
		return usJpa.findByIdUsuario(idUsuario);
	}
	
	@Override
	public List<UsuarioEntity> obtenerPorUsuarioPorSociedad(int idSociedad) {
		return usJpa.obtenerUsuariosPorSociedad(idSociedad);
	}

	@Override
	public UsuarioEntity obtenerUsuarioPorRol(int idRol) {
		return usJpa.obtenerUsuarioPorRol(idRol);
	}

	@Override
	public List<UsuarioEntity> obtenerUsuariosDeRol(int idRol,int revisor) {
		return usJpa.obtenerUsuariosPorRol(idRol,revisor);
	}

	@Override
	public List<Integer> encontrarUsuariosPorNombre(String nombre) {
		return usJpa.encontrarUsuariosPorNombre(nombre);
	}

	@Override
	public List<UsuarioEntity> obtenerUsuariosDeRoles(List<Integer> roles) {
		return usJpa.obtenerUsuarioPorRoles(roles);
	}
	
	@Override
	public List<UsuarioEntity> obtenerUsuariosDeRolesConDesc(List<Integer> roles) {
		return usJpa.obtenerUsuarioPorRolesConDesc(roles);
	}

	@Override
	public void actualizarEstatusUsuario(int estatus, int idUsuario) {
		usJpa.actualizarEstatusUsuario(estatus, idUsuario);
	}
	
	@Override
	public void actualizarImageUsuario(String image,int idUsuario){
		usJpa.actualizarImageUsuario(image, idUsuario);
	}

	@Override
	public void actualizarImageFirmaUsuario(String image,int idUsuario){
		usJpa.actualizarImageFirmaUsuario(image, idUsuario);
	}
	
	@Override
	public List<UsuarioEntity> obtenerUsuariosParaAssignedClinician(){
		return usJpa.obtenerUsuariosParaAssignedClinician();
	}

	@Override
	public void desbloquearUsuario(int idUsuario) {
		usJpa.desbloquearUsuario(idUsuario);
	}

	@Override
	public List<UsuarioObj> obtenerUsuariosObj(int idUsuario,String usuario) {
		
		List<Object[]> rows = null;
	    rows = obtenerUsuarioSql(idUsuario,usuario);
		
	    List<UsuarioObj> result = null;

	    result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirUsQueryAUsuario(row));
		}
		
		return result;
	}

	private UsuarioObj convertirUsQueryAUsuario(Object[] row){
		
		UsuarioObj o = new UsuarioObj();
		o.setIdUsuario((int) row[0]);
		o.setUsuario((String) row[1]);
		o.setNombre((String) row[2]);
		o.setSexo((String) row[3]);
		o.setCorreoElectronico((String) row[4]);
		o.setTelefono((String) row[5]);
		o.setDireccion((String) row[6]);
		o.setPais((String) row[7]);
		o.setIntentos((int) row[8]);
		o.setCiudad((String) row[9]);
		o.setEstatus((String) row[10]);
		o.setEdad((int) row[11]);
		int rol = (Integer) row[12];
		o.setRol(String.valueOf(rol));
		try {
			if(row[13] != null) {
				System.out.println("fechaCreacion:" + row[13].toString());
				o.setFechaCreacion(UtilidadesAdapter.cadenaAFechaDateTime((String) row[13].toString()));
				System.out.println("fechaCreacion:" + o.getFechaCreacion());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		o.setResumen((String) row[14]);
		o.setAusencia(Integer.valueOf(row[15].toString()) == 0 ? false : true);
		o.setRevisor(Integer.valueOf(row[16].toString()) == 0 ? false : true);
		o.setLicencia((String) row[17]);
		o.setLicenciaValida((String) row[18]);
		o.setDisponibilidad((String) row[19]);
		o.setColor((String) row[20]);
		o.setRate((String) row[21]);

		String image = (String) row[22];
		if(image != null){
			String rutaServidorFinal = rutaServidorFinal();
			o.setImage(rutaServidorFinal+image);
		}

		o.setWithSupervision(Integer.valueOf(row[23].toString()) == 0 ? false : true);
		o.setSupervisor((String) row[24]);
		o.setUnpaidVocFlag(Integer.valueOf(row[25].toString()) == 0 ? false : true);
		o.setEstado((String) row[26]);
		o.setContrasenia((String) row[27]);
		//o.setFirma((String) row[28]);

		o.setIniciales(UtilidadesAdapter.obtenerIniciales(o.getNombre()));
		return o;
		
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerUsuarioSql(int idUsuario,String usuario) {

		StringBuilder sb = new StringBuilder();
		StringBuilder sbW = new StringBuilder();

		String queryS = "SELECT usuario.id_usuario, usuario.usuario, usuario.nombre, usuario.sexo, usuario.correo_electronico, usuario.telefono, " + 
		"usuario.direccion, usuario.pais, usuario.intentos, usuario.ciudad, usuario.estatus, usuario.edad, usuario.id_rol, usuario.fecha_creacion, " +
		"usuario.resumen, usuario.ausencia, usuario.revisor, usuario.licencia, usuario.licencia_valida, usuario.disponibilidad, usuario.color, usuario.rate, " + 
		"usuario.image, usuario.with_supervision, usuario.supervisor, usuario.unpaid_voc_flag, usuario.estado,usuario.contrasenia,firma FROM usuario ";

		sb.append(queryS);

		if (idUsuario != 0) {
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(" usuario.id_usuario = :idUsuario ");
		}

		if (!"".equals(usuario)) {
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(" usuario.usuario = :usuario ");
		}


		sb.append("\n");
		if (sbW.length() != 0) {
			sb.append(" WHERE " + sbW.toString());
		}

		String queryFinal = sb.toString();
		UtilidadesAdapter.pintarLog("query:" + queryFinal);

		Query query = entityManager.createNativeQuery(queryFinal);
		if (idUsuario != 0) {
		query.setParameter("idUsuario", idUsuario);
		}
		if (!"".equals(usuario)) {
		query.setParameter("usuario", usuario);
		}

		List<Object[]> rows = query.getResultList();
		return rows;
	}

	private String rutaServidorFinal(){
		String rutaServidorFinal = "";	
		switch (ambiente) {
		case "qas":
			rutaServidorFinal = rutaServidorQas;
			break;
		case "pro":
			rutaServidorFinal = rutaServidorPro;
			break;
		case "local":
			rutaServidorFinal = rutaServidor;
			break;
		}
		return rutaServidorFinal;
	}

}
