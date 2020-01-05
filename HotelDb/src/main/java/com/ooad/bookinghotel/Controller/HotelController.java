package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.HotelRoomRepository;
import com.ooad.bookinghotel.HotelDb.HotelView;
import com.ooad.bookinghotel.HotelDb.HotelViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/Hotel") // This means URL's start with /demo (after Application path)
public class HotelController {

    @Autowired
    private HotelViewRepository hotelRepository;

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<HotelView> getAllHotels(@RequestParam(value = "stars", required = false) List<Integer> stars,
                                 @RequestParam(value = "locality", required = false) String locality,
                                 @RequestParam(value = "roomType", required = false) Integer roomType,
                                 @RequestParam(value = "startDate", required = false) String startDateObj,
                                 @RequestParam(value = "endDate", required = false) String endDateObj,
                                 @RequestParam("page") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                 @RequestParam(value = "sortKey", required = false) String sortKey,
                                 @RequestParam(value = "sortDesc", required = false) Boolean sortDesc) {

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        // today
        Date startDate = calendar.getTime();
        String startDateString =  startDate.toInstant().atZone(TimeZone.getTimeZone("Asia/Taipei").toZoneId()).toLocalDate().toString();

        calendar.add(Calendar.MONTH, 1);
        Date endDate = calendar.getTime();
        String endDateString =  endDate.toInstant().atZone(TimeZone.getTimeZone("Asia/Taipei").toZoneId()).toLocalDate().toString();
        System.out.println("endDateString:"+endDateString);


        try {
            if(startDateObj!=null){
                startDate = formatter1.parse(startDateObj);
            }
        } catch (ParseException e) {
            // do nothing
//            e.printStackTrace();
        }

        try {
            if(endDateObj!=null){
                endDate = formatter1.parse(endDateObj);
            }
        } catch (ParseException e) {
            // do nothing
//            e.printStackTrace();
        }

        Pageable pageable = null;
        if(size<0){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }else {
            pageable = PageRequest.of(page, size);
        }

        if(sortKey != null && sortKey.isEmpty()==false ){
            Sort sort = Sort.by(sortKey);
            if(sortDesc){
                sort = sort.descending();
            }else {
                sort = sort.ascending();
            }

            pageable = PageRequest.of(page, size, sort);
        }

        Boolean filterStar = stars!=null && stars.size()>0;

        return hotelRepository.searchHotel(filterStar, stars, locality, roomType,startDateString, endDateString, pageable);
    }

    @GetMapping(path="/findById")
    public @ResponseBody
    List<HotelView> test(@RequestParam(value = "ids") List<Integer> ids,
                             @RequestParam(value = "startDate") String startDate,
                             @RequestParam(value = "endDate") String endDate) {

        return hotelRepository.findOne(ids, startDate, endDate);
    }

//    http://localhost:8080/Hotel/test?jsonFileId=5&roomIds=2854, 2852
//    @GetMapping(path="/test")
////    public @ResponseBody
////    List<HotelRoom> test(@RequestParam List<Integer> roomIds, @RequestParam Integer jsonFileId) {
////        // This returns a JSON or XML with the users
////        return hotelRoomRepository.findByRoomIds(roomIds, jsonFileId);
////    }
}