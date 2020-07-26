package com.atm.backend.services;

import com.atm.backend.bills.Bill;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AtmServiceImpl implements AtmService {
    private final HashMap<Bill.Type, Integer> numberOfBillsByType;

    public AtmServiceImpl() {
        numberOfBillsByType = new HashMap<>();
        setATMState(new int[]{100, 100, 100, 50, 50});
    }

    /**
     * Add cash to ATM
     *
     * @param type     -> type of bill to be added
     * @param quantity -> number of bills
     */
    public void fillUpOneTypeBill(Bill.Type type, int quantity) {
        int leftOverCash = numberOfBillsByType.get(type);
        numberOfBillsByType.put(type, quantity + leftOverCash);
    }

    /**
     * Getter for the amount of a certain type of bill
     *
     * @param type -> bill to be investigated
     * @return -> number of bills of requested type
     */
    public int getOneTypeBillQuantity(Bill.Type type) {
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
     */
    public void addBills(int[] billsToBeModified) {

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

    /**
     * Set the ATM with a specified number of cash
     *
     * @param billsArr representing the number of bills
     *                 in ascending order (One, Five, Ten, etc.)
     */
    public void setATMState(int [] billsArr) {
        int cont = 0;
        for(Bill.Type type : Bill.Type.values()){
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
            addBills(billsUsedHistory[currentAmount - 1]);
        } else {
            addBills(billsUsedHistory[currentAmount]);
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
    public HashMap<Bill.Type, Integer> getNumberOfBillsByTypeAsMap() {
        return numberOfBillsByType;
    }
}
