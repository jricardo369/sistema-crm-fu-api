## Limpiar caché y espacio en Tomcat (Linux)

### 1. Parar Tomcat

- Con systemd:
	```bash
	sudo systemctl stop tomcat
	# o tomcat9 / tomcat8 según el servicio
	```
- Con scripts de Tomcat:
	```bash
	cd /opt/tomcat/bin
	sudo ./shutdown.sh
	```

> Ajusta `/opt/tomcat` al CATALINA_HOME real de tu servidor.

### 2. Limpiar carpetas temporales y de trabajo

Estas carpetas son cache/temporales; Tomcat las regenera solo.

```bash
cd /opt/tomcat
sudo rm -rf temp/*
sudo rm -rf work/*
```

### 3. Limpiar logs viejos de Tomcat

```bash
cd /opt/tomcat/logs
ls -lh

# Borrar logs de más de 14 días
sudo find . -type f -name "*.log*" -mtime +14 -delete

# Si catalina.out es muy grande, truncarlo
sudo truncate -s 0 catalina.out
```

### 4. Eliminar aplicaciones que ya no se usan

```bash
cd /opt/tomcat/webapps
ls -lh

# Borrar apps de prueba o viejas (solo si estás seguro)
sudo rm -rf docs examples host-manager manager ROOT-old* app-vieja*
```

### 5. Arrancar de nuevo Tomcat

```bash
sudo systemctl start tomcat
# o
cd /opt/tomcat/bin
sudo ./startup.sh
```

---

## Limpiar espacio general en Linux

### 1. Ver qué está ocupando espacio

```bash
df -h                       # uso de discos
sudo du -sh /* 2>/dev/null | sort -h | tail -n 15
```

### 2. Borrar archivos temporales

```bash
# /tmp (temporales de apps ya cerradas)
sudo find /tmp -type f -mtime +3 -delete

# /var/tmp (temporales de larga duración)
sudo find /var/tmp -type f -mtime +30 -delete
```

### 3. Limpiar logs del sistema

```bash
cd /var/log
sudo find . -type f -name "*.log*" -mtime +14 -delete
```

Si usas `systemd` (journald):

```bash
sudo journalctl --vacuum-size=500M
# o
sudo journalctl --vacuum-time=14d
```

### 4. Limpiar caché de paquetes

#### Debian/Ubuntu

```bash
sudo apt-get clean          # limpia cache .deb
sudo apt-get autoremove     # quita paquetes ya no usados
```

#### CentOS/RHEL

```bash
sudo yum clean all
```

### 5. Revisar kernels antiguos (con cuidado)

```bash
uname -r                      # kernel actual (NO borrar este)
dpkg --list 'linux-image*'    # listar kernels (Debian/Ubuntu)
```

Desinstala manualmente solo los kernels muy viejos que ya no se usan.

### 6. Limpiar cachés de usuario

```bash
du -sh /home/* 2>/dev/null
du -sh /root   2>/dev/null

# Ejemplo: limpiar caché de un usuario
sudo du -sh /home/USUARIO/.cache
sudo rm -rf /home/USUARIO/.cache/*
```

### 7. (Opcional) Limpiar Docker

Si usas Docker:

```bash
docker system df
docker image prune -a       # imágenes no usadas
docker container prune      # contenedores parados
docker volume prune         # volúmenes no usados
```

> Siempre revisa qué se va a borrar antes de ejecutar comandos `rm -rf`.

