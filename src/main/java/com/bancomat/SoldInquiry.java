package com.bancomat;

import com.atm.backend.bills.Bill;

import java.util.HashMap;

public class SoldInquiry {
    private final HashMap<Bill.Type, Integer> typeToNrOfBills;

    public SoldInquiry(HashMap<Bill.Type, Integer> typeToNrOfBills) {
        this.typeToNrOfBills = typeToNrOfBills;
    }

    public HashMap<Bill.Type, Integer> getTypeToNrOfBills() {
        return typeToNrOfBills;
    }

}
