package com.ooad.bookinghotel.HotelDb;

import javax.persistence.*;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
public class Ordering extends BaseDbo{
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

    //public Integer getBookingId() { return BookingId; }

    //public void setBookingId(Integer BookingId){this.BookingId = BookingId;}

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

    public Boolean getIsPaid() { return isPaid; }


    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }
}
