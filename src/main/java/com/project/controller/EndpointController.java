package com.project.controller;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;

@RestController
public class EndpointController {

    private final ApplicationContext applicationContext;

    public EndpointController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @GetMapping("/endpoints")
    public void printAllEndpoints() {
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);
        for (Object controller : controllers.values()) {
            Class<?> controllerClass = controller.getClass();
            RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
            String baseUrl = "";
            if (requestMapping != null) {
                baseUrl = requestMapping.value()[0];
            }

            Method[] methods = controllerClass.getMethods();
            for (Method method : methods) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if (getMapping != null) {
                    String endpoint = baseUrl + getMapping.value()[0];
                    System.out.println("Endpoint: " + endpoint);
                }
            }
        }
    }
}

