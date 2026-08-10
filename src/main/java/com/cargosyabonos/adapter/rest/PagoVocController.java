package com.cargosyabonos.adapter.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cargosyabonos.application.port.in.PagoVocUseCase;
import com.cargosyabonos.domain.PagoVoc;

@RequestMapping("/pagos-voc")
@RestController
public class PagoVocController {

	Logger log = LoggerFactory.getLogger(PagoVocController.class);

	@Autowired
	PagoVocUseCase pagoVocUseCase;

	@GetMapping
	public List<PagoVoc> obtenerPagos() {
		return pagoVocUseCase.obtenerPagos();
	}

	@GetMapping("{idPago}")
	public PagoVoc obtenerPagoPorId(@PathVariable("idPago") int idPago) {
		return pagoVocUseCase.obtenerPagoPorId(idPago);
	}

	@GetMapping("cita/{idCita}")
	public List<PagoVoc> obtenerPagosPorIdCita(@PathVariable("idCita") int idCita) {
		return pagoVocUseCase.obtenerPagosPorIdCita(idCita);
	}

	@PostMapping
	public void crearPago(@RequestBody PagoVoc pago) {
		pagoVocUseCase.crearPago(pago);
	}

	@PutMapping
	public void actualizarPago(@RequestBody PagoVoc pago) {
		pagoVocUseCase.actualizarPago(pago);
	}

	@DeleteMapping("{idPago}")
	public void eliminarPago(@PathVariable("idPago") int idPago) {
		pagoVocUseCase.eliminarPago(idPago);
	}

}
