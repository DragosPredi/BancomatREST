package com.atm.backend.myUtils;

import com.atm.backend.bills.Bill;

import java.util.HashMap;
import java.util.Map;

public class MyUtils {

    public static void sendEmail(String emailAddr, String emailContent) {
        System.out.println(emailContent + "sent to " + emailAddr);
    }

    public static HashMap<String, Integer> billTypeToStringTypeMapConverter(HashMap<Bill.Type, Integer> numberOfBillsByType){
        HashMap<String, Integer> newMap = new HashMap<>();

        for(Bill.Type type : Bill.Type.values()){
            String st = type.toString() + "(" + type.getLabelValue() + ")";
            newMap.put(st, numberOfBillsByType.get(type));
        }

        return newMap;
    }

}
