package com.atm.backend.infrastructure;

import com.sun.tools.javac.util.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TransactionHistoryDto {
    List<Pair<Map<String, Integer>, LocalDateTime>> list;

    public TransactionHistoryDto(List<Pair<Map<String, Integer>, LocalDateTime>> list) {
        this.list = list;
    }

    public List<Pair<Map<String, Integer>, LocalDateTime>> getList() {
        return list;
    }

    public void setList(List<Pair<Map<String, Integer>, LocalDateTime>> list) {
        this.list = list;
    }
}
