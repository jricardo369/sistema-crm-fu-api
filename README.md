# CRM FILES 
Proyecto Java Spring Boot para gestión de requests files y procesos CRM.

## Requisitos previos
- Java 8 o superior
- Maven
- MySQL

## Instalación y configuración
1. Clona el repositorio:
   ```sh
   git clone <url-del-repositorio>
   ```
2. Configura los archivos de propiedades en `src/main/resources/`:
   - `application.properties` (común)
   - `application-dev.properties` (desarrollo)
   - `application-prod.properties` (producción)
   - `application-local.properties` (local)

## Ejecución
Para iniciar el proyecto
```sh
mvn spring-boot:run 
```

## Endpoints principales
- `/autenticaciones` - Autenticación de usuarios
- `/usuarios` - Gestión de usuarios
- `/solicitudes` - Gestión de solicitudes de CRM
- `/swagger-ui.html` - Documentación de la API

## Pruebas
Para ejecutar las pruebas:
```sh
mvn test
```

## Contribución
Las contribuciones son bienvenidas. Por favor, abre un issue o envía un pull request.

## Licencia
Este proyecto está bajo la licencia MIT.


# VER HERRAMIENTAS

## Acceder Swagger: 
http://localhost:18080/pe_crm_api/swagger-ui/

## Acceder Actuator
https://crm-familiasunidasla.com/pe_crm_api/actuator



# INDICES CREADOS PARA CONSULTA DE DISPONIBILIDAD

  
-- Para disponibilidad_usuario (filtros por fecha y joins)
``` SQL
CREATE INDEX idx_disp_fecha_usuario_tipo_hora
ON disponibilidad_usuario (fecha, id_usuario, tipo, hora);
```

-- Para evento_solicitud (NOT EXISTS / LEFT JOIN)
```SQL
CREATE INDEX idx_evento_fecha_usuario_tipo_hora_estatus
ON evento_solicitud (fecha_schedule, usuario_schedule, tipo_schedule, hora_schedule, estatus_schedule);
```

-- Opcional: para filtros por rol/estatus en usuario
```SQL
CREATE INDEX idx_usuario_rol_estatus
ON usuario (id_rol, estatus);
```


