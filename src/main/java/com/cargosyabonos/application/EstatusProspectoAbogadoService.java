package com.cargosyabonos.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargosyabonos.application.port.in.EstatusProspectoAbogadoUseCase;
import com.cargosyabonos.application.port.out.EstatusProspectoAbogadoPort;
import com.cargosyabonos.domain.EstatusProspectoAbogadoEntity;

@Service
public class EstatusProspectoAbogadoService implements EstatusProspectoAbogadoUseCase {

    Logger log = LoggerFactory.getLogger(EstatusProspectoAbogadoEntity.class);

    @Autowired
	private EstatusProspectoAbogadoPort estProsAbPort;

    @Override
    public List<EstatusProspectoAbogadoEntity> obtenerEstatusProspectosAbogado() {
        return estProsAbPort.obtenerEstatusProspectosAbogado();
    }

}
