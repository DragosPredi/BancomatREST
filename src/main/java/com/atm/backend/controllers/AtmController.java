package com.atm.backend.controllers;

import com.atm.backend.exceptions.NotEnoughMoneyException;
import com.atm.backend.infrastructure.dto.SoldInquiryDto;
import com.atm.backend.infrastructure.dto.TransactionHistoryDto;
import com.atm.backend.services.AtmService;
import com.atm.backend.services.CashRequestService;
import com.atm.backend.services.RemoteAtmService;
import com.atm.backend.services.TransactionHistoryService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AtmController {
    AtmService Atm;
    RemoteAtmService remoteAtm;
    CashRequestService requestService;
    TransactionHistoryService historyService;


    public AtmController(AtmService atm, RemoteAtmService remoteAtm, CashRequestService requestService, TransactionHistoryService historyService) {
        this.Atm = atm;
        this.remoteAtm = remoteAtm;
        this.requestService = requestService;
        this.historyService = historyService;
    }

    @GetMapping("/online")
    public ResponseEntity<String> online() {
        return new ResponseEntity<>("Server is up", HttpStatus.OK);
    }

    @GetMapping("/check-balance")
    public ResponseEntity<SoldInquiryDto> availableCash() {
        return new ResponseEntity<>(Atm.availableCash(), HttpStatus.OK);
    }

    @GetMapping("/save-records/csv")
    public String saveRecordsAsCvs() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {

        historyService.saveHistoryToFileAsCsv();
        return "Success";
    }

    @GetMapping("/save-records/pdf")
    public String saveRecordsAsPdf() throws IOException {
        historyService.saveHistoryToFileAsPdf();
        return "Succes";
    }

    @GetMapping("/history")
    public ResponseEntity<TransactionHistoryDto> history(@RequestParam(defaultValue = "0") int minutes) {
        return new ResponseEntity<>(historyService.getTransactionHistory(minutes), HttpStatus.OK);
    }

    @GetMapping("/new-transaction")
    public ResponseEntity<SoldInquiryDto> transaction(@RequestParam(defaultValue = "0") int sum) {
        ResponseEntity<SoldInquiryDto> sold = requestService.withdrawalRequest(sum);
        if (sold != null) {
            historyService.addRecord(sold.getBody());
            return sold;
        } else {
            throw new NotEnoughMoneyException();
        }
    }
}
