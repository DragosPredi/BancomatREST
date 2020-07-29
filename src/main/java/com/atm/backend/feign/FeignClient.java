package com.atm.backend.feign;

import com.atm.backend.dto.SoldInquiryDto;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

public interface FeignClient {
    ResponseEntity<SoldInquiryDto> requestTransaction(int sum);

    ResponseEntity<String> isOnline();

    ResponseEntity<SoldInquiryDto> checkBalance();
}
