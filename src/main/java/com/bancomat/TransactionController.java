package com.bancomat;

import com.atm.backend.AutomaticTellerMachine;
import com.atm.backend.bills.Bill;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class TransactionController {
    AutomaticTellerMachine ATM = AutomaticTellerMachine.getInstance();
    StringBuilder finalText = new StringBuilder();
    @GetMapping("/transaction")
    public SoldInquiry transaction(@RequestParam(defaultValue = "0") int cashAmount){
        finalText.setLength(0);
        if(cashAmount < 0){
            return null;
        }
        if(cashAmount == 0)
            return new SoldInquiry(ATM.getNumberOfBillsByType());
        return new SoldInquiry(ATM.withdrawalRequestAsMap(cashAmount));
    }

}
