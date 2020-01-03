package com.ooad.bookinghotel.HotelDb;


import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.util.Date;

@Immutable
@Entity
public class OrderView extends BaseDbo{

    @Column
    private Integer id;

    @Id
    @Column
    private Integer hotelRoomId;

    @Column
    private Boolean isDisabled;

    @Column
    private Boolean isPaid;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column
    private Integer userId;

    @Column
    private Integer Total;

    @Column
    private Double Discount;

    @Column
    private String Memo;

    public Integer getId() { return id; }

    public Integer getHotelRoomId() { return hotelRoomId; }


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


    @Column
    private String name;

    @Column
    private Integer star;

    @Column
    private String locality;

    @Column
    private String address;

    @Column
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

    @Column
    private Integer roomType;

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    @Column
    private Boolean bookedIsDisabled;

    public Boolean getBookedIsDisabled() { return bookedIsDisabled; }

    public void setBookedIsDisabled(Boolean bookedIsDisabled) { this.bookedIsDisabled = bookedIsDisabled; }

    @Column
    private Integer bookedQuantity;

    public Integer getBookedQuantity() { return bookedQuantity; }

    public void setBookedQuantity(Integer bookedQuantity) { this.bookedQuantity = bookedQuantity; }

    @Column
    private Integer price;

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }

}
