package com.ooad.bookinghotel.HotelDb;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class User extends BaseDbo {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String firstname;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String lastname;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false, updatable = false)
    private String account;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String email;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String password;

    public Integer getId() {
        return id;
    }

//    public void setId(Integer id) {
//        this.id = id;
//    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}