package com.cargosyabonos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.in.MovimientosUseCase;
import com.cargosyabonos.application.port.out.EnviarCorreoPort;
import com.cargosyabonos.application.port.out.EventoSolicitudPort;
import com.cargosyabonos.application.port.out.MovimientosPort;
import com.cargosyabonos.application.port.out.MsgPort;
import com.cargosyabonos.application.port.out.SolicitudPort;
import com.cargosyabonos.application.port.out.TipoPagoPort;
import com.cargosyabonos.application.port.out.UsuariosPort;
import com.cargosyabonos.domain.Adeudo;
import com.cargosyabonos.domain.DetalleMovimientos;
import com.cargosyabonos.domain.Movimiento;
import com.cargosyabonos.domain.MovimientoEntity;
import com.cargosyabonos.domain.MovimientosReporte;
import com.cargosyabonos.domain.ReporteMovimientosSolicitudes;
import com.cargosyabonos.domain.SolicitudEntity;
import com.cargosyabonos.domain.TipoPagoEntity;
import com.cargosyabonos.domain.UsuarioEntity;
  
@Service
public class MovimientosService implements MovimientosUseCase{
	
	Logger log = LoggerFactory.getLogger(MovimientosService.class);
	
	@Autowired
	private MovimientosPort movPort;
	
	@Autowired
	private EnviarCorreoPort envCorrPort;
	
	@Autowired
	private SolicitudPort reqPort;
	
	@Autowired
	private TipoPagoPort tpPort;
	
	@Autowired
	private UsuariosPort usPort;
	
	@Autowired
	private EventoSolicitudPort evPort;
	
	@Autowired
	private MsgPort msgPort;

	@Override
	public MovimientosReporte obtenerMovimientosDeClientePorFecha(String cliente,Date fechai,Date fechaf) {
		
		MovimientosReporte mrep = new MovimientosReporte();
		//List<MovimientoEntity> movs = movPort.obtenerMovimientosPorFecha(fechai, fechaf);
		List<ReporteMovimientosSolicitudes> movs = null;
		if("".equals(cliente)){
			movs = movPort.obtenerMovimientosSolsPorFecha(fechai, fechaf,2);
		}else{
			movs = movPort.obtenerMovimientosSolsDeClientePorFecha(cliente, fechai, fechaf,2);
		}
		BigDecimal sumaPayments = BigDecimal.ZERO;
		BigDecimal sumaAmounts = BigDecimal.ZERO;
		BigDecimal diferencia = BigDecimal.ZERO;
		
		List<Movimiento> movsSalida = new ArrayList<>();
		for (ReporteMovimientosSolicitudes m : movs) {
			sumaPayments = sumaPayments.add(m.getmonto());
			Movimiento mo = convertirAMovimiento(m);
			movsSalida.add(mo);
		}
	    sumaAmounts = reqPort.obtenerSumaAmountPorFechayCliente(fechai, fechaf, cliente);
	    diferencia = sumaAmounts.subtract(sumaPayments);
		UtilidadesAdapter.pintarLog("suma payments:"+sumaPayments+"|suma amounts:"+sumaAmounts+"|diferencia:"+diferencia);
		
		mrep.setMovimientos(movsSalida);
		mrep.setAbonos(sumaPayments);
		mrep.setCargos(sumaAmounts);
		mrep.setBalance(diferencia);
		
		return mrep;
	
	}
	
	@Override
	public MovimientosReporte obtenerAdeudosDeClientePorFecha(String fechai, String fechaf,int idUsuario,String campo,String valor,String tipo) {
		
		MovimientosReporte mrep = new MovimientosReporte();
		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		
		BigDecimal sumaPayments = BigDecimal.ZERO;
		BigDecimal sumaAmounts = BigDecimal.ZERO;
		BigDecimal diferencia = BigDecimal.ZERO;
		
		List<Adeudo> adeudos = movPort.obtenerAdeudos(fechai, fechaf, idUsuario, campo, valor, tipo,us.getRol());
		
		Double sumaAmountsDouble = adeudos.stream().mapToDouble(o-> o.getAmount().doubleValue()).sum();
		//Double sumaAdeudosDouble = adeudos.stream().mapToDouble(o-> o.getAdeudo().doubleValue()).sum();
		Double sumaPaymentsDouble = adeudos.stream().mapToDouble(o-> o.getPagado().doubleValue()).sum();
		
		sumaAmounts = new BigDecimal(sumaAmountsDouble);
		sumaPayments = new BigDecimal(sumaPaymentsDouble);
		
		diferencia = sumaAmounts.subtract(sumaPayments);
		
		mrep.setAbonos(sumaPayments);
		mrep.setCargos(sumaAmounts);
		mrep.setBalance(diferencia);
		mrep.setAdeudos(adeudos);
		
		return mrep;
	}

	@Override
	public void crearMovimiento(Movimiento mov, boolean envioNotificacion,int idUsuario) {
		
		BigDecimal amount = BigDecimal.ZERO;
		envioNotificacion = true;

		mov.setFecha(UtilidadesAdapter.fechaActualDate());
		MovimientoEntity me = convertirAMovimientoEntity(mov);

		BigDecimal sm = movPort.obtenerSumaMovimientosSolicitud(me.getSolicitud().getIdSolicitud());
		UtilidadesAdapter.pintarLog("sm:"+sm);
		sm = sm.add(me.getMonto());
		UtilidadesAdapter.pintarLog("suma movimientos:"+sm);
		
		amount = me.getSolicitud().getAmount() == null ? BigDecimal.ZERO : me.getSolicitud().getAmount();
		UtilidadesAdapter.pintarLog("Amount:"+amount);
		if(amount.compareTo(BigDecimal.ZERO) == 0){
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The request has no amount, payment cannot be added");
		}
		
		UtilidadesAdapter.pintarLog("Comparar suma vs monto solicitud:"+amount.compareTo(sm));
		if (amount.compareTo(sm) == -1) {
			
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Payments exceed the request amount");
			
		} else {
			
			int sumaMovsInt = sm.intValue();
			int amountInt = amount.intValue();
			
			// Actualizar estatus si son iguales el monto vs pagos
			if (sumaMovsInt == amountInt) {
				reqPort.actualizarEstatusPago(2, me.getSolicitud().getIdSolicitud());
			}

			int fol = movPort.obtenerUltimoFolio(mov.getIdSolicitud());
			fol = fol + 1;
			// UtilidadesAdapter.pintarLog("fol:"+fol);
			me.setFolio(String.valueOf(fol));
			
			TipoPagoEntity tpe = tpPort.obtenerTiposPago(mov.getIdTipoPago());
			String desMov = tpe.getNombre().toUpperCase();
			UtilidadesAdapter.pintarLog("desMov:"+desMov);
			
			if(desMov.contains("CONTRACT")||desMov.contains("PRO-BONO")){
				UtilidadesAdapter.pintarLog("No enviara notificacion de movimiento");
				envioNotificacion = false;
			}
			
			if (envioNotificacion) {
				
				UtilidadesAdapter.pintarLog("Envio mail pago");
				Map<String, Object> params = new HashMap<>();

				params.put("${usuario}", me.getSolicitud().obtenerClienteParaMail());
				params.put("${monto}", mov.getMonto());
				params.put("${tipo}", me.getTipo());
				
				String email = me.getSolicitud().getEmail() ==  null ? "" : me.getSolicitud().getEmail();
				String telefono = me.getSolicitud().getTelefono() ==  null ? "" : me.getSolicitud().getTelefono();

				if(!"".equals(email)){
					envCorrPort.enviarCorreoMovimiento(me.getSolicitud().getEmail(), params);
				}
				
				if(!"".equals(telefono)){
					if(!"".equals(me.getSolicitud().getTelefono())){
						msgPort.envioMensaje(me.getSolicitud().getTelefono(), "Dear " + me.getSolicitud().obtenerClienteParaMail()+",A "+me.getTipo()+" of $"+mov.getMonto()+" dollars has been registered in FamiliasUnidas, thank you.",me.getSolicitud(),false);
					}
				}
				
			}

			movPort.crearMovimiento(me);

			UsuarioEntity us = usPort.buscarPorId(idUsuario);
			String desc = me.getDescripcion() == null ? "" : me.getDescripcion();
			evPort.ingresarEventoDeSolicitud("Payment registration",
					"Payment was added " + desc + " for $" + me.getMonto(), "Info", us.getNombre(),
					me.getSolicitud());

		}
		
	}

	@Override
	public void actualizarMovimiento(Movimiento mov) {
		movPort.actualizarMovimiento(convertirAMovimientoEntity(mov));
	}

	@Override
	public void eliminarMovimiento(int idMov,int idUsuario) {
		
		MovimientoEntity m = movPort.obtenerMovimiento(idMov);
		SolicitudEntity se = m.getSolicitud();
		UsuarioEntity us = usPort.buscarPorId(idUsuario);
		//UtilidadesAdapter.pintarLog("mov:"+m+"|us:"+us);
		String desc = m.getDescripcion() == null ? "" : m.getDescripcion();
		evPort.ingresarEventoDeSolicitud("Payment elimination", "Payment was eliminated "+ desc + " for " + m.getMonto(), "Info", us.getNombre(), m.getSolicitud());
		movPort.eliminarMovimiento(m);
		
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal sm = movPort.obtenerSumaMovimientosSolicitud(se.getIdSolicitud());
		UtilidadesAdapter.pintarLog("Suma movimientos:"+sm);
		amount = se.getAmount() == null ? BigDecimal.ZERO : se.getAmount();
		UtilidadesAdapter.pintarLog("Amount:"+amount);
		
		// Actualizar estatus si son iguales el monto vs pagos
		if (amount.compareTo(sm) == 0) {
			reqPort.actualizarEstatusPago(2, se.getIdSolicitud());
		}else{
			reqPort.actualizarEstatusPago(1, se.getIdSolicitud());
		}
	}

	@Override
	public Adeudo obtenerAdeudo(int idCliente) {
		Adeudo a = new Adeudo();
		a.setAdeudo(movPort.obtenerAdeudo(idCliente));
		return a;
	}
	
	private MovimientoEntity convertirAMovimientoEntity(Movimiento mov){
		MovimientoEntity m = new MovimientoEntity();
		m.setFecha(mov.getFecha());
		m.setFolio(mov.getFolio());
		m.setTipo(mov.getTipoMovimiento());
		m.setMonto(mov.getMonto());
		SolicitudEntity r = reqPort.obtenerSolicitud(mov.getIdSolicitud());
		m.setSolicitud(r);
		m.setDescripcion(mov.getDescripcion());
		TipoPagoEntity tp = tpPort.obtenerTiposPago(mov.getIdTipoPago());
		m.setTipoPago(tp);
		m.setDescuento(mov.getDescuento());
		return m;
	}

	@Override
	public List<Movimiento> obtenerMovimientos(int idSolicitud) {
		
		List<MovimientoEntity> movs =  movPort.obtenerMovimientos(idSolicitud);
		List<Movimiento> movsSalida = new ArrayList<>();
		for (MovimientoEntity m : movs) {
			movsSalida.add(convertirAMovimiento(m));
		}
		return movsSalida;
	}
	
	private Movimiento convertirAMovimiento(MovimientoEntity mov){
		
		Movimiento o = new Movimiento();
		
		o.setDescripcion(mov.getDescripcion());
		o.setDescuento(mov.getDescuento());
		o.setFecha(mov.getFecha());
		o.setFolio(mov.getFolio());
		o.setIdMovimiento(mov.getIdMovimiento());
		o.setIdTipoPago(mov.getTipoPago().getIdTipoPago());
		TipoPagoEntity tp = tpPort.obtenerTiposPago(mov.getTipoPago().getIdTipoPago());
		o.setDescripcionTipoPago(tp.getNombre());
		o.setIdSolicitud(mov.getSolicitud().getIdSolicitud());
		o.setMonto(mov.getMonto());
		o.setTipoMovimiento(mov.getTipo());
		
		return o;
	}
	
	private Movimiento convertirAMovimiento(ReporteMovimientosSolicitudes mov){
		
		Movimiento o = new Movimiento();
		
		o.setDescripcion(mov.getdescripcion());
		o.setDescuento(mov.getdescuento());
		o.setFecha(mov.getfecha());
		o.setFolio(mov.getfolio());
		o.setIdMovimiento(mov.getid_movimiento());
		o.setIdTipoPago(mov.getid_tipo_pago());
		o.setDescripcionTipoPago(mov.gettipo_pago());
		o.setIdSolicitud(mov.getid_solicitud());
		o.setMonto(mov.getmonto());
		o.setTipoMovimiento(mov.gettipo());
		o.setCliente(mov.getcliente());
		
		return o;
	}

	@Override
	public DetalleMovimientos obtenerDetalleMovimientos(int idSolicitud) {
		
		DetalleMovimientos salida = new DetalleMovimientos();
		BigDecimal totalPagos = BigDecimal.ZERO;
		BigDecimal pendiente = BigDecimal.ZERO;
		List<MovimientoEntity> movs =  movPort.obtenerMovimientos(idSolicitud);
		List<Movimiento> movsSalida = new ArrayList<>();
		for (MovimientoEntity m : movs) {
			movsSalida.add(convertirAMovimiento(m));
			totalPagos = totalPagos.add(m.getMonto());
		}
		SolicitudEntity s = reqPort.obtenerSolicitud(idSolicitud);
		BigDecimal amount = s.getAmount() == null ? BigDecimal.ZERO : s.getAmount();
		UtilidadesAdapter.pintarLog("amount:"+amount);
		if(amount.compareTo(BigDecimal.ZERO) == 1){
			pendiente = s.getAmount().subtract(totalPagos);
		}
		
		salida.setMovimientos(movsSalida);
		salida.setPendiente(pendiente);
		return salida;
	}

	

}
