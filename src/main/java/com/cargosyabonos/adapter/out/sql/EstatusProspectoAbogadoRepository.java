package com.cargosyabonos.adapter.out.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.out.EstatusProspectoAbogadoPort;
import com.cargosyabonos.application.port.out.jpa.EstatusProspectoAbogadoJpa;
import com.cargosyabonos.domain.EstatusProspectoAbogadoEntity;

@Service
public class EstatusProspectoAbogadoRepository implements EstatusProspectoAbogadoPort{

    @Autowired
	EstatusProspectoAbogadoJpa estProsJpa;

    @Override
    public List<EstatusProspectoAbogadoEntity> obtenerEstatusProspectosAbogado() {
        return estProsJpa.findAll();
    }

	

}
