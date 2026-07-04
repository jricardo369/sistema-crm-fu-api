HECHOS
Para template agregar due date, past due date, contar los que tienen due date atrasadoagregar evento de cuando se sube archivo después de cerrado y ver tile que cuente cuantos files se modificaron después de cerrado
No show mostrar los files y quien dio no show y su motivo pero solo si no tiene un reschedule
Agregar posibilidad de pagar varias citas en VOC con un check al inicio de cada renglón
En reporte de pagos, en caso que el file este cerrado no mostrarlo en el reporte, al ejecutar la función de close forzado eso sfiels no contar que deben si no que ya se pago completo
Agregar gender en migration
Ver filtros en reporte de pagos
Agregar filtro de file en reporte pagos
en el filtro de cliente de pagos, que busque por cliente pero por su nombre y/o apellidos
Agregar gender en reporte voc excel
Agregar uno adicional "Sessions without notes", contar las citas que ya pasaron de la fecha actual y no tiene nota
En el contador de schedules solo cuente las que tienen nota, a partir de noviembre 
en lugar de schedules seria completed sessions
En lugar de session seria approbed sessions

the appointment date was changed    cambiarlo por que esta con is en lugar de was 
Subir el tamaño de dirección en solicitud voc y solicitud
En la consulta de canceled schedules no tomar los de the appointment date was changed, Re-Scheduled , este también es aplicado para no show
Agregar botón de limpiar fechas en reporte firmas abogados
Ver que los idabogados de las solicitudes, guardar su email en el campo email_abo_sel
agregar tile donde muestre total de files creados
Agregar tile nuevo de cancel schedule por reasignación
mostrar lost files y su detalle de quien fue
Al seleccionar un abogado guardar el id abogado y el mail que se seleciono, y quede asociado al file
Agregar botón de nuevo abogado y nuevo correo abogado existente
ver que al entrar a solicitud voc no cargue scales, estatus que no son de voc
Add an email to an existing contact
Ver al save en voc note cierre ventana y te mande mensaje de guardado correcto
ver al dar en close en note voc te muestre mensaje que si realmente deseas cerrar por que perderás la info
Ver que en calendario de voc no mostrar mensaje de “Attende” y “No atiende yet” y mostrar si ya tiene nota o no
Quitar filtro de therapist a terapeuta
Quitar columna de therapist a terapeuta
Crear tabla de mails de abogado y poder seleccionar uno 
En el cuadro de calendario para terapeutas mostrar horario, cliente y si ya tiene nota o no


Dar de alta abogados en el file pero solo backoffice
Agregar list drop down de posibles opciones al no show, cancel  y reject, en lost agregar motivo igual
mostrar en estatus pago si es partially mostrar cuanto debe por ejemplo si son 700 y pago 200 poner 200/700
Agregar filtro de terapeuta en files voc
revisar validación de vacío en nuevo campo lawyer
al agregar pago refresh al file
ver quien borra scales y se guarde en el log
Agregar en calendario en not attended yet en color naranja fuerte
cuando agregues abogado si esta repetido el abogado en correo
Asessed
Ver que cuando sea voc y vea abobados no mande a users
Agregar flag de fin template
Llenar la tabla de lawyers a partir del excel
Revisar filtro de important también trae los no show no debe hacer eso
Ver el tema de no show y de reject 
Revisar botón de reste password o que cuando se cambie pass limpie numero de intentos fallidos y cambie a estatus activo
Ver que el template ya no vea los botones si ya envío a siguiente proceso
Cuando se agrega scale agregar en eventos
si es fecha anterior marcar como atendida la cita automáticamente tanto first int y scales
Cambiar el texto del botón “”Send to case manager”  por “Send to Interview”
Ver cuando guardas que no lo quite el assigned a case manager y salce interview
Ver tema de número de solicitudes atendidas para Octavio deben ser 75 del 9 al 20 de junio
Ver tema de no poder enviar siguiente proceso si no se acaban las entrevistas, y agregar otro botón para hacer esa función 
Cambiar de lugar el lost
ver tema de que no valide el num_de entrevistas si no el de terminar scales y entrevista una
Quitar won y dejar solo lost y si le da clic se cierra
el important quitarlo en columna y agregarlo en el mismo estatus pero un circulo rojo
Agregar permiso de reporte law firm files a Eduardo
Se dejara la columna Interview Review
si el clinician es case manager al dar clic en finish cambiar estatus y terminar asignación de case manager
Generar modulo de lawyers como usuarios en admin, los datos serian, Law firm, Lawyer name, Emails, Phone, sinónimos y fecha alta, aparecerá un select en el file y seleccionar el abogado
Ver el método de salir() en dialogo-login.component lo comenté por que marca error
Agregar mejora en voc, al dar no show que te pida motivo y lo guarde en log
Revisar assging template no lo vea cualquiera
Revisar nuevamente cuando se agrega pago y no cambia de estatus a completo
2025-09-08
- [ ] mensaje sms con podium
El cupón debe estar ligado a bogados
Solo un cupón por abogado, y es a nuevos, y se aplica el cupón a un file, al seleccionar que aplica el cupón se debe marcar al abogado que ya uso su cupón
Ver tema de cupones, ESTIMARLO
Ver tema de borrar la nota y la cita, debe sumar nuevamente a schedules pendientes, esto es una MEJORA
Posibilidad de descargar excel de files VOC, MEJORA
2025-10-02
- [ ] Ver que cuente bien en el reporte dash
- [ ] agregar columnas al reporte de files por fecha, agregar columnas de lost, actives y no show
Ver tema de seleccionar dos correos de abogados
Al momento de pagar masivo ver validacion si ya esta en estatus pagado no hacerlo
Agregar columna de creation schedule
2025-10-15
todos los fieles voc que no tengan A al inicio cambiarlos a estatus cerrado
Ver tema de error en pro a agregar otro mail
Ver reporte de usuarios terapeutas ver que este ok
Ver tema de sesiones en voc
Ver filtros de charges voc
Agregar sincronizar en sesiones de voc
Ver tema de borrar un abogado
Quitar generar file voc a partir de un file migración
2025-11-04
agregar el gender en voc charges
ajustar en bd los rates atrasados, eduardo mandara mail
2025-12-15
Cambiar reporte de files de abogados
Revisar en noviembre el reporte de firmas si funciona bien eliminar lo comentado de código anterior
Revisar el reject para scales, de una vez para todos, que limpie bien los datos de solicitud para cada interview
Ver en usuario editor si un file esta en Recieved no puede enviar el file, cambiar para que pueda mandarlo no importa si esta en estatus recieved, reviewng, interview
Quitar campo importante de código web y java y de base de datos
Ver con edgar como asignar responsable de entrevistadores y editor, por que todos pueden tener el file al mismo tiempo
Revisar file 6134 para tema de que esta en recieved pero no se asigno Editor
Ver cuando den no show como hacer que no pierda la asignación de otros usuarios
Que al no show y reject limpie asignacion_int_sc
Borrar metodo obtenerDisponbilidadRolsB de DisponibilidadUsuarioRepository en Feb 20, ya que se halla probado lo nuevo de estado en disponibilidad
No deja borrar las fechas en files voc
No guarda el birth date en file voc
Al ejecutar eventos, solo ver "Payment registration" pagos si eres backoffice
Quitar el que ponga imporante cuando es no show
Al dar no show en clinician como case manager, no limpia el assigned_clinician y usuario_interview de solicitud
Ver tema de no tomar los lost en contador de files en dashboard
Ver tema de jobs dejarlos todo en la misma tabla y al desactivarlos también sea en esa misma
2026-01-15
Recordatorio de VOC en caso que se acerquen que se acaben citas mandar mail
Agendar tercera llamada
Al seleccionar el clinician debe seleccionar cuando sera la cita en una pantalla similar al asignar las otras citas, y ya ahora si sera la tercera cita
Si se le asigna clinician a case manager ya no existe la tercer llamada
Agregar botón unpaid en pagos solo a usuario eduardo
Agregar en cofia el admin email voc
agregar en admin users en caso de ser voc poder seleccionar admin voc

Después de las tres citas mandar a Juan
Poner validación al enviar mensaje que si esta en pro mande si no no mande mensaje
En el reporte de comparición de años los numero de files debe todos menos los que sean lost
En el listado de files en interviewer review cambiar por Clinician Appointment poner la cita de clinician en caso que no aplique tercera llamada poner N/A
Ver tema de descargar layout de dispo en pro
Ver por que marca error al descargar layout dispo- fue tema del nombre del archivo
Cambiar titulo de Interviewer Appointment por Case Manager Appointment
al filtrar por teléfono en files dejar los guíñese y quitárselos al buscar
Revisar el reopen
Agregar State en filtros de reporte files
al filtrar por teléfono en files quitar los espacios 
El input de assigned clinician se oculte si el case manager es clinician

Si cancelan template debe limpiarse responsable y asignárselo a juan
Si editor termino se va a ready on draft y se va a juan
Al cancelar clinician no limpia el usuario interview de solicitud
Si no se han terminado las tres citas no se debe poder pasar a estatus Reviewer
Al agendar cita a los entrevistadores no cambiar responsable
Después de que editor termina mandar a Juan


Ajustes 2025-10-20
Agregar campo a tabla solicitud fin_asg_clnc
Se agrego nuevo servicio con lógica para asignación clinician
Se agrego nuevo campo de clinician en la consulta de files
Agregar nuevo valor de ADMIN-VOC en la tabla config
Agregar columna nueva en usuario que se llama unpaid_voc_flag 
Agregar columna nueva en tarea_programada se llama activo
Quitar los jobs de config, ya que se cambiaron a tareas programadas
Renombrar descripción por titulo y agregar subtitulo en la tabla tareas_programadas
Subir el html de correo de voc de ending sessions
Revisar para ajuste clinician
* Cancelar cita clinician desde usuario clinician
* Cancelar cita clinician desde usuario no clinician
* Terminar preso desde clinician "Finish review"
* Dar siguiente proceso ya que acabe clinician
* Dar siguiente proceso y que se halla asignado primero clinician, marcar error si se selecciona un clinician si es case manager


Revisar el no show de case manager
Cancelacion de citas debe mandar mail cancelando y ver si se puede cancelar la cita de su calendario
En correoUs.enviarCorreoNoShow agregar que sea correo con cancelación 
subir nuevo correo de cancelar cita
Revisar si se usa este template email-recuperacion-pwd.html
subir version nueva de app-confirmacion-message
Agregar que al revisor ver como contarle cuales son sus solicitudes atendidas
En caso de no show o cancelación se debe enviar cancelación por mail a cliente?, ya revise con edgar y si


ver tema de buscar en payments por fecha y que sea orden secuencial
cambiar Intake form por MH Billing Intake Form 
cambiar nombre therapist a psychotherapist
en license validity cambiar el formato fecha por el de US