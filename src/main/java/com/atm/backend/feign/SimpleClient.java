package com.atm.backend.feign;

import com.atm.backend.dto.SoldInquiryDto;
import feign.RequestLine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@FeignClient(name = "simpleClient", url = "https://this-is-a-placeholder.com")
public interface SimpleClient{

    @GetMapping(path = "/api/new-transaction")
    SoldInquiryDto getDataFromDiana(URI baseUri, @RequestParam(defaultValue = "0") int sum);

    @GetMapping(path = "/api/atm")
    SoldInquiryDto getDataFromAdelina(URI baseUri, @RequestParam(defaultValue = "0") int amount);
}