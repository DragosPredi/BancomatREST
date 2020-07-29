package com.atm.backend.services;

import com.atm.backend.infrastructure.Bill;
import com.atm.backend.infrastructure.MyUtils;
import com.atm.backend.infrastructure.SoldInquiryDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class AtmServiceImpl implements AtmService {
    private final HashMap<Bill.Type, Integer> numberOfBillsByType;

    public AtmServiceImpl() {
        numberOfBillsByType = new HashMap<>();
        setATMState(new int[]{100, 100, 100, 50, 50});
    }

    public void fillUpOneTypeBill(Bill.Type type, int quantity) {
        int leftOverCash = numberOfBillsByType.get(type);
        numberOfBillsByType.put(type, quantity + leftOverCash);
    }

    public int getOneTypeBillQuantity(Bill.Type type) {
        return numberOfBillsByType.get(type);
    }

    public void fillUpAllTypeOfBills(int[] billsToBeModified) {

        int cont = 0;
        for (Bill.Type billType : Bill.Type.values()) {
            fillUpOneTypeBill(billType, billsToBeModified[cont++]);
        }
    }

    public void removeBills(int[] billsToBeModified) {
        int cont = 0;
        for (Bill.Type billType : Bill.Type.values()) {
            fillUpOneTypeBill(billType, (-1 * billsToBeModified[cont++]));
        }
    }

    public void setATMState(int[] billsArr) {
        int cont = 0;
        for (Bill.Type type : Bill.Type.values()) {
            numberOfBillsByType.put(type, billsArr[cont++]);
        }
    }

    /**
     * It takes a type of bill and it return it's representation
     * in array index (1RON->0 | 5RON->1 | 10RON->2 | etc.)
     *
     * @param type -> bill type to be converted
     * @return -> index in array
     */
    public int billToArrayIndex(Bill.Type type) {
        if (type.getLabelValue() == 1)
            return 0;
        if (type.getLabelValue() == 5)
            return 1;
        if (type.getLabelValue() == 10)
            return 2;
        if (type.getLabelValue() == 50)
            return 3;
        if (type.getLabelValue() == 100)
            return 4;
        return -1;
    }

    /**
     * Function that determines the combination of bills used
     * for the current amount of cash that is being computed
     *
     * @param billsUsedHistory -> the combination of bills used for all amounts up until that point
     * @param currentAmount    -> the amount currently being computed
     * @param billType         -> the bill added to form the current amount of cash
     */
    public void computeBillsUsed(int[][] billsUsedHistory, int currentAmount, Bill.Type billType) {
        int billValue = billType.getLabelValue();

        //Add last combination of bills no longer available
        if (billType == Bill.Type.ONE_RON) {
            fillUpAllTypeOfBills(billsUsedHistory[currentAmount - 1]);
        } else {
            fillUpAllTypeOfBills(billsUsedHistory[currentAmount]);
        }
        //Get base combination
        System.arraycopy(billsUsedHistory[currentAmount - billValue],
                0, billsUsedHistory[currentAmount], 0, 5);
        //Add the bill used
        billsUsedHistory[currentAmount][billToArrayIndex(billType)] =
                billsUsedHistory[currentAmount - billValue][billToArrayIndex(billType)] + 1;
        //Subtract combination from ATM cash
        removeBills(billsUsedHistory[currentAmount]);
    }

    /**
     * It returns an array with the amount of each bill necessary to form
     * the amount to be withdrawn.
     * Each cell corresponds in ascending order to the available bills
     * (1, 5, 10, 50, 100)
     * <p>
     * In case the ATM doesn't have enough bills the form the amount
     * needed, it returns an array filled with MAX_INT
     *
     * @param cashAmount -> amount requested
     * @return -> array with the bills requested or MAX_INT in case of failure
     */
    public int[] withdrawalRequestAsArray(int cashAmount) {

        int[] numberOfBillsNeeded = new int[cashAmount + 1];
        int[][] billsUsedHistory = new int[cashAmount + 1][5];

        Arrays.fill(numberOfBillsNeeded, Integer.MAX_VALUE);
        numberOfBillsNeeded[0] = 0;

        for (int currentAmount = 1; currentAmount <= cashAmount; currentAmount++) {
            for (Bill.Type billType : Bill.Type.values()) {
                int billValue = billType.getLabelValue();
                if (billValue <= currentAmount && numberOfBillsByType.get(billType) > 0) {
                    int rest = numberOfBillsNeeded[currentAmount - billValue];
                    if (rest != Integer.MAX_VALUE && rest + 1 < numberOfBillsNeeded[currentAmount]) {
                        numberOfBillsNeeded[currentAmount] = rest + 1;
                        computeBillsUsed(billsUsedHistory, currentAmount, billType);
                    }
                }
            }
        }

        if (numberOfBillsNeeded[cashAmount] == Integer.MAX_VALUE) {
            Arrays.fill(billsUsedHistory[cashAmount], Integer.MAX_VALUE);
        }
        return billsUsedHistory[cashAmount];
    }

    public HashMap<Bill.Type, Integer> withdrawalRequestAsMap(int cashAmount) {
        int[] bills = withdrawalRequestAsArray(cashAmount);
        HashMap<Bill.Type, Integer> typeToNrOfBills = new HashMap<>();
        int cont = 0;
        for (Bill.Type billType : Bill.Type.values()) {
            typeToNrOfBills.put(billType, bills[cont++]);
        }
        return typeToNrOfBills;
    }

    public SoldInquiryDto withdrawalRequest(int cashAmount) {
        HashMap<Bill.Type, Integer> map = withdrawalRequestAsMap(cashAmount);
        if (map.containsValue(Integer.MAX_VALUE))
            return null;
        return new SoldInquiryDto(MyUtils.billTypeToStringTypeMapConverter(map), "Transaction approved");
    }

    public SoldInquiryDto availableCash() {
        return new SoldInquiryDto(MyUtils.billTypeToStringTypeMapConverter(numberOfBillsByType), "Available cash in ATM");
    }

    public int totalAmountAvailable() {
        int sum = 0;
        for (Bill.Type type : Bill.Type.values()) {
            sum += numberOfBillsByType.get(type) * type.getLabelValue();
        }
        return sum;
    }

    public HashMap<Bill.Type, Integer> withdrawAllMoney() {
        HashMap<Bill.Type, Integer> requestedMoney = new HashMap<>();
        for (Bill.Type type : Bill.Type.values()) {
            requestedMoney.put(type, numberOfBillsByType.get(type));
            numberOfBillsByType.put(type, 0);
        }
        return requestedMoney;
    }

    @Override
    public void fillUpWithMap(HashMap<Bill.Type, Integer> billsToBeAdded) {
        for (Bill.Type type : Bill.Type.values()) {
            numberOfBillsByType.put(type, billsToBeAdded.get(type));
        }
    }
}

