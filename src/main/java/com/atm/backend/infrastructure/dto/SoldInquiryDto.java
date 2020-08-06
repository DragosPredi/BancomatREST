package com.atm.backend.infrastructure.dto;

import java.util.Map;


public class SoldInquiryDto {

    Map<String, Integer> bills;
    String message;

    public SoldInquiryDto() {

    }

    public SoldInquiryDto(Map<String, Integer> bills, String message) {
        this.bills = bills;
        this.message = message;
    }


    public Map<String, Integer> getBills() {
        return bills;
    }

    public void setBills(Map<String, Integer> bills) {
        this.bills = bills;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
