package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import java.util.concurrent.TimeUnit;


public class FareCalculatorService {

    private final int HALF_AN_HOUR = 30;
    private final int AN_HOUR = 60;


    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct



        long durationMinutes = Math.abs(TimeUnit.MINUTES.convert((inHour - outHour), TimeUnit.MILLISECONDS));

        if (durationMinutes <= HALF_AN_HOUR) {
            ticket.setPrice(0);
        } else if (durationMinutes < AN_HOUR) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice((durationMinutes * Fare.CAR_RATE_PER_HOUR) / AN_HOUR );
                    break;
                }
                case BIKE: {
                    ticket.setPrice((durationMinutes * Fare.BIKE_RATE_PER_HOUR) / AN_HOUR );
                    break;
                }
        }} else {

            long duration = Math.abs(TimeUnit.HOURS.convert(durationMinutes, TimeUnit.MINUTES));
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }
}