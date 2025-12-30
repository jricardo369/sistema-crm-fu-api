package com.cargosyabonos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.springframework.security.crypto.codec.Hex;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class UtilidadesAdapter {
	
	public static String ambiente = "test";
	
	public static String currencyFormat(BigDecimal n) {
		NumberFormat.getInstance(new Locale("es", "MX"));
	    return NumberFormat.getCurrencyInstance().format(n);
	}
	
	public static String formatearFecha(Date fecha) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "MX"));
		String strFecha = "";
		if (fecha != null) {
			strFecha = formatter.format(fecha);
		} else {
			return strFecha;
		}
		return strFecha;
	}
	
	public static String formatearFechaUS(Date fecha) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", new Locale("es", "MX"));
		String strFecha = "";
		if (fecha != null) {
			strFecha = formatter.format(fecha);
		} else {
			return strFecha;
		}
		return strFecha;
	}

	public static String formatearFechaConHora(Date fecha) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "MX"));
		String strFecha = formatter.format(fecha);
		return strFecha;
	}
	
	public static String formatearFechaConHoraCalendar(Date fecha) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'", new Locale("es", "MX"));
		String strFecha = formatter.format(fecha);
		return strFecha;
	}

	public static String formatearHora(Date fecha) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", new Locale("es", "MX"));
		String strFecha = formatter.format(fecha);
		return strFecha;
	}

	public static String horaActual(Date fecha) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss z");
		String strFecha = formatter.format(fecha);
		return strFecha;
	}

	public static Date cadenaAFecha(String fecha) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
		return date;
	}
	
	public static Date cadenaAFechaFormatUS(String fecha) throws ParseException {
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
		return date;
	}
	
	public static Date cadenaAFechaYHora(String fecha) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fecha);
		return date;
	}

	public static String formatNumber(Object number) {
		Locale currentLocale = Locale.getDefault();
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
		return currencyFormatter.format(number);
	}

	public static String tomarAnioActual() {
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	}

	public static String tomarFechaYHora() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		System.out.print(dateFormat.format(date));
		return dateFormat.format(date);
	}

	@SuppressWarnings("null")
	public static Properties readPropertiesFile(String fileName) throws IOException {
		File f = new File(fileName);
		FileInputStream fis = null;
		Properties prop = null;

		try {
			fis = new FileInputStream(f);
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}

		return prop;
	}

	public static Date sumarDiasAFecha(Date fecha, int dias) {
		if (dias == 0)
			return fecha;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
	}
	
	public static Date sumarDiasAFechaSinFinesDeSemana(Date dateA, int days) {
	    LocalDate result = convertToLocalDateViaInstant(dateA);
	    int addedDays = 0;
	    while (addedDays < days) {
	        result = result.plusDays(1);
	        if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
	            ++addedDays;
	        }
	    }
	    Date salida = java.sql.Date.valueOf(result);
	    return salida;
	}
	
	public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}


	public static String sha256(String usuario, String cont) {
		String contranenia = usuario + cont;
		MessageDigest digest;
		String sha256hex = "";
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(contranenia.getBytes(StandardCharsets.UTF_8));
			sha256hex = new String(Hex.encode(hash));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return sha256hex;
	}

	public static String isJSONValid(String json) {

		try {

			JsonParser parser = new JsonParser();
			if (!parser.parse(json).isJsonObject())
				return "No es un fromato JSON correcto";

		} catch (JsonSyntaxException jse) {
			return jse.getMessage();
		}

		return "";
	}

	public static String generarRutaArchivo(int idSol) {
		return "/" + Calendar.getInstance().get(Calendar.YEAR) + "/" + idSol;
	}
	
	public static String generarRutaArchivoVoc(int idSol) {
		return "/" + Calendar.getInstance().get(Calendar.YEAR) + "/voc/" + idSol;
	}
	
	public static String generarRutaImgProfile(int idUsuario) {
		return "/" + idUsuario;
	}
	
	public static String obtenerIniciales(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "";
        }

        // Dividir por espacios (uno o más)
        String[] trozos = nombre.trim().split("\\s+");
        // Lista para guardar las palabras limpias que contengan al menos una letra
        java.util.List<String> validas = new java.util.ArrayList<>();

        for (String trozo : trozos) {
            // Eliminar todo lo que no sea letra (Unicode)
            String limpio = trozo.replaceAll("[^\\p{L}]", "");
            if (!limpio.isEmpty()) {
                validas.add(limpio);
            }
            // Si ya tenemos dos palabras válidas, podemos salir del bucle
            if (validas.size() == 2) {
                break;
            }
        }

        if (validas.isEmpty()) {
            return "";
        }

        // Si solo hay una palabra válida, devolvemos su primera letra
        if (validas.size() == 1) {
            return validas.get(0).substring(0, 1).toUpperCase();
        }

        // Si hay al menos dos palabras válidas:
        // Tomamos la inicial de la primera y la inicial de la segunda
        String primera = validas.get(0).substring(0, 1).toUpperCase();
        String segunda = validas.get(1).substring(0, 1).toUpperCase();
        return primera + segunda;
    }
	
	public static String fechaEnLetraEnghish(String fechaOriginal){
		// Parsear la fecha ISO (yyyy-MM-dd)
        LocalDate fecha = LocalDate.parse(fechaOriginal);

        // Formatear a "February 5, 2025"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        String fechaFormateada = fecha.format(formatter);
        return fechaFormateada;
	}

	public static String fechaEnLetra(Date d) {
		String fecha = UtilidadesAdapter.formatearFecha(d);
		System.out.println("Fecha:" + fecha);
		String salida = "";
		String mesLetra = "";
		int mes = 0;
		int diaFecha = 0;
		String dia = "";
		int anio = 0;

		try {
			String pattern = "YYYY-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("es", "MX"));
			Calendar c = Calendar.getInstance();
			c.setTime(simpleDateFormat.parse(fecha));

			mes = Integer.valueOf(fecha.substring(5, 7));
			diaFecha = c.get(Calendar.DAY_OF_WEEK);
			dia = fecha.substring(8, 10);
			anio = Integer.valueOf(fecha.substring(0, 4));

			System.out.println("Día fecha:" + diaFecha);
			System.out.println("Día:" + dia);
			System.out.println("Mes:" + mes);
			System.out.println("Anio:" + anio);

			if (diaFecha == 2) {
				salida = "Lunes";
			}
			if (diaFecha == 3) {
				salida = "Martes";
			}
			if (diaFecha == 4) {
				salida = "Miercoles";
			}
			if (diaFecha == 5) {
				salida = "Jueves";
			}
			if (diaFecha == 6) {
				salida = "Viernes";
			}
			if (diaFecha == 7) {
				salida = "Sabado";
			}
			if (diaFecha == 1) {
				salida = "Domingo";
			}

			switch (mes) {
			case 1:
				mesLetra = "Enero";
				break;
			case 2:
				mesLetra = "Febrero";
				break;
			case 3:
				mesLetra = "Marzo";
				break;
			case 4:
				mesLetra = "Abril";
				break;
			case 5:
				mesLetra = "Mayo";
				break;
			case 6:
				mesLetra = "Junio";
				break;
			case 7:
				mesLetra = "Julio";
				break;
			case 8:
				mesLetra = "Agosto";
				break;
			case 9:
				mesLetra = "Septiembre";
				break;
			case 10:
				mesLetra = "Octubre";
				break;
			case 11:
				mesLetra = "Noviembre";
				break;
			case 12:
				mesLetra = "Diciembre";
				break;
			default:
				break;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return salida + " " + dia + " de " + mesLetra + " del " + anio;
	}

	
	
	public static Date fechaActualDate(){
		Date date = new Date();
		Date d =  null;
		try {
			d = cadenaAFechaPST(formatDateToString(date, "yyyy-MM-dd hh:mm:ss a", "PST"));
			System.out.println("d:"+d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	public static Date cadenaAFechaPST(String fecha) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").parse(fecha);
		return date;
	}
	
	public static String formatDateToString(Date date, String format,
			String timeZone) {
		// null check
		if (date == null) return null;
		// create SimpleDateFormat object with input format
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		// default system timezone if passed null or empty
		if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
			timeZone = Calendar.getInstance().getTimeZone().getID();
		}
		// set timezone to SimpleDateFormat
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		// return Date in required format with timezone as String
		return sdf.format(date);
	}
		
	public static String fechaActual() {
			
			Date todayDate = new Date();
			String pattern = "yyyy-MM-dd";
			//SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("es", "MX"));
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
			String salida = simpleDateFormat.format(todayDate);
			//System.out.println("fecha actual:"+salida);
	
			return salida;
		}
	
	
	public static int diferenciaDias(String fecha1, String fecha2){
		//System.out.println("f1:"+fecha1+"/f2:"+fecha2);
		int salida = 0;
		LocalDate dateBefore = LocalDate.parse(fecha1);
		LocalDate dateAfter = LocalDate.parse(fecha2);
		long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
		//System.out.println(noOfDaysBetween);
		salida = (int) noOfDaysBetween;
		return salida;
	}
	
	public static int obtenerMesSheet(String mes){
		
		int mesSalida = 0;
		if(mes.contains("JAN")){
			mesSalida = 1;
		}else if(mes.contains("FEB")){
			mesSalida = 2;
		}else if(mes.contains("MARCH")){
			mesSalida = 3;
		}else if(mes.contains("APRIL")){
			mesSalida = 4;
		}else if(mes.contains("MAY")){
			mesSalida = 5;
		}else if(mes.contains("JUNE")){
			mesSalida = 6;
		}else if(mes.contains("JULY")){
			mesSalida = 7;
		}else if(mes.contains("AUGUST")){
			mesSalida = 8;
		}else if(mes.contains("SEP")){
			mesSalida = 9;
		}else if(mes.contains("OCT")){
			mesSalida = 10;
		}else if(mes.contains("NOV")){
			mesSalida = 11;
		}else if(mes.contains("DEC")){
			mesSalida = 12;
		}
		return mesSalida;
		
	}
	
	public static String generarFecha(boolean soloFecha, boolean soloHora, boolean dateTime, String replace,
			int diasASumar, String fechaEntrada) throws ParseException {
		
		Date d = new Date();
		String fecha = "";
		String hora = "";
		String salida = "";
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("es", "MX"));
		String horapatt = "HH:mm:ss.SSS";
		SimpleDateFormat simpleDateFormatS = new SimpleDateFormat(horapatt);
		simpleDateFormatS.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

		if (fechaEntrada != null) {

			Date fechaS = simpleDateFormat.parse(fechaEntrada);

			if (diasASumar == 0) {
				fecha = simpleDateFormat.format(fechaS);
			} else {
				fecha = simpleDateFormat.format(fechaS);
				Calendar c = Calendar.getInstance();
				c.setTime(simpleDateFormat.parse(fecha));
				c.add(Calendar.DATE, diasASumar);
				fecha = simpleDateFormat.format(c.getTime());
			}

			hora = simpleDateFormatS.format(d);

		} else {
			if (diasASumar == 0) {
				fecha = simpleDateFormat.format(d);
			} else {
				fecha = simpleDateFormat.format(d);
				Calendar c = Calendar.getInstance();
				c.setTime(simpleDateFormat.parse(fecha)); 
				c.add(Calendar.DATE, diasASumar);
				fecha = simpleDateFormat.format(c.getTime());
			}

			hora = simpleDateFormatS.format(d);
		}
		// log.info("Fecha:" + fecha);
		System.out.println("Hora obt:" + hora);

		if (soloFecha) {
			salida = fecha;
		}
		if (soloHora) {
			salida = hora;
		}
		if (dateTime) {
			salida = fecha + "T" + hora + "Z";
		}
		if (!replace.equals("")) {
			salida = fecha.toString().substring(0, 10).replace(replace, "");
		}

		return salida;
	}
	
	public static String convertirHoraA24Hrs(String hora) {
		String salida = "";
		switch (hora) {
		case "01":
			salida = "13";
			break;
		case "02":
			salida = "14";
			break;
		case "03":
			salida = "15";
			break;
		case "04":
			salida = "16";
			break;
		case "05":
			salida = "17";
			break;
		case "06":
			salida = "18";
			break;
		case "07":
			salida = "19";
			break;
		case "08":
			salida = "20";
			break;
		case "09":
			salida = "21";
			break;
		case "10":
			salida = "22";
			break;
		case "11":
			salida = "23";
			break;
		case "12":
			salida = "24";
			break;
		default:
			salida = hora;
			break;
		}
		return salida;
	}
	
	public static String convertirHora24a12(String hora24) {
        DateTimeFormatter formato24 = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formato12 = DateTimeFormatter.ofPattern("hh:mm a");
        
        LocalTime hora = LocalTime.parse(hora24, formato24);
        return hora.format(formato12);
    }
	
	public static boolean isCorreoValido(String correo){
		boolean p = false;
		if(correo != null){
			String emailAddress = correo;
		    String regexPattern = "^(.+)@(\\S+)$";
		    p = Pattern.compile(regexPattern)
		      .matcher(emailAddress)
		      .matches();
		}
	    return p;
	}
	
	public static int calcularAge(String fechaScale,String fechaInterview, int dias){
		
		Date fechaInterviewF = null;
		Date fechaScaleF = null;
		Date fechaIntFinal = null;
		
		try {
			fechaInterviewF = UtilidadesAdapter.cadenaAFecha(fechaScale);
			fechaScaleF = UtilidadesAdapter
					.cadenaAFecha(fechaInterview);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		fechaIntFinal = calcularFechaIntFinal(fechaScaleF, fechaInterviewF);
		fechaIntFinal = UtilidadesAdapter.sumarDiasAFechaSinFinesDeSemana(fechaIntFinal, 3);
		return dueDays(UtilidadesAdapter.fechaActual(), UtilidadesAdapter.formatearFecha(fechaIntFinal));
	}
	
	public static int dueDays(String fi, String ff) {
		int df = UtilidadesAdapter.diferenciaDias(fi, ff);
		// System.out.println("df:"+df);
		
		return df;
	}
	
	public static Date calcularFechaIntFinal(Date fechaScale,Date fechaInterview){
		Date fechaIntFinal = null;
		if (fechaScale != null && fechaInterview != null) {
			// System.out.println("Id
			// Solicitud:"+sol+"fs:"+fechaScale.compareTo(fechaInterview));
			if (fechaScale.compareTo(fechaInterview) == 1) {
				fechaIntFinal = fechaScale;
			} else {
				fechaIntFinal = fechaInterview;
			}
		}
		if (fechaInterview != null) {
			fechaIntFinal = fechaInterview;
		}
		if (fechaScale != null) {
			fechaIntFinal = fechaScale;
		}
		return fechaIntFinal;
	}
	
	public static List<String> fechasMesYMesAnterior(){
		
		List<String> salida = new ArrayList<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		//Fecha actual
		Calendar calendar = Calendar.getInstance(); 

		//A la fecha actual le pongo el día 1
		calendar.set(Calendar.DAY_OF_MONTH,1);
		String diaFinalMesActual = ""+calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		String fechaActual = ""+sdf.format(calendar.getTime());
		String fechaActualFinal = fechaActual.substring(0,8)+diaFinalMesActual;
		//System.out.println("fechaActualFinal:" + fechaActualFinal);

		//Se le agrega 1 mes 
		/*calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
		System.out.println("Fecha del del siguiente mes:" + sdf.format(calendar.getTime()));
		System.out.println("1-Último día del mes siguiente " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));*/

		//Se le quita 1 mes
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-2);//le quito 2 meses porque ya le había sumado 1 mes
		String fechaMesAnterior = sdf.format(calendar.getTime());
		//System.out.println("Fecha del primer día del mes anterior: " + sdf.format(calendar.getTime()));
		System.out.println("2.- Primer día del mes anterior" + calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		
		salida.add(fechaMesAnterior);
		salida.add(fechaActualFinal);
		
		return salida;
	}
	
	public static String formatearFechaStringAtipoUS(String fecha){
		String salida = "";
		if(!"".equals(fecha)){
			String[] p = fecha.split("-"); 
			String m = agregarCerosALaIzquierda(2,p[2]);
			String d = agregarCerosALaIzquierda(2,p[1]);
			salida = d+"/"+m+"/"+p[0];
			
			//salida =  fecha.substring(6,7)+"/"+fecha.substring(8,10)+"/"+fecha.substring(0,4);
		}
		return salida;
	}
	
	public static String formatearFechaStringAtipoMX(String fecha){
		String salida = "";
		if(!"".equals(fecha)){
			String[] p = fecha.split("/"); 
			String m = agregarCerosALaIzquierda(2,p[0]);
			String d = agregarCerosALaIzquierda(2,p[1]);
			salida =  p[2]+"-"+m+"-"+d;
		}
		return salida;
	}
	
	public static String agregarCerosALaIzquierda(int tamanio,String valor){
		String m = String.format("%0" + tamanio + "d", Integer.valueOf(valor));
		return m;
	}
	
	public static String convertir12ha24h(String hora12){
		// Formato de entrada (12 horas con AM/PM)
        DateTimeFormatter formato12 = DateTimeFormatter.ofPattern("hh:mm a");

        // Formato de salida (24 horas)
        DateTimeFormatter formato24 = DateTimeFormatter.ofPattern("HH:mm");

        // Parseo y conversión
        LocalTime hora = LocalTime.parse(hora12, formato12);
        String horaEn24 = hora.format(formato24);

        System.out.println("Hora en 24 horas: " + horaEn24);
        return horaEn24;
	}
	
	public static String convertirAHoraEstado(String fecha, String hora12,String tipo,String estado,String zonaHoraria){
		
		if(zonaHoraria != null){
			if("".equals(zonaHoraria)){
				zonaHoraria = "PST";
			}
		}else{
			zonaHoraria = "PST";
		}
		UtilidadesAdapter.pintarLog("Zona horaria:"+zonaHoraria);
		String horaSalida;
		String fechaSalida;
		String salida = "";
		String horaAEnviar = "";
		pintarLog("hora12:"+hora12);
		
		String[] p = hora12.split(":"); 
		String m = p[0];
		int vh = Integer.valueOf(m);
		
		pintarLog("vh:"+vh);
		if(vh < 12){
			if(tipo.equals("PM")){
				horaAEnviar = convertir12ha24h(hora12+" "+tipo);
			}else{
				horaAEnviar = hora12;
			}
		}else{
			horaAEnviar =  hora12;
		}
		
		String[] dhoras = horaAEnviar.split(":");
		int horaE = Integer.valueOf(dhoras[0]);
		int minutosE = Integer.valueOf(dhoras[1]);
		int anio = Integer.valueOf(fecha.substring(6,10));
		int mes = Integer.valueOf(fecha.substring(0,2));
		int dia = Integer.valueOf(fecha.substring(3,5));
	
		pintarLog("hora:"+horaE+"|minutos"+minutosE+"|anio"+anio+"|mes"+mes+"|dia"+dia+"|estado:"+estado);
		
		pintarLog("\n**Convirtiendo hora");
		/*salida = obtenerHoraDeEstado(anio, mes, dia, horaE, minutosE, estado);
		if(!"".equals(salida)){
			salida = UtilidadesAdapter.convertirHora24a12(salida);
			pintarLog("horaf:"+salida);
		}*/
		
		salida = convertirHoraPersonalizada(anio, mes, dia, horaE, minutosE, zonaHoraria,estado);
		fechaSalida = salida.substring(0,10);
		horaSalida = salida.substring(11,16);
		pintarLog("Fecha salida:"+fechaSalida+" horaSalida:"+horaSalida);
		pintarLog("**Termina convirtiendo hora");
		if(!"".equals(salida)){
			salida = UtilidadesAdapter.convertirHora24a12(horaSalida);
			pintarLog("horaf:"+salida);
		}
		return salida;
		
	}
	
	// Mapa de abreviaturas a zonas horarias
    private static final Map<String, String> ZONA_ABREVIATURA = new HashMap<>();
    static {
        ZONA_ABREVIATURA.put("EST", "America/New_York");
        ZONA_ABREVIATURA.put("CST", "America/Chicago");
        ZONA_ABREVIATURA.put("MST", "America/Denver");
        ZONA_ABREVIATURA.put("PST", "America/Los_Angeles");
        ZONA_ABREVIATURA.put("AKST", "America/Anchorage");
        ZONA_ABREVIATURA.put("HAST", "Pacific/Honolulu");
    }
	
	// Mapa de estados a zonas horarias
    private static final Map<String, String> ESTADOS_ZONA = new HashMap<>();
    static {
        
        ESTADOS_ZONA.put("AL", "America/Chicago"); 
        ESTADOS_ZONA.put("CT", "America/New_York");
        ESTADOS_ZONA.put("DE", "America/New_York");
        ESTADOS_ZONA.put("FL", "America/New_York"); 
        ESTADOS_ZONA.put("GA", "America/New_York");
        ESTADOS_ZONA.put("IN", "America/New_York"); 
        ESTADOS_ZONA.put("KY", "America/New_York"); 
        ESTADOS_ZONA.put("ME", "America/New_York");
        ESTADOS_ZONA.put("MD", "America/New_York");
        ESTADOS_ZONA.put("MA", "America/New_York");
        ESTADOS_ZONA.put("MI", "America/New_York"); 
        ESTADOS_ZONA.put("NH", "America/New_York");
        ESTADOS_ZONA.put("NJ", "America/New_York");
        ESTADOS_ZONA.put("NY", "America/New_York");
        ESTADOS_ZONA.put("NC", "America/New_York");
        ESTADOS_ZONA.put("OH", "America/New_York");
        ESTADOS_ZONA.put("PA", "America/New_York");
        ESTADOS_ZONA.put("RI", "America/New_York");
        ESTADOS_ZONA.put("SC", "America/New_York");
        ESTADOS_ZONA.put("TN", "America/Chicago"); 
        ESTADOS_ZONA.put("VT", "America/New_York");
        ESTADOS_ZONA.put("VA", "America/New_York");
        ESTADOS_ZONA.put("WV", "America/New_York");
        ESTADOS_ZONA.put("WA", "America/New_York");
        // Central Time
        ESTADOS_ZONA.put("AR", "America/Chicago");
        ESTADOS_ZONA.put("IL", "America/Chicago");
        ESTADOS_ZONA.put("IA", "America/Chicago");
        ESTADOS_ZONA.put("KS", "America/Chicago"); 
        ESTADOS_ZONA.put("LA", "America/Chicago");
        ESTADOS_ZONA.put("MN", "America/Chicago");
        ESTADOS_ZONA.put("MS", "America/Chicago");
        ESTADOS_ZONA.put("MO", "America/Chicago");
        ESTADOS_ZONA.put("NE", "America/Chicago"); 
        ESTADOS_ZONA.put("ND", "America/Chicago");
        ESTADOS_ZONA.put("OK", "America/Chicago");
        ESTADOS_ZONA.put("SD", "America/Chicago");
        ESTADOS_ZONA.put("TX", "America/Chicago");
        ESTADOS_ZONA.put("WI", "America/Chicago");
        // Mountain Time
        ESTADOS_ZONA.put("AZ", "America/Phoenix");
        ESTADOS_ZONA.put("CO", "America/Denver");
        ESTADOS_ZONA.put("ID", "America/Denver");
        ESTADOS_ZONA.put("MT", "America/Denver");
        ESTADOS_ZONA.put("NV", "America/Los_Angeles");
        ESTADOS_ZONA.put("NM", "America/Denver");
        ESTADOS_ZONA.put("UT", "America/Denver");
        ESTADOS_ZONA.put("WY", "America/Denver");
        // Pacific Time
        ESTADOS_ZONA.put("CA", "America/Los_Angeles");
        ESTADOS_ZONA.put("OR", "America/Los_Angeles");
        ESTADOS_ZONA.put("WA", "America/Los_Angeles");
        // Alaska Time
        ESTADOS_ZONA.put("AK", "America/Anchorage");
        // Hawaii-Aleutian Time
        ESTADOS_ZONA.put("HI", "Pacific/Honolulu");
    }

    public static String convertirHoraPersonalizada(int anio, int mes, int dia, int hora, int minutos,
                                                  String zonaAbreviada, String estadoDestino) {
    	
    	String salida = "";
        zonaAbreviada = zonaAbreviada.toUpperCase();
        estadoDestino = estadoDestino.toUpperCase();

        if (!ZONA_ABREVIATURA.containsKey(zonaAbreviada)) {
            System.out.println("Zona horaria no reconocida: " + zonaAbreviada);
            return "";
        }

        if (!ESTADOS_ZONA.containsKey(estadoDestino)) {
            System.out.println("Estado no reconocido: " + estadoDestino);
            return "";
        }

        ZoneId zonaOrigen = ZoneId.of(ZONA_ABREVIATURA.get(zonaAbreviada));
        ZoneId zonaDestino = ZoneId.of(ESTADOS_ZONA.get(estadoDestino));

        // Crear fecha y hora en zona origen
        ZonedDateTime fechaOrigen = ZonedDateTime.of(anio, mes, dia, hora, minutos, 0, 0, zonaOrigen);
        ZonedDateTime fechaDestino = fechaOrigen.withZoneSameInstant(zonaDestino);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");

        System.out.println("Hora original en " + zonaAbreviada + ": " + fechaOrigen.format(formatter));
        System.out.println("Hora convertida en " + estadoDestino + ": " + fechaDestino.format(formatter));
        salida =  fechaDestino.format(formatter);
        
        return salida;
    }
	

	
	
	public static void main(String args[]) {
		
		System.out.println(UtilidadesAdapter.fechaEnLetraEnghish(UtilidadesAdapter.fechaActual()));
		
		Date date = new Date();
		System.out.println(formatDateToString(date, "yyyy-MM-dd hh:mm:ss a", "PST"));
		
		Date d =  null;
		try {
			d = cadenaAFechaPST(formatDateToString(date, "yyyy-MM-dd hh:mm:ss a", "PST"));
			System.out.println(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String limpiarCaracteres(String cadena){
		return cadena.replaceAll("[^\\w ]+", "");
	}
	
	public static void testThread(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					UtilidadesAdapter.pintarLog("prueba thread mail");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
		}).start();
		UtilidadesAdapter.pintarLog("prueba thread");
	}
	
	public static void pintarLog(String mensaje){
		
		if(ambiente.equals("test")){
			System.out.println(mensaje);
			//sb.append(mensaje+"\n");
		}
		
	} 

}
