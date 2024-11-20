package ma.nabil.WRM.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Waiting Room Management API")
                        .description("API pour la gestion des salles d'attente")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Nabil")
                                .email("nettihadi@gmai.com")));
    }
}