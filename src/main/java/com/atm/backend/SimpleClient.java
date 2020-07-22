package com.atm.backend;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client", url = "http://192.168.243.253:8080")
public interface SimpleClient {

    @GetMapping("/new-transaction")
    String getData(@RequestParam(defaultValue = "0") int sum);
}