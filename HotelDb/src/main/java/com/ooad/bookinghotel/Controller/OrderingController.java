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

    public Boolean CheckLegalDateRegion (Date StartDate,Date EndDate) {
        Date now = new Date();
        if (StartDate.compareTo(EndDate) > 0) {
            System.out.println("Illegal time region");
            return false;
        } else if (StartDate.compareTo(now) < 0) {
            System.out.println(StartDate + " have passed");
            return false;
        } else {
            return true;
        }
    }

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

    @PostMapping(path="/add", consumes = "application/json")
    Ordering newOrdering(@RequestBody Map<String, String> orderingObj) throws ParseException {

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = formatter1.parse(orderingObj.get("StartDate"));
        Date endDate = formatter1.parse(orderingObj.get("EndDate"));

        Ordering newOrdering = new Ordering();
        newOrdering.setUserId(Integer.parseInt(orderingObj.get("UserId")));
//        newOrdering.setDiscount(Double.parseDouble(orderingObj.get("Discount")));
        newOrdering.setDiscount(1.0);
        newOrdering.setStartDate(startDate);
        newOrdering.setEndDate(endDate);
        newOrdering.setMemo(orderingObj.get("Memo"));
        newOrdering.setIsDisabled(false);
        newOrdering.setIsPaid(false);
        String HotelRoomTypes = orderingObj.get("HotelRoomTypes");
        String[] roomTypeList = HotelRoomTypes.split(",");

        ArrayList bookingArray = new ArrayList();
        Integer hotelId = Integer.parseInt(orderingObj.get("HotelId"));

        List<String> roomIdList_toList = Arrays.asList(roomTypeList);

        List<Integer> int_roomIdList_toList = new ArrayList<>();
        for(String s : roomIdList_toList) int_roomIdList_toList.add(Integer.valueOf(s));

        List<HotelRoom> roomIdList_checked = new ArrayList<>();
        roomIdList_checked =  hotelRoomRepository.findByRoomTypes(int_roomIdList_toList, hotelId);
        Integer Total = 0;

        Dictionary<Integer, Integer> roomDict = new Hashtable();
        Dictionary<Integer, Integer> roomIdDict = new Hashtable();
        for(HotelRoom item : roomIdList_checked){
            roomDict.put(item.getRoomType(), item.getPrice());
            roomIdDict.put(item.getRoomType(), item.getId());
        }
        Integer roomPrice = 0;
        for(Integer i : int_roomIdList_toList){
            //System.out.println(roomDict.get(i));
            roomPrice = roomDict.get(i);
            Total = Total + roomPrice;
        }

        Long days = (endDate.getTime() - startDate.getTime())/(24*60*60*1000);
        int Days = Math.toIntExact(days);

        newOrdering.setTotal(Total*Days);
        newOrdering = orderingRepository.save(newOrdering);

        for(String roomType: roomTypeList){
            Booking newBooking = new Booking();
            newBooking.setHotelId(hotelId);
            newBooking.setHotelRoomId(roomIdDict.get(Integer.parseInt(roomType)));
            newBooking.setOrderId(newOrdering.getId());
            newBooking.setIsDisabled(false);
            bookingArray.add(newBooking);
        }

        bookingRepository.saveAll(bookingArray);

        return newOrdering;
    }

    @PostMapping("/updateOne/{id}")
    Ordering updateOrderingByDate(@RequestBody Map<String, String> orderingObj,@PathVariable int id) throws ParseException {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

        //Get information "startDate"
        Date startDate = formatter1.parse(orderingObj.get("StartDate"));

        //Get information "endDate"
        Date endDate = formatter1.parse(orderingObj.get("EndDate"));

        Ordering originalOrdering = orderingRepository.findById(id).get();

        //The order has been disabled
        if (originalOrdering.getIsDisabled() == true) {
            System.out.println("This order has been disabled");
            return originalOrdering;
        }

        if (originalOrdering.getIsPaid() == true) {
            System.out.println("This order has been paid,can't modified");
            return originalOrdering;
        }

        if (CheckLegalDateRegion(startDate,endDate) == false) {
            return originalOrdering;
        }

        Date originalStartDate = originalOrdering.getStartDate();
        Date originalEndDate = originalOrdering.getEndDate();

        Long originaldays = (originalEndDate.getTime() - originalStartDate.getTime())/(24*60*60*1000);

        int originalDays = Math.toIntExact(originaldays);

        originalOrdering.setStartDate(startDate);
        originalOrdering.setEndDate(endDate);

        Long days = (endDate.getTime() - startDate.getTime())/(24*60*60*1000);

        int newDays = Math.toIntExact(days);

        Integer originalTotal = originalOrdering.getTotal();

        Integer newTotal = originalTotal*newDays/originalDays;

        originalOrdering.setTotal(newTotal);

        orderingRepository.save(originalOrdering);
        return originalOrdering;
    }

}
