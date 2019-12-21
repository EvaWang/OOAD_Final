package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.Hotel;
import com.ooad.bookinghotel.HotelDb.HotelRepository;
import com.ooad.bookinghotel.HotelDb.HotelRoom;
import com.ooad.bookinghotel.HotelDb.HotelRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/Hotel") // This means URL's start with /demo (after Application path)
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

//    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Hotel> getAllUsers() {
        // This returns a JSON or XML with the users
        return hotelRepository.findAll();
    }

//    http://localhost:8080/Hotel/test?jsonFileId=5&roomIds=2854, 2852
    @GetMapping(path="/test")
    public @ResponseBody
    List<HotelRoom> test(@RequestParam List<Integer> roomIds, @RequestParam Integer jsonFileId) {
        // This returns a JSON or XML with the users
        return hotelRoomRepository.findByRoomIds(roomIds, jsonFileId);
    }


}