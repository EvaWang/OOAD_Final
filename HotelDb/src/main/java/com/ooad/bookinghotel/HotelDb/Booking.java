package com.ooad.bookinghotel.HotelDb;


import javax.persistence.*;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
public class Booking extends BaseDbo {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(updatable = false, nullable = false)
    private Integer hotelId;

    @Column(updatable = false, nullable = false)
    private Integer hotelRoomId;

    @Column(updatable = false, nullable = false)
    private Integer orderId;



    @Column(nullable = false)
    private Boolean isDisabled;


    public Integer getId() {
        return id;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getHotelRoomId() {
        return hotelRoomId;
    }

    public void setHotelRoomId(Integer hotelRoomId) {
        this.hotelRoomId = hotelRoomId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
}