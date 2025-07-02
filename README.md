# Taller: Patrón Publicador/Suscriptor con APIs y RabbitMQ

Este proyecto implementa una solución distribuida basada en el patrón **Pub/Sub** para el sistema de procesamiento de tareas de la empresa ficticia **StreamEdu**. Utiliza **Apache Camel sobre Spring Boot** para la lógica de integración y **RabbitMQ** como middleware de mensajería.

---

## 📌 Diagrama de Arquitectura Funcional

El sistema se compone de una **API REST** que actúa como **publicador** y de **dos servicios independientes** que actúan como **suscriptores**. La comunicación entre ellos se desacopla mediante un **exchange de tipo `fanout`** en RabbitMQ, lo que permite que cada tarea enviada sea procesada en paralelo por todos los servicios suscritos.

> 📎 Diagrama ilustrativo:

![Diagrama de Arquitectura](img/diagrama.png)

---

## 🛠️ Instrucciones para Ejecutar los Componentes

Sigue estos pasos para levantar y probar el ecosistema completo del proyecto.

### ✅ Prerrequisitos

- Java 11 o superior  
- Apache Maven  
- Docker (o una instalación local de RabbitMQ)

---

### 1️⃣ Levantar RabbitMQ

Abre una terminal y ejecuta el siguiente comando para iniciar un contenedor RabbitMQ con su panel de gestión:

```bash
docker run -d --hostname my-rabbit --name rabbit-pubsub \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:3-management
```

📍 Luego, accede a `http://localhost:15672` con las credenciales:

- **Usuario:** `guest`  
- **Contraseña:** `guest`

Ve a la pestaña **Exchanges** y crea manualmente un exchange de tipo `fanout` con el nombre:

```
entregas.tareas
```

> 🖼️ Referencias visuales:
- ![](img/rabbit-docker.png)
- ![](img/crear-exchange.png)
- ![](img/exchange.png)

---

### 2️⃣ Ejecutar los Servicios

Abre **tres terminales separadas**, una por cada componente del sistema:

#### 📣 Terminal 1 – Servicio de Notificaciones

```bash
cd servicio-notificaciones
mvn spring-boot:run
```

#### 🔍 Terminal 2 – Servicio de Análisis de Plagio

```bash
cd servicio-plagio
mvn spring-boot:run
```

#### 🚀 Terminal 3 – API Publicador

```bash
cd publicador-api
mvn spring-boot:run
```

> Espera a que las tres aplicaciones se inicien correctamente antes de continuar.

---

### 3️⃣ Probar el Sistema

Puedes utilizar `curl` o Postman para enviar una tarea al endpoint expuesto por el publicador.

```bash
curl -X POST http://localhost:8081/subir-tarea \
  -H "Content-Type: application/json" \
  -d '{
    "estudiante": "Juan Perez",
    "curso": "Integración de Sistemas",
    "archivo": "tarea1.docx",
    "fechaEnvio": "2025-06-13T14:22:00"
  }'
```

![Ejemplo con curl](img/curl.png)

---

## 📷 Evidencia de Funcionamiento

### 🔄 Publicador Enviando Mensaje

![Publicador](img/publicador.png)

---

### 📥 Suscriptores Recibiendo y Procesando el Mensaje

#### Servicio de Notificaciones

- ![](img/notificaciones-mensaje.png)
- ![](img/notificaciones-procesamiento.png)

#### Servicio de Análisis de Plagio

- ![](img/plagio-mensaje.png)
- ![](img/plagio-procesamiento.png)
