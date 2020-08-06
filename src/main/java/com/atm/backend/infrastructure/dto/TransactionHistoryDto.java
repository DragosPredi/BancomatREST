package com.atm.backend.infrastructure.dto;

import com.atm.backend.infrastructure.TransactionBlueprint;

import java.util.List;

public class TransactionHistoryDto {
    List<TransactionBlueprint> list;

    public TransactionHistoryDto(List<TransactionBlueprint> list) {
        this.list = list;
    }

    public List<TransactionBlueprint> getList() {
        return list;
    }

    public void setList(List<TransactionBlueprint> list) {
        this.list = list;
    }
}
