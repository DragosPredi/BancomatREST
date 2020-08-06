package com.atm.backend.infrastructure;

import java.time.LocalDateTime;
import java.util.Map;

public class TransactionBlueprint {
    Map<String, Integer> billsUsed;
    LocalDateTime timeAtWhichTransactionOccurred;

    public TransactionBlueprint(Map<String, Integer> billsUsed, LocalDateTime timeAtWhichTransactionOccurred) {
        this.billsUsed = billsUsed;
        this.timeAtWhichTransactionOccurred = timeAtWhichTransactionOccurred;
    }

    public Map<String, Integer> getBillsUsed() {
        return billsUsed;
    }

    public void setBillsUsed(Map<String, Integer> billsUsed) {
        this.billsUsed = billsUsed;
    }

    public LocalDateTime getTimeAtWhichTransactionOccurred() {
        return timeAtWhichTransactionOccurred;
    }

    public void setTimeAtWhichTransactionOccurred(LocalDateTime timeAtWhichTransactionOccurred) {
        this.timeAtWhichTransactionOccurred = timeAtWhichTransactionOccurred;
    }
}
