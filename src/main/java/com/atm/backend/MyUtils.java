package com.atm.backend;


import com.atm.backend.bills.Bill;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MyUtils {
    /**
     * Continuously asks the user for input until it receives an Int
     *
     * @return -> int
     */
    public static int readPositiveInt() {
        Scanner scanner = new Scanner(System.in);
        int cashAmount = -1;
        try {
            cashAmount = scanner.nextInt();
            if (cashAmount <= 0) {
                throw new InputMismatchException();
            }
        } catch (InputMismatchException e) {
            System.out.println("It would seem the input was faulty, please try again!");
        }
        return cashAmount;
    }

    /**
     * Helper function to print in human readable format the bills requested
     *
     * @param bills -> the bills used
     */
    public static void printBills(int[] bills) {
        int cont = 0;
        if (bills[cont] == Integer.MAX_VALUE) {
            System.out.println("The ATM is out of cash, please try again later!");
        } else {
            for (Bill.Type billType : Bill.Type.values()) {
                System.out.println("Number of " + billType + " is " + bills[cont++]);
            }
        }
    }

    public static String readString() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static void sendEmail(String emailAddr, String emailContent) {
        System.out.println(emailContent + "sent to " + emailAddr);
    }

}
