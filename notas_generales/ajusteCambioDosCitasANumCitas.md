Para cambir de dos_citas a num_citas se debe preguntar a Sebastian lo siguiente:

Al contar el total de citas de un file en algunos casos saldran de mas, ya que actualmente toma lo que se agrega en dos_citas el valor puede ser 1 o 2, si se cambia que tome de la nota el campo tiempo_sesion tomara valor de 1 a 4, eso tenemos que ver que puede que de negaticos ya que pueden tener 100 citas aprobadas y con este nuevo contador pueden salir que ya tomo 105 citas saldra un -5

para ver esa parte se tiene esta consutla que ya probe donde se ve si son dos citas y el tiempo que se invirtio

select nc.id_nota,c.id_cita,c.id_solicitud,nc.tiempo_sesion
from cita c 
join nota_cita nc on nc.id_cita= c.id_cita
where c.id_solicitud = 85


si se cambia para num_citas la conulsta quedar asi:

SELECT
    s.id_solicitud,
    s.fecha_inicio,
    s.cliente,
    s.telefono,
    s.email,
    s.terapeuta,
    ut.nombre,
    ut.correo_electronico,
    s.num_sesiones,
    COALESCE(t.sesiones_contadas, 0) AS sesiones_contadas,
    s.num_sesiones - COALESCE(t.sesiones_contadas, 0) AS sesiones_pendientes
FROM request_evaluation.solicitud_voc s
LEFT JOIN usuario ut
    ON ut.id_usuario = s.terapeuta
LEFT JOIN (
    SELECT
        c.id_solicitud,
        SUM(
            CASE
                WHEN nc.tiempo_sesion BETWEEN 0 AND 44 THEN 1
                WHEN nc.tiempo_sesion BETWEEN 45 AND 74 THEN 2
                WHEN nc.tiempo_sesion BETWEEN 75 AND 104 THEN 3
                WHEN nc.tiempo_sesion >= 105 THEN 4
                ELSE 0
            END
        ) AS sesiones_contadas
    FROM cita c
    INNER JOIN nota_cita nc
        ON nc.id_cita = c.id_cita
    WHERE c.no_show = 0
    GROUP BY c.id_solicitud
) t
    ON t.id_solicitud = s.id_solicitud
WHERE s.id_estatus_solicitud != 11
  AND s.num_sesiones > 0
  AND (
        s.num_sesiones - COALESCE(t.sesiones_contadas, 0)
      ) <= 10
ORDER BY s.cliente ASC;