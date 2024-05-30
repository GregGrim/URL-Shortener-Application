package com.tpo.tpo_10;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("TPO10");

        Contact myContact = new Contact();
        myContact.setName("Hryhorii Hrymailo");
        myContact.setEmail("s27157@pjwstk.edu.pl");

        Info information = new Info()
                .title("TPO10 API")
                .version("1.0")
                .description("TPO10")
                .contact(myContact);

        Parameter langParam = new Parameter()
                .in("query")
                .name("lang")
                .description("Language")
                .required(false)
                .allowEmptyValue(false)
                .schema(new StringSchema()._enum(Arrays.asList("en", "pl", "de")));

        return new OpenAPI()
                .info(information)
                .servers(List.of(server))
                .components(new Components().addParameters("lang", langParam));
    }
}
