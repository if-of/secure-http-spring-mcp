package com.example.mcp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.DefaultMcpTransportContext;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Autowired
    private ToolProvider toolProvider;

    @Bean
    public HttpServletStreamableServerTransportProvider transportProvider() {
        return new HttpServletStreamableServerTransportProvider.Builder()
            .objectMapper(new ObjectMapper())
            .mcpEndpoint("")
            .disallowDelete(true)
            .contextExtractor((request, transportContext) -> new DefaultMcpTransportContext())
            .build();
    }

    @Bean
    public ServletRegistrationBean<HttpServletStreamableServerTransportProvider> configurableServlet() {
        return new ServletRegistrationBean<>(transportProvider(), "/mcp");
    }

    @Bean
    public McpSyncServer mcpServer() {
        var transportProvider = transportProvider();

        var mcpServer = McpServer.sync(transportProvider)
            .serverInfo("random-number-server", "1.0.0")
            .capabilities(ServerCapabilities.builder()
                .tools(true)
                .logging()
                .build())
            .build();

        mcpServer.addTool(toolProvider.provide());

        return mcpServer;

    }

}
