package com.atm.backend;

import com.atm.backend.bills.Bill;

public class CashManager {
    private static void critical() {
        MyUtils.sendEmail("fillMeUpPlease@superbancomat.com",
                Bill.Type.ONEHUNDRED_RON + " amount is under 10% ");
    }

    private static void warning(Bill.Type type) {
        if (type == Bill.Type.ONEHUNDRED_RON) {
            MyUtils.sendEmail("fillMeUpPlease@superbancomat.com",
                    type + " amount is under 20% ");
        } else {
            MyUtils.sendEmail("fillMeUpPlease@superbancomat.com",
                    type + " amount is under 15% ");
        }
    }

    public static void checkATMBalance(AtmServiceImpl ATM) {
        int nrOfHundredBills = ATM.getBillsQuantityByType(Bill.Type.ONEHUNDRED_RON);
        int nrOfFiftyBills = ATM.getBillsQuantityByType(Bill.Type.FIFTY_RON);

        if (nrOfHundredBills < 5) {
            critical();
        } else if (nrOfHundredBills < 10) {
            warning(Bill.Type.ONEHUNDRED_RON);
        }

        if (nrOfFiftyBills < 7) {
            warning(Bill.Type.FIFTY_RON);
        }

    }
}