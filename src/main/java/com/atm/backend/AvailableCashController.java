package com.atm.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AvailableCashController {

    @Autowired
    AtmService ATM;

    @GetMapping("/available")
    public SoldInquiry availableCash(){
        return new SoldInquiry(ATM.getNumberOfBillsByType());
    }
}
