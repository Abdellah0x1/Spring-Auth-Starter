package com.abdellah.spring_auth_starter.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        Map<String , Object> body = Map.of(
                "status", 403,
                "error", "Access Denied",
                "message", "You don't have the right to access this resource",
                "path", request.getServletPath()
        );

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
