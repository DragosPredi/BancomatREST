package com.atm.backend.services.impl;

import com.atm.backend.infrastructure.TransactionBlueprint;
import com.atm.backend.infrastructure.dto.SoldInquiryDto;
import com.atm.backend.infrastructure.dto.TransactionHistoryDto;
import com.atm.backend.services.TransactionHistoryService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionHistoryImpl implements TransactionHistoryService {
    List<TransactionBlueprint> transactionHistory;

    @Value("${transaction_history_file_cvs}")
    String CvsFilename;
    @Value("${transaction_history_file_pdf}")
    String PdfFilename;


    public TransactionHistoryImpl() {
        this.transactionHistory = new LinkedList<>();
    }

    @Override
    public void addRecord(SoldInquiryDto data) {
        transactionHistory.add(new TransactionBlueprint(data.getBills(), LocalDateTime.now()));
    }

    static String[] getHeader(){
        return new String[]{"ONE_RON(1)", "FIVE_RON(5)", "TEN_RON(10)", "FIFTY_RON(50)", "ONEHUNDRED_RON(100)"};
    }

    @Override
    public void saveHistoryToFileAsCsv() throws IOException {
        try (ICsvMapWriter mapWriter = new CsvMapWriter(new FileWriter(CvsFilename),
                CsvPreference.STANDARD_PREFERENCE)) {

            final CellProcessor[] processors = getProcessors();

            // write the header
            mapWriter.writeHeader(getHeader());

            // write the maps
            transactionHistory.forEach(oneTransaction -> {
                try {
                    mapWriter.write(oneTransaction.getBillsUsed(), getHeader(), processors);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
    }

    public void saveHistoryToFileAsPdf() throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(PDType1Font.COURIER, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(25, 700);
        for(TransactionBlueprint oneTransaction : transactionHistory){
            contentStream.showText(oneTransaction.getBillsUsed().toString());
            contentStream.newLineAtOffset(0, 10);
        }

        contentStream.endText();
        contentStream.close();


        document.save(PdfFilename);
        document.close();
    }

    public TransactionHistoryDto getTransactionHistory(int minutes){
        LocalDateTime referenceTime = LocalDateTime.now().minusMinutes(minutes);
        return new TransactionHistoryDto(transactionHistory
                .stream()
                .filter(oneTransaction -> oneTransaction.getTimeAtWhichTransactionOccurred().isAfter(referenceTime))
                .collect(Collectors.toList()));
    }
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
}
