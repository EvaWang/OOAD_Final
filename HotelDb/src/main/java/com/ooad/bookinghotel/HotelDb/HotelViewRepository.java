package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface HotelViewRepository extends PagingAndSortingRepository<HotelView, Integer> {
    @Query(
            value ="select * " +
                    "from hotel_info " +
                    "where address LIKE %:searchKey% or name like %:searchKey% or locality like %:searchKey%",
            nativeQuery = true)
    Page<HotelView> findAllDetail(String searchKey, Pageable pageable);

    @Query(
            value ="select * " +
                    "from hotel_info ",
            nativeQuery = true)
    Page<HotelView> findAll(Pageable pageable);

    @Query(value ="select booked_hotel_info.id, booked_hotel_info.star, booked_hotel_info.locality, booked_hotel_info.address, " +
                    "booked_hotel_info.json_file_id, booked_hotel_info.name, " +
                    "max(CASE WHEN booked_hotel_info.room_type =1 THEN booked_hotel_info.quantity ELSE 0 END) AS single_room, " +
                    "max(CASE WHEN booked_hotel_info.room_type =2 THEN booked_hotel_info.quantity ELSE 0 END) AS double_room, " +
                    "max(CASE WHEN booked_hotel_info.room_type =4 THEN booked_hotel_info.quantity ELSE 0 END) AS quad_room, " +
                    "max(CASE WHEN booked_hotel_info.room_type =1 THEN booked_hotel_info.price ELSE 0 END) AS single_room_price, " +
                    "max(CASE WHEN booked_hotel_info.room_type =2 THEN booked_hotel_info.price ELSE 0 END) AS double_room_price, " +
                    "max(CASE WHEN booked_hotel_info.room_type =4 THEN booked_hotel_info.price ELSE 0 END) AS quad_room_price, " +
                    "max(CASE WHEN booked_hotel_info.room_type =1 THEN booked_hotel_info.booked_quantity ELSE 0 END) AS booked_single_room, " +
                    "max(CASE WHEN booked_hotel_info.room_type =2 THEN booked_hotel_info.booked_quantity ELSE 0 END) AS booked_double_room, " +
                    "max(CASE WHEN booked_hotel_info.room_type =4 THEN booked_hotel_info.booked_quantity ELSE 0 END) AS booked_quad_room " +
                    " from booked_hotel_info " +
                    " where (dt between :startDate and :endDate) and (:checkStar is False or star IN (:stars)) " +
                    " AND  (:locality is null or locality like %:locality% or address LIKE %:locality% or name like %:locality%) " +
                    " and  (:roomType is null or 1=1) " +
                    " group by booked_hotel_info.id, booked_hotel_info.star, booked_hotel_info.locality, booked_hotel_info.address, booked_hotel_info.json_file_id, booked_hotel_info.name " +
                    " HAVING (:roomType is null) or ((1 <> :roomType or (single_room-booked_single_room) > 0) \n" +
                    " and ( 2 <> :roomType or (double_room-booked_double_room) > 0) \n" +
                    " and ( 4 <> :roomType or (quad_room-booked_quad_room) > 0))",
            countQuery = "select distinct count(id) over() from booked_hotel_info " +
                    " where (dt between :startDate and :endDate) and (:checkStar is False or star IN (:stars)) " +
                    " AND  (:locality is null or locality like %:locality% or address LIKE %:locality% or name like %:locality%) " +
                    " and  (:roomType is null or 1=1) " +
                    " group by booked_hotel_info.id, booked_hotel_info.star, booked_hotel_info.locality, booked_hotel_info.address, booked_hotel_info.json_file_id, booked_hotel_info.name " +
                    " HAVING (:roomType is null) or (" +
                    "(1 <> 4 or (max(CASE WHEN booked_hotel_info.room_type =1 THEN booked_hotel_info.quantity ELSE 0 END) - max(CASE WHEN booked_hotel_info.room_type =1 THEN booked_hotel_info.booked_quantity ELSE 0 END)) > 0) \n" +
                    "and ( 2 <> 4 or (max(CASE WHEN booked_hotel_info.room_type =2 THEN booked_hotel_info.quantity ELSE 0 END) - max(CASE WHEN booked_hotel_info.room_type =2 THEN booked_hotel_info.booked_quantity ELSE 0 END)) > 0) \n" +
                    "and ( 4 <> 4 or (max(CASE WHEN booked_hotel_info.room_type =4 THEN booked_hotel_info.quantity ELSE 0 END) - max(CASE WHEN booked_hotel_info.room_type =4 THEN booked_hotel_info.booked_quantity ELSE 0 END)) > 0))",
            nativeQuery = true)
    Page<HotelView> searchHotel(Boolean checkStar, List<Integer> stars, String locality, Integer roomType, String startDate, String endDate, Pageable pageable);

    @Query(value = "select booked_hotel_info.id, booked_hotel_info.star, booked_hotel_info.locality, booked_hotel_info.address, " +
            "booked_hotel_info.json_file_id, booked_hotel_info.name, " +
            "max(CASE WHEN booked_hotel_info.room_type =1 THEN booked_hotel_info.quantity ELSE 0 END) AS single_room, " +
            "max(CASE WHEN booked_hotel_info.room_type =2 THEN booked_hotel_info.quantity ELSE 0 END) AS double_room, " +
            "max(CASE WHEN booked_hotel_info.room_type =4 THEN booked_hotel_info.quantity ELSE 0 END) AS quad_room, " +
            "max(CASE WHEN booked_hotel_info.room_type =1 THEN booked_hotel_info.price ELSE 0 END) AS single_room_price, " +
            "max(CASE WHEN booked_hotel_info.room_type =2 THEN booked_hotel_info.price ELSE 0 END) AS double_room_price, " +
            "max(CASE WHEN booked_hotel_info.room_type =4 THEN booked_hotel_info.price ELSE 0 END) AS quad_room_price, " +
            "max(CASE WHEN booked_hotel_info.room_type =1 THEN booked_hotel_info.booked_quantity ELSE 0 END) AS booked_single_room, " +
            "max(CASE WHEN booked_hotel_info.room_type =2 THEN booked_hotel_info.booked_quantity ELSE 0 END) AS booked_double_room, " +
            "max(CASE WHEN booked_hotel_info.room_type =4 THEN booked_hotel_info.booked_quantity ELSE 0 END) AS booked_quad_room " +
            " from booked_hotel_info " +
            " where dt between :startDate and :endDate " +
            " and  booked_hotel_info.json_file_id IN (:id )  " +
            " group by booked_hotel_info.id, booked_hotel_info.star, booked_hotel_info.locality, booked_hotel_info.address, booked_hotel_info.json_file_id, booked_hotel_info.name ",
    nativeQuery = true)
    List<HotelView> findOne(List<Integer> id, String startDate, String endDate);

}
