package yq.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @Author qf
 * @Date 2019/8/22
 * @Version 1.0
 */
@Configuration
public class SwaggerUtil {

    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
              .apiInfo(new ApiInfoBuilder()
                    .title("用户模块 接口文档")
                    .contact("vincent")
                    .description("API 接口文档~")
                    .version("2.0.0")
                    .build())
              .select()
              .apis(RequestHandlerSelectors.basePackage("com.eqlee.*.controller"))
              .paths(PathSelectors.any())
              .build();
    }

}
