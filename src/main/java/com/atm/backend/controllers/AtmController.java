package com.atm.backend.controllers;

import com.atm.backend.bills.Bill;
import com.atm.backend.dto.SoldInquiryDto;
import com.atm.backend.exceptions.NotEnoughMoneyException;
import com.atm.backend.feign.FeignController;
import com.atm.backend.feign.SimpleClient;
import com.atm.backend.myUtils.CashManager;
import com.atm.backend.myUtils.MyUtils;
import com.atm.backend.services.AtmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

@RestController
public class AtmController {
    AtmService ATM;

    FeignController client;

    public AtmController(AtmService ATM, FeignController client) {
        this.ATM = ATM;
        this.client = client;
    }

    @GetMapping("/available")
    public SoldInquiryDto availableCash() {
        return new SoldInquiryDto(MyUtils.billTypeToStringTypeMapConverter(ATM.getNumberOfBillsByTypeAsMap()), "Available cash in ATM");
    }

    @GetMapping("/transaction")
    public SoldInquiryDto transaction(@RequestParam(defaultValue = "0") int cashAmount) {

        if (cashAmount < 0) {
            return null;
        }

        HashMap<Bill.Type, Integer> transactionBillsMap = ATM.withdrawalRequestAsMap(cashAmount);
        CashManager.checkATMBalance(ATM);

        boolean noMoreCash = false;

        for (Bill.Type billType : Bill.Type.values()) {
            if (transactionBillsMap.get(billType).equals(Integer.MAX_VALUE))
                noMoreCash = true;
        }
        if (!noMoreCash) {
            return new SoldInquiryDto(MyUtils.billTypeToStringTypeMapConverter(transactionBillsMap), "Transaction approved");
        } else {
            return client.getDataFromDiana(cashAmount);
        }
    }
}
