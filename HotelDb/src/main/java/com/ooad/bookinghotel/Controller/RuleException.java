package com.ooad.bookinghotel.Controller;

public class RuleException extends RuntimeException {

    RuleException(int id, String message) {
        super("Data id: " + id+", message: "+message);
    }
}