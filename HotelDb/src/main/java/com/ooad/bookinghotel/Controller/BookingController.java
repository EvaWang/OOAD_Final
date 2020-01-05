package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.Booking;
import com.ooad.bookinghotel.HotelDb.BookingRepository;
import com.ooad.bookinghotel.HotelDb.Ordering;
import com.ooad.bookinghotel.HotelDb.OrderingRepository;
import com.ooad.bookinghotel.HotelDb.HotelDbApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/Booking") // This means URL's start with /demo (after Application path)
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(HotelDbApplication.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private OrderingRepository orderingRepository;


//    debug use
//    @GetMapping("/all")
//    Iterable<User> all() {
//        return userRepository.findAll();
//    }

//Post Sample
//    {
//        "HotelId": 0,
//            "HotelRoomId": 0,
//            "OrderId": 0,
//            "StartDate": "2019-12-20T00:00:00",
//            "EndDate": "2019-12-21T00:00:00",
//            "IsDisabled": false
//    }
    @PostMapping(path="/add", consumes = "application/json")
    Booking newBooking(@RequestBody Map<String, String> bookingObj)  {
        Booking newBooking = new Booking();
        newBooking.setHotelId(Integer.parseInt(bookingObj.get("HotelId")));
        newBooking.setOrderId(Integer.parseInt(bookingObj.get("OrderId")));
        newBooking.setHotelRoomId(Integer.parseInt(bookingObj.get("HotelRoomId")));
        newBooking.setIsDisabled(Boolean.getBoolean(bookingObj.get("IsDisabled")));
        //newBooking.setStartDate(startDate);
        //newBooking.setEndDate(endDate);
        return bookingRepository.save(newBooking);
    }

    // Single item
    @GetMapping("/findOne/{id}")
    Booking one(@PathVariable int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @PutMapping("/updateOne/{id}")
    Booking updateBooking(@RequestBody Map<String, String> bookingObj, @PathVariable int id) {
        Optional<Booking> findBooking = bookingRepository.findById(id);
        if (findBooking.isPresent() == false) {
            throw new NotFoundException(id);
        }
        Booking Book = findBooking.get();

        if (Book.getIsDisabled()) {
            throw new RuleException(id,"This booking has been disabled");
        }

        Integer orderId = Book.getOrderId();

        Ordering ordering = orderingRepository.findById(orderId).get();

        if (ordering.getIsDisabled() || ordering.getIsPaid()) {
            throw new RuleException(id,"Can't modify this booking");
        }

        Boolean Disabled = Boolean.parseBoolean(bookingObj.get("isDisabled"));

        Book.setIsDisabled(Disabled);

        return bookingRepository.save(Book);
    }

}
