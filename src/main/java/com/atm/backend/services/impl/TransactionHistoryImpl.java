package com.atm.backend.services.impl;

import com.atm.backend.infrastructure.SoldInquiryDto;
import com.atm.backend.services.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class TransactionHistoryImpl implements TransactionHistoryService {
    List<Map<String, Integer>> transactionHistory;

    @Value("${transaction_history_file}")
    String filename;

    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(),
                new NotNull(),
                new NotNull(),
                new NotNull(),
                new NotNull()
        };

        return processors;
    }

    public TransactionHistoryImpl() {
        this.transactionHistory = new LinkedList<>();
    }

    @Override
    public void addRecord(SoldInquiryDto data) {
        transactionHistory.add(data.getBills());
    }

    static String[] getHeader(){
        return new String[]{"ONE_RON(1)", "FIVE_RON(5)", "TEN_RON(10)", "FIFTY_RON(50)", "ONEHUNDRED_RON(100)"};
    }

    @Override
    public void saveHistoryToFileAsCsv() throws IOException {
        try (ICsvMapWriter mapWriter = new CsvMapWriter(new FileWriter(filename),
                CsvPreference.STANDARD_PREFERENCE)) {

            final CellProcessor[] processors = getProcessors();

            // write the header
            mapWriter.writeHeader(getHeader());

            // write the maps
            transactionHistory.forEach(map -> {
                try {
                    mapWriter.write(map, getHeader(), processors);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
    }
}
