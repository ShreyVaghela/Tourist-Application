package com.example.tourismcanada;

public class Location {
    private int id;
    private int address_id;
    private String name;
    private String address;
    private String description;
    private String highlights;
    private String price;
    private String image_url;

    public Location(int id, int address_id, String name, String address, String description, String highlights, String price, String image_url) {
        this.id = id;
        this.address_id = address_id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.highlights = highlights;
        this.price = price;
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
