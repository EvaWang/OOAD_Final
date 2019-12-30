package com.ooad.bookinghotel.HotelDb;


import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.util.Date;

@Immutable
@Entity
public class OrderView {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Boolean isDisabled;

    @Column(nullable = false)
    private Boolean isPaid;

    @Column(nullable = false, columnDefinition = "date")
    private Date startDate;

    @Column(nullable = false, columnDefinition = "date")
    private Date endDate;

    @Column(updatable = false, nullable = false)
    private Integer userId;

    @Column(columnDefinition = "varchar(255) default 0", nullable = false)
    private Integer Total;

    @Column(columnDefinition = "varchar(255) default 1", nullable = false)
    private Double Discount;

    @Column(columnDefinition = "varchar(500) default 0", nullable = false)
    private String Memo;

    public Integer getId() { return id; }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer UserId) {this.userId = UserId;}

    public Integer getTotal() { return Total;}

    public void setTotal(Integer Total) {this.Total = Total;}

    public Double getDiscount() { return Discount;}

    public void setDiscount(Double Discount) {this.Discount = Discount;}

    public String getMemo() { return Memo;}

    public void setMemo(String Memo) { this.Memo = Memo; }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsDisabled() { return isDisabled; }

    public void setIsDisabled(Boolean isDisabled) { this.isDisabled = isDisabled; }

    public Boolean getIsPaid() { return  isPaid; }

    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }


    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String name;

    @Column(columnDefinition = "varchar(255) default 0", nullable = false)
    private Integer star;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String locality;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String address;

    @Column(updatable = false, nullable = false)
    private Integer jsonFileId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Integer getStar() {
        return star;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getJsonFileId() {
        return jsonFileId;
    }

    public void setJsonFileId(Integer jsonFileId) {
        this.jsonFileId = jsonFileId;
    }

    @Column(columnDefinition = "varchar(255) default 0", nullable = false)
    private int roomType;

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType.number;
    }

//    booked_is_disabled
    //bookedQuantity

    @Column(nullable = false)
    private Boolean bookedIsDisabled;

    public Boolean getBookedIsDisabled() { return bookedIsDisabled; }

    public void setBookedIsDisabled(Boolean bookedIsDisabled) { this.bookedIsDisabled = bookedIsDisabled; }

    @Column(nullable = false)
    private Integer bookedQuantity;

    public Integer getBookedQuantity() { return bookedQuantity; }

    public void setBookedQuantity(Integer bookedQuantity) { this.bookedQuantity = bookedQuantity; }

}
