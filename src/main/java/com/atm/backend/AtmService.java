package com.atm.backend;

import com.atm.backend.bills.Bill;

import java.util.HashMap;

public interface AtmService {
    HashMap<Bill.Type, Integer> withdrawalRequestAsMap(int cashAmount);
    HashMap<Bill.Type, Integer> getNumberOfBillsByType();
    int getBillsQuantityByType(Bill.Type type);
}
