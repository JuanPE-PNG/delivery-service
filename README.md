# Delivery Service

Microservicio de entregas para el proyecto DS3. Este servicio maneja la creación y gestión de entregas después de que se complete un pago exitoso.

## Características

- Creación de entregas con dirección y ciudad del cliente
- Validación de JWT tokens
- Gestión de estados de entrega (PENDIENTE, ASIGNADA, COMPLETADA)
- Endpoints para repartidores y administradores
- Asignación de repartidores a entregas

## Endpoints

### POST /delivery/create
Crea una nueva entrega para un pago completado.
- Requiere: Bearer token
- Body: `DeliveryRequest` (paymentId, customerAddress, customerCity)

### GET /delivery/pending
Obtiene todas las entregas pendientes.
- Requiere: Bearer token (Repartidor o Administrador)

### PUT /delivery/update-status
Actualiza el estado de una entrega.
- Requiere: Bearer token (Repartidor o Administrador)
- Body: `UpdateDeliveryStatusRequest` (deliveryId, newStatus)

### GET /delivery/my-deliveries
Obtiene las entregas del usuario autenticado.
- Requiere: Bearer token

### PUT /delivery/assign-driver/{deliveryId}
Asigna un repartidor a una entrega.
- Requiere: Bearer token (Administrador)
- Parámetros: deliveryId (path), driverId (query)

## Estados de Entrega

- **PENDIENTE**: Entrega creada, esperando asignación
- **ASIGNADA**: Repartidor asignado
- **COMPLETADA**: Entrega finalizada

## Configuración

- Puerto: 8085
- Base de datos: PostgreSQL
- Registro en Eureka Server
- Configuración centralizada con Spring Cloud Config

## Dependencias

- Spring Boot 3.2.5
- Spring Cloud 2023.0.1
- Spring Data JPA
- PostgreSQL
- JWT para autenticación
- Eureka Client 