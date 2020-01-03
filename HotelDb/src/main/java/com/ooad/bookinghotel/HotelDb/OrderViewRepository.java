package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrderViewRepository extends CrudRepository<OrderView, Integer> {

    @Query(
            value = "select * from order_info where user_id=:userId AND (:orderId is null or :orderId = id) ",
            nativeQuery = true)
    Page<OrderView> searchDetail(Integer userId,Integer orderId,Pageable pageable);

//    id as hotel_room_id 為了生成unique id的假動作
    @Query(
            value =" select id as hotel_room_id, id, create_time, update_time, discount, memo, total, user_id, is_disabled, " +
                    " end_date, start_date, is_paid, json_file_id, star, locality, address, name, " +
                    " 0 as booked_is_disabled, 0 as room_type, 0 as price, " +
                    " SUM(booked_quantity) as booked_quantity " +
                    " from order_info " +
                    " where user_id=:userId AND (:orderId is null or :orderId = id) AND booked_is_disabled = 0" +
                    " group by id, create_time, update_time, discount, memo, total, user_id, is_disabled, " +
                    " end_date, start_date, is_paid, json_file_id, star, locality, address, name",
            nativeQuery = true)
    Page<OrderView> searchOrder(Integer userId, Integer orderId,Pageable pageable);

}