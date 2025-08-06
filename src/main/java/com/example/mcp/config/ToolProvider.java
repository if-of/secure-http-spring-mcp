package com.example.mcp.config;

import java.security.SecureRandom;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.Content;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ToolProvider {

    @Autowired
    private ObjectMapper objectMapper;

    public SyncToolSpecification provide() {
        var randomGenerator = new SecureRandom();

        var schema = """
            {
              "type" : "object",
              "id" : "urn:jsonschema:Operation",
              "properties" : {
              }
            }
            """;

        return new McpServerFeatures.SyncToolSpecification(
            new Tool("create-random-number", "Create random value", schema),
            (exchange, arguments) -> {
                String serializedArguments;
                try {
                    serializedArguments = objectMapper.writeValueAsString(arguments);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                var message = "The random number is: %s Params: %s".formatted(randomGenerator.nextInt(), serializedArguments);
                var result = new TextContent(message);
                var results = List.<Content>of(result);
                return new CallToolResult(results, false);
            }
        );
    }

}