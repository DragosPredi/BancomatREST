package com.atm.backend;

import com.atm.backend.bills.Bill;

import java.util.*;

public class AutomaticTellerMachine {
    private final HashMap<Bill.Type, Integer> numberOfBillsByType;

    public AutomaticTellerMachine() {
        numberOfBillsByType = new HashMap<>();
        setATMState(100, 100, 100, 50, 50);
    }

    /**
     * Add cash to ATM
     *
     * @param type     -> type of bill to be added
     * @param quantity -> number of bills
     */
    public void fillUp(Bill.Type type, int quantity) {
        int leftOverCash = numberOfBillsByType.get(type);
        numberOfBillsByType.put(type, quantity + leftOverCash);
    }

    /**
     * Getter for the amount of a certain type of bill
     *
     * @param type -> bill to be investigated
     * @return -> number of bills of requested type
     */
    public int getBillsQuantityByType(Bill.Type type) {
        return numberOfBillsByType.get(type);
    }

    /**
     * Used to add or remove multiple bills at once.
     * if sign == 1 the bills are added, if sign == -1 the bills are removed
     * <p>
     * Each cell corresponds in ascending order to the available bills
     * (1, 5, 10, 50, 100)
     *
     * @param billsToBeModified -> array conatining the number of bills to be modified
     * @param sign              -> +1 for adding, -1 for removing
     */
    public void modifyAllBills(int[] billsToBeModified, int sign) {
        assert (sign == -1 || sign == 1) : "Invalid sign";

        int cont = 0;
        for (Bill.Type billType : Bill.Type.values()) {
            fillUp(billType, (sign * billsToBeModified[cont++]));
        }
    }

    /**
     * Set the ATM with a specified number of cash
     *
     * @param one        -> number of 1 RON bills
     * @param five       -> number of 5 RON bills
     * @param ten        -> number of 10 RON bills
     * @param fifty      -> number of 50 RON bills
     * @param oneHundred -> number of 100 RON bills
     */
    public void setATMState(int one, int five, int ten, int fifty, int oneHundred) {
        numberOfBillsByType.put(Bill.Type.ONE_RON, one);
        numberOfBillsByType.put(Bill.Type.FIVE_RON, five);
        numberOfBillsByType.put(Bill.Type.TEN_RON, ten);
        numberOfBillsByType.put(Bill.Type.FIFTY_RON, fifty);
        numberOfBillsByType.put(Bill.Type.ONEHUNDRED_RON, oneHundred);
    }

    /**
     * It takes a type of bill and it return it's representation
     * in array index (1RON->0 | 5RON->1 | 10RON->2 | etc.)
     *
     * @param type -> bill type to be converted
     * @return -> index in array
     */
    public int billToArrayIndex(Bill.Type type) {
        if(type.getLabelValue() == 1)
            return 0;
        if(type.getLabelValue() == 5)
            return 1;
        if(type.getLabelValue() == 10)
            return 2;
        if(type.getLabelValue() == 50)
            return 3;
        if(type.getLabelValue() == 100)
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
            modifyAllBills(billsUsedHistory[currentAmount - 1], 1);
        } else {
            modifyAllBills(billsUsedHistory[currentAmount], 1);
        }
        //Get base combination
        System.arraycopy(billsUsedHistory[currentAmount - billValue],
                0, billsUsedHistory[currentAmount], 0, 5);
        //Add the bill used
        billsUsedHistory[currentAmount][billToArrayIndex(billType)] =
                billsUsedHistory[currentAmount - billValue][billToArrayIndex(billType)] + 1;
        //Subtract combination from ATM cash
        modifyAllBills(billsUsedHistory[currentAmount], -1);
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
     * @return -> array with the bills requested or -1 in case of failure
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

    public HashMap<Bill.Type, Integer> withdrawalRequestAsMap(int cashAmount){
        int []bills = withdrawalRequestAsArray(cashAmount);
        HashMap<Bill.Type, Integer> typeToNrOfBills = new HashMap<>();
        int cont = 0;
        for (Bill.Type billType : Bill.Type.values()) {
            typeToNrOfBills.put(billType,bills[cont++]);
        }
        return typeToNrOfBills;
    }
    public HashMap<Bill.Type, Integer> getNumberOfBillsByType() {
        return numberOfBillsByType;
    }
}
