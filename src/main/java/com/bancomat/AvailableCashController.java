package com.bancomat;

import com.atm.backend.AutomaticTellerMachine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AvailableCashController {
    AutomaticTellerMachine ATM = AutomaticTellerMachine.getInstance();
    @GetMapping("/available")
    public SoldInquiry availableCash(){
        return new SoldInquiry(ATM.getNumberOfBillsByType());
    }
}
