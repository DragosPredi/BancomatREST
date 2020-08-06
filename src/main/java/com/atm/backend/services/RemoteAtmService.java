package com.atm.backend.services;

import com.atm.backend.infrastructure.dto.SoldInquiryDto;

public interface RemoteAtmService {

    boolean isOnlineAdelina();

    boolean isOnlineDiana();

    SoldInquiryDto remoteWithdrawalRequest(int cashAmount);
}
