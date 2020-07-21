package com.atm.backend;

import com.atm.backend.bills.Bill;

import java.util.HashMap;

public class SoldInquiry {
    private final int ONE_RON;
    private final int FIVE_RON;
    private final int TEN_RON;
    private final int FIFTY_RON;
    private final int ONEHUNDRED_RON;


    public SoldInquiry(HashMap<Bill.Type, Integer> typeToNrOfBills) {
        ONE_RON = typeToNrOfBills.get(Bill.Type.ONE_RON);
        FIVE_RON = typeToNrOfBills.get(Bill.Type.FIVE_RON);
        TEN_RON = typeToNrOfBills.get(Bill.Type.TEN_RON);
        FIFTY_RON = typeToNrOfBills.get(Bill.Type.FIFTY_RON);
        ONEHUNDRED_RON = typeToNrOfBills.get(Bill.Type.ONEHUNDRED_RON);
    }

    public int getONE_RON() {
        return ONE_RON;
    }

    public int getFIVE_RON() {
        return FIVE_RON;
    }

    public int getTEN_RON() {
        return TEN_RON;
    }

    public int getFIFTY_RON() {
        return FIFTY_RON;
    }

    public int getONEHUNDRED_RON() {
        return ONEHUNDRED_RON;
    }
}
