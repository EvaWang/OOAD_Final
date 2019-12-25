package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface HotelRepository extends PagingAndSortingRepository<Hotel, Integer> {

    List<Hotel> findByJsonFileId(Integer jsonFileId);

}