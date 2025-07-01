// src/main/java/com/ejemplo/camel/TareaController.java
package com.ejemplo.camel;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TareaController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping("/subir-tarea")
    public ResponseEntity<String> subirTarea(@RequestBody Tarea tarea) {
        // Envía el objeto de la tarea a la ruta de Camel para su procesamiento
        producerTemplate.sendBody("direct:publicarTarea", tarea);
        return ResponseEntity.ok("Tarea recibida y enviada para procesamiento.");
    }
}