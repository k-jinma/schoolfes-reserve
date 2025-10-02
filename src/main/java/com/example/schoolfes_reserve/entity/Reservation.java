package com.example.schoolfes_reserve.entity;

import java.time.LocalDateTime;

public class Reservation {
    private Long id;
    private String name;
    private String email;
    private Integer ticketNumber;
    private LocalDateTime createdAt;
    private String status;
    private Boolean emailSent;

    // デフォルトコンストラクタ
    public Reservation() {
    }

    // コンストラクタ
    public Reservation(String name, String email, Integer ticketNumber) {
        this.name = name;
        this.email = email;
        this.ticketNumber = ticketNumber;
        this.createdAt = LocalDateTime.now();
        this.status = "待機中";
        this.emailSent = false;
    }

    // getter/setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        this.emailSent = emailSent;
    }
}
