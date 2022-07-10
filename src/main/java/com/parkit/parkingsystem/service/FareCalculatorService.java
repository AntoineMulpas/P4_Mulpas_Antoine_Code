package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import java.util.concurrent.TimeUnit;


public class FareCalculatorService {

    private static final int HALF_AN_HOUR = 30;
    private static final double DISCOUNT = 0.95;
    private final TicketDAO ticketDAO;

    public FareCalculatorService() {
        ticketDAO = new TicketDAO();
    }

    public FareCalculatorService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct


        boolean discount = isDiscount(ticket);
        long durationMinutes = getDurationMinutes(inHour, outHour);
        long duration = getDuration(durationMinutes);

        if (durationMinutes <= HALF_AN_HOUR) {
            ticket.setPrice(0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    if (discount) {
                        ticket.setPrice((duration * Fare.CAR_RATE_PER_HOUR) * DISCOUNT);
                    } else {
                            ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    }
                    break;
                }
                case BIKE: {
                    if (discount) {
                        ticket.setPrice((duration * Fare.BIKE_RATE_PER_HOUR) * DISCOUNT);
                    } else {
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }

    private long getDurationMinutes(long inHour, long outHour) {
        long durationMinutes = Math.abs(TimeUnit.MINUTES.convert((inHour - outHour), TimeUnit.MILLISECONDS));
        return durationMinutes;
    }

    private long getDuration(long durationMinutes) {
        long duration = 1;
        if (durationMinutes > 60) {
            duration = Math.abs(TimeUnit.HOURS.convert(durationMinutes, TimeUnit.MINUTES));
        }
        return duration;
    }

    private boolean isDiscount(Ticket ticket) {
        boolean discount = ticketDAO.isDiscount(ticket);
        return discount;
    }


}
