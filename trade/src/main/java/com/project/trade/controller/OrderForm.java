package com.project.trade.controller;

public class OrderForm {

    private Long orderid;
    private Long bookid;
    private String client;

    public Long getOrderid() {
        return orderid;
    }
    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }
    public Long getBookid() {
        return bookid;
    }
    public void setBookid(Long bookid) {
        this.bookid = bookid;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
