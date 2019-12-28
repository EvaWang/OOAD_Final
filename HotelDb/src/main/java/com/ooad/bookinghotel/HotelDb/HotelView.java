package com.ooad.bookinghotel.HotelDb;


import org.springframework.data.annotation.Immutable;

import javax.persistence.*;

@Immutable
@Entity // This tells Hibernate to make a table out of this class
@Table(name="booked_hotel_info")
public class HotelView {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

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

    @Column
    private Integer singleRoom;

    @Column
    private Double singleRoomPrice;

    @Column
    private Integer doubleRoom;

    @Column
    private Double doubleRoomPrice;

    @Column
    private Integer quadRoom;

    @Column
    private Double quadRoomPrice;

    @Column
    private Integer bookedSingleRoom;

    @Column
    private Integer bookedDoubleRoom;

    @Column
    private Integer bookedQuadRoom;


    @Column
    public Integer getId() {
        return id;
    }

//    public void setId(Integer id) {
//        this.id = id;
//    }

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

    public Integer getSingleRoom() {
        return singleRoom;
    }

    public void setSingleRoom(Integer singleRoom) {
        this.singleRoom = singleRoom;
    }

    public Double getSingleRoomPrice() {
        return singleRoomPrice;
    }

    public void setSingleRoomPrice(Double singleRoomPrice) {
        this.singleRoomPrice = singleRoomPrice;
    }

    public Integer getDoubleRoom() {
        return doubleRoom;
    }

    public void setDoubleRoom(Integer doubleRoom) {
        this.doubleRoom = doubleRoom;
    }

    public Double getDoubleRoomPrice() {
        return doubleRoomPrice;
    }

    public void setDoubleRoomPrice(Double doubleRoomPrice){
        this.doubleRoomPrice = doubleRoomPrice;
    }

    public Integer getQuadRoom() {
        return quadRoom;
    }

    public void setQuadRoom(Integer quadRoom) {
        this.quadRoom = quadRoom;
    }

    public Double getQuadRoomPrice() {
        return quadRoomPrice;
    }

    public void setQuadRoomPrice(Double quadRoomPrice) {
        this.quadRoomPrice = quadRoomPrice;
    }

    public Integer getBookedSingleRoom() {
        return bookedSingleRoom;
    }

    public void setBookedSingleRoom(Integer bookedSingleRoom) {
        this.bookedSingleRoom = bookedSingleRoom;
    }

    public Integer getBookedDoubleRoom(){return bookedDoubleRoom;}

    public void setBookedDoubleRoom(Integer bookedDoubleRoom){this.bookedDoubleRoom = bookedDoubleRoom;}

    public Integer getBookedQuadRoom(){return bookedQuadRoom;}

    public void setBookedQuadRoom(){ this.bookedQuadRoom = bookedQuadRoom;}


}