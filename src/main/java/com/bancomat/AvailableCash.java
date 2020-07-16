package com.bancomat;

import com.atm.backend.bills.Bill;

import java.util.HashMap;

public class AvailableCash {
    private final HashMap<Bill.Type, Integer> typeToNrOfBillsAvailable;

    public AvailableCash(HashMap<Bill.Type, Integer> typeToNrOfBillsAvailable) {
        this.typeToNrOfBillsAvailable = typeToNrOfBillsAvailable;
    }

    public HashMap<Bill.Type, Integer> getTypeToNrOfBillsAvailable() {
        return typeToNrOfBillsAvailable;
    }

}
