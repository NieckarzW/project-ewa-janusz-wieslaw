package pl.coderstrust.configuration;

import io.swagger.annotations.Api;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
        .paths(PathSelectors.ant("/invoices/**"))
        .build()
        .consumes(Collections.singleton("application/json"))
        .produces(Collections.singleton("application/json"))
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Invoices REST API")
        .description("This API provides simple functionality to save, update, delete, search for your invoices.")
        .version("1.0.0")
        .contact(new Contact("Ewa Wiesław Janusz", "www.invoices.com", "EwaJanusWieslaw@o2.pl"))
        .build();
  }
}
