package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRoomRepository  extends CrudRepository<HotelRoom, Integer> {

    /**
     * @param hotelId jsonFileId from related hotel
     * @param roomType
     * @return
     */
    List<HotelRoom> findByHotelIdAndRoomType(Integer hotelId, Integer roomType);

    /**
     * @param roomTypes
     * @param hotelId id from related hotel jsonFileId
     * @return
     */
    @Query(value = "SELECT * FROM hotel_room WHERE hotel_id=:hotelId AND room_type IN (:roomTypes) ", nativeQuery = true)
    List<HotelRoom> findByRoomTypes(@Param("roomTypes")List<Integer> roomTypes, @Param("hotelId")Integer hotelId);
}
