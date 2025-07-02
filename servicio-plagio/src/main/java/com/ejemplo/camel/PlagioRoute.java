package com.ejemplo.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PlagioRoute extends RouteBuilder {

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void configure() {
        from("rabbitmq:entregas.tareas"          
             + "?exchangeType=fanout"            
             + "&queue=plagio.queue"            
             + "&autoDelete=false")
            .routeId("servicio-plagio")

            .log("[Servicio de Plagio] Recibiendo tarea para analisis de plagio")

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
            .log("[Servicio de Plagio] Iniciando analisis de plagio para el estudiante: ${header.estudiante}");
    }
}