package com.cargosyabonos.application.port.out.jpa;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cargosyabonos.domain.ProspectoAbogadoEntity;

@Repository
public interface ProspectoAbogadoJpa extends CrudRepository<ProspectoAbogadoEntity, Serializable>{

    public List<ProspectoAbogadoEntity> findAll();

    public ProspectoAbogadoEntity findByIdProspectoAbogado(int idProspectoAbogado);

    @Transactional
	@Query(value = "UPDATE prospecto_abogado SET id_abogado_relacionado = ?2, fecha_envio_cliente = ?3, solicitud_relacionada = ?4 WHERE id_prospecto_abogado = ?1", nativeQuery = true)
	@Modifying
	public void actualizarIdProspectoAbogado(int idProspectoAbogado, int idAbogadoRelacionado, String fechaEnvioCliente,int idSolicitud);

    @Transactional
	@Query(value = "UPDATE prospecto_abogado SET fecha_recordatorio_liaison = ?2 WHERE id_prospecto_abogado = ?1", nativeQuery = true)
	@Modifying
	public void actualizarFechaRecordatorioLiaison(int idProspectoAbogado, String  fechaRecordatorioLiaison);

    @Query(value = "SELECT * FROM prospecto_abogado p WHERE DATE(p.fecha_recordatorio_liaison) = CURDATE() AND p.id_estatus_prospecto = 2", nativeQuery = true)
	public List<ProspectoAbogadoEntity> obtenerProspectosAbogadosParaReminderLiaison();

	@Transactional
	@Query(value = "UPDATE prospecto_abogado SET solicitud_relacionada = ?1,fecha_envio_cliente = ?2 WHERE id_prospecto_abogado = ?3", nativeQuery = true)
	@Modifying
	public void actualizarSolicitud(int crmFile, String fecha, int idProspectoAbogado);

	public ProspectoAbogadoEntity findBySolicitudRelacionada(int solicitudRelacionada);

	

}
