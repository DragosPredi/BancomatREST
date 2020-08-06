package com.atm.backend.feign;

import com.atm.backend.infrastructure.dto.SoldInquiryDto;
import org.springframework.http.ResponseEntity;

public interface FeignClient {
    ResponseEntity<SoldInquiryDto> requestTransaction(int sum);

    ResponseEntity<String> isOnline();

    ResponseEntity<SoldInquiryDto> checkBalance();
}
