package com.project.trade.domain;

public class Book {

    private Long id;
    private String name;
    private String publisher;
    private Integer price;
    private String owner;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {this.id = id;}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
