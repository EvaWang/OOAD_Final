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
import java.util.List;
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
    Booking newBooking(@RequestBody Map<String, String> bookingObj) throws ParseException {

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date startDate = formatter1.parse(bookingObj.get("StartDate"));
        Date endDate = formatter1.parse(bookingObj.get("EndDate"));

        Booking newBooking = new Booking();
        newBooking.setHotelId(Integer.parseInt(bookingObj.get("HotelId")));
        newBooking.setOrderId(Integer.parseInt(bookingObj.get("OrderId")));
        newBooking.setHotelRoomId(Integer.parseInt(bookingObj.get("HotelRoomId")));
        newBooking.setIsDisabled(Boolean.getBoolean(bookingObj.get("IsDisabled")));
        newBooking.setStartDate(startDate);
        newBooking.setEndDate(endDate);
        return bookingRepository.save(newBooking);
    }

    // Single item
    @GetMapping("/findOne/{id}")
    Booking one(@PathVariable int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }


    @PutMapping("/updateOne/{id}")
    Booking updateBooking(@RequestBody Booking newBooking, @PathVariable int id) {

        return bookingRepository.findById(id)
                .map(updateBooking -> {
                    updateBooking.setStartDate(newBooking.getStartDate());
                    updateBooking.setEndDate(newBooking.getEndDate());
                    return bookingRepository.save(updateBooking);
                }).orElseThrow(() -> new NotFoundException(id));

    }

    @PostMapping(path="/testAdd", consumes = "application/json")
    Ordering newOrdering(@RequestBody Map<String, String> orderingObj) throws ParseException {

        Ordering newOrdering = new Ordering();
        //newOrdering.setBookingId(Integer.parseInt(orderingObj.get("BookingId")));
        newOrdering.setUserId(Integer.parseInt(orderingObj.get("UserId")));
        newOrdering.setTotal(Integer.parseInt(orderingObj.get("Total")));
        newOrdering.setDiscount(Double.parseDouble(orderingObj.get("Discount")));
        newOrdering.setMemo(orderingObj.get("Memo"));
        return orderingRepository.save(newOrdering);
    }
}
