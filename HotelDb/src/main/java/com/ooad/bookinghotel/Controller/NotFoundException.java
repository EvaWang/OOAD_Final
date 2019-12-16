package com.ooad.bookinghotel.Controller;

public class NotFoundException extends RuntimeException {

    NotFoundException(int id) {
        super("Could not find employee " + id);
    }
}
