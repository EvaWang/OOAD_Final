package com.ooad.bookinghotel.HotelDb;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class Order extends BaseDbo{

    @Column(columnDefinition = "varchar(255) default 0", nullable = false)
    private Integer HotelId;

    @Column(columnDefinition = "varchar(255) default 0", nullable = false)
    private Integer id;

    @Column(columnDefinition = "varchar(255) default 0", nullable = false)
    private Integer RoomQuantity;

    @Temporal(TemporalType.TIMESTAMP)
    private Date StartDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date EndDate;

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getHotelId() { return HotelId;}

    public void setHotelId(Integer HotelId){this.HotelId = HotelId;}

    public Date getStartDate() { return StartDate;}

    public void setStartDate(Date StartDate) {this.StartDate = StartDate;}
}
