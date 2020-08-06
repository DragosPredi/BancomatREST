package com.atm.backend.services;

import com.atm.backend.infrastructure.dto.SoldInquiryDto;
import org.springframework.http.ResponseEntity;

public interface CashRequestService {
    ResponseEntity<SoldInquiryDto> withdrawalRequest(int cashAmount);
}
