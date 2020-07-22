package com.atm.backend.dto;

import com.atm.backend.bills.Bill;

import java.util.HashMap;

public class SoldInquiryDto {
    HashMap<Bill.Type, Integer> typeToNrOfBills;


    public SoldInquiryDto(HashMap<Bill.Type, Integer> typeToNrOfBills) {
        this.typeToNrOfBills = typeToNrOfBills;
    }

    public HashMap<Bill.Type, Integer> getTypeToNrOfBills() {
        return typeToNrOfBills;
    }
}
