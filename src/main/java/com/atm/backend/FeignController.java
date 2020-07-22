package com.atm.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignController {

    private final SimpleClient client;

    public FeignController(SimpleClient client) {
        this.client = client;
    }

    @GetMapping("/new-transaction")
    public String getData(@RequestParam int sum) {
        return client.getData(sum);
    }
}
