Query para que de solicitudes genere un pago si ya fue pagada la cita

INSERT INTO pagos_voc (id_cita, monto, id_usuario, fecha,folio,tipo_pago)
SELECT 
    c.id_cita, 
    c.amount, 
    1, 
    IFNULL(c.fecha_pagado, NOW()),
    0,
    0
FROM cita c
LEFT JOIN pagos_voc p ON p.id_cita = c.id_cita
WHERE c.pagado = 1 
  AND p.id_cita IS NULL;