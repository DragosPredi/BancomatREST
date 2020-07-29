package com.atm.backend.infrastructure;

import java.util.HashMap;

public class MyUtils {

    public static void sendEmail(String emailAddr, String emailContent) {
        System.out.println(emailContent + "sent to " + emailAddr);
    }

    public static HashMap<String, Integer> billTypeToStringTypeMapConverter(HashMap<Bill.Type, Integer> numberOfBillsByType) {
        HashMap<String, Integer> newMap = new HashMap<>();

        for (Bill.Type type : Bill.Type.values()) {
            String st = type.toString() + "(" + type.getLabelValue() + ")";
            newMap.put(st, numberOfBillsByType.get(type));
        }

        return newMap;
    }

}
