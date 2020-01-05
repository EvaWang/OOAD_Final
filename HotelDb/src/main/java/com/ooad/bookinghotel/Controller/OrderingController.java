package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private OrderViewRepository orderViewRepository;

    @Autowired
    private HotelViewRepository hotelViewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtToken jwtToken;

    public Boolean CheckLegalDateRegion (Date StartDate,Date EndDate) {
        if (StartDate.compareTo(EndDate) >= 0) {
            System.out.println("Illegal time region");
            return false;
        }  else {
            return true;
        }
    }

    //Single item
    @GetMapping("/findOne/{id}")
    Ordering one (@PathVariable int id) {
        return orderingRepository.findById(id)
                .orElseThrow(()->new NotFoundException(id));
    }

    @GetMapping("/findMyOrders")
    ResponseEntity<?> findMyOrders (@RequestParam(value="orderId", required = false) Integer orderId,
                                    @RequestParam(value="page", defaultValue = "0", required = false) int page,
                                    @RequestParam(value = "size", defaultValue = "-1", required = false) int size,
                                    @RequestParam(value = "sortKey", required = false) String sortKey,
                                    @RequestParam(value = "sortDesc", required = false) Boolean sortDesc) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String credentials = auth.getCredentials().toString();
        Integer  userId = jwtToken.getUserId(credentials);

        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isPresent() == false) {
            throw new NotFoundException(userId);
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

        return ResponseEntity.ok(orderViewRepository.searchOrder(userId, orderId, pageable));
    }



    @GetMapping("/findMyOrderDetails")
    Page<OrderView> findMyOrderDetails (@RequestParam(value="orderId", required = false) Integer orderId,
                                  @RequestParam(value="page", defaultValue = "0", required = false) int page,
                                  @RequestParam(value = "size", defaultValue = "-1", required = false) int size,
                                  @RequestParam(value = "sortKey", required = false) String sortKey,
                                  @RequestParam(value = "sortDesc", required = false) Boolean sortDesc) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String credentials = auth.getCredentials().toString();
        Integer  userId = jwtToken.getUserId(credentials);

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

        return orderViewRepository.searchDetail(userId, orderId, pageable);
    }

    @PostMapping(path="/add", consumes = "application/json")
    Ordering newOrdering(@RequestBody Map<String, String> orderingObj) throws ParseException {

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = formatter1.parse(orderingObj.get("StartDate"));
        Date endDate = formatter1.parse(orderingObj.get("EndDate"));

        Ordering newOrdering = new Ordering();
        if (CheckLegalDateRegion(startDate,endDate) == false) {
            throw new ValidationException("Illegal Date Region");
        }
        String HotelRoomTypes = orderingObj.get("HotelRoomTypes");
        if (HotelRoomTypes == null) {
            throw new ValidationException("No Room Types Entered");
        }
        String[] roomTypeList = HotelRoomTypes.split(",");

        ArrayList bookingArray = new ArrayList();
        Integer hotelId = Integer.parseInt(orderingObj.get("HotelId"));

        List<String> roomIdList_toList = Arrays.asList(roomTypeList);

        List<Integer> int_roomIdList_toList = new ArrayList<>();

        //Count the rooms needed for this order
        Integer RoomsNeeded_Single = 0;
        Integer RoomsNeeded_Double = 0;
        Integer RoomsNeeded_Quad = 0;
        for(String s : roomIdList_toList) {
            int_roomIdList_toList.add(Integer.valueOf(s));
            Integer Value = Integer.valueOf(s);
            if (Value == 1) RoomsNeeded_Single = RoomsNeeded_Single + 1;
            else if (Value == 2) RoomsNeeded_Double = RoomsNeeded_Double + 1;
            else if (Value == 4) RoomsNeeded_Quad = RoomsNeeded_Quad + 1;
        }


        List<HotelRoom> roomIdList_checked = new ArrayList<>();
        List<Integer> HotelIds = new ArrayList<>();
        HotelIds.add(hotelId);
        roomIdList_checked = hotelRoomRepository.findByRoomTypes(int_roomIdList_toList, hotelId);
        List<HotelView> CheckIfExist = hotelViewRepository.findOne(HotelIds,orderingObj.get("StartDate"),orderingObj.get("EndDate"));

        //Check if this hotel has enough rooms for this order
        for (HotelView RoomView:CheckIfExist) {
            if ((RoomView.getSingleRoom() - RoomView.getBookedSingleRoom()) < RoomsNeeded_Single) {
                throw new ValidationException("Not enough single rooms in this hotel between " + startDate + " ~ " + endDate);
            } else if ((RoomView.getDoubleRoom() - RoomView.getBookedDoubleRoom()) < RoomsNeeded_Double) {
                throw new ValidationException("Not enough double rooms in this hotel between " + startDate + " ~ " + endDate);
            } else if ( (RoomView.getQuadRoom() - RoomView.getBookedQuadRoom()) < RoomsNeeded_Quad) {
                throw new ValidationException("Not enough quad rooms in this hotel between " + startDate + " ~ " + endDate);
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String credentials = auth.getCredentials().toString();
        Integer  userId = jwtToken.getUserId(credentials);

        newOrdering.setUserId(userId);
//        newOrdering.setDiscount(Double.parseDouble(orderingObj.get("Discount")));
        newOrdering.setDiscount(1.0);
        newOrdering.setStartDate(startDate);
        newOrdering.setEndDate(endDate);
        newOrdering.setMemo(orderingObj.get("Memo"));
        newOrdering.setIsDisabled(false);
        newOrdering.setIsPaid(false);

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

    @PostMapping("/updateByDate/{id}")
    Ordering updateOrderingByDate(@RequestBody Map<String, String> orderingObj,@PathVariable int id) throws ParseException {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = formatter1.parse(orderingObj.get("StartDate"));
        Date endDate = formatter1.parse(orderingObj.get("EndDate"));

        Optional<Ordering> findOrder = orderingRepository.findById(id);
        if(findOrder.isPresent() == false){
            return findOrder.orElseThrow(() -> new NotFoundException(id));
        }

        Ordering originalOrdering = findOrder.get();

        //The order has been disabled
        if (originalOrdering.getIsDisabled() == true) {
            throw new RuleException(id, "This Order has been disabled.");
        }

        if (originalOrdering.getIsPaid() == true) {
            throw new RuleException(id, "This Order has been paid,can't modified");
        }

        if (CheckLegalDateRegion(startDate,endDate) == false) {
            throw new ValidationException("Illegal Date Region");
        }

        List<Booking> BookingList = bookingRepository.findByOrderId(id);

        Integer RoomsNeeded_Single = 0;
        Integer RoomsNeeded_Double = 0;
        Integer RoomsNeeded_Quad = 0;
        Integer HotelId = 0;

        for (Booking Book:BookingList) {
            HotelId = Book.getHotelId();
            HotelRoom hotelRoom = hotelRoomRepository.findById(Book.getHotelRoomId()).get();
            Integer RoomType = hotelRoom.getRoomType();
            if (RoomType == 1) {
                RoomsNeeded_Single = RoomsNeeded_Single + 1;
            } else if (RoomType == 2) {
                RoomsNeeded_Double = RoomsNeeded_Double + 1;
            } else if (RoomType == 4) {
                RoomsNeeded_Quad = RoomsNeeded_Quad + 1;
            }
        }
        List<Integer> HotelIds = new ArrayList<>();
        HotelIds.add(HotelId);
        List<HotelView> hotelViews = hotelViewRepository.findOne(HotelIds,orderingObj.get("StartDate"),orderingObj.get("EndDate"));
        for (HotelView RoomView:hotelViews) {
            if ((RoomView.getSingleRoom() - RoomView.getBookedSingleRoom()) < 0) {
                throw new ValidationException("Not enough single rooms in this hotel between " + startDate + " ~ " + endDate);
            } else if ((RoomView.getDoubleRoom() - RoomView.getBookedDoubleRoom()) < RoomsNeeded_Double) {
                throw new ValidationException("Not enough double rooms in this hotel between " + startDate + " ~ " + endDate);
            } else if ( (RoomView.getQuadRoom() - RoomView.getBookedQuadRoom()) < RoomsNeeded_Quad) {
                throw new ValidationException("Not enough quad rooms in this hotel between " + startDate + " ~ " + endDate);
            }
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

        Integer newTotal = 0;

        if (originalDays == 0) {
            newTotal = originalTotal*newDays;
        } else {
            newTotal = originalTotal*newDays/originalDays;
        }

        originalOrdering.setTotal(newTotal);

        return orderingRepository.save(originalOrdering);
    }

    @PostMapping("/updateByBooking/{id}")
    Ordering updateOrderByBooking(@RequestBody Map<String, String> orderingObj,@PathVariable int id) throws ParseException{
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();

        Optional<Ordering> findOrder = orderingRepository.findById(id);
        if(findOrder.isPresent() == false){
            return findOrder.orElseThrow(() -> new NotFoundException(id));
        }

        Ordering originalOrdering = findOrder.get();

        if (originalOrdering.getIsPaid() == true) {
            throw new RuleException(id,"This Order has been paid,can't modified");
        }

        if (originalOrdering.getIsDisabled() == true) {
            throw new RuleException(id, "This Order has been disabled.");
        }

        Date startDate = originalOrdering.getStartDate();
        if (CheckLegalDateRegion(now,startDate) == false) {
            throw new ValidationException("Can't modified this Order due to deadline");
        }

        Date endDate = originalOrdering.getEndDate();
        Long days = (endDate.getTime() - startDate.getTime())/(24*60*60*1000);
        int Days = Math.toIntExact(days);

        List<Booking> BookingList = bookingRepository.findByOrderId(id);

        Integer hotelId = 0;

        String HotelRoomTypes = orderingObj.get("HotelRoomTypes");
        if (HotelRoomTypes == null) {
            originalOrdering.setIsDisabled(true);
            return orderingRepository.save(originalOrdering);
        }

        String[] roomTypeList = HotelRoomTypes.split(",");

        ArrayList bookingArray = new ArrayList();

        for (Booking Book: BookingList) {
            hotelId = Book.getHotelId();
            break;
        }

        List<String> roomIdList_toList = Arrays.asList(roomTypeList);

        List<Integer> int_roomIdList_toList = new ArrayList<>();

        //Count the rooms needed for this order
        Integer RoomsNeeded_Single = 0;
        Integer RoomsNeeded_Double = 0;
        Integer RoomsNeeded_Quad = 0;
        for(String s : roomIdList_toList) {
            int_roomIdList_toList.add(Integer.valueOf(s));
            Integer Value = Integer.valueOf(s);
            if (Value == 1) RoomsNeeded_Single = RoomsNeeded_Single + 1;
            else if (Value == 2) RoomsNeeded_Double = RoomsNeeded_Double + 1;
            else if (Value == 4) RoomsNeeded_Quad = RoomsNeeded_Quad + 1;
        }


        List<HotelRoom> roomIdList_checked = hotelRoomRepository.findByRoomTypes(int_roomIdList_toList, hotelId);;
        List<Integer> HotelIds = new ArrayList<>();
        HotelIds.add(hotelId);
        List<HotelView> CheckIfExist = hotelViewRepository.findOne(HotelIds,formatter1.format(startDate),formatter1.format(endDate));

        //Check if this hotel has enough rooms for this order
        for (HotelView RoomView:CheckIfExist) {
            if ((RoomView.getSingleRoom() - RoomView.getBookedSingleRoom()) < RoomsNeeded_Single) {
                throw new ValidationException("Not enough single rooms in this hotel between " + startDate + " ~ " + endDate);
            } else if ((RoomView.getDoubleRoom() - RoomView.getBookedDoubleRoom()) < RoomsNeeded_Double) {
                throw new ValidationException("Not enough double rooms in this hotel between " + startDate + " ~ " + endDate);
            } else if ( (RoomView.getQuadRoom() - RoomView.getBookedQuadRoom()) < RoomsNeeded_Quad) {
                throw new ValidationException("Not enough quad rooms in this hotel between " + startDate + " ~ " + endDate);
            }
        }
        for (Booking Book: BookingList) {
            Book.setIsDisabled(true);
        }
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

        originalOrdering.setTotal(Total*Days);

        for(String roomType: roomTypeList){
            Booking newBooking = new Booking();
            newBooking.setHotelId(hotelId);
            newBooking.setHotelRoomId(roomIdDict.get(Integer.parseInt(roomType)));
            newBooking.setOrderId(originalOrdering.getId());
            newBooking.setIsDisabled(false);
            bookingArray.add(newBooking);
        }

        bookingRepository.saveAll(bookingArray);

        return orderingRepository.save(originalOrdering);
    }

    @PutMapping("/payOrder/{id}")
    Ordering payOrder(@PathVariable int id){
        Optional<Ordering> findOrder = orderingRepository.findById(id);
        if(findOrder.isPresent() == false){
            return findOrder.orElseThrow(() -> new NotFoundException(id));
        }

        Ordering updateOrder = findOrder.get();
        if(updateOrder.getIsDisabled()){
            throw new RuleException(id, "This Order is disabled.");
        }
        if (updateOrder.getIsPaid()) {
            throw new RuleException(id, "This Order has been paid.");
        }

        Date startDate = updateOrder.getStartDate();
        Date now = new Date();

        Boolean canUpdatePay = CheckLegalDateRegion(now, startDate);
        if(canUpdatePay){
            updateOrder.setIsPaid(true);
            orderingRepository.save(updateOrder);
        }else {
            updateOrder.setIsDisabled(true);
            orderingRepository.save(updateOrder);
            throw new RuleException(id, "Payment deadline is passed.");
        }

        return updateOrder;
    }

}
