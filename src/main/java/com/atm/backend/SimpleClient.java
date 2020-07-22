package com.atm.backend;

import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@FeignClient(name = "simpleClient", url = "https://this-is-a-placeholder.com")
public interface SimpleClient{

    @GetMapping(path = "new-transaction")
    String getDataFromDiana(URI baseUri, @RequestParam(defaultValue = "0") int sum);

    @GetMapping(path = "/api/atm")
    String getDataFromAdelina(URI baseUri, @RequestParam(defaultValue = "0") int amount);
}