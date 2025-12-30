package com.cargosyabonos.adapter.in.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cargosyabonos.application.port.in.TareaProgramadaUseCase;

@Component
public class LayoutJobs {
	
	@Autowired
	private TareaProgramadaUseCase tpUC;
	
	@Scheduled(cron = "#{@getCronTarProgPayLunes}") // Ejemplo 0 32 11 * * WED 
	//@Scheduled(fixedRate = 3000)
	private void envioMailsPaymentsLunes(){
		tpUC.ejecutarTareaProgramada("payments",null,null,1);
	}
	
	@Scheduled(cron = "#{@getCronTareaProgramadaPaymentsMiercoles}")
	private void envioMailsPaymentsMartes(){
		tpUC.ejecutarTareaProgramada("payments",null,null,2);
	}
	
	@Scheduled(cron = "#{@getCronTareaProgramadaPaymentsViernes}")
	private void envioMailsPaymentsMiercoles(){
		tpUC.ejecutarTareaProgramada("payments",null,null,3);
	}
	
	@Scheduled(cron = "#{@getCronTareaProgramadaLateRequests}")
	private void envioMailsLateRequets(){
		tpUC.ejecutarTareaProgramada("late-requests",null,null,0);		
	}
	
	@Scheduled(cron = "#{@getCronRecordatorioCitas}")
	private void envioMailsRecordatorioCitas(){
		tpUC.ejecutarTareaProgramada("appointment-reminder",null,null,0);		
	}
	
	@Scheduled(cron = "#{@getCronLimpiezaDisponibilidad}")
	private void LimpiezaDisponibilidad(){
		tpUC.limpiezaDisponibilidad();		
	}
	
	@Scheduled(cron = "#{@getValidacionMsms}")
	private void validacionMsms(){
		tpUC.validacionMsms();		
	}
	
	@Scheduled(cron = "#{@getSolsEndingSession}")
	private void getSolsEndingSession(){
		tpUC.solicitudesVocEndingSessions();
	}

}
