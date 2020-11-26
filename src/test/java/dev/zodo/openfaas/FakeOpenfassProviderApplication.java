package dev.zodo.openfaas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class FakeOpenfassProviderApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(FakeOpenfassProviderApplication.class, args);
    }
}
