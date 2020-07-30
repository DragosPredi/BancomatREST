package com.bancomat;

import com.atm.backend.infrastructure.Bill;
import com.atm.backend.infrastructure.SoldInquiryDto;
import com.atm.backend.services.AtmService;
import com.atm.backend.services.impl.AtmServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.jayway.restassured.RestAssured.get;
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

    public void assertBillsQuantityHelper(int[] baseArray, String errorMsg, AtmService tester) {
        int cont = 0;
        for (Bill.Type billType : Bill.Type.values()) {
            assertEquals(baseArray[cont++], tester.getOneTypeBillQuantity(billType), errorMsg);
        }
    }

    @Test
    public void initialATMStateChecker(){
        AtmService tester = new AtmServiceImpl();
        int []bills = {100, 100, 100, 50, 50};
        assertBillsQuantityHelper(bills, "set ATM state faulty", tester);
    }

    @Test
    public void setATMStateChecker(){
        AtmServiceImpl tester = new AtmServiceImpl();

        int []bills = {85, 0, 75, 80, 10};
        tester.setATMState(bills);
        assertBillsQuantityHelper(bills, "set ATM state faulty", tester);
    }

    @Test
    public void fillUpChecker() {
        AtmServiceImpl tester = new AtmServiceImpl();

        int []bills = {100, 100, 100, 50, 50};

        tester.setATMState(bills);

        tester.fillUpOneTypeBill(Bill.Type.ONE_RON, 10);
        tester.fillUpOneTypeBill(Bill.Type.FIVE_RON, 10);
        tester.fillUpOneTypeBill(Bill.Type.TEN_RON, 10);
        tester.fillUpOneTypeBill(Bill.Type.FIFTY_RON, 10);
        tester.fillUpOneTypeBill(Bill.Type.ONEHUNDRED_RON, 10);

        setArray(bills, 110, 110, 110, 60,60);
        assertBillsQuantityHelper(bills, "Fill up is faulty", tester);
    }

    @Test
    public void modifyNrBillsChecker() {
        AtmServiceImpl tester = new AtmServiceImpl();

        int []bills = {100, 100, 100, 50, 50};

        tester.setATMState(bills);

        setArray(bills, 5, 1, 2,3, 4);

        tester.fillUpAllTypeOfBills(bills);

        setArray(bills, 105, 101, 102, 53, 54);
        assertBillsQuantityHelper(bills, "Add bills is faulty", tester);

        setArray(bills, 5, 1, 2, 3, 4);
        tester.removeBills(bills);

        setArray(bills, 100, 100, 100, 50, 50);
        assertBillsQuantityHelper(bills, "Remove bills is faulty", tester);

    }

    @Test
    public void totalAmountAvailableChecker(){
        AtmServiceImpl tester = new AtmServiceImpl();

        assertEquals(9100, tester.totalAmountAvailable());
        tester.localWithdrawalRequest(100);
        assertEquals(9000, tester.totalAmountAvailable());
        tester.localWithdrawalRequest(9000);
        assertEquals(0, tester.totalAmountAvailable());
    }

    @Test
    public void withdrawAllMoneyChecker(){
        AtmServiceImpl tester = new AtmServiceImpl();
        int []bills = {0, 0, 0, 0, 0};
        tester.withdrawAllMoney();
        assertBillsQuantityHelper(bills, "withdraw all money faulty", tester);
        assertEquals(0, tester.totalAmountAvailable());

    }

    @Test
    public void fillUpWithMapChecker(){
        AtmService tester = new AtmServiceImpl();
        int []bills = {100, 100, 100, 50, 50};
        HashMap<Bill.Type, Integer> map = tester.withdrawAllMoney();
        tester.fillUpWithMap(map);
        assertBillsQuantityHelper(bills, "fill up with map faulty", tester);

    }

    @Test
    public void withdrawalRequestsChecker() {
        AtmServiceImpl tester = new AtmServiceImpl();

        int []bills = {1, 0, 0, 1, 1};

        assertArrayEquals(bills, tester.withdrawalRequestAsArray(151));

        setArray(bills, 3, 0, 0, 0, 1);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(103));

        setArray(bills, 4, 1, 4, 1, 0);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(99));

        setArray(bills, 100, 100, 100, 50, 50);
        tester.setATMState(bills);
        setArray(bills, 0, 0, 1, 50, 50);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(7510));
    }

    @Test
    public void multipleWithdrawalRequestsChecker() {
        AtmServiceImpl tester = new AtmServiceImpl();

        int[] bills = {10, 9, 5, 1, 1};
        tester.setATMState(bills);

        setArray(bills, 0, 0, 0, 1, 1);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(150));

        setArray(bills, 0, 0, 5, 0, 0);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(50));

        setArray(bills, 5, 9, 0, 0, 0);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(50));

        setArray(bills, 10, 9, 5, 3, 10);
        tester.setATMState(bills);

        setArray(bills, 3, 1, 1, 1, 9);
        assertArrayEquals(bills, tester.withdrawalRequestAsArray(968));
    }

    @Test
    public void withdraw101RestChecker() {
        SoldInquiryDto output = get("http://localhost:8080/transaction?cashAmount=101").as(SoldInquiryDto.class);
        Map<String, Integer> expected = new TreeMap<>();
        expected.put("ONEHUNDRED_RON(100)", 1);
        expected.put("ONE_RON(1)", 1);
        expected.put("TEN_RON(10)", 0);
        expected.put("FIVE_RON(5)", 0);
        expected.put("FIFTY_RON(50)", 0);

        assertEquals(expected, output.getBills());
        assertEquals("Transaction approved", output.getMessage());
    }

    @Test
    public void withdraw101and1000RestChecker() {
        SoldInquiryDto output = get("http://localhost:8080/transaction?cashAmount=101").as(SoldInquiryDto.class);
        Map<String, Integer> expected = new TreeMap<>();
        expected.put("ONEHUNDRED_RON(100)", 1);
        expected.put("ONE_RON(1)", 1);
        expected.put("TEN_RON(10)", 0);
        expected.put("FIVE_RON(5)", 0);
        expected.put("FIFTY_RON(50)", 0);
        assertEquals(expected, output.getBills());
        assertEquals("Transaction approved", output.getMessage());

        output = get("http://localhost:8080/transaction?cashAmount=1000").as(SoldInquiryDto.class);
        expected.clear();
        expected.put("ONEHUNDRED_RON(100)", 10);
        expected.put("ONE_RON(1)", 0);
        expected.put("TEN_RON(10)", 0);
        expected.put("FIVE_RON(5)", 0);
        expected.put("FIFTY_RON(50)", 0);

        assertEquals(expected, output.getBills());
        assertEquals("Transaction approved", output.getMessage());
    }

}
