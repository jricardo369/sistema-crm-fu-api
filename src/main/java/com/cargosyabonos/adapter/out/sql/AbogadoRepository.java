package com.cargosyabonos.adapter.out.sql;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.AbogadoPort;
import com.cargosyabonos.application.port.out.jpa.AbogadoJpa;
import com.cargosyabonos.application.port.out.jpa.FuenteDeReferenciaJpa;
import com.cargosyabonos.domain.Abogado;
import com.cargosyabonos.domain.AbogadoEntity;
import com.cargosyabonos.domain.FuenteDeReferenciaEntity;

@Service
public class AbogadoRepository implements AbogadoPort{

	@Autowired
	AbogadoJpa abogadoJpa;

	@Autowired
	FuenteDeReferenciaJpa fuenteDeReferenciaJpa;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<AbogadoEntity> obtenerAbogados() {
		return abogadoJpa.obtenerTodosAbogadosActivos();
	}

	@Override
	public AbogadoEntity obtenerAbogadoPorId(int idAbogado) {
		return abogadoJpa.findByIdAbogado(idAbogado);
	}

	@Override
	public List<AbogadoEntity> obtenerAbogadosPorNombreYSinonimo(String nombre) {
		return abogadoJpa.obtenerAbogadosPorNombreYSinonimo(nombre);
	}

	@Override
	public AbogadoEntity crearAbogado(AbogadoEntity a) {
		return abogadoJpa.save(a);
	}

	@Override
	public void actualizarAbogado(AbogadoEntity a) {
		abogadoJpa.save(a);
	}

	@Override
	public void eliminarAbogado(AbogadoEntity a) {
		abogadoJpa.delete(a);
	}

	@Override
	public AbogadoEntity obtenerAbogadoPorEmail(String email){
		return abogadoJpa.obtenerAbogadoByEmail(email);
	}

	private Abogado convertirAAbogado(Object[] row) {
		Abogado o = new Abogado();
		o.setIdAbogado((int) row[0]);
		o.setFirma((String) row[1]);
		o.setNombre((String) row[2]);
		o.setTelefono((String) row[3]);
		o.setEmail((String) row[4]);
		o.setSinonimos((String) row[5]);
		return o;
	}

	private Abogado convertirAAbogadoObj(Object[] row) {
		Abogado o = new Abogado();
		o.setIdAbogado((int) row[0]);
		o.setEmail((String) row[1]);
		return o;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerAbogadosConMailsSql(String valorBusqueda) {

		UtilidadesAdapter.pintarLog("ejecutando query anios");
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT a.id_abogado,a.firma,a.nombre,a.telefono,e.email,a.sinonimos,a.fecha_creacion " 
				+"FROM abogado a "
				+"LEFT JOIN email_abogado e ON e.id_abogado = a.id_abogado "
				+ "WHERE a.nombre LIKE :valor OR a.sinonimos LIKE :valor OR e.email LIKE :valor");

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());
		
		query.setParameter("valor", "%" + valorBusqueda + "%");

		List<Object[]> rows = query.getResultList();
		return rows;
	}

	@Override
	public List<Abogado> obtenerAbogadosConMail(String valorBusqueda) {
		
		List<Object[]> rows = null;	
		rows = obtenerAbogadosConMailsSql(valorBusqueda);			
		List<Abogado> result = null;

		result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirAAbogado(row));
		}
		
		return result;
	}

	@Override
	public void actualizarCuponAbogado(int idAbogado) {
		 abogadoJpa.actualizarCuponAbogado(idAbogado);
	}


	@SuppressWarnings("unchecked")
	public Object[] obtenerSiExisteEmailAbogadoSQL(String emailAbogado) {

		UtilidadesAdapter.pintarLog("ejecutando query anios");
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT a.id_abogado,e.email FROM email_abogado e "
				+"JOIN abogado a "
				+"WHERE e.email = :emailAbogado LIMIT 1;");

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());
		
		query.setParameter("emailAbogado",emailAbogado);

		List<Object[]> rows = query.getResultList();
		if (!rows.isEmpty()) {
        	return rows.get(0);
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	public String obtenerSiExisteNombreAbogadoSQL(String nombreAbogado) {
		UtilidadesAdapter.pintarLog("ejecutando query anios");
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT a.nombre FROM abogado a "
				+ "WHERE a.nombre = :nombreAbogado LIMIT 1;");

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("nombreAbogado", nombreAbogado);

		List<String> rows = query.getResultList();
		if (!rows.isEmpty()) {
			return rows.get(0);
		}
		return null;
	}

	@Override
	public Abogado obtenerSiExisteEmailAbogado(String emailAbogado) {

		Object[] row = obtenerSiExisteEmailAbogadoSQL(emailAbogado);
		if(row == null) {
			return null;
		}
		Abogado result = convertirAAbogadoObj(row);
		return result;

	}

	@Override
	public Abogado obtenerSiExisteNombreAbogado(String nombreAbogado) {
		String nombre = obtenerSiExisteNombreAbogadoSQL(nombreAbogado);
		if (nombre == null) {
			return null;
		}
		Abogado o = new Abogado();
		o.setNombre(nombre);
		return o;
	}

	@Override
	public List<FuenteDeReferenciaEntity> obtenerFuentesDeReferencia() {
		return fuenteDeReferenciaJpa.findAll();
	}

}
