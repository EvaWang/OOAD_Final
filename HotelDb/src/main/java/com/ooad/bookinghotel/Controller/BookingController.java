package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.Booking;
import com.ooad.bookinghotel.HotelDb.BookingRepository;
import com.ooad.bookinghotel.HotelDb.HotelDbApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/Booking") // This means URL's start with /demo (after Application path)
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(HotelDbApplication.class);

    @Autowired
    private BookingRepository bookingRepository;


//    debug use
//    @GetMapping("/all")
//    Iterable<User> all() {
//        return userRepository.findAll();
//    }

    @PostMapping("/add")
    Booking newBooking(@RequestBody Booking newBooking) {
        return bookingRepository.save(newBooking);
    }

    // Single item

    @GetMapping("/findOne/{id}")
    Booking one(@PathVariable int id) {

        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @PutMapping("/updateOne/{id}")
    Booking updateUser(@RequestBody Booking newBooking, @PathVariable int id) {
        Optional<Booking> booking = bookingRepository.findById(id);


        return bookingRepository.findById(id)
                .map(employee -> {
                    employee.setStartDate(newBooking.getStartDate());
                    employee.setEndDate(newBooking.getEndDate());
                    return bookingRepository.save(employee);
                }).orElseThrow(() -> new NotFoundException(id));
    }

}
