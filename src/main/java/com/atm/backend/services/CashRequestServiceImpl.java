package com.atm.backend.services;

import com.atm.backend.infrastructure.Bill;
import com.atm.backend.infrastructure.MyUtils;
import com.atm.backend.infrastructure.SoldInquiryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CashRequestServiceImpl implements CashRequestService {

    private final AtmService Atm;
    private final RemoteAtmService remoteAtm;

    public CashRequestServiceImpl(AtmService atm, RemoteAtmService remoteAtm) {
        Atm = atm;
        this.remoteAtm = remoteAtm;
    }

    @Override
    public ResponseEntity<SoldInquiryDto> withdrawalRequest(int cashAmount) {
        SoldInquiryDto body = Atm.withdrawalRequest(cashAmount);
        if (body != null) {
            return new ResponseEntity<>(body, HttpStatus.OK);
        } else {
            int totalAmountAvailableLocally = Atm.totalAmountAvailable();
            HashMap<Bill.Type, Integer> availableInLocalAtm = Atm.withdrawAllMoney();
            body = remoteAtm.remoteWithdrawalRequest(cashAmount - totalAmountAvailableLocally);
            if (body != null) {
                HashMap<String, Integer> localBills = MyUtils.billTypeToStringTypeMapConverter(availableInLocalAtm);
                for (String st : body.getBills().keySet())
                    body.getBills().put(st, body.getBills().get(st) + localBills.get(st));
                return new ResponseEntity<>(body, HttpStatus.OK);
            } else {
                Atm.fillUpWithMap(availableInLocalAtm);
                return null;
            }
        }
    }
}
