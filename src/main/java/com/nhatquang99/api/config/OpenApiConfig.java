package com.nhatquang99.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> lists = new ArrayList<>();
        lists.add(new Server().url("https://javaclusters-28976-0.cloudclusters.net"));
        lists.add(new Server().url("https://nguyenhnhatquang.website"));
        return new OpenAPI()
                .servers(lists)
                // info
                .info(new Info().title("Application API")
                        .description("Document using OpenAPI 3.0 - Design by Nhật Quang")
                        .contact(new Contact()
                                .email("nguyenhnhatquang@gmail.com")
                                .name("quang")
                                .url(""))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                        .version("1.0.0"));
    }
}
