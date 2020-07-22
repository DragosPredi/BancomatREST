package com.atm.backend.controllers;

import com.atm.backend.services.AtmService;
import com.atm.backend.myUtils.CashManager;
import com.atm.backend.feign.SimpleClient;
import com.atm.backend.dto.SoldInquiryDto;
import com.atm.backend.bills.Bill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;

@RestController
public class AtmController {
    AtmService ATM;

    SimpleClient client;

    @Value("${adelina.atm}")
    private String adelinaAtmUrl;

    @Value("${diana.atm}")
    private String dianaAtmUrl;

    public AtmController(AtmService ATM, SimpleClient client) {
        this.ATM = ATM;
        this.client = client;
    }

    @GetMapping("/available")
    public SoldInquiryDto availableCash() {
        return new SoldInquiryDto(ATM.getNumberOfBillsByType());
    }

    @GetMapping("/transaction")
    public String transaction(@RequestParam(defaultValue = "0") int cashAmount) {
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
            //return new SoldInquiryDto(transactionBillsMap).toString();
            return transactionBillsMap.toString();
        } else {
            if (Math.random() > 0.5)
                return client.getDataFromAdelina(URI.create(adelinaAtmUrl), cashAmount);
            else
                return client.getDataFromDiana(URI.create(dianaAtmUrl), cashAmount);

        }
    }
}
