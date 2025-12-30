package com.cargosyabonos.adapter.out.sql;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.MovimientosPort;
import com.cargosyabonos.application.port.out.jpa.MovimientoJpa;
import com.cargosyabonos.domain.Adeudo;
import com.cargosyabonos.domain.AdeudoSolicitudes;
import com.cargosyabonos.domain.MovimientoEntity;
import com.cargosyabonos.domain.ReporteMovimientosSolicitudes;

@Service
public class MovimientoRepository implements MovimientosPort {
	
	@Value("classpath:/querys/queryPagosTerminados.txt")
    private Resource queryPagosTerminados;
	
	@Value("classpath:/querys/queryPagosPendientes.txt")
    private Resource queryPagosPendientes;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	MovimientoJpa movJpa;

	@Override
	public List<MovimientoEntity> obtenerMovimientos(int idSolicitud) {
		return movJpa.obtenerMovimientoDeSolicitud(idSolicitud);
	}

	@Override
	public List<MovimientoEntity> obtenerMovimientosDeClientePorFecha(int idCliente, Date fechai, Date fechaf) {
		return movJpa.obtenerMovimientoDeClientePorFecha(idCliente, fechai, fechaf);
	}

	@Override
	public MovimientoEntity obtenerMovimiento(int idMovimiento) {
		return movJpa.obtenerMovimientoPorId(idMovimiento);
	}

	@Override
	public void crearMovimiento(MovimientoEntity mov) {
		movJpa.save(mov);
	}

	@Override
	public void actualizarMovimiento(MovimientoEntity mov) {
		movJpa.save(mov);
	}

	@Override
	public void eliminarMovimiento(MovimientoEntity mov) {
		movJpa.delete(mov);

	}

	@Override
	public BigDecimal obtenerAdeudo(int idCliente) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal obtenerSumaTipoMovimiento(int tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int obtenerUltimoFolio(int idSolicitud) {
		return movJpa.obtenerUltimoFolio(idSolicitud);
	}

	@Override
	public List<MovimientoEntity> obtenerMovimientosPorFecha(Date fechai, Date fechaf) {
		return movJpa.obtenerMovimientoPorFecha(fechai, fechaf);
	}

	@Override
	public BigDecimal obtenerSumaMovimientosSolicitud(int idSolicitud) {
		return movJpa.obtenerSumaMovimientosPorIdSolicitud(idSolicitud);
	}

	@Override
	public List<AdeudoSolicitudes> obtenerDeudasSolicitudes(Date fechai, Date fechaf,int tipoPayment) {
		
		List<AdeudoSolicitudes> salida = new ArrayList<>();
		
		switch (tipoPayment) {
		case 1:
			salida = movJpa.obtenerDeudasSolicitudes("A","F");	
			break;
		case 2:
			salida = movJpa.obtenerDeudasSolicitudes("G","N");	
			break;	
		case 3:
			salida = movJpa.obtenerDeudasSolicitudes("Ñ","Z");	
			break;
		default:
			break;
		}
		
		return salida;
	}

	@Override
	public List<ReporteMovimientosSolicitudes> obtenerMovimientosSolsDeClientePorFecha(String cliente, Date fechai,
			Date fechaf, int estatusPago) {
		return movJpa.obteneReporterMovimientoPorFechaYClt(cliente, fechai, fechaf, estatusPago);
	}

	@Override
	public List<ReporteMovimientosSolicitudes> obtenerMovimientosSolsPorFecha(Date fechai, Date fechaf,
			int estatusPago) {
		return movJpa.obteneReporterMovimientoPorFecha(fechai, fechaf, estatusPago);
	}

	@Override
	public BigDecimal obtenerSumaMovimientosPorIdSolicitud(int idSolicitud) {
		return movJpa.obtenerSumaMovimientosPorIdSolicitud(idSolicitud);
	}

	@Override
	public AdeudoSolicitudes obtenerDeudasSolicitud(int idSolicitud) {
		return movJpa.obtenerDeudasSolicitud(idSolicitud);
	}

	@Override
	public List<Adeudo> obtenerAdeudos(String fechai, String fechaf, int idUsuario, String campo, String valor,
			String tipo, String rol) {
		List<Adeudo> result = null;
		List<Object[]> rows = obtenerSolsFiltro(fechai, fechaf, idUsuario, campo, valor, tipo, rol);
		result = new ArrayList<>(rows.size());
		for (Object[] row : rows) {
			result.add(convertirSolQueryASolicitud(row));
		}
		return result;
	}

	@Override
	public int tieneProbono(int idSolicitud) {
		return movJpa.tieneProbono(idSolicitud);
	}

	public Adeudo convertirSolQueryASolicitud(Object[] row) {
		Adeudo d = new Adeudo();
		d.setIdSolicitud((Integer) row[0]);
		// d.setFecha((java.sql.Date) row[1]);
		d.setCliente((String) row[2]);
		d.setTelefono((String) row[3]);
		d.setEmail((String) row[4]);
		d.setAmount((BigDecimal) row[5]);
		d.setPagado((BigDecimal) row[6]);
		d.setAdeudo((BigDecimal) row[7]);
		return d;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerSolsFiltro(String fechai, String fechaf, int idUsuario, String campo, String valor,String tipo, String rol) {

		String queryS = "";
		StringBuilder sbW = new StringBuilder();

		if (tipo.equals("Unpaid")) {
			try (Scanner scanner = new Scanner(queryPagosPendientes.getInputStream(), StandardCharsets.UTF_8.name())) {
				queryS = scanner.useDelimiter("\\A").next();
			} catch (Exception e) {
				throw new RuntimeException("Error al leer el archivo", e);
			}
		} else if (tipo.equals("Paid")) {
			try (Scanner scanner = new Scanner(queryPagosTerminados.getInputStream(), StandardCharsets.UTF_8.name())) {
				queryS = scanner.useDelimiter("\\A").next();
			} catch (Exception e) {
				throw new RuntimeException("Error al leer el archivo", e);
			}
		}
		
		if(!fechai.equals("")){
			sbW.append(" fecha_inicio BETWEEN :fechai AND :fechaf ");
		}


		if (campo != null) {
			switch (campo) {
			case "File":
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.id_solicitud = "+valor+" ");
				//queryS = queryS.replace("$whereAdicional", "  AND s.id_solicitud = "+valor+" ");
				break;
			case "Customer":
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" CONCAT(s.cliente, ' ', s.apellidos) LIKE '%"+valor+"%' ");
				//queryS = queryS.replace("$whereAdicional", " AND CONCAT(s.cliente, ' ', s.apellidos) LIKE '%"+valor+"%' ");
				break;
			case "Phone":
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.telefono like '%"+valor+"%' ");
				//queryS = queryS.replace("$whereAdicional", " AND s.telefono like '%"+valor+"%' ");
				break;
			case "Email":
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.email like '%"+valor+"%' ");
				//queryS = queryS.replace("$whereAdicional", " AND s.email like '%"+valor+"%' ");
				break;

			default:
				//queryS = queryS.replace("$whereAdicional", " ");
				break;
			}
		}	
		
		if (sbW.length() != 0) {
			if(tipo.equals("Unpaid")){
				String where = " WHERE " + sbW.toString() + " AND ";
				queryS = queryS.replace("$whereAdicional", where);
			}else{
				String where = " WHERE " + sbW.toString();
				queryS = queryS.replace("$whereAdicional", where);
			}
		}else{
			if(tipo.equals("Unpaid")){
				queryS = queryS.replace("$whereAdicional", " WHERE ");
			}else{
				queryS = queryS.replace("$whereAdicional", " ");
			}
		}
		
		UtilidadesAdapter.pintarLog("query:" + queryS);

		Query query = entityManager.createNativeQuery(queryS);
		
		if(!fechai.equals("")){
			query.setParameter("fechai", fechai);
			query.setParameter("fechaf", fechaf);
		}

		List<Object[]> rows = query.getResultList();
		return rows;

	}

}
