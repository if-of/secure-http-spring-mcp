package com.example.mcp.config.security;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WellKnownController {

    @Value("${security.oauth-protected-resource.resource}")
    private String resource;
    @Value("${security.oauth-protected-resource.authorization_server}")
    private String authorizationServer;

    @GetMapping("/.well-known/oauth-protected-resource")
    public Map<String, Object> getPublicResourceMetadata() {
        return Map.of(
            "scopes_supported", List.of("mcp-client-scope"),
            "resource", resource,
            "authorization_servers", List.of(authorizationServer)
        );
    }

}
