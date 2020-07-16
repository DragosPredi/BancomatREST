package com.bancomat;

import com.atm.backend.AutomaticTellerMachine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AvailableCashController {
    AutomaticTellerMachine ATM = new AutomaticTellerMachine();
    @GetMapping("/available")
    public AvailableCash availableCash(){
        return new AvailableCash(ATM.getNumberOfBillsByType());
    }
}
