package com.atm.backend.infrastructure;

public abstract class Bill {

    public enum Type {
        ONE_RON(1),
        FIVE_RON(5),
        TEN_RON(10),
        FIFTY_RON(50),
        ONEHUNDRED_RON(100);

        private final int labelValue;

        Type(int labelValue) {
            this.labelValue = labelValue;
        }

        public int getLabelValue() {
            return labelValue;
        }
    }
}
