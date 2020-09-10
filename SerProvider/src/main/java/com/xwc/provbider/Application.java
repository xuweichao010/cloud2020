package com.xwc.provbider;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 作者：徐卫超 cc
 * 时间：2020/9/10
 * 描述：项目启动类
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableOpenApi
@RestController
@RequestMapping("")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/test")
    @ApiOperation("这是一个测试类")
    public String test() {
        return "hello I'm Test ";
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .pathProvider(new CloudPathProvider())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xwc."))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("开放平台接口")
                .description("让接口文档更加优雅")
                .termsOfServiceUrl("http://localhost:8100")
                .contact(new Contact("", "", ""))
                .version("1.0")
                .build();
    }

    class CloudPathProvider implements PathProvider {

        protected String getDocumentationPath() {
            return "/";
        }

        public String getOperationPath(String operationPath) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
            String path = "/api/provider" + Paths.removeAdjacentForwardSlashes(uriComponentsBuilder.path(operationPath).build().toString());
            System.out.println("getOperationPath: " + path);
            return path;
        }

        public String getResourceListingPath(String groupName, String apiDeclaration) {
            String candidate = this.agnosticUriComponentBuilder(this.getDocumentationPath()).pathSegment(new String[]{groupName, apiDeclaration}).build().toString();
            String path = Paths.removeAdjacentForwardSlashes(candidate);
            System.out.println("getResourceListingPath: " + path);
            return path;
        }

        private UriComponentsBuilder agnosticUriComponentBuilder(String url) {
            UriComponentsBuilder uriComponentsBuilder;
            if (url.startsWith("http")) {
                uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
            } else {
                uriComponentsBuilder = UriComponentsBuilder.fromPath(url);
            }

            return uriComponentsBuilder;
        }
    }

}
