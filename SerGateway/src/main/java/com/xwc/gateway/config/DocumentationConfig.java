package com.xwc.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 作者：徐卫超 cc
 * 时间：2020/9/10
 * 描述：$END
 */
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

    @Autowired
    private GatewayProperties gatewayProperties;

    @Override
    public List<SwaggerResource> get() {
        return gatewayProperties.getRoutes().stream()
                .filter(router -> StringUtils.hasText(router.getId()) && router.getPredicates() != null
                        && !router.getPredicates().isEmpty())
                .map(router ->
                        swaggerResource(router.getId(), getPath(router.getPredicates()))
                ).collect(Collectors.toList());

    }

    private String getPath(List<PredicateDefinition> predicateList) {
        return predicateList.iterator().next()
                .getArgs().values().iterator().next()
                .replace("**", "v3/api-docs");
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(DocumentationType.OAS_30.getVersion());
        return swaggerResource;
    }
}
