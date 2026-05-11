# Microservicio de Ventas — FULLSTACK-VENTA

Microservicio REST desarrollado con **Spring Boot 3.5** que registra y gestiona las ventas del sistema ComputerStore. Persiste el historial de compras de cada usuario con sus detalles por producto. Forma parte del proyecto Full Stack III (DSY2205).

---

## Tecnologías

- Java 17
- Spring Boot 3.5 (Web, Data JPA, Validation, Actuator)
- Oracle Cloud (Wallet BBDDFS3)
- Lombok
- Docker
- JUnit 5 + Mockito

---

## Puerto

| Entorno | Puerto |
|---------|--------|
| Local   | `8082` |
| Docker  | `8082` |

---

## Endpoints disponibles

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/ventas` | Registra una nueva venta al confirmar el carrito |
| `GET` | `/api/ventas` | Lista todas las ventas (solo ADMIN) |
| `GET` | `/api/ventas/{id}` | Obtiene una venta por ID |
| `GET` | `/api/ventas/usuario/{usuarioId}` | Historial de compras de un usuario |
| `PATCH` | `/api/ventas/{id}/cancelar` | Cancela una venta existente |

---

## Estructura del proyecto

```
src/
├── main/java/com/fullstack_venta/fullstack_venta/
│   ├── controller/     VentaController.java
│   ├── service/        VentaService.java, VentaServiceImpl.java
│   ├── repository/     VentaRepository.java
│   ├── model/          Venta.java, DetalleVenta.java, VentaRequest.java
│   └── exception/      ResourceNotFoundException.java, GlobalExceptionHandler.java
└── test/java/com/fullstack_venta/fullstack_venta/
    ├── VentaServiceImplTest.java
    └── VentaControllerTest.java
```

---

## Ejemplo de request — registrar venta

`POST /api/ventas`

```json
{
  "usuarioId": 1,
  "items": [
    {
      "productoId": 10,
      "nombreProducto": "Laptop Gamer",
      "cantidad": 2,
      "precioUnitario": 999.99
    },
    {
      "productoId": 15,
      "nombreProducto": "Mouse Inalámbrico",
      "cantidad": 1,
      "precioUnitario": 29.99
    }
  ]
}
```

Respuesta `201 Created`:

```json
{
  "id": 1,
  "usuarioId": 1,
  "fecha": "2026-05-10T22:00:00",
  "total": 2029.97,
  "estado": "CONFIRMADA",
  "detalles": [...]
}
```

---

## Levantar en local

### 1. Requisitos previos

- Java 17+
- Maven 3.8+
- Carpeta `Wallet_BBDDFS3/` presente en la raíz del proyecto

### 2. Compilar

```bash
mvn clean package -DskipTests
```

### 3. Ejecutar

```bash
mvn spring-boot:run
```

La API queda disponible en: `http://localhost:8082/api/ventas`

---

## Levantar con Docker

```bash
# 1. Compilar primero (genera el .jar en target/)
mvn clean package -DskipTests

# 2. Levantar contenedor
docker compose up --build
```

> **Importante:** el `.dockerignore` no debe incluir `target/` para que Docker pueda copiar el `.jar`.

---

## Ejecutar pruebas unitarias

```bash
mvn test
```

Cobertura de tests incluida:

| Clase testeada | Tests |
|----------------|-------|
| `VentaServiceImpl` | registrar, carrito vacío, cálculo de total, listar, buscarPorId, buscarPorUsuario, cancelar |
| `VentaController` | POST 201, GET lista, GET por id, GET por usuario, PATCH cancelar, 404 |

---

## Variables de entorno (Docker)

| Variable | Valor |
|----------|-------|
| `SPRING_DATASOURCE_URL` | `jdbc:oracle:thin:@bbddfs3_tp?TNS_ADMIN=/app/wallet` |
| `SPRING_DATASOURCE_USERNAME` | `ADMIN` |
| `SPRING_DATASOURCE_PASSWORD` | `BBDD_fullstack2026` |

---

## Estados de una venta

| Estado | Descripción |
|--------|-------------|
| `CONFIRMADA` | Venta registrada exitosamente |
| `CANCELADA` | Venta cancelada mediante PATCH |
