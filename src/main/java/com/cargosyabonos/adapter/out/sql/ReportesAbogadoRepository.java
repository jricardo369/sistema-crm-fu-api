package com.cargosyabonos.adapter.out.sql;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.ReportesAbogadoPort;
import com.cargosyabonos.domain.AbogadoEstado;
import com.cargosyabonos.domain.ClientesPorAbogado;
import com.cargosyabonos.domain.OficinaAnioYMes;

@Repository
public class ReportesAbogadoRepository implements ReportesAbogadoPort {

    @PersistenceContext
	private EntityManager entityManager;

    @Override
    public List<AbogadoEstado> obtenerAbogadosPorEstadoPorFecha(String fechaInicio, String fechaFin) {
        
        List<Object[]> rows = null;	
		rows = obtenerAbogadosPorEstado(fechaInicio, fechaFin);			
		List<AbogadoEstado> result = null;

		result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirAAbogadoEstado(row));
		}
		
		return result;

    }

    private AbogadoEstado convertirAAbogadoEstado(Object[] row) {
        AbogadoEstado abogadoEstado = new AbogadoEstado();
        abogadoEstado.setEstado((String) row[0]);
        abogadoEstado.setNumeroSolicitudes(((Number) row[1]).intValue());
        return abogadoEstado;
    }

    @SuppressWarnings("unchecked")
	public List<Object[]> obtenerAbogadosPorEstado(String fechaInicio, String fechaFin) {

		UtilidadesAdapter.pintarLog("Abogados por estado");
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT COALESCE(NULLIF(TRIM(a.estado), ''), 'With out state') AS estado_abogado, " + 
                  "COUNT(*) AS numero_solicitudes " + 
                  "FROM solicitud s " +
                  "LEFT JOIN abogado a " + 
                  "    ON s.id_abogado = a.id_abogado " + 
                  "WHERE " + 
                  "    s.fecha_inicio BETWEEN :fechai AND :fechaf " + 
                  "GROUP BY " + 
                  "    estado_abogado;");

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("fechai", fechaInicio);
		query.setParameter("fechaf", fechaFin);

		List<Object[]> rows = query.getResultList();
		return rows;
	}

    @Override
    public List<ClientesPorAbogado> obtenerClientesPorFirmaYFechas(String fechaInicio, String fechaFin) {

        List<Object[]> rows = null;	
		rows = obtenerClientesPorFirmaYFechasSql(fechaInicio, fechaFin);			
		List<ClientesPorAbogado> result = null;

		result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirAClientesPorFirma(row));
		}
		
		return result;
    }

    private ClientesPorAbogado convertirAClientesPorFirma(Object[] row) {
        ClientesPorAbogado clientesPorAbogado = new ClientesPorAbogado();
        clientesPorAbogado.setFirma((String) row[0]);
        clientesPorAbogado.setEmail((String) row[1]);
        clientesPorAbogado.setTotal(((Number) row[2]).intValue());
        return clientesPorAbogado;
    }


    @SuppressWarnings("unchecked")
	public List<Object[]> obtenerClientesPorFirmaYFechasSql(String fechaInicio, String fechaFin) {

		UtilidadesAdapter.pintarLog("Abogados por estado");
		StringBuilder sb = new StringBuilder();

		sb.append("     SELECT " + 
                        "    a.firma," + 
                        "    s.email_abo_sel," + 
                        "    COUNT(s.id_solicitud) AS total_solicitudes " + 
                        "FROM " + 
                        "    solicitud s " + 
                        "JOIN " + 
                        "    email_abogado epa ON s.email_abo_sel = epa.email " + 
                        "JOIN " + 
                        "    abogado a ON epa.id_abogado = a.id_abogado " + 
                        "WHERE " + 
                        "    s.fecha_inicio BETWEEN :fechai AND :fechaf " + 
                        "GROUP BY " + 
                        "    a.firma, s.email_abo_sel  HAVING total_solicitudes > 1 " + 
                        "ORDER BY " + 
                        "    total_solicitudes DESC");

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("fechai", fechaInicio);
		query.setParameter("fechaf", fechaFin);

		List<Object[]> rows = query.getResultList();
		return rows;
	}

     @Override
    public List<OficinaAnioYMes> obtenerTotalFirmasAnioYMes(String fechaInicio, String fechaFin) {

        List<Object[]> rows = null;	
		rows = obtenerTotalFirmasAnioYMesSql(fechaInicio, fechaFin);			
		List<OficinaAnioYMes> result = null;

		result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirAOficinaAnioYMes(row));
		}
		
		return result;
    }

    private OficinaAnioYMes convertirAOficinaAnioYMes(Object[] row) {
        OficinaAnioYMes oficinaAnioYMes = new OficinaAnioYMes();
        oficinaAnioYMes.setAnio(((Number) row[0]).intValue());
        oficinaAnioYMes.setMes(((Number) row[1]).intValue());
        oficinaAnioYMes.setTotal(((Number) row[2]).intValue());
        return oficinaAnioYMes;
    }

    @SuppressWarnings("unchecked")
	public List<Object[]> obtenerTotalFirmasAnioYMesSql(String fechaInicio, String fechaFin) {

		UtilidadesAdapter.pintarLog("Abogados por estado");
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT " + 
                        "    YEAR(fecha_creacion) AS anio, " + 
                        "    MONTH(fecha_creacion) AS mes, " + 
                        "    COUNT(*) AS total_abogados_nuevos " + 
                        "FROM " + 
                        "    abogado " + 
                        "WHERE " + 
                        "    fecha_creacion BETWEEN :fechai AND :fechaf " + 
                        "GROUP BY anio, mes " + 
                        "ORDER BY anio, mes;");

		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("fechai", fechaInicio);
		query.setParameter("fechaf", fechaFin);

		List<Object[]> rows = query.getResultList();
		return rows;
	}


}
