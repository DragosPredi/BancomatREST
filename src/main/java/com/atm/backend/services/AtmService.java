package com.atm.backend.services;

import com.atm.backend.bills.Bill;

import java.util.HashMap;

public interface AtmService {
    HashMap<Bill.Type, Integer> withdrawalRequestAsMap(int cashAmount);

    int[] withdrawalRequestAsArray(int cashAmount);

    HashMap<Bill.Type, Integer> getNumberOfBillsByTypeAsMap();

    int getOneTypeBillQuantity(Bill.Type type);

    void setATMState(int [] billsArr);

    void fillUpOneTypeBill(Bill.Type type, int quantity);

    void addBills(int[] billsArr);

    void removeBills(int[] billsArr);
}
