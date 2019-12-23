package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderingRepository extends CrudRepository<Ordering, Integer> {

    List<Ordering> findByUserId(Integer userId);
}
