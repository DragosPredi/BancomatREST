package com.atm.backend.exceptions;

public class NotEnoughMoneyException extends RuntimeException{
    public NotEnoughMoneyException() {
        super("Not enough money in ATM");
    }
}
