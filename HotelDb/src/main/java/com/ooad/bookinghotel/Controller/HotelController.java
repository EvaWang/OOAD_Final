package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.Hotel;
import com.ooad.bookinghotel.HotelDb.HotelRepository;
import com.ooad.bookinghotel.HotelDb.HotelRoom;
import com.ooad.bookinghotel.HotelDb.HotelRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/Hotel") // This means URL's start with /demo (after Application path)
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<Hotel> getAllHotels(@RequestParam("page") int page,
                                    @RequestParam("size") int size,
                                 @RequestParam(value = "sortKey", required = false) String sortKey,
                                 @RequestParam(value = "sortDesc", required = false) Boolean sortDesc,
                                 @RequestParam(value = "search", required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);

        if(sortKey != null && sortKey.isEmpty()==false ){
            Sort sort = Sort.by(sortKey);
            if(sortDesc){
                sort = sort.descending();
            }else {
                sort = sort.ascending();
            }

            pageable = PageRequest.of(page, size, sort);
        }

        if(search != null && search.isEmpty()==false){
            return hotelRepository.findAllDetail(search, pageable);
        }else{
            return hotelRepository.findAllDetail(pageable);
        }
    }

//    http://localhost:8080/Hotel/test?jsonFileId=5&roomIds=2854, 2852
    @GetMapping(path="/test")
    public @ResponseBody
    List<HotelRoom> test(@RequestParam List<Integer> roomIds, @RequestParam Integer jsonFileId) {
        // This returns a JSON or XML with the users
        return hotelRoomRepository.findByRoomIds(roomIds, jsonFileId);
    }


}