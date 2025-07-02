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
        from("rabbitmq:entregas.tareas"           
             + "?exchangeType=fanout"             
             + "&queue=notificaciones.queue"     
             + "&autoDelete=false")
            .routeId("servicio-notificaciones")

            .log("[Servicio de Notificacion] Recibiendo tarea para notificacion")          

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
            .log("[Servicio de Notificacion] Notificacion enviada para la tarea del estudiante: ${header.estudiante}");
    }
}