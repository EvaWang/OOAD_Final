package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface HotelRepository extends PagingAndSortingRepository<Hotel, Integer>, JpaSpecificationExecutor {

    @Query(
            value ="select * " +
                    "from hotel_info " +
                    "where address LIKE %:searchKey% or name like %:searchKey% or locality like %:searchKey%",
            nativeQuery = true)
    Page<Hotel> findAllDetail(String searchKey, Pageable pageable);

    @Query(
            value ="select hotel_info.id, hotel_info.star, hotel_info.locality, hotel_info.address " +
                    "from hotel_info ",
            nativeQuery = true)
    Page<Hotel> findAll(Pageable pageable);

    @Query(
            value ="select hotel.*, hotel_room.price, hotel_room.room_type, hotel_room.quantity " +
                    "from hotel inner join hotel_room on hotel_room.hotel_id = hotel.json_file_id " +
                    "WHERE (:stars is null or star IN (:stars)) " +
                    "AND  (:locality is null or locality = :locality) " +
                    "AND (:roomType is null or room_type = :roomType) AND (:startDate is null) AND (:endDate is null)",
            nativeQuery = true)
    Page<Hotel> searchHotel(List<Integer> stars, String locality, Integer roomType, Date startDate, Date endDate, Pageable pageable);


    List<Hotel> findByJsonFileId(Integer jsonFileId);

}