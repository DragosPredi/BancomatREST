package com.atm.backend.services.impl;

import com.atm.backend.infrastructure.Bill;
import com.atm.backend.infrastructure.MyUtils;
import com.atm.backend.infrastructure.dto.SoldInquiryDto;
import com.atm.backend.services.AtmService;
import com.atm.backend.services.CashRequestService;
import com.atm.backend.services.RemoteAtmService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CashRequestServiceImpl implements CashRequestService {

    private final AtmService localAtm;
    private final RemoteAtmService remoteAtm;

    public CashRequestServiceImpl(AtmService atm, RemoteAtmService remoteAtm) {
        this.localAtm = atm;
        this.remoteAtm = remoteAtm;
    }

    public Map<String, Integer> addMapValues(Map<String, Integer> map1, Map<String, Integer> map2){
        map1.replaceAll((s, v) -> map1.get(s) + map2.get(s));
        return map1;
    }

    public SoldInquiryDto requestRemoteCashDifference(int totalAmount){
        int totalAmountAvailableLocally = localAtm.totalAmountAvailable();
        return remoteAtm.remoteWithdrawalRequest(totalAmount - totalAmountAvailableLocally);
    }

    public ResponseEntity<SoldInquiryDto> computeRemoteTransactionIfPossible(int amountRequired){
        HashMap<Bill.Type, Integer> availableInLocalAtm = localAtm.withdrawAllMoney();
        SoldInquiryDto body = requestRemoteCashDifference(amountRequired);

        if (body != null && body.getBills() != null) {
            HashMap<String, Integer> localBills = MyUtils.billTypeToStringTypeMapConverter(availableInLocalAtm);
            body.setBills(addMapValues(body.getBills(), localBills));
            return new ResponseEntity<>(body, HttpStatus.OK);
        } else {
            localAtm.fillUpWithMap(availableInLocalAtm);
            return null;
        }
    }

    @Override
    public ResponseEntity<SoldInquiryDto> withdrawalRequest(int cashAmount) {
        SoldInquiryDto body = localAtm.localWithdrawalRequest(cashAmount);
        if (body != null) {
            return new ResponseEntity<>(body, HttpStatus.OK);
        } else {
            return computeRemoteTransactionIfPossible(cashAmount);
        }
    }
}
