package com.atm.backend.infrastructure;

import com.atm.backend.services.AtmService;

public class NotificationManager {

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

    public static void checkATMBalance(AtmService ATM) {
        int nrOfHundredBills = ATM.getOneTypeBillQuantity(Bill.Type.ONEHUNDRED_RON);
        int nrOfFiftyBills = ATM.getOneTypeBillQuantity(Bill.Type.FIFTY_RON);

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