package com.ddd.moodof.adapter.infrastructure.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

import javax.servlet.http.HttpServletRequest;

@Component
public class SpringfoxSwaggerHostResolver implements WebMvcOpenApiTransformationFilter {
    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        OpenAPI swagger = context.getSpecification();

        Server server = swagger.getServers().get(0);
        if (server.getUrl().contains(":443")) {
            server.setUrl(server.getUrl().replace(":443", ""));
        }

        return swagger;
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return delimiter == DocumentationType.OAS_30;
    }
}
