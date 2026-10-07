# Migración a Java 21 — sistema-crm-fu-api

**Estado actual:** Spring Boot 2.3.5.RELEASE · Java 8 · WAR desplegado en Tomcat externo (AWS)  
**Objetivo:** Spring Boot 3.2+ · Java 21

---

## Ruta de migración (incremental)

El salto directo no es viable. Spring Boot 3.x requiere Java 17 como mínimo, por lo que la ruta es:

```
Paso 1: Spring Boot 2.3.5 → Spring Boot 2.7.x   (aún en Java 8/11)
Paso 2: Java 8 → Java 17                          (mínimo para SB 3.x)
Paso 3: Spring Boot 2.7.x → Spring Boot 3.2.x    (Jakarta EE 10)
Paso 4: Java 17 → Java 21                         (objetivo final)
```

---

## PARTE 1 — Cambios en el proyecto

### 1.1 pom.xml — Actualizaciones de versiones

```xml
<!-- Spring Boot: 2.3.5.RELEASE → 3.2.x -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.5</version>
</parent>

<!-- Java version -->
<properties>
    <java.version>21</java.version>
</properties>
```

### 1.2 Dependencias que deben cambiar

| Dependencia actual | Versión actual | Acción | Versión/Artefacto destino | Motivo |
|--------------------|---------------|--------|--------------------------|--------|
| `jjwt` | 0.9.1 | Reemplazar | `jjwt-api` + `jjwt-impl` + `jjwt-jackson` 0.12.x | API completamente reescrita; 0.9.x incompatible con SB 3.x |
| `springfox-swagger2` | 3.0.0 | Reemplazar | `springdoc-openapi-starter-webmvc-ui` 2.x | Springfox abandonado, incompatible con SB 3.x |
| `springfox-boot-starter` | 3.0.0 | Eliminar | — | Reemplazado por springdoc |
| `javax.xml.bind:jaxb-api` | 2.4.0 | Reemplazar | `jakarta.xml.bind-api` 4.0.x | Cambio de namespace javax → jakarta |
| `spring-boot-starter-mail` | 2.2.5.RELEASE | Quitar versión explícita | Gestionado por BOM de SB 3.x | Dejar que el parent gestione la versión |
| `gson` | 2.8.5 | Actualizar | 2.10.x | CVEs en versiones antiguas |
| `jackson-core/databind/annotations` | 2.12.0 | Quitar versiones explícitas | Gestionado por BOM de SB 3.x | Jackson 2.15+ incluido en SB 3.x |

#### Reemplazo de jjwt (0.9.x → 0.12.x)

```xml
<!-- ELIMINAR esto -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>

<!-- AGREGAR esto -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
```

#### Reemplazo de Springfox por Springdoc

```xml
<!-- ELIMINAR -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>3.0.0</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>

<!-- AGREGAR -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

---

### 1.2 Código fuente — Cambio de namespace javax → jakarta

Spring Boot 3.x usa **Jakarta EE 10**. Todos los imports `javax.*` deben cambiar a `jakarta.*`.

Los más comunes en este proyecto:

```java
// ANTES                                      // DESPUÉS
import javax.persistence.*;               →   import jakarta.persistence.*;
import javax.servlet.http.*;              →   import jakarta.servlet.http.*;
import javax.validation.constraints.*;    →   import jakarta.validation.constraints.*;
import javax.annotation.*;               →   import jakarta.annotation.*;
import javax.transaction.*;              →   import jakarta.transaction.*;
import javax.xml.bind.*;                 →   import jakarta.xml.bind.*;
```

> **Tip:** OpenRewrite puede automatizar este cambio. Ejecutar:
> ```bash
> mvn org.openrewrite.maven:rewrite-maven-plugin:run \
>   -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:LATEST \
>   -Drewrite.activeRecipes=org.openrewrite.java.migrate.jakarta.JavaxMigrationToJakarta
> ```

---

### 1.3 Código fuente — Spring Security 6.x

`WebSecurityConfigurerAdapter` fue eliminado en Spring Security 6 (incluido en Spring Boot 3.x).

```java
// ANTES — ya no compila en SB 3.x
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/public/**").permitAll()
            .anyRequest().authenticated();
    }
}

// DESPUÉS
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
        );
        return http.build();
    }
}
```

Cambios clave en Spring Security 6:
- `authorizeRequests()` → `authorizeHttpRequests()`
- `antMatchers()` → `requestMatchers()`
- `WebSecurityConfigurerAdapter` → Bean `SecurityFilterChain`
- CSRF, CORS y session management ahora usan lambda DSL

---

### 1.4 Código fuente — JWT (jjwt 0.9.x → 0.12.x)

La API de jjwt 0.12.x es completamente diferente:

```java
// ANTES (0.9.x)
String token = Jwts.builder()
    .setSubject(username)
    .setExpiration(expDate)
    .signWith(SignatureAlgorithm.HS512, secret)
    .compact();

Claims claims = Jwts.parser()
    .setSigningKey(secret)
    .parseClaimsJws(token)
    .getBody();

// DESPUÉS (0.12.x)
SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

String token = Jwts.builder()
    .subject(username)
    .expiration(expDate)
    .signWith(key)
    .compact();

Claims claims = Jwts.parser()
    .verifyWith(key)
    .build()
    .parseSignedClaims(token)
    .getPayload();
```

---

### 1.5 Código fuente — Anotaciones Swagger

Las anotaciones de Springfox deben reemplazarse por las de Springdoc/OpenAPI 3:

```java
// ANTES (Springfox)
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Usuarios", tags = {"usuarios"})
@ApiOperation(value = "Obtiene todos los usuarios")

// DESPUÉS (Springdoc)
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Usuarios")
@Operation(summary = "Obtiene todos los usuarios")
```

La URL de Swagger UI cambia de `/swagger-ui/` a `/swagger-ui/index.html` (o configurar en `application.properties`):
```properties
springdoc.swagger-ui.path=/swagger-ui.html
```

---

### 1.6 Tomcat — Versión compatible

Como el proyecto se empaqueta como WAR y se despliega en Tomcat externo:

| Tomcat | Servlet API | Compatible con SB 3.x |
|--------|-------------|----------------------|
| 9.x | javax.servlet | ❌ No |
| 10.0.x | jakarta.servlet | ⚠️ Parcial |
| **10.1.x** | **jakarta.servlet** | **✅ Sí** |

**Se requiere actualizar Tomcat a 10.1.x en el servidor.**

---

## PARTE 2 — Cambios en el servidor AWS

### 2.1 Instalar JDK 21

```bash
# Amazon Linux 2023
sudo dnf install java-21-amazon-corretto-headless -y

# Amazon Linux 2
sudo amazon-linux-extras install java-openjdk21 -y
# o usar corretto:
sudo rpm --import https://yum.corretto.aws/corretto.key
sudo curl -L -o /etc/yum.repos.d/corretto.repo https://yum.corretto.aws/corretto.repo
sudo yum install -y java-21-amazon-corretto-devel

# Ubuntu/Debian
sudo apt-get update
sudo apt-get install -y java-21-amazon-corretto-jdk
```

### 2.2 Configurar JAVA_HOME

```bash
# Verificar la ruta del JDK instalado
which java
java -version
readlink -f $(which java)

# Configurar en el perfil del sistema
sudo nano /etc/environment
# Agregar o modificar:
JAVA_HOME="/usr/lib/jvm/java-21-amazon-corretto"
PATH="$JAVA_HOME/bin:$PATH"

# Aplicar cambios
source /etc/environment

# Verificar
echo $JAVA_HOME
java -version
```

### 2.3 Actualizar Tomcat a 10.1.x

```bash
# 1. Detener Tomcat actual
sudo systemctl stop tomcat

# 2. Hacer backup de configuraciones
sudo cp -r /opt/tomcat/conf /opt/tomcat-conf-backup
sudo cp -r /opt/tomcat/webapps /opt/tomcat-webapps-backup

# 3. Descargar Tomcat 10.1.x
cd /tmp
wget https://downloads.apache.org/tomcat/tomcat-10/v10.1.26/bin/apache-tomcat-10.1.26.tar.gz
tar -xzf apache-tomcat-10.1.26.tar.gz

# 4. Reemplazar binarios (conservar conf/ y webapps/)
sudo cp -r apache-tomcat-10.1.26/bin /opt/tomcat/
sudo cp -r apache-tomcat-10.1.26/lib /opt/tomcat/

# 5. Restaurar configuraciones
sudo cp -r /opt/tomcat-conf-backup/* /opt/tomcat/conf/

# 6. Actualizar JAVA_HOME en el servicio de Tomcat
sudo nano /etc/systemd/system/tomcat.service
# Modificar la línea:
# Environment=JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto

# 7. Recargar systemd y arrancar
sudo systemctl daemon-reload
sudo systemctl start tomcat
sudo systemctl status tomcat
```

### 2.4 Actualizar el archivo de servicio de Tomcat

```ini
# /etc/systemd/system/tomcat.service
[Unit]
Description=Apache Tomcat
After=network.target

[Service]
Type=forking
Environment=JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto
Environment=CATALINA_HOME=/opt/tomcat
Environment=CATALINA_BASE=/opt/tomcat
Environment=CATALINA_PID=/opt/tomcat/temp/tomcat.pid
ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh
User=tomcat
Group=tomcat
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

---

## PARTE 3 — Orden de ejecución recomendado

```
[ ] 1. Crear rama git: feature/migracion-java-21
[ ] 2. Paso intermedio: actualizar SB a 2.7.x y verificar que compila/funciona
[ ] 3. Actualizar Java a 17 en pom.xml y verificar
[ ] 4. Migrar javax → jakarta (con OpenRewrite o manualmente)
[ ] 5. Actualizar SB a 3.2.x
[ ] 6. Reemplazar jjwt (0.9.x → 0.12.x) y reescribir lógica JWT
[ ] 7. Reemplazar Springfox por Springdoc
[ ] 8. Reescribir SecurityConfig (Spring Security 6.x)
[ ] 9. Actualizar java.version a 21 en pom.xml
[ ] 10. Ejecutar mvn clean test y corregir errores
[ ] 11. En servidor AWS: instalar JDK 21, actualizar Tomcat 10.1.x
[ ] 12. Desplegar y validar en QAS antes de PRO
```

---

## Verificación final

```bash
# En local — debe compilar y pasar tests
mvn clean test

# En servidor — verificar versiones
java -version          # debe mostrar 21.x
/opt/tomcat/bin/version.sh   # debe mostrar Tomcat 10.1.x

# Validar endpoints críticos después del despliegue
curl -i https://tu-dominio/api/health
```

---

## Referencias

- [Spring Boot 3.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
- [Spring Security 6.x Migration](https://docs.spring.io/spring-security/reference/migration/index.html)
- [jjwt 0.12.x Migration](https://github.com/jwtk/jjwt#jjwt-01x-to-012x-migration)
- [Springdoc OpenAPI](https://springdoc.org/#migrating-from-springfox)
- [Amazon Corretto 21](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/what-is-corretto-21.html)
- [Apache Tomcat 10.1](https://tomcat.apache.org/download-10.cgi)
