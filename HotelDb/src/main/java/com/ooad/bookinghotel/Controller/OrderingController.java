package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.Hotel;
import com.ooad.bookinghotel.HotelDb.HotelDbApplication;
import com.ooad.bookinghotel.HotelDb.Ordering;
import com.ooad.bookinghotel.HotelDb.OrderingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/Ordering") // This means URL's start with /demo (after Application path)

public class OrderingController {
    private static final Logger log = LoggerFactory.getLogger(HotelDbApplication.class);

    @Autowired
    private OrderingRepository orderingRepository;

    @PostMapping(path="/add", consumes = "application/json")

    Ordering newordering (@RequestBody Map<String, String> OrderingObj) throws ParseException {

        Ordering newordering = new Ordering();

        //newordering.setBookingId(Integer.parseInt(OrderingObj.get("BookingId")));
        newordering.setUserId(Integer.parseInt(OrderingObj.get("UserId")));
        newordering.setTotal(Integer.parseInt(OrderingObj.get("Total"))); //May be modified
        newordering.setDiscount(Double.parseDouble(OrderingObj.get("Discount")));
        newordering.setMemo(OrderingObj.get("Memo"));

        return orderingRepository.save(newordering);
    }

    //Single item
    @GetMapping("/findOne/{id}")
    Ordering one (@PathVariable int id) {
        return orderingRepository.findById(id)
                .orElseThrow(()->new NotFoundException(id));
    }


    @GetMapping("/test")
    List<Ordering> test () {
        return orderingRepository.findByUserId(1);
    }


}
