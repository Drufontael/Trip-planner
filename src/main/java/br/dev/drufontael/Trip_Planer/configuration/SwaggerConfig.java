package br.dev.drufontael.Trip_Planer.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI docConfig(){
        return new OpenAPI()
                .info(new Info()
                        .title("Trip-planner API RestFull")
                        .version("1.0.0-SNAPSHOT")
                        .description("API for a planner and tracker of travel activities and expenses")
                );
    }
}
