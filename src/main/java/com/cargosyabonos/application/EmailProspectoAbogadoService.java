package com.cargosyabonos.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cargosyabonos.application.port.in.EmailProspectoAbogadoUseCase;
import com.cargosyabonos.application.port.out.EmailProspectoAbogadoPort;
import com.cargosyabonos.domain.EmailProspectoAbogadoEntity;

@Service
public class EmailProspectoAbogadoService implements EmailProspectoAbogadoUseCase {

    Logger log = LoggerFactory.getLogger(EmailProspectoAbogadoService.class);

    @Autowired
    private EmailProspectoAbogadoPort emailProsAboPort;

    @Override
    public List<EmailProspectoAbogadoEntity> obtenerEmailsDeProspectoAbogado(int idProspectoAbogado) {
        return emailProsAboPort.obtenerEmailsDeProspectoAbogado(idProspectoAbogado);
    }

    @Override
    public EmailProspectoAbogadoEntity obtenerEmailProspectoAbogado(String emailProspectoAbogado) {
        return emailProsAboPort.obtenerEmailProspectoAbogado(emailProspectoAbogado);
    }

    @Override
    public void crearEmailProspectoAbogado(EmailProspectoAbogadoEntity ab) {
        EmailProspectoAbogadoEntity eae = emailProsAboPort.obtenerEmailProspectoAbogado(ab.getEmail());
        if (eae != null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "This email is already registered");
        }
        emailProsAboPort.crearEmailProspectoAbogado(ab);
    }

    @Override
    public void actualizarEmailProspectoAbogado(EmailProspectoAbogadoEntity ab) {
        emailProsAboPort.actualizarEmailProspectoAbogado(ab);
    }

    @Override
    public void eliminarEmailProspectoAbogado(int idEmailProspectoAbogado) {
        EmailProspectoAbogadoEntity ab = emailProsAboPort.obtenerPorIdProspectoAbogado(idEmailProspectoAbogado);
        emailProsAboPort.eliminarEmailProspectoAbogado(ab);
    }
}
