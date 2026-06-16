package com.cargosyabonos.adapter.out.sql;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.NotaProspectoAbogadoPort;
import com.cargosyabonos.application.port.out.jpa.NotaProspectoAbogadoJpa;
import com.cargosyabonos.domain.NotaProspectoAbogadoEntity;
import com.cargosyabonos.domain.NotaProspectoAbogado;

@Service
public class NotaProspectoAbogadoRepository implements NotaProspectoAbogadoPort{

     @PersistenceContext
	private EntityManager entityManager;

    @Autowired
	NotaProspectoAbogadoJpa notaProspectoAbogadoJpa;

    @Override
    public NotaProspectoAbogadoEntity findByIdNota(int idNota) {
       return notaProspectoAbogadoJpa.obtenerNotaPorId(idNota);
    }

    @Override
    public List<NotaProspectoAbogado> obtenerNotasProspectosAbogadosPorIdProspecto(int idProspectoAbogado) {

        List<Object[]> rows = null;
	    rows = obtenerNotasProspecto(idProspectoAbogado);

        List<NotaProspectoAbogado> result = null;
        result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
            result.add(convertirANotaProspectoAbogado(row));
        }

        return result;

        //return notaProspectoAbogadoJpa.obtenerNotasDeProspectoAbogado(idProspectoAbogado);
    }

    private NotaProspectoAbogado convertirANotaProspectoAbogado(Object[] row) {
		
		NotaProspectoAbogado o = new NotaProspectoAbogado();
        o.setIdNota((Integer) row[0]);
        o.setDescripcion((String) row[1]);
        o.setFechaCreacion((String) row[2]);
        o.setHora((String) row[3]);
        o.setIdProspectoAbogado((Integer) row[4]);
        o.setIdUsuario((Integer) row[5]);
        o.setNombreUsuario((String) row[6]);
		
		return o;
	}

    @SuppressWarnings("unchecked")
	public List<Object[]> obtenerNotasProspecto(int idProspectoAbogado) {

		StringBuilder sb = new StringBuilder();
		
		String queryS = "SELECT n.id_nota, n.descripcion, n.fecha_creacion,n.hora,n.id_prospecto_abogado,n.id_usuario,u.nombre " + 
                        "FROM nota_prospecto_abogado n " + 
                        "JOIN usuario u ON u.id_usuario = n.id_usuario " +
                        "WHERE n.id_prospecto_abogado = :idProspectoAbogado " +
                        "ORDER BY n.id_nota DESC";
		sb.append(queryS);

        
		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idProspectoAbogado", idProspectoAbogado);

		List<Object[]> rows = query.getResultList();
		return rows;
	}

    @Override
    public NotaProspectoAbogadoEntity crearNotaProspecto(NotaProspectoAbogadoEntity o) {
        return notaProspectoAbogadoJpa.save(o);
    }

    @Override
    public void eliminarNotaProspecto(NotaProspectoAbogadoEntity o) {
        notaProspectoAbogadoJpa.delete(o);
    }

    @Override
    public void actualizarNotaProspecto(NotaProspectoAbogadoEntity o) {
        notaProspectoAbogadoJpa.save(o);
    }

    

}
