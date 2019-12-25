package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

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

//  TODO 檢查剩餘房間數
    @Query(
            value ="SELECT * " +
                    "from hotel_info " +
                    "WHERE ( :checkStar is False or star IN (:stars)) " +
                    "AND  (:locality is null or locality like %:locality% or address LIKE %:locality% or name like %:locality%) " +
                    "AND (:roomType is null or 1=1) " +
                    "AND (:startDate is null or 1=1) " +
                    "AND (:endDate is null or 1=1)",
            nativeQuery = true)
    Page<HotelView> searchHotel(Boolean checkStar, List<Integer> stars, String locality, Integer roomType, Date startDate, Date endDate, Pageable pageable);
}
