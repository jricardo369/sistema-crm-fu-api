# CRM Files
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
Para iniciar el proyecto con un perfil específico:
```sh
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
O para producción:
```sh
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Endpoints principales
- `/autenticaciones` - Autenticación de usuarios
- `/usuarios` - Gestión de usuarios
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


