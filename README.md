https://docs.google.com/document/d/1AyjgVFN7BtOZ5Ynqh8-1JGjacLZ4tIuhhpu7EVwjGkI/edit

# **<center> Sistema de ventas de vuelos </center>**

## **Nombre de la unidad de negocio: FlySky - Sistema de Ventas de Vuelos**

## <u>Unidad de negocio:</u>

FlySky es una agencia de viajes especializada en la venta de boletos de avión. Con el objetivo de mejorar la eficiencia operativa y brindar una experiencia de reserva más fluida y conveniente para los clientes, FlySky ha decidido desarrollar un sistema de ventas de vuelos personalizado.

El sistema de ventas de vuelos de FlySky se diseñará para agilizar el proceso de reserva y ofrecer una amplia gama de opciones de vuelo a los clientes. El sistema permitirá a los usuarios, tanto a los agentes de ventas como a los clientes finales, acceder a una base de datos actualizada de vuelos, que incluirá información detallada sobre horarios, precios, aerolíneas, aeropuertos de origen y destino, entre otros datos relevantes.

## <u>Principales características del sistema de ventas de vuelos:</u>

**Gestión de inventario de vuelos:** El sistema proporcionará una interfaz intuitiva y fácil de usar para que los usuarios puedan buscar y explorar los vuelos disponibles. Mostrará información actualizada sobre los vuelos, incluyendo horarios, precios, disponibilidad de asientos, aerolíneas asociadas y opciones de conexión.

**<u>User Story 1:</u>** Como usuario, quiero poder ver la lista de vuelos disponibles con su información detallada, como horarios, precios y aerolíneas, para poder elegir el vuelo que mejor se adapte a mis necesidades.

**Proceso de reserva de boletos:** El sistema permitirá a los usuarios seleccionar y reservar vuelos de manera rápida y sencilla. Guiará a los usuarios a través de un proceso de reserva paso a paso, solicitando la información necesaria, como nombres de pasajeros, fechas de viaje, preferencias de asientos y detalles de contacto.

**<u>User Story 2:</u>** Como usuario, quiero poder realizar una reserva de vuelo, ingresando los detalles requeridos, como nombres de pasajeros, fechas de viaje y preferencias de asientos, para asegurarme de tener un asiento reservado en el vuelo deseado.

**Integración de pagos seguros:** El sistema contará con un módulo de pago seguro que admitirá diversas opciones de pago, como tarjetas de crédito, transferencias bancarias y servicios de pago en línea. Los usuarios podrán realizar transacciones de forma segura y recibir confirmaciones de pago inmediatas.

**<u>User Story 3:</u>** Como usuario, quiero poder realizar pagos en línea de forma segura utilizando diferentes métodos de pago, como tarjetas de crédito o transferencias bancarias, para completar mi reserva de vuelo de manera conveniente y confiable.

**Gestión de clientes:** El sistema permitirá a FlySky administrar y mantener un registro actualizado de los datos de los clientes. Esto incluirá información personal, historial de reservas, preferencias de viaje y detalles de contacto. Con esta funcionalidad, FlySky podrá brindar un servicio personalizado, promociones especiales y recomendaciones basadas en las preferencias de cada cliente.

**<u>User Story 4:</u>** Como agente de ventas, quiero poder acceder al historial de reservas y preferencias de viaje de un cliente, para ofrecerle un servicio personalizado y promociones relevantes.

**Generación de informes y análisis:** El sistema proporcionará herramientas de generación de informes y análisis que permitirán a FlySky obtener una visión clara del rendimiento del negocio. Estos informes mostrarán estadísticas importantes, como el número de ventas diarias, ingresos generados, destinos populares y tendencias de reserva, lo que permitirá a la empresa tomar decisiones estratégicas informadas.

**<u>User Story 5:</u>** Como administrador del sistema, quiero poder generar informes diarios de ventas que muestren el número de ventas realizadas y los ingresos generados, para analizar el rendimiento del negocio y tomar decisiones basadas en datos.

El sistema de ventas de vuelos de FlySky tiene como objetivo mejorar la experiencia del cliente al simplificar el proceso de reserva de boletos, ofrecer opciones de vuelo más completas y garantizar transacciones seguras. Se desarrollará una API RESTful en Java para implementar estas funcionalidades. Al utilizar esta API, FlySky busca fortalecer su posición en el mercado de agencias de viajes y brindar un servicio excepcional a sus clientes, superando sus expectativas y fomentando relaciones comerciales duraderas.
------------------------------------------------------------------------------------------------------------------------'

Mediante Spring Profile (application.yml) utilizamos H2 para el entorno de test y MySql para producción realizando una precarga con un archivo data.sql