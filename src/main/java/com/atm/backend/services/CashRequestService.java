package com.atm.backend.services;

import com.atm.backend.dto.SoldInquiryDto;
import org.springframework.http.ResponseEntity;

public interface CashRequestService {
    ResponseEntity<SoldInquiryDto> withdrawalRequest(int cashAmount);
}
