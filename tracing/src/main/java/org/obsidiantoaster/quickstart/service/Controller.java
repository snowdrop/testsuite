package org.obsidiantoaster.quickstart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private Tracer tracer;

    @Autowired
    private SpanAccessor accessor;

    @Value("${server.port}")
    private String port;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RequestMapping("/test")
    public String test() {
        tracer.addTag("method", "test");
        Span span = tracer.createSpan("sleep");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tracer.close(span);
        String response = restTemplate
                .getForObject("http://localhost:" + port + "/test2", String.class);
        return "OK/" + response;
    }

    @RequestMapping("/test2")
    public String test2() {
        tracer.addTag("method", "test2");
        return "OK";
    }
}