package com.bancomat;

import com.atm.backend.AutomaticTellerMachine;
import com.atm.backend.bills.Bill;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class ATMTests {
    public void setArray(int[] arr, int one, int five, int ten, int fifty, int oneH) {
        arr[0] = one;
        arr[1] = five;
        arr[2] = ten;
        arr[3] = fifty;
        arr[4] = oneH;
    }

    public void assertBillsQuantityHelper(int[] baseArray, String errorMsg, AutomaticTellerMachine tester) {
        int cont = 0;
        for (Bill.Type billType : Bill.Type.values()) {
            assertEquals(baseArray[cont++], tester.getBillsQuantityByType(billType), errorMsg);
        }
    }

    @Test
    public void initialStateChecker() {
        AutomaticTellerMachine tester = new AutomaticTellerMachine();
        int[] bills = {100, 100, 100, 50, 50};
        assertBillsQuantityHelper(bills, "Initial state is faulty", tester);
    }

    @Test
    public void fillUpChecker() {
        AutomaticTellerMachine tester = new AutomaticTellerMachine();
        tester.fillUp(Bill.Type.ONE_RON, 10);
        tester.fillUp(Bill.Type.FIVE_RON, 10);
        tester.fillUp(Bill.Type.TEN_RON, 10);
        tester.fillUp(Bill.Type.FIFTY_RON, 10);
        tester.fillUp(Bill.Type.ONEHUNDRED_RON, 10);

        int[] bills = {110, 110, 110, 60, 60};
        assertBillsQuantityHelper(bills, "Fill up is faulty", tester);
    }

    @Test
    public void modifyNrBillsChecker() {
        AutomaticTellerMachine tester = new AutomaticTellerMachine();
        int[] bills = {5, 1, 2, 3, 4};

        tester.modifyAllBills(bills, 1);

        setArray(bills, 105, 101, 102, 53, 54);
        assertBillsQuantityHelper(bills, "Modify all bills is faulty", tester);

        setArray(bills, 5, 1, 2, 3, 4);
        tester.modifyAllBills(bills, -1);

        setArray(bills, 100, 100, 100, 50, 50);
        assertBillsQuantityHelper(bills, "Initial state is faulty", tester);

    }

    @Test
    public void withdrawalRequestsChecker() {
        AutomaticTellerMachine tester = new AutomaticTellerMachine();
        int[] bills = {1, 0, 0, 1, 1};
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(151));

        setArray(bills, 3, 0, 0, 0, 1);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(103));

        setArray(bills, 4, 1, 4, 1, 0);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(99));

        tester.setATMState(100, 100, 100, 50, 50);
        setArray(bills, 0, 0, 1, 50, 50);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(7510));
    }

    @Test
    public void multipleWithdrawalRequestsChecker() {
        AutomaticTellerMachine tester = new AutomaticTellerMachine();
        int[] bills = new int[5];
        tester.setATMState(10, 9, 5, 1, 1);

        setArray(bills, 0, 0, 0, 1, 1);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(150));

        setArray(bills, 0, 0, 5, 0, 0);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(50));

        setArray(bills, 5, 9, 0, 0, 0);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(50));


        tester.setATMState(10, 9, 5, 3, 10);

        setArray(bills, 3, 1, 1, 1, 9);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(968));
    }

}
