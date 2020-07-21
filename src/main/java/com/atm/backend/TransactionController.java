package com.atm.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    AtmService ATM;

    @GetMapping("/transaction")
    public SoldInquiry transaction(@RequestParam(defaultValue = "0") int cashAmount){
        if(cashAmount < 0){
            return null;
        }
        if(cashAmount == 0)
            return new SoldInquiry(ATM.getNumberOfBillsByType());
        return new SoldInquiry(ATM.withdrawalRequestAsMap(cashAmount));
    }

}
