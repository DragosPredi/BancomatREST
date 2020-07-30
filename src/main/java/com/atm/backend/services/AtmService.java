package com.atm.backend.services;

import com.atm.backend.infrastructure.Bill;
import com.atm.backend.infrastructure.SoldInquiryDto;

import java.util.HashMap;

public interface AtmService {
    SoldInquiryDto localWithdrawalRequest(int cashAmount);

    SoldInquiryDto availableCash();

    int getOneTypeBillQuantity(Bill.Type type);

    HashMap<Bill.Type, Integer> withdrawAllMoney();

    int totalAmountAvailable();

    void fillUpWithMap(HashMap<Bill.Type, Integer> billsToBeAdded);
}
