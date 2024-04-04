package kz.services.romshop;

import kz.services.romshop.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RomShopApplication implements CommandLineRunner {
    private final AuthService service;

    @Autowired
    public RomShopApplication (AuthService service) {
        this.service = service;
    }


    public static void main(String[] args) {
        SpringApplication.run(RomShopApplication.class, args);
    }

    @Override
    public void run(String[] args) { service.firstAdmin(); }
}
