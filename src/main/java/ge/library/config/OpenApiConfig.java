package ge.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library Management API")
                        .description("""
                                ბიბლიოთეკის მართვის RESTful API.
                                
                                **ფუნქციონალი:**
                                - ავტორების CRUD
                                - წიგნების CRUD (ჟანრი, ISBN, ხელმისაწვდომობა)
                                - წიგნების გაცემა / დაბრუნება
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Library Dev Team")
                                .email("dev@library.ge"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development")
                ));
    }
}
