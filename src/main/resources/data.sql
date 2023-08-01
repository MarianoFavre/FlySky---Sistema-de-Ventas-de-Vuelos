INSERT INTO usuario(telefono, nombre_usuario, tipo_usuario)
VALUES (156453,'Miguel','CLIENTE'),
       (467538,'Carlos','AGENTE_DE_VENTAS'),
       (984866,'Juan', 'ADMINISTRADOR'),
       (666666,'Mariano','CLIENTE');

--Sería recomendable agregar avion_id (relacion 1 a 1 con vuelo) en lugar de capacidad y sacar precio
INSERT INTO vuelo(numero_vuelo, capacidad, aerolinea, fecha_hora_partida, fecha_hora_llegada, precio, origen, destino)
VALUES (666,156,'Aerolineas Argentinas','2050-07-25 08:00:00','2050-07-25 08:45:00', 15000, 'Buenos Aires', 'Uruguay'),
       (578,156,'Aerolineas Argentinas','2050-07-25 08:00:00','2050-07-25 08:45:00', 15000, 'Buenos Aires', 'Uruguay'),
       (934,156,'Aerolineas Argentinas','2023-07-25 08:00:00','2023-07-25 08:45:00', 15000, 'Buenos Aires', 'Uruguay'),
       --(125,156,'Aerolineas Uruguayas',DATEADD('MINUTE',  -9, CURRENT_TIMESTAMP), NOW(), 15000, 'Buenos Aires', 'Uruguay');
       --(125,156,'Aerolineas Uruguayas',DATEADD('DAY',  1, CURRENT_TIMESTAMP), NOW() + 1 , 15000, 'Buenos Aires', 'Uruguay');
       (125,156,'Aerolineas Uruguayas',DATEADD('HOUR',  1, CURRENT_TIMESTAMP), DATEADD('MINUTE',  105, CURRENT_TIMESTAMP) , 15000, 'Buenos Aires', 'Uruguay');

INSERT INTO reserva(tipo_pago, usuario_id, vuelo_id, monto_pago, fecha_reserva)
VALUES ('TRANSFERENCIA_BANCARIA',1,1, 15000, '2023-04-25 13:45:12');

--Sería recomendable agregar la clase, precio, pasajero_id
--AIRBUS A320 200
INSERT INTO butaca(disponible, posicion, vuelo_id, reserva_id, nombre_pasajero)
VALUES (FALSE, 'A1', 1, 1,'Miguel'),
       (TRUE, 'B1', 1, null, null),
       (TRUE, 'C1', 1, null, null),
       (TRUE, 'D1', 1, null, null),
       (TRUE, 'A2', 1, null, null),
       (TRUE, 'B2', 1, null, null),
       (TRUE, 'C2', 1, null, null),
       (TRUE, 'D2', 1, null, null),
       (TRUE, 'A3', 1, null, null),
       (TRUE, 'B3', 1, null, null),
       (TRUE, 'C3', 1, null, null),
       (TRUE, 'D3', 1, null, null),
       (TRUE, 'A5', 1, null, null),
       (TRUE, 'B5', 1, null, null),
       (TRUE, 'C5', 1, null, null),
       (TRUE, 'D5', 1, null, null),
       (TRUE, 'E5', 1, null, null),
       (TRUE, 'F5', 1, null, null),
       -----------------------------
       (TRUE, 'A28', 1, null, null),
       (TRUE, 'B28', 1, null, null),
       (TRUE, 'C28', 1, null, null),
       (TRUE, 'D28', 1, null, null),
       (TRUE, 'E28', 1, null, null),
       (TRUE, 'F28', 1, null, null);
