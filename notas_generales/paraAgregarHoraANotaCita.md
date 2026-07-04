Este documento solo es para que cuando se pase el cambio de hora de nota, asignar las horas a todas las notas para que no esten en blanco, se puede borrar ya que se pase ese cambio:

SELECT * FROM request_evaluation.nota_cita ORDER BY id_nota DESC;

SELECT * FROM cita ORDER BY id_cita DESC;

// Para consultar la hora de nota y de cita
SELECT
    nc.id_nota,
    nc.hora AS hora_actual,
    c.fecha_creacion,
    DATE_FORMAT(c.fecha_creacion, '%H:%i') AS nueva_hora
FROM nota_cita nc
INNER JOIN cita c ON c.id_cita = nc.id_cita;

// Para actualizar la hora de la nota y se toma de la cita 
UPDATE nota_cita nc
INNER JOIN cita c ON c.id_cita = nc.id_cita
SET nc.hora = DATE_FORMAT(c.fecha_creacion, '%H:%i')
WHERE nc.hora IS NULL
   OR nc.hora = '';