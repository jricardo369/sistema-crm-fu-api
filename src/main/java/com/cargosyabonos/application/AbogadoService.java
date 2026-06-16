package com.cargosyabonos.application;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.AbogadoUseCase;
import com.cargosyabonos.application.port.in.CorreoElectronicoUseCase;
import com.cargosyabonos.application.port.out.AbogadoPort;
import com.cargosyabonos.application.port.out.EmailAbogadoPort;
import com.cargosyabonos.application.port.out.EmailProspectoAbogadoPort;
import com.cargosyabonos.application.port.out.NotaProspectoAbogadoPort;
import com.cargosyabonos.application.port.out.ProspectoAbogadoPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.Abogado;
import com.cargosyabonos.domain.AbogadoEntity;
import com.cargosyabonos.domain.EmailAbogado;
import com.cargosyabonos.domain.EmailAbogadoEntity;
import com.cargosyabonos.domain.EmailProspectoAbogadoEntity;
import com.cargosyabonos.domain.FuenteDeReferenciaEntity;
import com.cargosyabonos.domain.NotaProspectoAbogadoEntity;
import com.cargosyabonos.domain.ProspectoAbogadoEntity;
import com.cargosyabonos.domain.UsuarioEntity;

@Service
public class AbogadoService implements AbogadoUseCase {

	@Autowired
	private AbogadoPort aboPort;
	
	@Autowired
	private EmailAbogadoPort emAboPort;

	@Autowired
	private ProspectoAbogadoPort prosAboPort;

	@Autowired
	private EmailProspectoAbogadoPort emAProsboPort;

	@Autowired
	private NotaProspectoAbogadoPort notaProsAboPort;

	@Autowired
	private CorreoElectronicoUseCase correoUs;

	@Autowired
	private UsuariosPort usPort;

	@Override
	public List<AbogadoEntity> obtenerAbogados() {
		return aboPort.obtenerAbogados();
	}

	@Override
	public AbogadoEntity obtenerAbogadoPorId(int idAbogado) {
		return aboPort.obtenerAbogadoPorId(idAbogado);
	}

	@Override
	public List<AbogadoEntity> obtenerAbogadosPorNombreYSinonimo(String nombre) {
		return aboPort.obtenerAbogadosPorNombreYSinonimo(nombre);
	}

	@Override
	public void crearAbogado(Abogado a, int idUsuario, int idProspectoAbogado, int idSolicitud) {

		for (EmailAbogado emA : a.getEmailsAbogado()) {
			UtilidadesAdapter.pintarLog("Email procesado: " + emA.getEmail());
			// Validar que el email no exista para otro abogado
			AbogadoEntity aboExistente = aboPort.obtenerAbogadoPorEmail(emA.getEmail());
			if (aboExistente != null) {
				throw new RuntimeException(
						"El email " + emA.getEmail() + " ya está asociado al abogado: " + aboExistente.getNombre());
			}
		}

		a.setFechaCreacion(null);

		AbogadoEntity ae = aboPort.crearAbogado(convertirAbogadoEntity(a));

		for (EmailAbogado emA : a.getEmailsAbogado()) {

			UtilidadesAdapter.pintarLog("Email procesado: " + emA.getEmail());
			EmailAbogadoEntity eae = new EmailAbogadoEntity();
			eae.setIdAbogado(ae.getIdAbogado());
			eae.setEmail(emA.getEmail());
			eae.setNombre(emA.getNombre());
			eae.setTipo(emA.getTipo());
			emAboPort.crearEmailAbogado(eae);

		}

		// Si el abogado se creó correctamente y se proporcionó un idProspectoAbogado,
		// actualizar el prospecto abogado
		if (idProspectoAbogado > 0) {

			String emailsPros = "";
			ProspectoAbogadoEntity prospectoAbogado = prosAboPort.findByIdProspectoAbogado(idProspectoAbogado);
			List<EmailProspectoAbogadoEntity> emails = emAProsboPort.obtenerEmailsDeProspectoAbogado(idProspectoAbogado);
			if(!emails.isEmpty()) {
				emailsPros = emails.stream().map(EmailProspectoAbogadoEntity::getEmail).collect(Collectors.joining(","));
			}
			NotaProspectoAbogadoEntity nota = new NotaProspectoAbogadoEntity();
			nota.setIdProspectoAbogado(idProspectoAbogado);
			nota.setDescripcion("Assigned lawyer: " + ae.getNombre() + " in request" + idSolicitud);
			String fecha = "";
			String hora = "";
			try {
				fecha = UtilidadesAdapter.generarFecha(true, false, false, "", 0, null);
				hora = UtilidadesAdapter.generarFecha(false, true, false, "", 0, null).substring(0, 5);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			nota.setFechaCreacion(fecha);
			nota.setHora(hora);
			nota.setIdUsuario(1);
			prosAboPort.actualizarIdProspectoAbogado(idProspectoAbogado, ae.getIdAbogado(), fecha, idSolicitud);
			notaProsAboPort.crearNotaProspecto(nota);

			List<Integer> li = new ArrayList<>();
			li.add(13);
			li.add(14);

			List<UsuarioEntity> usList = usPort.obtenerUsuariosDeRoles(li);
			for (UsuarioEntity us : usList) {
				// Enviar correo a Liaison y Digital Marketing Manager
				correoUs.enviarCorreoClienteAProspectoAbogado(us.getCorreoElectronico(), us.getNombre(),
						idProspectoAbogado, prospectoAbogado.getNombre(), prospectoAbogado.getTelefono(),emailsPros, idSolicitud);

			}

		}

	}
	
	public AbogadoEntity convertirAbogadoEntity(Abogado a) {

		AbogadoEntity ae = new AbogadoEntity();
		ae.setIdAbogado(a.getIdAbogado());
		ae.setFirma(a.getFirma());
		ae.setNombre(a.getNombre());
		ae.setTelefono(a.getTelefono());
		ae.setSinonimos(a.getSinonimos());
		ae.setFechaCreacion(a.getFechaCreacion());
		ae.setCupon(a.isCupon());
		ae.setFechaCupon(a.getFechaCupon());
		ae.setEstado(a.getEstado());
		ae.setReferencia(a.getReferencia());

		return ae;
		
	}
	

	@Override
	public void actualizarAbogado(AbogadoEntity a) {
		aboPort.actualizarAbogado(a);
	}

	@Override
	public void eliminarAbogado(int idAbogado) {
		AbogadoEntity a = aboPort.obtenerAbogadoPorId(idAbogado);
		emAboPort.eliminarEmailAbogadoByIdAbogado(a.getIdAbogado());
		aboPort.eliminarAbogado(a);
	}

	@Override
	public List<Abogado> obtenerAbogadosConMail(String valorBusqueda) {
		return aboPort.obtenerAbogadosConMail(valorBusqueda);
	}

	@Override
	public List<FuenteDeReferenciaEntity> obtenerFuentesDeReferencia() {
		return aboPort.obtenerFuentesDeReferencia();
	}

}
