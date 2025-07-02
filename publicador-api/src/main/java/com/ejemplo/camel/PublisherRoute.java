// src/main/java/com/ejemplo/camel/PublisherRoute.java
package com.ejemplo.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class PublisherRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("direct:publicarTarea")
            .routeId("publicador-tareas-route")
            .marshal().json(JsonLibrary.Jackson) 
            .log("Publicando mensaje en 'entregas.tareas': ${body}")
            .to("rabbitmq:entregas.tareas"      
                + "?exchangeType=fanout"        
                + "&autoDelete=false"           
                + "&declare=false");     
    }
}