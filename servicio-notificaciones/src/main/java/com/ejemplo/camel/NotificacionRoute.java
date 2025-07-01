package com.ejemplo.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class NotificacionRoute extends RouteBuilder {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void configure() {
        from("rabbitmq:entregas.tareas"          // Nombre del Exchange 
             + "?exchangeType=fanout"            // Tipo Fanout 
             + "&queue=notificaciones.queue"     // Nombre de queue exclusivo para este servicio
             + "&autoDelete=false")
            .routeId("servicio-notificaciones")
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    String jsonBody = exchange.getIn().getBody(String.class);
                    try {
                        JsonNode jsonNode = objectMapper.readTree(jsonBody);
                        String estudiante = jsonNode.get("estudiante").asText();
                        exchange.getIn().setHeader("estudiante", estudiante);
                    } catch (Exception e) {
                        exchange.getIn().setHeader("estudiante", "unknown");
                    }
                }
            })
            .log("🔔 [Servicio de Notificación] Notificación enviada para la tarea del estudiante: ${header.estudiante}");
    }
}