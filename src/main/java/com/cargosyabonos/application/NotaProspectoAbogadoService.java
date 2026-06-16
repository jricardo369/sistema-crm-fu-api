package com.cargosyabonos.application;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.NotaProspectoAbogadoUseCase;
import com.cargosyabonos.application.port.out.NotaProspectoAbogadoPort;
import com.cargosyabonos.domain.NotaProspectoAbogado;
import com.cargosyabonos.domain.NotaProspectoAbogadoEntity;

@Service
public class NotaProspectoAbogadoService implements NotaProspectoAbogadoUseCase{

    @Autowired
	NotaProspectoAbogadoPort notaProspectoAbogadoPort;

    @Override
    public NotaProspectoAbogadoEntity findByIdNota(int idNota) {
        return notaProspectoAbogadoPort.findByIdNota(idNota);
    }

    @Override
    public List<NotaProspectoAbogado> obtenerNotasProspectosAbogadosPorIdProspecto(int idProspectoAbogado) {
        return notaProspectoAbogadoPort.obtenerNotasProspectosAbogadosPorIdProspecto(idProspectoAbogado);
    }

    @Override
    public NotaProspectoAbogadoEntity crearNotaProspecto(NotaProspectoAbogadoEntity o) {
        try {
            o.setFechaCreacion(UtilidadesAdapter.generarFecha(true, false, false, "", 0, null));
            String hora = UtilidadesAdapter.generarFecha(false, true, false, "", 0, null).substring(0,5);
            System.out.println("hora:"+hora);
            o.setHora(hora);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       return notaProspectoAbogadoPort.crearNotaProspecto(o);
    }

    @Override
    public void eliminarNotaProspecto(int idNota) {
        notaProspectoAbogadoPort.eliminarNotaProspecto(notaProspectoAbogadoPort.findByIdNota(idNota));
    }

    @Override
    public void actualizarNotaProspecto(NotaProspectoAbogadoEntity o) {
       notaProspectoAbogadoPort.actualizarNotaProspecto(o);
    }

    

}
