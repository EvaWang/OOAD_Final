package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderingRepository extends CrudRepository<Ordering, Integer>{

    List<Ordering> findByUserId(Integer userId);

    Optional<Ordering> findByIdAndUserId(Integer id, Integer userId);
}
