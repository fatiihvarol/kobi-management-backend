package com.example.emailservice.service;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TemplateRenderer {
    public String render(String template, Map<String, String> variables) {
        String rendered = template;
        if (variables == null || variables.isEmpty()) {
            return rendered;
        }
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            rendered = rendered.replace(placeholder, entry.getValue() == null ? "" : entry.getValue());
        }
        return rendered;
    }
}


