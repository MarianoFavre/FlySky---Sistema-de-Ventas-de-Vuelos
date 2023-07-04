INSERT INTO usuario(telefono, nombre_usuario, tipo_usuario)
VALUES (156453,'Miguel','CLIENTE'),
       (467538,'Carlos','AGENTE_DE_VENTAS'),
       (984866,'Juan', 'ADMINISTRADOR'),
       (666666,'Mariano','CLIENTE');

INSERT INTO vuelo(disponible, capacidad, aerolinea, fecha_hora_partida, fecha_hora_llegada, precio, origen, destino)
VALUES (TRUE,2,'Aerolineas Argentinas','2023-06-25 23:53:30','2023-06-25 23:53:30', 15000, 'Buenos Aires', 'Uruguay'),
       (FALSE,50,'Aerolineas Argentinas','2023-06-25 23:53:30','2023-06-25 23:53:30', 15000, 'Buenos Aires', 'Uruguay'),
       (FALSE,50,'Aerolineas Argentinas','2023-06-25 23:53:30','2023-06-25 23:53:30', 15000, 'Buenos Aires', 'Uruguay'),
       (TRUE,50,'Aerolineas Uruguayas','2023-06-25 23:53:30','2023-06-25 23:53:30', 15000, 'Buenos Aires', 'Uruguay');

INSERT INTO reserva(tipo_pago, usuario_id, vuelo_id, monto_pago, fecha_hora_reserva)
VALUES ('TARJETA_CREDITO',1,1, 15000, '2023-04-25 12:32:56');

INSERT INTO butaca(disponible, posicion, vuelo_id)
VALUES (TRUE, 'AE04', 1),
       (TRUE, 'AE05', 1),
       (TRUE, 'AE06', 1);