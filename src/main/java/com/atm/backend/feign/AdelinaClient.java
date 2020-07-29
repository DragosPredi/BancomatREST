package com.atm.backend.feign;

import com.atm.backend.dto.SoldInquiryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "adelinaClient", url = "${adelina.atm}")
public interface AdelinaClient extends com.atm.backend.feign.FeignClient {

    @GetMapping(path = "/api/new-transaction")
    ResponseEntity<SoldInquiryDto> requestTransaction(@RequestParam(defaultValue = "0") int sum);

    @GetMapping(path = "/api/online")
    ResponseEntity<String> isOnline();

    @GetMapping(path = "/api/check-balance")
    ResponseEntity<SoldInquiryDto> checkBalance();
}