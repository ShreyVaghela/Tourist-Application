package com.example.tourismcanada;

public class Order {
    private int id;
    private String from_to_address;
    private String time;
    private int num_passengers;

    public Order(int id, String from_to_address, String time, int num_passengers) {
        this.id = id;
        this.from_to_address = from_to_address;
        this.time = time;
        this.num_passengers = num_passengers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom_to_address() {
        return from_to_address;
    }

    public void setFrom_to_address(String from_to_address) {
        this.from_to_address = from_to_address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNum_passengers() {
        return num_passengers;
    }

    public void setNum_passengers(int num_passengers) {
        this.num_passengers = num_passengers;
    }
}
