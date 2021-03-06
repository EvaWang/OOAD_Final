package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

//import com.ooad.bookinghotel.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface BookingRepository extends CrudRepository<Booking, Integer> {

    List<Booking> findByOrderId(Integer orderId);
}