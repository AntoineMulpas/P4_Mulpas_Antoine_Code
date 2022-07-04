package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public Ticket(int id, ParkingSpot parkingSpot, String vehicleRegNumber, double price, Date inTime) {
        this.id = id;
        this.parkingSpot = parkingSpot;
        this.vehicleRegNumber = vehicleRegNumber;
        this.price = price;
        this.inTime = inTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return this.vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime() {
        return new Date(inTime.getTime());
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return this.outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }
}
