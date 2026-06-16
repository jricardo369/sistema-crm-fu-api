package com.cargosyabonos.application.port.in;

import java.util.List;

import com.cargosyabonos.domain.EmailProspectoAbogadoEntity;

public interface EmailProspectoAbogadoUseCase {
    List<EmailProspectoAbogadoEntity> obtenerEmailsDeProspectoAbogado(int idProspectoAbogado);
    EmailProspectoAbogadoEntity obtenerEmailProspectoAbogado(String emailProspectoAbogado);
    void crearEmailProspectoAbogado(EmailProspectoAbogadoEntity ab);
    void actualizarEmailProspectoAbogado(EmailProspectoAbogadoEntity ab);
    void eliminarEmailProspectoAbogado(int idEmailProspectoAbogado);
}
