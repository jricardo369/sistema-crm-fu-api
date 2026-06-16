Aquí van el 1 y el 2, ya aterrizados a este proyecto.

## 1. Matriz De Cambios

| Archivo | Qué depende de `evento_solicitud.fecha` | Tipo de cambio esperado | Riesgo |
| --- | --- | --- | --- |
| [EventoSolicitudEntity.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/domain/EventoSolicitudEntity.java#L21) | `fecha` está mapeado como `Date` con `@Temporal(TIMESTAMP)` | Cambio obligatorio de modelo JPA: `Date -> String` o conversor custom | Alto |
| [EventoSolicitudService.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/EventoSolicitudService.java#L80) | Hace `setFecha(Date)`, parsea con `cadenaAFecha(...)`, formatea fechas para descripciones | Rehacer creación, parseo y validación de eventos | Alto |
| [SolicitudService.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/SolicitudService.java#L883) | Consume `evSol.getFecha()` y la manda a `formatearFechaUS(...)` en cancelaciones y no-shows | Ajuste de consumidores: dejar de asumir `Date` | Medio-Alto |
| [EventoSolicitudJpa.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/port/out/jpa/EventoSolicitudJpa.java#L20) | Tiene filtros y ordenamientos sobre `fecha`, además de métodos que devuelven `EventoSolicitudEntity` | Reescritura de queries o normalización estricta del formato texto | Alto |
| [SolicitudJpa.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/port/out/jpa/SolicitudJpa.java#L140) | Usa `e.fecha BETWEEN ?1 AND ?2`, `ORDER BY fecha DESC`, `GROUP BY e.fecha` | Reescritura de reportes nativos | Alto |
| [querySolicitudes.txt](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/resources/querys/querySolicitudes.txt#L24) | Subconsultas que toman la última cita con `ORDER BY fecha DESC` | Cambiar lógica de “último evento” si `fecha` deja de ser temporal real | Alto |
| [queryReporteDash.txt](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/resources/querys/queryReporteDash.txt#L72) | `WHERE e.fecha BETWEEN :fechai AND :fechaf` | Reescritura obligatoria; es el caso más sensible | Muy alto |
| [queryDetallesDash.txt](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/resources/querys/queryDetallesDash.txt#L39) | Subconsultas con `ORDER BY ... fecha DESC` | Ajuste obligatorio para seguir obteniendo la cita más reciente | Alto |
| [ReporteSolsDeUsuario.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/domain/ReporteSolsDeUsuario.java#L1) | La proyección ya expone `getfecha()` como `String` | Cambio menor o ninguno | Bajo |
| [EventoSolicitud.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/domain/EventoSolicitud.java#L1) | El DTO ya maneja `fecha` como `String` | Cambio menor o ninguno | Bajo |

Notas puntuales sobre la matriz:

- El punto más delicado no es solo SQL; es que hoy [EventoSolicitudEntity.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/domain/EventoSolicitudEntity.java#L26) sigue obligando a Hibernate/JPA a hidratar `fecha` como `Date`.
- [SolicitudService.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/SolicitudService.java#L883) y [SolicitudService.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/SolicitudService.java#L1049) no escriben directo en BD, pero sí se rompen cuando reciben `EventoSolicitudEntity` con `fecha` ya no temporal.

## 2. Qué Consultas Se Rompen Primero

Si cambias la columna en la BD antes de adaptar Java, el orden práctico de ruptura sería este:

1. [EventoSolicitudEntity.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/domain/EventoSolicitudEntity.java#L26) + cualquier `SELECT * FROM evento_solicitud`

	Primer impacto real: hidratación JPA. Antes incluso de hablar de reportes, los métodos de [EventoSolicitudJpa.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/port/out/jpa/EventoSolicitudJpa.java#L24) que regresan `EventoSolicitudEntity` quedan expuestos a fallos de mapeo o conversiones inconsistentes.

2. [queryReporteDash.txt](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/resources/querys/queryReporteDash.txt#L72)

	`WHERE e.fecha BETWEEN :fechai AND :fechaf`

	Esta es la más frágil. Si `fecha` pasa a texto:

	- deja de filtrar correctamente,
	- compara lexicográficamente,
	- depende de que el formato sea exactamente `yyyy-MM-dd HH:mm:ss`.

	Si cambia un solo formato, el dashboard da totales malos de inmediato.

3. [SolicitudJpa.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/port/out/jpa/SolicitudJpa.java#L140) y [SolicitudJpa.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/port/out/jpa/SolicitudJpa.java#L151)

	También usan `e.fecha BETWEEN ?1 AND ?2`.

	Estos reportes de usuario se rompen muy pronto por la misma razón que el dashboard.

4. [querySolicitudes.txt](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/resources/querys/querySolicitudes.txt#L24) y [queryDetallesDash.txt](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/resources/querys/queryDetallesDash.txt#L39)

	`ORDER BY fecha DESC`

	Aquí el riesgo principal no siempre es excepción, sino resultado incorrecto:

	- puede elegir una cita vieja como si fuera la más nueva,
	- puede desordenar historial,
	- puede mostrar “última cita” equivocada.

5. [EventoSolicitudJpa.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/port/out/jpa/EventoSolicitudJpa.java#L29)

	`ORDER BY fecha DESC` en `obtenerEventosDesolicitudSinPagos(...)`

	Esto rompe el orden cronológico del historial del expediente.

6. [EventoSolicitudJpa.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/port/out/jpa/EventoSolicitudJpa.java#L63)

	`fecha LIKE %?1%`

	Esta probablemente no “truena” primero. De hecho, como texto puede seguir funcionando, pero queda más frágil y dependiente del formato exacto guardado.

## Conclusión Operativa

Si haces el cambio en BD sin tocar código, lo primero que esperaría es:

- fallos o comportamiento inconsistente en lecturas JPA de [EventoSolicitudEntity.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/domain/EventoSolicitudEntity.java#L24)
- dashboards con conteos erróneos por `BETWEEN`
- últimas citas mal calculadas por `ORDER BY fecha DESC`
- consumidores de servicio como [SolicitudService.java](/Users/joser.vazquez/git/sistema-crm-fu-api/src/main/java/com/cargosyabonos/application/SolicitudService.java#L883) fallando al formatear `Date`
