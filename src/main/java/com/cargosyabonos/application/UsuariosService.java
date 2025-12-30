package com.cargosyabonos.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cargosyabonos.ApiException;
import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.UsuariosUseCase;
import com.cargosyabonos.application.port.out.ArchivosPort;
import com.cargosyabonos.application.port.out.PermisosUsuariosPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.PermisoUsuario;
import com.cargosyabonos.domain.PermisoUsuarioEntity;
import com.cargosyabonos.domain.UsuarioEntity;
import com.cargosyabonos.domain.UsuarioObj;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:configuraciones-global.properties")
public class UsuariosService implements UsuariosUseCase {

	Logger log = LoggerFactory.getLogger(UsuariosService.class);
	
	@Value("${ambiente}")
	private String ambiente;
	
	@Value("${ruta.servidor}")
	private String rutaServidor;

	@Value("${ruta.servidor.qas}")
	private String rutaServidorQas;

	@Value("${ruta.servidor.pro}")
	private String rutaServidorPro;

	@Autowired
	private UsuariosPort usrPort;

	@Autowired
	private PermisosUsuariosPort puPort;
	
	@Autowired
	private ArchivosPort arcPort;

	@Override
	public List<UsuarioEntity> obtenerUsuarios() {
		
		String rutaServidorFinal = rutaServidorFinal();
		List<UsuarioEntity> lu = usrPort.obtenerUsuarios();
		for (UsuarioEntity ue : lu) {
			ue.setImage(rutaServidorFinal+ue.getImage());
		}
		
		return lu;
	}

	@Override
	public UsuarioEntity buscarPorUsuario(String usuario) {
		UsuarioEntity u = usrPort.buscarPorUsuario(usuario);
		String rutaServidorFinal = rutaServidorFinal();
		u.setImage(rutaServidorFinal+u.getImage());
		return u;
	}
	
	@Override
	public UsuarioEntity buscarPorCorreo(String correo) {
		return usrPort.buscarPorCorreo(correo);
	}

	@Override
	public UsuarioEntity buscarPorId(int idUsuario) {
		List<PermisoUsuario> permisos = new ArrayList<>();
		PermisoUsuario p = null;
		List<PermisoUsuarioEntity> p1 = puPort.obtenerPermisosDeUsuario(idUsuario);
		for (PermisoUsuarioEntity pe : p1) {
			p = new PermisoUsuario();
			p.setId(pe.getId());
			p.setNombre(pe.getNombre());
			permisos.add(p);
		}
		UsuarioEntity u = usrPort.buscarPorId(idUsuario);
		u.setPermisos(permisos);
		return u;
	}
	
	@Override
	public UsuarioObj buscarPorIdObj(int idUsuario){
		List<PermisoUsuario> permisos = new ArrayList<>();
		PermisoUsuario p = null;
		List<PermisoUsuarioEntity> p1 = puPort.obtenerPermisosDeUsuario(idUsuario);
		for (PermisoUsuarioEntity pe : p1) {
			p = new PermisoUsuario();
			p.setId(pe.getId());
			p.setNombre(pe.getNombre());
			permisos.add(p);
		}
		UsuarioEntity u = usrPort.buscarPorId(idUsuario);
		u.setPermisos(permisos);
		UsuarioObj uo = converterUsEntityToUsObj(u);
		return uo;
	}

	@Override
	public void crearUsuario(UsuarioEntity usuario) {

		UsuarioEntity usE = usrPort.buscarPorUsuario(usuario.getUsuario());
		if (usE == null) {
			UtilidadesAdapter.pintarLog(UtilidadesAdapter.sha256(usuario.getUsuario(), usuario.getContrasenia()));
			usuario.setContrasenia(UtilidadesAdapter.sha256(usuario.getUsuario(), usuario.getContrasenia()));
			usuario.setEstatus("1");
			List<PermisoUsuario> permisos = usuario.getPermisos();

			UsuarioEntity us = usrPort.crearUsuario(usuario);

			if (permisos != null) {
				if (!permisos.isEmpty()) {
					for (PermisoUsuario p : permisos) {
						puPort.insertarPermisoUsuario(p.getId(), us.getIdUsuario());
					}
				}
			}
		} else {
			// Error de duplicidad
			throw new ApiException(409, "Nombre de usuario ya existente");

		}

	}

	@Override
	public void actualizarUsuario(UsuarioEntity usuario) {
		
		UsuarioEntity u = usrPort.buscarPorId(usuario.getIdUsuario());
		String contEntrante = usuario.getContrasenia();
		String contExistente = u.getContrasenia();
		String contNueva = UtilidadesAdapter.sha256(usuario.getUsuario(), usuario.getContrasenia()); 
		UtilidadesAdapter.pintarLog("Contraseña entrante:"+contEntrante);
		UtilidadesAdapter.pintarLog("Contraseña existente:"+usuario.getContrasenia());
		//Validar si cambiaron contrasenia
		if(!contExistente.equals(contEntrante)){
			usuario.setContrasenia(contNueva);
		}
		usrPort.actualizarUsuario(usuario);
		List<PermisoUsuario> permisos = usuario.getPermisos();
		puPort.eliminarPermisosDeUsuario(usuario.getIdUsuario());
		if (permisos != null) {
			if (!permisos.isEmpty()) { 
				for (PermisoUsuario p : permisos) {
					puPort.insertarPermisoUsuario(p.getId(), usuario.getIdUsuario());
				}
			}
		}
	}

	@Override
	public void eliminarUsuario(int idUsuario) {
		UsuarioEntity u = usrPort.buscarPorId(idUsuario);
		UtilidadesAdapter.pintarLog("usuario:" + u.getUsuario());
		usrPort.actualizarEstatusUsuario(4, idUsuario);
		//usrPort.eliminarUsuario(u);
	}

	@Override
	public List<UsuarioEntity> obtenerUsuariosDeRol(int idRol,int revisor) {
		return usrPort.obtenerUsuariosDeRol(idRol, revisor);
	}
	
	@Override
	public List<UsuarioEntity> obtenerUsuariosParaAssignedClinician(){
		return usrPort.obtenerUsuariosParaAssignedClinician();
	}

	@Override
	public List<UsuarioEntity> obtenerUsuariosParaSchedules(int idUsuario) {
		
		List<UsuarioEntity> usList = null;
		UsuarioEntity us = usrPort.buscarPorId(idUsuario);
		UtilidadesAdapter.pintarLog("us rol:"+us.getRol());
		
		if(us.getRol().equals("2")||us.getRol().equals("3")||us.getRol().equals("4")){
			
			List<Integer> li = new ArrayList<>();
			li.add(5);
			li.add(8);
			li.add(11);
			
			usList = usrPort.obtenerUsuariosDeRoles(li);
		}else{
			usList = new ArrayList<>();
		}
		return usList;
	}
	
	@Override
	public List<UsuarioEntity> obtenerUsuariosParaDash(int idUsuario) {

		List<UsuarioEntity> usList = null;
		UsuarioEntity us = usrPort.buscarPorId(idUsuario);
		UtilidadesAdapter.pintarLog("us rol:" + us.getRol());

		List<Integer> li = new ArrayList<>();
		li.add(4);
		li.add(5);
		li.add(7);
		li.add(8);
		li.add(11);

		usList = usrPort.obtenerUsuariosDeRolesConDesc(li);

		return usList;
	}
	
	@Override
	public void cargarImagen(MultipartFile archivo,int idUsuario){
		String nombreOriginal = archivo.getOriginalFilename();
		System.out.println("ContentType:"+archivo.getContentType());
		String tipoArchivo = "";
		
		if(archivo.getContentType().contains("word")){
			tipoArchivo = "docx";
		}else{
			tipoArchivo = archivo.getContentType().substring(archivo.getContentType().length() - 3,
					archivo.getContentType().length());
		}
		
		String rutaArchivos = UtilidadesAdapter.generarRutaImgProfile(idUsuario);
		String nombre = "/" + idUsuario + "." + tipoArchivo;
		
		byte[] archivoB = null;

		try {
			archivoB = archivo.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("rutaArchios:"+rutaArchivos);
		System.out.println("bytes:"+archivoB);
		System.out.println("nombre:"+nombre);
		System.out.println("original nombre:"+nombreOriginal);
		
		arcPort.guardarArchivo(rutaArchivos, archivoB, nombre);
		usrPort.actualizarImageUsuario(rutaArchivos + nombre, idUsuario);
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
		case "test":
			rutaServidorFinal = rutaServidor;
			break;
		}
		return rutaServidorFinal;
	}
	
	private UsuarioObj converterUsEntityToUsObj(UsuarioEntity ue){
		
		UsuarioObj o = new UsuarioObj();
		o.setAusencia(ue.isAusencia());
		o.setCiudad(ue.getCiudad());
		o.setColor(ue.getColor());
		o.setCorreoElectronico(ue.getCorreoElectronico());
		o.setDireccion(ue.getDireccion());
		o.setDisponibilidad(ue.getDisponibilidad());
		o.setEdad(ue.getEdad());
		o.setEstatus(ue.getEstatus());
		o.setFechaCreacion(ue.getFechaCreacion());
		o.setIdUsuario(ue.getIdUsuario());
		
		if(ue.getImage() != null){
			String rutaServidorFinal = rutaServidorFinal();
			o.setImage(rutaServidorFinal+ue.getImage());
		}
		
		o.setIntentos(ue.getIntentos());
		o.setLicencia(ue.getLicencia());
		o.setLicenciaValida(ue.getLicenciaValida());
		o.setNombre(ue.getNombre());
		o.setPais(ue.getPais());
		o.setPermisos(ue.getPermisos());
		o.setRate(ue.getRate());
		o.setResumen(ue.getResumen());
		o.setRevisor(ue.isRevisor());
		o.setRol(ue.getRol());
		o.setSexo(ue.getSexo());
		o.setTelefono(ue.getTelefono());
		o.setUsuario(ue.getUsuario());
		o.setIniciales(UtilidadesAdapter.obtenerIniciales(o.getNombre()));
		o.setWithSupervision(o.isWithSupervision());
		o.setSupervisor(o.getSupervisor());
		return o;
		
	}

	@Override
	public void desbloquearUsuario(int idUsuario) {
		usrPort.desbloquearUsuario(idUsuario);
	}

}
