package com.atm.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController

public class FeignController {

    private final SimpleClient client;

    @Value("${adelina.atm}")
    private String adelinaAtmUrl;

    @Value("${diana.atm}")
    private String dianaAtmUrl;

    public FeignController(SimpleClient client) {
        this.client = client;
    }

    @GetMapping("/new-transaction")
    public String getDataFromDiana(@RequestParam int sum) {
        return client.getDataFromDiana(URI.create(dianaAtmUrl), sum);
    }

    @GetMapping("/api/atm")
    public String getDataFromAdelina(@RequestParam int amount) {
        return client.getDataFromDiana(URI.create(adelinaAtmUrl), amount);
    }
}
