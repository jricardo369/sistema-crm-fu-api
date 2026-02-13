1. Actualización de dependencias y versión de Java
Usas Java 1.8 y Spring Boot 2.3.5. Considera actualizar a una versión más reciente de Java (11 o 17) y Spring Boot (3.x) para mayor seguridad, rendimiento y soporte.
Revisa las dependencias en el pom.xml para eliminar las que no se usan y actualizar versiones.
2. Seguridad
El método de seguridad actual (WebSecurityConfigurerAdapter) está obsoleto en Spring Boot 3.x. Se recomienda migrar a la nueva forma basada en beans de configuración.
El código permite acceso a todos los endpoints (/**). Evalúa restringir el acceso y aplicar roles/permisos según la lógica de negocio.



# Plan de mejoras – Proyecto Spring Boot `sistema-crm-fu-api`

Este documento lista mejoras propuestas para aplicar paulatinamente al proyecto.

---

## 1. Estructura y calidad de código

| Tema | Situación actual | Mejora propuesta | Prioridad |
|------|------------------|------------------|-----------|
| Capas y paquetes | Existen `adapter`, `application`, `domain`, sugiere arquitectura limpia. | Formalizar arquitectura: `adapter` = entrada/salida, `application` = casos de uso, `domain` = modelo y lógica. Documentar en `ARCHITECTURE.md`. | Media |
| Excepciones | `ApiException` solo guarda mensaje, sin status ni body claros. | Extender `ApiException` con campos `status`, `errorCode`, `details` y constructores claros. | Alta |
| Manejo de errores | Excepciones manejadas de forma dispersa. | Crear `@RestControllerAdvice` global para mapear excepciones a respuestas JSON consistentes. | Alta |
| Servicios grandes | Muchos `*Service` en `application`. | Revisar y separar responsabilidades: servicios de dominio vs adaptadores/repositorios. | Media |
| Consultas SQL | Queries en `/resources/querys/*.txt`. | Encapsular acceso a datos en una capa de repositorios (Spring JDBC/JPA) y centralizar queries. | Media |

---

## 2. API y validaciones

| Área | Mejora propuesta | Detalle | Prioridad |
|------|------------------|---------|-----------|
| DTOs y validación | Introducir Bean Validation. | Usar `@Valid`, `@NotNull`, `@Size`, etc. en DTOs de entrada de controladores. | Media |
| Respuesta de error estandarizada | JSON unificado de error. | Estructura tipo: `timestamp`, `status`, `error`, `message`, `path`, `details`. | Alta |
| Documentación API | Aprovechar `SwaggerConfig`. | Verificar que todos los endpoints REST estén documentados y probables desde Swagger UI. | Media |

---

## 3. Seguridad

| Tema | Mejora propuesta | Detalle | Prioridad |
|------|------------------|---------|-----------|
| JWT | Revisar `JWTAuthorizationFilter`. | Verificar expiración, roles/authorities y limpieza del parsing del token. | Media |
| Configuración sensible | Proteger credenciales. | Mover usuarios/passwords a variables de entorno o config externa (no en git). | Alta |
| CORS / CSRF | Endpoints expuestos. | Restringir CORS solo a orígenes necesarios; revisar CSRF según tipo de clientes. | Media |

---

## 4. Observabilidad (logs, métricas)

| Área | Mejora propuesta | Detalle | Prioridad |
|------|------------------|---------|-----------|
| Logging | Estandarizar logs. | Reemplazar `UtilidadesAdapter.pintarLog` por `Logger` donde aplique y usar niveles (`INFO`, `WARN`, `ERROR`). | Media |
| Trazabilidad | Log de IDs clave. | Loguear `idUsuario`, `idSolicitud`, `idEvento` en puntos críticos (citas, correos, pagos). | Media |
| Métricas | Integrar Actuator. | Agregar `spring-boot-starter-actuator` y exponer `/actuator/health`, `/info` y algunas métricas. | Baja |

---

## 5. Correo y tareas asíncronas

| Tema | Situación actual | Mejora propuesta | Prioridad |
|------|------------------|------------------|-----------|
| Envío de correos | `EnvioCorreoAdapter` y `EmailAdapter` usan hilos manuales (`new Thread`). | Migrar a `@Async` (Spring) para enviar correos asíncronos sin manejar hilos a mano. | Media |
| Flag `enviarCorreos` | Se lee de BD en `@PostConstruct`. | Usar el flag de forma homogénea en todos los métodos de envío (invitaciones, recordatorios, layouts). | Alta |
| Límites SMTP (Gmail) | Errores de límite diario excedido. | Evaluar migrar a proveedor de correo transaccional (SendGrid, SES, etc.) o Google Workspace. | Media |

---

## 6. Base de datos y rendimiento

| Área | Mejora propuesta | Detalle | Prioridad |
|------|------------------|---------|-----------|
| Índices y queries | Queries complejas en `/resources/querys/*.txt`. | Revisar planes de ejecución e índices en columnas usadas en `WHERE` y `JOIN`. | Media |
| Filtros de fecha | Uso de `YEARWEEK` y rangos. | Evaluar uso de rangos (`>=` y `<`) para rangos de fechas, evitando funciones en columna cuando impacten índices. | Baja |
| Paginación | Listados potencialmente grandes. | Implementar paginación en endpoints de listados (citas, solicitudes, pagos). | Media |

---

## 7. Testing

| Tipo de prueba | Mejora propuesta | Detalle | Prioridad |
|----------------|------------------|---------|-----------|
| Unit tests | Casos de uso principales. | Tests para servicios de `application` (citas, solicitudes, correos, pagos). | Media |
| Integration tests | DB real/embebida. | Probar queries y repositorios con base embebida o Testcontainers. | Baja |
| Tests de controladores | MockMvc. | Probar endpoints REST críticos: creación de cita, recordatorios, pagos. | Media |

---

## 8. Productividad de desarrollo

| Área | Mejora propuesta | Detalle | Prioridad |
|------|------------------|---------|-----------|
| DevTools | Asegurar recarga en caliente. | Confirmar `spring-boot-devtools` activo, `spring.devtools.restart.enabled` en `true` (o no definido) y ejecución desde IDE / `mvn spring-boot:run`. | Alta |
| Scripts y README | Documentar operación. | Incluir en `README.md`: cómo levantar local, perfiles (`local`, `qas`, `pro`), comandos de build/test. | Media |
| Configuración por ambiente | Claridad de perfiles. | Revisar `application-*.properties` para que cada ambiente tenga solo lo necesario, evitando duplicidad/confusión. | Media |

---

### Sugerencia de orden de implementación

1. Manejo de errores y `ApiException` (`@RestControllerAdvice`, JSON estándar).  
2. Flag `enviarCorreos` aplicado en todos los envíos + revisión de límites SMTP.  
3. DevTools y productividad (hot reload estable).  
4. Seguridad básica (JWT y configuración sensible).  
5. Refactor de servicios / repositorios y documentación (`ARCHITECTURE.md`).  
6. Tests (unitarios + algunos de integración).  
7. Optimizaciones de rendimiento (DB, paginación) y métricas con Actuator.







cambair en

5. (Mejora extra) Evitar funciones sobre columnas

YEARWEEK(e.fecha_schedule, 1) hace que muchas veces el índice sobre fecha_schedule no se use bien.

Mejor cambiar la condición a un rango:

WHERE e.fecha_schedule >= :inicioSemana
  AND e.fecha_schedule <  :finSemana