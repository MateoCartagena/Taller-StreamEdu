# Taller: Patrón Publicador/Suscriptor con APIs y RabbitMQ

[cite_start]Este proyecto implementa una solución distribuida basada en el patrón Pub/Sub para el sistema de procesamiento de tareas de la empresa ficticia StreamEdu[cite: 5, 6]. [cite_start]Utiliza Apache Camel sobre Spring Boot para la lógica de integración y RabbitMQ como middleware de mensajería[cite: 4].

## [cite_start]1. Link al Repositorio de Código Fuente [cite: 42]

* **Repositorio:** `https://github.com/tu-usuario/tu-repositorio`

---

## [cite_start]2. Diagrama de Arquitectura Funcional [cite: 46]

El sistema se compone de una API REST que actúa como publicador y dos servicios independientes que actúan como suscriptores. [cite_start]La comunicación se desacopla a través de un exchange de tipo `fanout` en RabbitMQ, asegurando que cada tarea enviada sea procesada en paralelo por todos los servicios suscritos[cite: 7, 8, 9, 10, 11].

```mermaid
graph TD
    subgraph "Cliente"
        A[▶️ Cliente API]
    end

    subgraph "Publicador"
        B[Publicador API <br> (Spring Boot + Camel)]
    end

    subgraph "Middleware"
        C{entregas.tareas <br> (Fanout Exchange)}
    end

    subgraph "Suscriptores"
        D[Servicio Notificaciones <br> (Spring Boot + Camel)]
        E[Servicio Plagio <br> (Spring Boot + Camel)]
    end

    A -- "1. POST /subir-tarea (JSON)" --> B
    B -- "2. Publica Mensaje" --> C
    C -- "3. Envía copia a todos" --> D
    C -- "3. Envía copia a todos" --> E
```

---

## [cite_start]3. Instrucciones para Ejecutar los Componentes [cite: 47]

Sigue estos pasos para levantar y probar el ecosistema completo.

### Prerrequisitos
* Java 11 o superior
* Apache Maven
* Docker (o una instalación local de RabbitMQ)

### Paso 1: Levantar RabbitMQ
Abre una terminal y ejecuta el siguiente comando para iniciar un contenedor de RabbitMQ con su panel de gestión:
```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```
[cite_start]Luego, accede a `http://localhost:15672` (user: `guest`, pass: `guest`), ve a la pestaña **Exchanges** y crea manualmente un exchange de tipo **`fanout`** llamado **`entregas.tareas`**[cite: 16].

### Paso 2: Ejecutar los Servicios
Abre **tres terminales separadas**, una para cada servicio.

* **Terminal 1 (Servicio de Notificaciones):**
    ```bash
    cd servicio-notificaciones
    mvn spring-boot:run
    ```

* **Terminal 2 (Servicio de Plagio):**
    ```bash
    cd servicio-plagio
    mvn spring-boot:run
    ```

* **Terminal 3 (API Publicador):**
    ```bash
    cd publicador-api
    mvn spring-boot:run
    ```
Espera a que las tres aplicaciones se inicien correctamente.

### Paso 3: Probar el Sistema
[cite_start]Usa `curl` o una herramienta como Postman para enviar una tarea a la API del publicador[cite: 21].

```bash
curl -X POST http://localhost:8080/subir-tarea \
-H "Content-Type: application/json" \
-d '{
  "estudiante": "Juan Perez",
  "curso": "Integración de Sistemas",
  "archivo": "tarea1.docx",
  "fechaEnvio": "2025-06-30T21:30:00"
}'
```

---

## [cite_start]4. Evidencia de Funcionamiento [cite: 43]

A continuación se muestra la evidencia del sistema en ejecución.

### a. [cite_start]Publicador Enviando Mensaje [cite: 44]

*(Aquí pegarías una captura de pantalla de la terminal del `publicador-api` mostrando el log de publicación)*

**Ejemplo de log esperado en la consola del publicador:**
```
INFO 12345 --- [nio-8080-exec-1] publicador-tareas-route : 📡 Publicando mensaje en 'entregas.tareas': {"estudiante":"Juan Perez",...}
```

### b. [cite_start]Suscriptores Recibiendo y Procesando el Mensaje [cite: 45]

*(Aquí pegarías una captura de pantalla de las terminales de los suscriptores mostrando los logs de recepción)*

**Ejemplo de log esperado en la consola de `servicio-notificaciones`:**
```
INFO 23456 --- [ #0 - q_notif] servicio-notificaciones : 🔔 [Servicio de Notificación] Notificación enviada para la tarea del estudiante: Juan Perez
```

**Ejemplo de log esperado en la consola de `servicio-plagio`:**
```
INFO 34567 --- [ #0 - q_plagio] servicio-plagio : 🔍 [Servicio de Plagio] Iniciando análisis de plagio para el estudiante: Juan Perez
```
