# Plan para activar validación de token JWT en todos los endpoints

## Diagnóstico

La API ya tiene un filtro `JWTAuthorizationFilter` que sabe validar tokens JWT, pero **nunca se ejecuta** porque:

1. La configuración de seguridad (`WebSecurityConfig`) tiene `"/**").permitAll()` — esto deja pasar **todas** las peticiones sin revisar nada
2. El filtro `JWTAuthorizationFilter` **no está registrado** en la cadena de filtros de Spring Security
3. Hay una **discrepancia de llaves**: `JwtAdapter` firma los tokens con la llave hardcodeada `"ricma"`, pero el filtro intenta validar usando la llave del archivo `configuraciones-global.properties` (`change_me_local`) — son diferentes, por lo que aunque el filtro estuviera activo, **invalidaría todos los tokens**
4. El método `validateToken()` del filtro lee el archivo desde `src/main/resorces/...` que tiene un **error de escritura** ("resorces" en vez de "resources"), lo que causaría un error en producción

---

## Paso 1: Unificar la llave secreta en `JwtAdapter.java`

**Archivo:** `adapter/out/jwt/JwtAdapter.java`

Reemplazar la llave hardcodeada `"ricma"` para que use la misma llave del archivo de propiedades:

```java
// ANTES:
private final String KEY = "ricma";

// DESPUÉS:
@Value("${jwt-secret-key}")
private String jwtSecretKey;
```

Y cambiar `KEY.getBytes()` por `jwtSecretKey.getBytes()` en la línea del `signWith()`.

**Por qué:** Para que tanto la creación como la validación del token usen la **misma llave secreta**.

---

## Paso 2: Corregir `JWTAuthorizationFilter.java`

**Archivo:** `JWTAuthorizationFilter.java`

Tres cambios:

### 2a. Agregar `@Component`

```java
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
```

### 2b. Modificar `validateToken()` para usar la propiedad inyectada

```java
// ANTES:
public Claims validateToken(HttpServletRequest request) {
    String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
    Properties prop = null;
    try {
        prop = UtilidadesAdapter.readPropertiesFile("src/main/resorces/configuraciones-global.properties");
    } catch (IOException e) {
        e.printStackTrace();
    }
    String sk = prop.getProperty("jwt-secret-key");
    return Jwts.parser().setSigningKey(sk.getBytes()).parseClaimsJws(jwtToken).getBody();
}

// DESPUÉS:
public Claims validateToken(HttpServletRequest request) {
    String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
    return Jwts.parser().setSigningKey(jwtSecretKey.getBytes()).parseClaimsJws(jwtToken).getBody();
}
```

### 2c. Ignorar OPTIONS (preflight CORS)

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
    if (request.getMethod().equals("OPTIONS")) {
        chain.doFilter(request, response);
        return;
    }
    // ... resto del código existente ...
}
```

---

## Paso 3: Actualizar `WebSecurityConfig` en `PEApplication.java`

**Archivo:** `PEApplication.java`

Reemplazar la configuración actual por una que permita solo Swagger, login y OPTIONS sin token, requiriendo autenticación para todo lo demás:

```java
@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .authorizeRequests()
            .antMatchers(
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/swagger-resources",
                "/v2/api-docs",
                "/v3/api-docs",
                "/webjars/**",
                "/configuration/ui",
                "/configuration/security",
                "/actuator/health",
                "/actuator/info"
            ).permitAll()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers(HttpMethod.POST, "/autenticaciones/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
```

---

## Paso 4: Actualizar `configuraciones-global.properties`

**Archivo:** `src/main/resources/configuraciones-global.properties`

Cambiar la llave secreta por una más segura (mínimo 64 bytes para HS512). Generar con:

```bash
openssl rand -base64 64
```

```properties
jwt-secret-key = <un-string-base64-de-64-bytes>
```

---

## Resumen de archivos a modificar

| Archivo | Cambio principal |
|---|---|
| `adapter/out/jwt/JwtAdapter.java` | Usar `@Value("${jwt-secret-key}")` en vez de `"ricma"` |
| `JWTAuthorizationFilter.java` | Agregar `@Component`, usar `jwtSecretKey` inyectado, ignorar OPTIONS |
| `PEApplication.java` (WebSecurityConfig) | Configurar seguridad real + registrar filtro + STATELESS |
| `configuraciones-global.properties` | Cambiar `change_me_local` por una clave real |

---

## Prueba rápida después de los cambios

1. **Login:** `POST /autenticaciones/inicio-sesion` con `{"usuario": "...", "contrasenia": "..."}` → obtienes el token
2. **Sin token:** `GET /solicitudes/1` → debe responder `403 Forbidden`
3. **Con token:** `GET /solicitudes/1` con header `Authorization: Bearer <token>` → debe responder OK
