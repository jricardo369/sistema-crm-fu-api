# Plan de Actualización Java 8 → Java 21 — sistema-crm-fu-api

> **Proyecto:** `pe_crm_api` (FamiliasUnidas / CRM FILES)
> **Estado actual:** Java 8 + Spring Boot 2.3.5.RELEASE + packaging WAR
> **Objetivo:** Java 21 LTS + Spring Boot 3.2.x + Jakarta EE 10
> **Fecha plan:** 2026-10-01
> **Alcance de este documento:** solo planificación. No se aplica ningún cambio de código aquí.

---

## 1. Resumen ejecutivo

No es viable cambiar únicamente `<java.version>1.8 → 21`. El proyecto está sobre
Spring Boot 2.3.5 (EOL desde 2021), que no soporta Java 21.

La migración real implica:

1. Subir Spring Boot 2.3.5 → 2.7.x → 3.2.x (obligatorio para Java 21).
2. Migrar `javax.* → jakarta.*` (Servlet, JPA, Validation, JAXB, Annotations).
3. Reescribir Seguridad (Spring Security 5 → 6): desaparece `WebSecurityConfigurerAdapter`.
4. Reemplazar Swagger Springfox 3.0.0 → springdoc-openapi 2.x.
5. Actualizar JWT jjwt 0.9.1 → 0.12.x (API y seguridad).
6. Actualizar drivers y librerías (MySQL, MSSQL, Jackson, POI, iText, Google API).
7. Subir infraestructura: Tomcat 9 o inferior → Tomcat 10.1+ (o migrar a JAR embebido).
8. Ampliar pruebas (hoy solo existe `contextLoads()` + 2 asserts).

**Estrategia recomendada: incremental en 2 fases.**

| Fase | Objetivo | Por qué |
|------|----------|---------|
| Fase 1 | Java 17 + Boot 2.7.18 | Última versión 2.x, estable, reversible. Corrige deprecations y dependencias sin Jakarta. |
| Fase 2 | Java 21 + Boot 3.2.x | Salto Jakarta + Security 6 + springdoc + jjwt nuevo. |

Hacerlo directo (big-bang 2.3 → 3.2) concentra ~40% de breaking changes en un solo paso y es muy difícil de depurar.

**Estimación orientativa:** 4–6 semanas con 1 dev senior + 0.5 QA (ver §9).

---

## 2. Inventario actual (verificado en repo)

### 2.1 `pom.xml`

| Elemento | Versión actual | Diagnóstico |
|----------|---------------|-------------|
| `spring-boot-starter-parent` | `2.3.5.RELEASE` | EOL. Subir a `2.7.18` y luego `3.2.5` (o 3.3/3.4). |
| `java.version` | `1.8` | Objetivo `21`. Usar `<maven.compiler.release>21</maven.compiler.release>`. |
| `packaging` | `war` + `ServletInitializer` + `spring-boot-starter-tomcat:provided` | Despliegue en Tomcat externo. Boot 3 exige Tomcat 10.1+. |
| `spring-boot-starter-mail` | `2.2.5.RELEASE` pineado | Quitar versión fija, heredar del parent. |
| `jackson-core/databind/annotations` | `2.12.0` pineados | Conflicto con Boot 3.2 (necesita Jackson 2.15+). Quitar versiones fijas. |
| `io.jsonwebtoken:jjwt` | `0.9.1` | De 2018. API `setSigningKey()` deprecada/insegura. Subir a `0.12.6` (`jjwt-api` + `jjwt-impl` + `jjwt-jackson`). |
| `javax.xml.bind:jaxb-api` | `2.4.0-b180830` | En Boot 3 se usa `jakarta.xml.bind-api` (viene transitiva). Eliminar dependencia explícita. |
| `mysql:mysql-connector-java` | sin versión (gestionada por Boot 2.3) | Artefacto retirado. Cambiar a `com.mysql:mysql-connector-j` (8.x / 9.x). |
| `com.microsoft.sqlserver:mssql-jdbc` | sin versión | Fijar `12.6+` compatible Java 21. |
| `io.springfox:springfox-swagger2/boot-starter` | `3.0.0` | Incompatible con Boot 3 / Spring 6. Reemplazar por `springdoc-openapi-starter-webmvc-ui:2.5.0`. |
| `com.lowagie:itext` | `4.2.2` | Antigua, con CVEs. Evaluar `com.github.librepdf:openpdf:1.3.30` (drop-in) o iText 8 con licencia. |
| `org.apache.poi:poi/poi-ooxml` | `5.0.0` | Subir a `5.2.5+` para Java 21. |
| `com.google.code.gson:gson` | `2.8.5` | Subir a `2.10.1+` (CVE en versiones viejas). |
| `com.google.api-client / api-common` | `1.35.1 / 2.2.0` | Revisar compatibilidad; subir a rama `2.x` si se usa Calendar/Gmail. |
| `spring-boot-maven-plugin` | con `<testFailureIgnore>true</testFailureIgnore>` | Quitar en migración: oculta fallos de tests. |

### 2.2 Código fuente (`src/main/java`, ~324 ficheros)

| Fichero / patrón | Problema |
|------------------|----------|
| `PEApplication.java:45` — `class WebSecurityConfig extends WebSecurityConfigurerAdapter` | Clase eliminada en Spring Security 6. Reescribir a `@Bean SecurityFilterChain`. `antMatchers()` → `requestMatchers()`, `authorizeRequests()` → `authorizeHttpRequests()`. |
| `PEApplication.java:130-155` — CORS doble (`WebMvcConfigurer` + `CorsFilter`) con `allowedOrigins("*")` + `allowCredentials(true)` | En Spring 6 esa combinación lanza error. Usar `allowedOriginPatterns("*")` o lista cerrada de orígenes. |
| `JWTAuthorizationFilter.java:8-11` — `javax.servlet.*` | Cambiar a `jakarta.servlet.*`. |
| `JWTAuthorizationFilter.java:71` — `Jwts.parser().setSigningKey(sk.getBytes())` | API eliminada en jjwt 0.12.x. Migrar a `parser().verifyWith(key).build().parseSignedClaims()`. Además lee fichero con ruta hardcodeada `src/main/resorces/...` (typo + no funciona en WAR). Usar `@Value` / `Environment`. |
| `adapter/out/jwt/JwtAdapter.java:36-48` — `signWith(HS512, KEY.getBytes())` con `KEY="ricma"` | Clave de 5 bytes, insuficiente para HS512 (requiere ≥64 bytes). Genera `WeakKeyException` en jjwt nuevo. Rotar secreto a ≥256 bits desde variable de entorno / vault. |
| Entidades `domain/*Entity.java` — `javax.persistence.*` | Migrar a `jakarta.persistence.*` (todas). Revisar `@Temporal`, `FetchType`, dialectos Hibernate 6. |
| Repositorios `adapter/out/sql/*Repository.java` — `javax.persistence.EntityManager/Query` | Migrar a `jakarta.persistence.*`. |
| `adapter/out/sql/MsgRepository.java` — `javax.annotation.PostConstruct` | Migrar a `jakarta.annotation.PostConstruct`. |
| `SwaggerConfig.java` — `@EnableSwagger2` + `Docket` | Eliminar. Crear config `OpenAPI` de springdoc. |
| `application.properties` | `server.servlet.context-path`, `spring.servlet.multipart.*`, `spring.jpa.*`, `management.*` tienen cambios/renombres en Boot 3. Password SMTP hardcodeado: externalizar. |
| `src/test` | Solo `CargosAbonosApplicationTests.contextLoads()` + 2 tests de `UtilidadesAdapter.isCorreoValido`. Cobertura insuficiente. |

### 2.3 Infraestructura y despliegue

- Empaquetado WAR `pe_crm_api.war`, `context-path /pe_crm_api`, puerto `18080`.
- URL prod actual: `https://crm-familiasunidasla.com/pe_crm_api/...` → Tomcat externo.
- Boot 3 + Jakarta requiere **Tomcat 10.1+ con JDK 21**. Tomcat 8.5/9 no sirve.
- Perfiles: `local, qas, pro` (`application-{local,qas,pro}.properties` + `configuraciones-global.properties`).
- Toolchain local ya lista: `JDK 21.0.2 + Maven 3.9.12` (verificado). Falta fijar `JAVA_HOME` y CI.

---

## 3. Qué implica Java 21 (más allá del JDK)

| Cambio | Detalle |
|--------|---------|
| LTS y performance | G1 mejorado, ZGC generacional, CDS/AppCDS, startup más rápido. Opcional: virtual threads (`spring.threads.virtual.enabled=true` en Boot 3.2+), solo tras medir. |
| Sintaxis nueva (oportunista, no obligatoria) | `records`, `sealed classes`, `pattern matching instanceof/switch`, `text blocks`. No reescribir todo en la migración; aplicar en código nuevo. |
| Encapsulación JDK | `SecurityManager` deprecado, accesos `sun.misc.*` / reflection a internos bloqueados. Revisar POI, iText viejo, Google client. |
| Codificación | `file.encoding=UTF-8` por defecto (JEP 400). Verificar reportes/querys con tildes. |
| Maven | Requiere Maven 3.9+ y `maven-compiler-plugin` con `release 21`. El `source/target 1.8` deja de compilar. |

---

## 4. Plan de migración paso a paso

### Fase 0 — Preparación (2–3 días)

1. Crear rama y tag de seguridad:
   ```sh
   git checkout -b chore/java21-migration
   git tag pre-migracion-v9.0
   mvn -v   # confirmar JDK 21 + Maven 3.9+
   export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.0.2.jdk/Contents/Home
   ```
2. Línea base:
   ```sh
   mvn dependency:tree -DoutputFile=deps-antes.txt
   mvn versions:display-dependency-updates > versiones-antes.txt
   mvn clean package -DskipTests -o  # confirma que hoy compila en Java 8
   ```
3. Añadir análisis CVEs (temporal o permanente):
   ```xml
   <dependency>
     <groupId>org.owasp</groupId>
     <artifactId>dependency-check-maven</artifactId>
     <version>9.0.9</version>
   </dependency>
   ```
4. Definir DONE: compila en 21, tests verdes, WAR en Tomcat 10.1, Swagger y `/actuator/health` OK, regresión funcional QAS sin errores, rollback probado.
5. Congelar `master` salvo hotfixes. Toda la migración en la rama.

### Fase 1 — Java 17 + Spring Boot 2.7.18 (1–2 semanas)

Objetivo: estabilizar sin Jakarta.

1. En `pom.xml`:
   ```xml
   <parent>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-parent</artifactId>
     <version>2.7.18</version>
   </parent>
   <properties>
     <java.version>17</java.version>
     <maven.compiler.release>17</maven.compiler.release>
   </properties>
   ```
2. Quitar versiones fijas gestionadas por el parent: `jackson-*`, `spring-boot-starter-mail`.
3. Subir drivers y utilidades compatibles Java 17:
   - `com.mysql:mysql-connector-j:8.1.0` (cambia groupId)
   - `com.microsoft.sqlserver:mssql-jdbc:12.4.2.jre11`
   - `org.apache.poi:poi/poi-ooxml:5.2.5`
   - `com.google.code.gson:gson:2.10.1`
4. Compilar y corregir warnings (`bootstrap classpath`, `deprecation` de Security).
5. Desplegar WAR en QAS con JDK 17 + Tomcat 9. Regresión completa.
6. Commit: `Fase 1 OK — Boot 2.7.18 + Java 17`.

### Fase 2 — Java 21 + Spring Boot 3.2.x (2–3 semanas, núcleo)

1. Subir parent:
   ```xml
   <parent>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-parent</artifactId>
     <version>3.2.5</version>
   </parent>
   <properties>
     <java.version>21</java.version>
     <maven.compiler.release>21</maven.compiler.release>
   </properties>
   ```
   > Alternativa válida: `3.3.x / 3.4.x` si se valida Tomcat y drivers. `3.2.x` es la opción conservadora LTS.
2. Migración `javax → jakarta` (masiva, ~324 ficheros):
   ```sh
   # Usar OpenRewrite (recomendado)
   mvn org.openrewrite.maven:rewrite-maven-plugin:run \
     -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-spring:5.20.0 \
     -DactiveRecipes=org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_2
   # Verificación: debe devolver 0
   grep -r "import javax\." src --include='*.java' | wc -l
   ```
   Mapeos principales:
   | Antes | Después |
   |-------|---------|
   | `javax.servlet.*` | `jakarta.servlet.*` |
   | `javax.persistence.*` | `jakarta.persistence.*` |
   | `javax.validation.*` | `jakarta.validation.*` |
   | `javax.annotation.*` | `jakarta.annotation.*` |
   | `javax.xml.bind.*` | `jakarta.xml.bind.*` |
3. Eliminar `jaxb-api:2.4.0` explícita del pom (Boot 3 la provee vía starter).
4. Hibernate 6 (viene con Boot 3): validar `spring.jpa.hibernate.ddl-auto=update`, `spring.jpa.properties.hibernate.jdbc.time_zone=UTC`, dialectos automáticos, y las queries nativas en `src/main/resources/querys/*.txt` + `@Query` con `EntityManager`.
5. Revisar `server.servlet.*`, `spring.servlet.multipart.*`, `management.*` (algunas claves cambiaron en Boot 3; arrancar y corregir según el `FailureAnalyzer`).

### Fase 3 — Spring Security 6 + JWT (1 semana)

1. Reescribir `PEApplication.WebSecurityConfig` (`WebSecurityConfigurerAdapter` ya no existe):
   ```java
   @Configuration
   @EnableWebSecurity
   public class SecurityConfig {
     @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http
         .csrf(csrf -> csrf.disable())
         .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
         .authorizeHttpRequests(a -> a
           .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()
           .requestMatchers(HttpMethod.POST, "/autenticaciones/**").permitAll()
           .anyRequest().authenticated());
       return http.build();
     }
   }
   ```
2. Arreglar CORS (Spring 6 rechaza `allowedOrigins("*")` + `allowCredentials(true)`):
   ```java
   registry.addMapping("/**")
     .allowedOriginPatterns("*") // o lista cerrada: https://app.familiasunidasla.com
     .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","PATCH")
     .allowedHeaders("*").allowCredentials(false);
   ```
   Unificar en un solo lugar (hoy hay `WebMvcConfigurer` + `CorsFilter` duplicados).
3. jjwt 0.9.1 → 0.12.6 en `pom.xml`:
   ```xml
   <dependency><groupId>io.jsonwebtoken</groupId><artifactId>jjwt-api</artifactId><version>0.12.6</version></dependency>
   <dependency><groupId>io.jsonwebtoken</groupId><artifactId>jjwt-impl</artifactId><version>0.12.6</version><scope>runtime</scope></dependency>
   <dependency><groupId>io.jsonwebtoken</groupId><artifactId>jjwt-jackson</artifactId><version>0.12.6</version><scope>runtime</scope></dependency>
   ```
   Cambios en código:
   ```java
   // Antes (JwtAdapter)
   Jwts.builder().signWith(SignatureAlgorithm.HS512, KEY.getBytes()).compact();
   Jwts.parser().setSigningKey(sk.getBytes()).parseClaimsJws(token).getBody();
   // Después
   SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // ≥64 bytes para HS512
   Jwts.builder().signWith(key).compact();
   Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
   ```
4. Rotar secreto JWT: el actual (`ricma`, 5 bytes) falla con `WeakKeyException` en jjwt nuevo. Generar secreto ≥256 bits, guardarlo en variable de entorno / vault, nunca en propiedades versionadas. Corregir además la lectura hardcodeada `src/main/resorces/configuraciones-global.properties` (typo + inválida dentro del WAR) usando `@Value("${jwt-secret-key}")`.

### Fase 4 — Swagger, PDFs/Excel y properties (3–5 días)

1. Quitar `springfox-swagger2` + `springfox-boot-starter`. Añadir:
   ```xml
   <dependency>
     <groupId>org.springdoc</groupId>
     <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
     <version>2.5.0</version>
   </dependency>
   ```
   Nueva config:
   ```java
   @Configuration
   public class OpenApiConfig {
     @Bean public OpenAPI api() {
       return new OpenAPI().info(new Info().title("FamiliasUnidas CRM API").version("9.0"));
     }
   }
   ```
   Actualizar README y consumidores: `/swagger-ui.html` → `/swagger-ui/index.html`, `/v2/api-docs` → `/v3/api-docs`.
2. iText `com.lowagie:itext:4.2.2` → `com.github.librepdf:openpdf:1.3.30` (cambio casi directo de imports `com.lowagie.*`) o iText 8 si hay licencia. Validar PDFs generados byte a byte.
3. `application*.properties`: revisar con el arranque Boot 3 y corregir claves. Externalizar `spring.mail.username/password` (hoy hardcodeados) a variables de entorno.
4. Revisar `@PropertySource("classpath:configuraciones-global.properties")` duplicados y rutas de querys `querys/*.txt`.

### Fase 5 — Infraestructura y despliegue (3–5 días)

**Opción A — Mantener WAR (mínimo cambio):**
- Subir servidores a `JDK 21 + Tomcat 10.1.x`. `ServletInitializer` sigue válido (ya usa `SpringBootServletInitializer`, solo cambia a `jakarta.servlet` transitivo).
- Verificar `context-path /pe_crm_api`, `port 18080`, reverse proxy y certificados.

**Opción B — Migrar a JAR embebido (recomendada a medio plazo):**
- Cambiar `<packaging>war</packaging>` → `jar`, eliminar `spring-boot-starter-tomcat:provided` y `ServletInitializer`.
- Dockerfile ejemplo:
  ```dockerfile
  FROM eclipse-temurin:21-jre
  WORKDIR /app
  COPY target/pe_crm_api.jar app.jar
  EXPOSE 18080
  HEALTHCHECK CMD wget -qO- http://localhost:18080/pe_crm_api/actuator/health | grep UP
  ENTRYPOINT ["java","-jar","/app/app.jar"]
  ```
- Actualizar pipelines `.github/`, `docker-compose` (MySQL/SQLServer) y documentación de despliegue.

### Fase 6 — QA, Go-Live y post (1 semana)

1. Ampliar tests mínimos antes del Go-Live:
   - Smoke: arranque contexto en cada perfil.
   - Integración JPA con Testcontainers (MySQL 8 + SQLServer 2022).
   - Contrato auth: login → token → acceso a `/usuarios`, `/solicitudes` + token expirado → 403.
   - Archivos: subida/descarga multipart 35MB, generación PDF/Excel.
   - Mail con GreenMail (no SMTP real).
2. Carga y observabilidad: JMeter sobre `/autenticaciones`, `/solicitudes`; revisar `/actuator/health,info,metrics`; medir arranque y memoria (G1 por defecto; evaluar ZGC solo si hay pausas).
3. Rollback: conservar WAR Java 8 desplegable + backup DB (Hibernate 6 puede alterar DDL con `update`). Despliegue blue/green, ventana de mantenimiento, checklist de reversión.
4. Post Go-Live (no durante la migración): activar virtual threads y modernizar sintaxis Java 21 de forma gradual.

---

## 5. Checklist de verificación

- [ ] `grep -r "import javax\." src` → 0 resultados.
- [ ] Sin `WebSecurityConfigurerAdapter`, `antMatchers`, `@EnableSwagger2`, `Docket`.
- [ ] Sin `springfox`, `jaxb-api:2.4.0`, `mysql-connector-java`, `setSigningKey(`.
- [ ] `mvn clean package` en JDK 21 verde, sin `testFailureIgnore=true`.
- [ ] WAR despliega en Tomcat 10.1 + JDK 21; `GET /pe_crm_api/actuator/health` → `UP`.
- [ ] Swagger UI accesible en `/swagger-ui/index.html`, spec en `/v3/api-docs`.
- [ ] Login en `/autenticaciones/inicio-sesion` emite `Bearer` válido; endpoints protegidos responden 200/403 según token.
- [ ] PDFs y Excels generados idénticos a prod actual.
- [ ] QAS regresión funcional firmada por negocio.

---

## 6. Comandos útiles

```sh
# Diagnóstico
mvn dependency:tree -DoutputFile=deps.txt
mvn versions:display-dependency-updates
grep -rn "javax\." src --include='*.java' | wc -l
grep -rn "WebSecurityConfigurerAdapter\|springfox\|setSigningKey" src --include='*.java'

# Migración asistida Boot 3
mvn org.openrewrite.maven:rewrite-maven-plugin:run \
  -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-spring:5.20.0 \
  -DactiveRecipes=org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_2

# Compilación Java 21
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.0.2.jdk/Contents/Home
mvn clean package
```

---

## 7. Riesgos y mitigación

| Riesgo | Impacto | Mitigación |
|--------|---------|------------|
| Jakarta masivo (324 ficheros) | Alto | OpenRewrite + compilación incremental por paquete. |
| Security 6 rompe auth | Alto | Colección Postman de `/autenticaciones` antes/después; tests de contrato. |
| Springfox → springdoc cambia URLs docs | Medio | Comunicar a front/consumidores; redirección temporal. |
| Drivers MySQL/MSSQL | Alto | Testcontainers en CI; probar contra copias QAS. |
| iText/POI generan binarios distintos | Medio | Comparación byte a byte de PDFs/XLSX de referencia. |
| Cobertura tests casi nula | Alto | No hacer Go-Live sin smoke + integración mínima. |
| `ddl-auto=update` + Hibernate 6 altera esquema | Alto | Backup DB, `validate` en prod primero, ventana de mantenimiento. |
| Secreto JWT débil + hardcodeado | Alto | Rotar secreto, mover a env/vault, coordinar re-login. |

---

## 8. Estimación y equipo

| Bloque | Duración |
|--------|----------|
| Fase 0 preparación | 2–3 días |
| Fase 1 Boot 2.7 + Java 17 | 1–2 semanas |
| Fase 2 Boot 3.2 + Java 21 + Jakarta | 2–3 semanas |
| Fase 3 Security + JWT | 1 semana |
| Fase 4 Swagger/PDFs/props | 3–5 días |
| Fase 5 Infra + Fase 6 QA/Go-Live | 1 semana |
| **Total** | **4–6 semanas (1 dev senior + 0.5 QA)** |

Requiere además: acceso a Tomcat prod, DBs QAS clon de prod, dueño de negocio para regresión, y gestión del secreto JWT/SMTP.

---

## 9. Decisiones pendientes del cliente

1. **Boot objetivo:** `3.2.5 LTS` (recomendado) vs `3.3/3.4` más reciente.
2. **Packaging:** mantener WAR en Tomcat 10.1 vs migrar a JAR embebido/Docker.
3. **iText:** OpenPDF (gratis) vs iText 8 con licencia comercial.
4. **Ventana de Go-Live y estrategia rollback** (blue/green vs in-place).
5. **Rotación de secreto JWT y credenciales SMTP** (coordinar re-login y cambio de password).

---

## 10. Anexos — diffs objetivo

### `pom.xml` (extracto objetivo Fase 2)

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>3.2.5</version>
</parent>
<properties>
  <java.version>21</java.version>
  <maven.compiler.release>21</maven.compiler.release>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
<!-- eliminar: jaxb-api, springfox-*, jackson con versión fija, spring-boot-starter-mail con versión fija -->
<!-- cambiar: mysql-connector-java → com.mysql:mysql-connector-j -->
<!-- añadir: springdoc-openapi-starter-webmvc-ui:2.5.0, jjwt-api/impl/jackson:0.12.6 -->
```

### Seguridad (antes → después)

```java
// ANTES (Boot 2.3 / Security 5) — PEApplication.java
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests()
      .antMatchers("/swagger-ui.html**").permitAll()
      .antMatchers("/**").permitAll().anyRequest().authenticated();
  }
}
// DESPUÉS (Boot 3.2 / Security 6)
@Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  http.csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(a -> a
      .requestMatchers("/v3/api-docs/**","/swagger-ui/**").permitAll()
      .requestMatchers(HttpMethod.POST, "/autenticaciones/**").permitAll()
      .anyRequest().authenticated())
    .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
  return http.build();
}
```

---

*Documento de planificación — pendiente de ejecución en rama `chore/java21-migration`. Ningún cambio de código fue aplicado al generarlo.*
