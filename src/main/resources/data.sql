INSERT INTO usuario(telefono, nombre_usuario, tipo_usuario)
VALUES (156453,'Miguel','CLIENTE'),
       (467538,'Carlos','AGENTE_DE_VENTAS'),
       (984866,'Juan', 'ADMINISTRADOR'),
       (666666,'Mariano','CLIENTE');

INSERT INTO vuelo(numero_vuelo, disponible, capacidad, aerolinea, fecha_hora_partida, fecha_hora_llegada, precio, origen, destino)
VALUES (666,TRUE,3,'Aerolineas Argentinas','2023-06-25 23:53:30','2023-06-25 23:53:30', 15000, 'Buenos Aires', 'Uruguay'),
       (578,FALSE,50,'Aerolineas Argentinas','2023-06-25 23:53:30','2023-06-25 23:53:30', 15000, 'Buenos Aires', 'Uruguay'),
       (934,FALSE,50,'Aerolineas Argentinas','2023-06-25 23:53:30','2023-06-25 23:53:30', 15000, 'Buenos Aires', 'Uruguay'),
       (125,TRUE,50,'Aerolineas Uruguayas','2023-06-25 23:53:30','2023-06-25 23:53:30', 15000, 'Buenos Aires', 'Uruguay');

INSERT INTO butaca(disponible, posicion, vuelo_id)
VALUES (FALSE, 'AE04', 1),
       (TRUE, 'AE05', 1),
       (TRUE, 'AE06', 1);

INSERT INTO reserva(tipo_pago, usuario_id, vuelo_id, monto_pago, fecha_reserva, butaca_id)
VALUES ('TRANSFERENCIA_BANCARIA',1,1, 15000, '2023-04-25', 1);