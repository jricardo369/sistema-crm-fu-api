package com.dental;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cargosyabonos.UtilidadesAdapter;

@SpringBootTest
class CargosAbonosApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void correoValidoAceptaUnaSolaDireccion() {
		assertTrue(UtilidadesAdapter.isCorreoValido("jricardo369@gmail.com"));
	}

	@Test
	void correoValidoRechazaMultiplesDireccionesOSeparadores() {
		assertFalse(UtilidadesAdapter.isCorreoValido("jricardo369@gmail.com & jricardo369@gmail.com"));
		assertFalse(UtilidadesAdapter.isCorreoValido("jricardo369@gmail.com,jricardo369@gmail.com"));
	}

}
