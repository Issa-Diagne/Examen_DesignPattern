package sn.ism.gestiondettes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestion des Dettes")
                        .version("1.0")
                        .description("API REST pour la gestion des dettes, clients et paiements")
                        .contact(new Contact()
                                .name("Support API")
                                .email("support@ism.sn")));
    }
}