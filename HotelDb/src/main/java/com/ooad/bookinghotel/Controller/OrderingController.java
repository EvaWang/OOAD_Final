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
@RequestMapping(path="/Ordering") // This means URL's start with /demo (after Application path)
public class OrderingController {
    private static final Logger log = LoggerFactory.getLogger(HotelDbApplication.class);

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private OrderingRepository orderingRepository;
    @Autowired
    private HotelRoomRepository hotelRoomRepository;

//    @PostMapping(path="/add", consumes = "application/json")
//    Ordering newordering (@RequestBody Map<String, String> OrderingObj) throws ParseException {
//
//        Ordering newordering = new Ordering();
//
//        //newordering.setBookingId(Integer.parseInt(OrderingObj.get("BookingId")));
//        newordering.setUserId(Integer.parseInt(OrderingObj.get("UserId")));
//        newordering.setTotal(Integer.parseInt(OrderingObj.get("Total"))); //May be modified
//        newordering.setDiscount(Double.parseDouble(OrderingObj.get("Discount")));
//        newordering.setMemo(OrderingObj.get("Memo"));
//
//        return orderingRepository.save(newordering);
//    }

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
    
    @PutMapping("/updateOne/{id}")
    Ordering updateOrderingByInformation(@RequestBody Ordering newordering,@PathVariable int id) {

        return orderingRepository.findById(id)
                .map(updateOrdering -> {
                    updateOrdering.setDiscount(newordering.getDiscount());
                    updateOrdering.setUserId(newordering.getUserId());
                    updateOrdering.setTotal(newordering.getTotal());
                    updateOrdering.setMemo(newordering.getMemo());
                    return orderingRepository.save(updateOrdering);
                }).orElseThrow(() -> new NotFoundException(id));
    }

    @PostMapping(path="/add", consumes = "application/json")
    Ordering newOrdering(@RequestBody Map<String, String> orderingObj) throws ParseException {

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = formatter1.parse(orderingObj.get("StartDate"));
        Date endDate = formatter1.parse(orderingObj.get("EndDate"));

        Ordering newOrdering = new Ordering();
        newOrdering.setUserId(Integer.parseInt(orderingObj.get("UserId")));
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

    @PostMapping("/updateOne/{id}")
    Ordering updateOrderingByBooking(@RequestBody Map<String, String> orderingObj,@PathVariable int id) throws ParseException {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

        //Get information "startDate"
        Date startDate = formatter1.parse(orderingObj.get("StartDate"));

        //Get information "endDate"
        Date endDate = formatter1.parse(orderingObj.get("EndDate"));

        //Get System Date
        Date now = formatter1.parse(orderingObj.get(new Date()));

        Ordering originalOrdering = new Ordering();

        //The order has been disabled
        if (originalOrdering.getIsDisabled() == true) {
            return originalOrdering;
        }

        //If startDate is over than endDate,then stop modifying
        if (startDate.compareTo(endDate) > 0) {
            return originalOrdering;
        }
        if (startDate.compareTo(now) < 0) {
            return  originalOrdering;
        }

        List<Booking> BookingList = bookingRepository.findByOrderId(id);

        ArrayList bookingArray = new ArrayList();

        for (Booking Book : BookingList) {
            if (Book.getIsDisabled() == false) {
                Booking newBooking = new Booking();
                newBooking.setStartDate(startDate);
                newBooking.setEndDate(endDate);
                newBooking.setOrderId(Book.getOrderId());
                newBooking.setHotelId(Book.getHotelId());
                newBooking.setHotelRoomId(Book.getHotelRoomId());
                newBooking.setIsDisabled(Book.getIsDisabled());
                Book.setIsDisabled(true);
                bookingArray.add(newBooking);
            }
        }

        bookingRepository.saveAll(bookingArray);
        return originalOrdering;
    }

}
