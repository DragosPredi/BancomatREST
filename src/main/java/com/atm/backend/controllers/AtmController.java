package com.atm.backend.controllers;

import com.atm.backend.exceptions.NotEnoughMoneyException;
import com.atm.backend.infrastructure.SoldInquiryDto;
import com.atm.backend.services.AtmService;
import com.atm.backend.services.CashRequestService;
import com.atm.backend.services.RemoteAtmService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AtmController {
    AtmService Atm;
    RemoteAtmService remoteAtm;
    CashRequestService requestService;


    public AtmController(AtmService atm, RemoteAtmService remoteAtm, CashRequestService requestService) {
        Atm = atm;
        this.remoteAtm = remoteAtm;
        this.requestService = requestService;
    }

    @GetMapping("/online")
    public ResponseEntity<String> online() {
        return new ResponseEntity<>("Server is up", HttpStatus.OK);
    }

    @GetMapping("/check-balance")
    public ResponseEntity<SoldInquiryDto> availableCash() {
        return new ResponseEntity<>(Atm.availableCash(), HttpStatus.OK);
    }

    @GetMapping("/new-transaction")
    public ResponseEntity<SoldInquiryDto> transaction(@RequestParam(defaultValue = "0") int sum) {
        ResponseEntity<SoldInquiryDto> sold = requestService.withdrawalRequest(sum);
        if(sold != null)
            return sold;
        else
            throw new NotEnoughMoneyException();
    }
}
