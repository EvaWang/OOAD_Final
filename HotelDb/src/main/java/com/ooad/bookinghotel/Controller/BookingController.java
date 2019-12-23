package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/Booking") // This means URL's start with /demo (after Application path)
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(HotelDbApplication.class);

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private OrderingRepository orderingRepository;
    @Autowired
    private HotelRoomRepository hotelRoomRepository;


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

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = formatter1.parse(orderingObj.get("StartDate"));
        Date endDate = formatter1.parse(orderingObj.get("EndDate"));

        Ordering newOrdering = new Ordering();
        newOrdering.setUserId(Integer.parseInt(orderingObj.get("UserId")));
//        newOrdering.setTotal(Integer.parseInt(orderingObj.get("Total")));
        newOrdering.setDiscount(Double.parseDouble(orderingObj.get("Discount")));
        newOrdering.setMemo(orderingObj.get("Memo"));
        //return OrderingRepository.findByHotelIdAndRoomType(HotelId, RoomType)
        //       .orElseThrow(() -> new NotFoundException(HotelId, RoomType));
        String HotelRoomIds = orderingObj.get("HotelRoomIds");
        String[] roomIdList = HotelRoomIds.split(",");

        ArrayList bookingArray = new ArrayList();
        Integer hotelId = Integer.parseInt(orderingObj.get("HotelId"));

        List<String> roomIdList_toList = Arrays.asList(roomIdList);

        List<Integer> int_roomIdList_toList = new ArrayList<>();
        for(String s : roomIdList_toList) int_roomIdList_toList.add(Integer.valueOf(s));

        List<HotelRoom> roomIdList_checked = new ArrayList<>();
        roomIdList_checked =  hotelRoomRepository.findByRoomIds(int_roomIdList_toList, hotelId);
        Integer Total = 0;

        Dictionary<Integer, Integer> roomDict = new Hashtable();
        for(HotelRoom item : roomIdList_checked){
            roomDict.put(item.getId(), item.getPrice());
        }
        Integer roomPrice = 0;
        for(Integer i : int_roomIdList_toList){
            //System.out.println(roomDict.get(i));
            roomPrice = roomDict.get(i);
            Total = Total + roomPrice;
        }

        newOrdering.setTotal(Total);
        newOrdering = orderingRepository.save(newOrdering);

        for(String roomId: roomIdList){
            Booking newBooking = new Booking();
            newBooking.setHotelId(hotelId);
            newBooking.setHotelRoomId(Integer.parseInt(roomId));
            newBooking.setOrderId(newOrdering.getId());
            newBooking.setStartDate(startDate);
            newBooking.setEndDate(endDate);
            newBooking.setIsDisabled(Boolean.getBoolean(orderingObj.get("IsDisabled")));
            //bookingRepository.save(newBooking);
            bookingArray.add(newBooking);
        }

         bookingRepository.saveAll(bookingArray);

        return newOrdering;
    }
}
