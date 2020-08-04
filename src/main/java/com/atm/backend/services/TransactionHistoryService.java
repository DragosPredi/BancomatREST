package com.atm.backend.services;

import com.atm.backend.infrastructure.SoldInquiryDto;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;

public interface TransactionHistoryService {
    void addRecord(SoldInquiryDto data);
    void saveHistoryToFileAsCsv() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException;
}
