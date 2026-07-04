SELECT
    s.id_solicitud,
    s.numero_de_caso,
    s.fecha_inicio,
    s.cliente,
    ts.nombre AS tipoSolicitud,
    ep.descripcion AS estatusPago,
    es.descripcion AS estatusSolicitud,
    ut.nombre AS terapeuta,
    s.fecha_nacimiento AS fechaNacimiento,
    s.direccion,
    s.apellidos,
    s.num_sesiones,
    s.num_schedules,
    s.sesiones_pendientes,
    COALESCE(ev.importantNotes, '') AS importantNotes,
    s.documento_1 AS MH_Billing_Intake_Form,
    s.fecha_doc_1 AS fecha_MH_Billing_Intake_Form,
    s.documento_2 AS Treatment_plan,
    s.fecha_doc_2 AS fecha_Treatment_plan,
    s.sexo
FROM request_evaluation.solicitud_voc s
LEFT JOIN tipo_solicitud ts
    ON ts.id_tipo_solicitud = s.id_tipo_solicitud
LEFT JOIN estatus_pago ep
    ON ep.id_estatus_pago = s.id_estatus_pago
LEFT JOIN estatus_solicitud es
    ON es.id_estatus_solicitud = s.id_estatus_solicitud
LEFT JOIN usuario ut
    ON ut.id_usuario = s.terapeuta
LEFT JOIN (
    SELECT
        id_solicitud,
        GROUP_CONCAT(descripcion SEPARATOR ', ') AS importantNotes
    FROM evento_solicitud_voc
    WHERE tipo IN ('Important', 'Suicide')
    GROUP BY id_solicitud
) ev
    ON ev.id_solicitud = s.id_solicitud
WHERE s.id_estatus_solicitud <> 11
  AND s.fecha_inicio >= '2023-01-02'
  AND s.fecha_inicio < '2025-11-01'
ORDER BY s.fecha_inicio DESC;