package com.atm.backend.services;

import com.atm.backend.dto.SoldInquiryDto;

public interface RemoteAtmService {

    boolean isOnlineAdelina();
    boolean isOnlineDiana();
    SoldInquiryDto remoteWithdrawalRequest(int cashAmount);
}
