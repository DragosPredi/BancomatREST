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
    public String transaction(@RequestParam(defaultValue = "0") int cashAmount){
        finalText.setLength(0);
        if(cashAmount < 0){
            return finalText.append("You cheecky bastard you! You can't withdraw negative values!").toString();
        }
        if(cashAmount == 0)
            return finalText.append("Here is the ATM remaining cash:").append("\r\n").append(ATM.getNumberOfBillsByType()).toString();
        return finalText.append("Here is your cash: ").append("\r\n").append(ATM.withdrawalRequestAsMap(cashAmount)).toString();
    }

}
