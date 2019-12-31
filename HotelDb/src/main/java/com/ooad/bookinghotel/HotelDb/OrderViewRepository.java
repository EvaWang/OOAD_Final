package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrderViewRepository extends CrudRepository<OrderView, Integer> {

    @Query(
            value ="select * " +
                    "from order_info where user_id=:userId",
            nativeQuery = true)
    Page<OrderView> findAll(Integer userId,Pageable pageable);

}