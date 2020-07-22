package com.atm.backend;

import com.atm.backend.bills.Bill;
import com.fasterxml.jackson.core.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AtmController {


    AtmService ATM;

    @Autowired
    SimpleClient clientDIana;

    public AtmController(AtmService ATM) {
        this.ATM = ATM;
    }

    @GetMapping("/available")
    public SoldInquiry availableCash(){
        return new SoldInquiry(ATM.getNumberOfBillsByType());
    }

    @GetMapping("/transaction")
    public SoldInquiry transaction(@RequestParam(defaultValue = "0") int cashAmount){
        if(cashAmount < 0){
            return null;
        }
        if(cashAmount == 0)
            return new SoldInquiry(ATM.getNumberOfBillsByType());

        HashMap<Bill.Type, Integer> transactionBillsMap = ATM.withdrawalRequestAsMap(cashAmount);
        return new SoldInquiry(transactionBillsMap);
    }

}
