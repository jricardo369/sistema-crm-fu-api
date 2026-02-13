package com.cargosyabonos;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cargosyabonos.application.port.out.TareaProgramadaPort;
import com.cargosyabonos.domain.TareaProgramadaEntity;


@SpringBootApplication
@EnableScheduling
@EnableAsync 
public class PEApplication extends SpringBootServletInitializer{

	private static final Logger logger = LoggerFactory.getLogger(PEApplication.class);
	
	@Autowired
	private TareaProgramadaPort tPort;

	public static void main(String[] args) {
		SpringApplication.run(PEApplication.class, args);
	}
	
	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			
			http.csrf().disable()
					.authorizeRequests()
					.antMatchers("/swagger-ui.html**").permitAll()
					.antMatchers("/webjars/**").permitAll()
					.antMatchers("/swagger-resources/**").permitAll()
					.antMatchers("/csrf/**").permitAll()
					.antMatchers("/v2/**").permitAll()
					.antMatchers(HttpMethod.POST, "/autenticaciones").permitAll()
					.antMatchers(HttpMethod.POST, "/autenticaciones/inicio-sesion").permitAll()
					.antMatchers(HttpMethod.POST, "/autenticaciones/*").permitAll()
					.and().authorizeRequests()
					.antMatchers("/usuarios/**").permitAll()
					.antMatchers("/**").permitAll()
					.anyRequest().authenticated();
		}

		/*protected void configure(HttpSecurity http) throws Exception {
					
			http
	        // Deshabilitar CSRF
	        .csrf().disable()
	        
	        // Configurar CORS
	        .cors().and()
	        
	        // Configurar autorizaciones
	        .authorizeRequests()
	        // === PERMITIR ACCESO PÚBLICO ===
	        .antMatchers(
	            // Swagger UI
	            "/swagger-ui.html",
	            "/swagger-ui/**",
	            "/swagger-resources/**",
	            "/swagger-resources",
	            "/v2/api-docs",
	            "/v3/api-docs",
	            "/v3/api-docs/**",
	            "/webjars/**",
	            "/configuration/ui",
	            "/configuration/security",
	            
	            // Health checks
	            "/actuator/health",
	            "/actuator/info",
	            
	            // Endpoints de autenticación (TODAS LAS VARIANTES POSIBLES)
	            "/autenticaciones/inicio-sesion",
	            "/autenticaciones/inicio-sesion/**",
	            "/pe_crm_api/autenticaciones/inicio-sesion",
	            "/pe_crm_api/autenticaciones/inicio-sesion/**",
	            "/autenticaciones/**",
	            "/pe_crm_api/autenticaciones/**",
	            
	            // Endpoints de usuarios
	            "/usuarios/**",
	            "/pe_crm_api/usuarios/**"
	        ).permitAll()
	        
	        // Métodos HTTP específicos para autenticación
	        .antMatchers(HttpMethod.POST, 
	            "/autenticaciones/inicio-sesion",
	            "/pe_crm_api/autenticaciones/inicio-sesion",
	            "/autenticaciones/**",
	            "/pe_crm_api/autenticaciones/**"
	        ).permitAll()
	        
	        // === TODAS LAS DEMÁS RUTAS REQUIEREN AUTENTICACIÓN ===
	        .anyRequest().authenticated()
	        
	        .and()
	        // Configuración de sesión
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}*/
		
	}
	
	
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("PUT", "DELETE","POST","OPTIONS","GET","PATCH").allowedHeaders("*",
                        "Access-Control-Request-Headers").exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
                .allowCredentials(true);
			}
		};
	}
	
	@Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200",  "<other urls>")); //Collections.singletonList("http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
	
	@Bean
	public String getCronTarProgPayLunes(){
		String salida = "";
		TareaProgramadaEntity tp = tPort.obtenerPorCodigo("payments");
		String dia = "Monday";
		dia =  dia.toUpperCase();
		dia = diaParaCron(dia);
		String hora = tp.getHora();
		salida = " 0 " + hora.substring(3,5) + " " + hora.substring(0,2) + " * * " + dia;
		logger.info("cron payments:"+salida);
		return salida;
	}
	
	@Bean
	public String getCronTareaProgramadaPaymentsMiercoles(){
		String salida = "";
		TareaProgramadaEntity tp = tPort.obtenerPorCodigo("payments");
		String dia = "Wednesday";
		dia =  dia.toUpperCase();
		dia = diaParaCron(dia);
		String hora = tp.getHora();
		salida = " 0 " + hora.substring(3,5) + " " + hora.substring(0,2) + " * * " + dia;
		logger.info("cron payments:"+salida);
		return salida;
	}
	
	@Bean
	public String getCronTareaProgramadaPaymentsViernes(){
		String salida = "";
		TareaProgramadaEntity tp = tPort.obtenerPorCodigo("payments");
		String dia = "Friday";
		dia =  dia.toUpperCase();
		dia = diaParaCron(dia);
		String hora = tp.getHora();
		salida = " 0 " + hora.substring(3,5) + " " + hora.substring(0,2) + " * * " + dia;
		logger.info("cron payments:"+salida);
		return salida;
	}
	
	@Bean
	public String getCronTareaProgramadaLateRequests(){
		String salida = "";
		TareaProgramadaEntity tp = tPort.obtenerPorCodigo("late-requests");
		String dia = tp.getDia();
		dia =  dia.toUpperCase();
		dia = diaParaCron(dia);
		String hora = tp.getHora();
		salida = " 0 " + hora.substring(3,5) + " " + hora.substring(0,2) + " * * " + dia;
		logger.info("cron late requests:"+salida);
		return salida;
	}
	
	@Bean
	public String getCronRecordatorioCitas(){
		String salida = "";
		//Todos los dias a la 5 pm horario california, quedo en 23 por que se le restan 6 horas para que sean las 5 pm
		salida = "0 0 23 * * *";
		logger.info("cron recordatorio citas:"+salida);
		return salida;
	}
	
	@Bean
	public String getCronLimpiezaDisponibilidad(){
		String salida = "";
		//Todos los dias a la media noche
		salida = "0 0 7 * * *";
		logger.info("cron limpieza disponibilidad:"+salida);
		return salida;
	}
	
	@Bean
	public String getValidacionMsms(){
		String salida = "";
		//Todos los dias a las 7 am horario california
		salida = "0 00 15 * * *";
		logger.info("cron validacion msms:"+salida);
		return salida;
	}
	
	@Bean
	public String getSolsEndingSession(){
		String salida = "";
		//Todos los dias a las 12 pm horario california,se le restan 6 horas a 18
		salida = "0 00 18 * * *";
		logger.info("cron sols ending session:"+salida);
		return salida;
	}

	private String diaParaCron(String dia) {
		String s = "";
		switch (dia) {
		case "MONDAY":
			s = "MON";
			break;
		case "TUESDAY":
			s = "TUE";
			break;
		case "WEDNESDAY":
			s = "WED";
			break;
		case "THURSDAY":
			s = "THU";
			break;
		case "FRIDAY":
			s = "FRI";
			break;
		case "SATURDAY":
			s = "SAT";
			break;
		case "SUNDAY":
			s = "SUN";
			break;
		default:
			s = "";
			break;
		}
		return s;
	}
	
	
	
}
