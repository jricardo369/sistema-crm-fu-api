package com.cargosyabonos.adapter.out.sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.CitaPort;
import com.cargosyabonos.application.port.out.jpa.CitaJpa;
import com.cargosyabonos.domain.CargosCitasVoc;
import com.cargosyabonos.domain.CitaEntity;
import com.cargosyabonos.domain.CitaSql;

@Service
public class CitaRepository implements CitaPort{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	CitaJpa citaJpa;

	@Override
	public List<CitaSql> obtenerCitasPorSolicitud(int idSolicitud) {
		return citaJpa.obtenerCitasDeSolicitud(idSolicitud);
	}
	
	@Override
	public List<CitaSql> obtenerCitasPorSolicitudSinNoShow(int idSolicitud) {
		return citaJpa.obtenerCitasDeSolicitudSinNoshow(idSolicitud);
	}

	@Override
	public List<CitaEntity> obtenerCitasDeUsuarioPorFecha(int idUsuario, String fecha) {
		return citaJpa.obtenerCitasDeUsuarioPorFecha(idUsuario, fecha);
	}
	
	@Override
	public List<CitaEntity> obtenerCitasDeUsuarioPorSemana(int idUsuario, String fecha,int noShow) {
		return citaJpa.obtenerCitasDeUsuarioPorSemana(fecha,idUsuario,noShow);
	}

	@Override
	public void crearCita(CitaEntity a) {
		citaJpa.save(a);
	}

	@Override
	public void actualizarCita(CitaEntity a) {
		citaJpa.save(a);
	}

	@Override
	public void eliminarCita(CitaEntity a) {
		citaJpa.delete(a);
	}

	@Override
	public CitaEntity obtenerCita(int idCita) {
		return citaJpa.obtenerPorId(idCita);
	}

	@Override
	public void actualizarNoShow(boolean noShow,int idCita) {
		citaJpa.actualizarNoShow(noShow, idCita);		
	}

	@Override
	public List<CargosCitasVoc> obtenerCargosPendientesFiltro(String fechai, String fechaf, int idUsuario, String campo, String valor,
			String tipo, String rol) {
		
			List<CargosCitasVoc> result = null;
			List<Object[]> rows = obtenerCargosCitas(fechai, fechaf, idUsuario, campo, valor, tipo, rol);
			result = new ArrayList<>(rows.size());
			for (Object[] row : rows) {
				result.add(convertirSolQueryACargosCitasVoc(row));
			}
			return result;
		
		//return citaJpa.obtenerCargosPendientesFiltro(filtro);
	}
	
	public CargosCitasVoc convertirSolQueryACargosCitasVoc(Object[] row) {
		CargosCitasVoc d = new CargosCitasVoc();
		
		d.setIdFile((Integer) row[0]);
		d.setIdCita((Integer) row[1]);
		d.setCaseNumber((String) row[2]);
		d.setFecha((String) row[3]);
		d.setCliente((String) row[4]);
		d.setTelefono((String) row[5]);
		d.setEmail((String) row[6]);
		d.setAmount((BigDecimal) row[7]);
		d.setComentario((String) row[8]);
		d.setPagado(Integer.valueOf(row[9].toString()) == 0 ? false : true);
		d.setFechaPagado((String) row[10]);
		d.setTerapeuta((String) row[11]);
		d.setAnioNacimiento((String) row[12]);
		d.setSexo((String) row[13]);
		d.setDireccion((String) row[14]);
		return d;
		
	}

	@Override
	public List<CitaEntity> obtenerTodasLasCitasSemana(String fecha,int noShow) {
		return citaJpa.obtenerTodasCitasPorSemana(fecha,noShow);
	}

	@Override
	public List<CitaEntity> obtenerTodasCitasPorFecha(String fecha) {
		return citaJpa.obtenerTodasCitasPorFecha(fecha);
	}

	@Override
	public void actualizarPagado(boolean pagado, String fechPagado,int idCita) {
		citaJpa.actualizarPagado(pagado, fechPagado,idCita);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerCargosCitas(String fechai, String fechaf, int idUsuario, String campo, String valor,
			String tipo, String rol) {

		StringBuilder sb = new StringBuilder();
		StringBuilder sbW = new StringBuilder();

		sb.append(
				"SELECT s.id_solicitud as file,c.id_cita as idCita,s.numero_de_caso as caseNumber,c.fecha as fecha, "
				+ "CONCAT(s.cliente,' ', IFNULL(s.apellidos,'')) as cliente,s.telefono,s.email, c.amount,c.comentario,c.pagado,c.fecha_pagado as fechaPagado,u.nombre as terapeuta,s.fecha_nacimiento,s.sexo,s.direccion "
			+"FROM cita c "
			+"JOIN solicitud_voc s ON s.id_solicitud = c.id_solicitud "
			+ "JOIN usuario u ON c.id_usuario = u.id_usuario ");

		if (campo != null) {
			switch (campo) {
			case "File":
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.id_solicitud LIKE '%" + valor + "%' OR s.apellidos LIKE '%" + valor + "%' ");
				break;
			case "Customer":
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" cliente LIKE '%" + valor + "%'  ");
				break;
			case "Phone":
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.telefono LIKE '%" + valor + "%'  ");
				break;
			case "Email":
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.email LIKE '%" + valor + "%' ");
				break;
			case "Case number":
				if (sbW.length() != 0)
					sbW.append(" AND ");
				sbW.append(" s.numero_de_caso LIKE '%" + valor + "%' ");
				break;
			case "All":
				if ("Unpaid".equals(tipo)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.numero_de_caso lIKE '%" + valor + "%' AND pagado = 0  ");
				} else if ("Paid".equals(tipo)) {
					if (sbW.length() != 0)
						sbW.append(" AND ");
					sbW.append(" s.numero_de_caso lIKE '%" + valor + "%' AND pagado = 1 ");
				}
			default:
				break;
			}
		}

		if ("Unpaid".equals(tipo)) {
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(
					" pagado = 0 ");
		} else if ("Paid".equals(tipo)) {
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(
					"pagado = 1 ");
		}

		if (!"".equals(fechai)) {
			if (sbW.length() != 0)
				sbW.append(" AND ");
			sbW.append(" fecha BETWEEN :fechai AND :fechaf ");
		}
		
		if (sbW.length() != 0)
			sbW.append(" AND ");
		sbW.append(" c.no_show = 0 ");

		if (sbW.length() != 0) {
			sb.append(" WHERE " + sbW.toString());
		}


		UtilidadesAdapter.pintarLog("query:" + sb.toString());

		Query query = entityManager.createNativeQuery(sb.toString());

		if (!"".equals(fechai)) {
			query.setParameter("fechai", fechai);
			query.setParameter("fechaf", fechaf);
		}

		List<Object[]> rows = query.getResultList();
		return rows;

	}

	@Override
	public List<CitaSql> obtenerCitasDeUsuarioPorSemanaV2(String fecha, int idUsuario, int noShow) {
		return citaJpa.obtenerCitasDeUsuarioPorSemanaV2(fecha, idUsuario, noShow);
	}

}
