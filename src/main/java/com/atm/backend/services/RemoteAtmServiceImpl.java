package com.atm.backend.services;

import com.atm.backend.feign.AdelinaClient;
import com.atm.backend.feign.DianaClient;
import com.atm.backend.infrastructure.SoldInquiryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RemoteAtmServiceImpl implements RemoteAtmService {

    AdelinaClient adelinaClient;
    DianaClient dianaClient;

    public RemoteAtmServiceImpl(AdelinaClient adelinaClient, DianaClient dianaClient) {
        this.adelinaClient = adelinaClient;
        this.dianaClient = dianaClient;
    }


    @Override
    public boolean isOnlineAdelina() {
        ResponseEntity<String> response = adelinaClient.isOnline();
        return response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public boolean isOnlineDiana() {
        ResponseEntity<String> response = dianaClient.isOnline();
        return response.getStatusCode().is2xxSuccessful();
    }

    public SoldInquiryDto remoteWithdrawalRequest(int cashAmount) {
        if (isOnlineDiana()) {
            ResponseEntity<SoldInquiryDto> response = dianaClient.requestTransaction(cashAmount);
            if (response.getStatusCode().is2xxSuccessful())
                return response.getBody();
        }
        if (isOnlineAdelina()) {
            ResponseEntity<SoldInquiryDto> response = adelinaClient.requestTransaction(cashAmount);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        }
        return null;
    }

}
