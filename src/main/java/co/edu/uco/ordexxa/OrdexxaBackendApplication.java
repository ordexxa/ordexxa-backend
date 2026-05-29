package co.edu.uco.ordexxa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class OrdexxaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdexxaBackendApplication.class, args);
    }

}
