package com.atm.backend;

public class Driver {

    /**
     * Helper function to complete the transaction, it takes an ATM obj
     * and prints the bills used
     *
     * @param ATM -> ATM object
     */
    public static void transaction(AutomaticTellerMachine ATM) {

        int cashAmount;
        do {
            cashAmount = MyUtils.readPositiveInt();
        } while (cashAmount <= 0);

        MyUtils.printBills(ATM.withdrawalRequestAsArray(cashAmount));
        CashManager.checkATMBalance(ATM);
    }

    public static void main(String[] args) {
        AutomaticTellerMachine ATM = AutomaticTellerMachine.getInstance();
        do {
            System.out.println("What sum would you like to withdraw? Please enter a positive number");
            transaction(ATM);
            System.out.println("Thank you for choosing our services, would you like to try again? (yes/no)");
        } while (MyUtils.readString().equals("yes"));

    }
}
