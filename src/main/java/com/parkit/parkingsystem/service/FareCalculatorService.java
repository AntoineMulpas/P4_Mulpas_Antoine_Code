package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import java.util.concurrent.TimeUnit;


public class FareCalculatorService {

    private static final int HALF_AN_HOUR = 30;
    private static final int AN_HOUR = 60;
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




        boolean discount = ticketDAO.isDiscount(ticket);
        long durationMinutes = Math.abs(TimeUnit.MINUTES.convert((inHour - outHour), TimeUnit.MILLISECONDS));
        long duration = Math.abs(TimeUnit.HOURS.convert(durationMinutes, TimeUnit.MINUTES));

        if (durationMinutes <= HALF_AN_HOUR) {
            ticket.setPrice(0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    if (discount) {
                        if (durationMinutes < AN_HOUR) {
                            ticket.setPrice(((durationMinutes * com.parkit.parkingsystem.constants.Fare.CAR_RATE_PER_HOUR) / AN_HOUR) * DISCOUNT);
                        } else {
                            ticket.setPrice((duration * com.parkit.parkingsystem.constants.Fare.CAR_RATE_PER_HOUR) * DISCOUNT);
                        }
                    } else {
                        if (durationMinutes < AN_HOUR) {
                            ticket.setPrice((durationMinutes * com.parkit.parkingsystem.constants.Fare.CAR_RATE_PER_HOUR) / AN_HOUR);
                        } else {
                            ticket.setPrice(duration * com.parkit.parkingsystem.constants.Fare.CAR_RATE_PER_HOUR);
                        }
                    }
                    break;
                }
                case BIKE: {
                    if (discount) {
                        if (durationMinutes < AN_HOUR) {
                            ticket.setPrice(((durationMinutes * com.parkit.parkingsystem.constants.Fare.BIKE_RATE_PER_HOUR) / AN_HOUR) * DISCOUNT);
                        } else {
                            ticket.setPrice((duration * com.parkit.parkingsystem.constants.Fare.BIKE_RATE_PER_HOUR) * DISCOUNT);
                        }
                    } else {
                        if (durationMinutes < AN_HOUR) {
                            ticket.setPrice((durationMinutes * com.parkit.parkingsystem.constants.Fare.BIKE_RATE_PER_HOUR) / AN_HOUR);
                        } else {
                            ticket.setPrice(duration * com.parkit.parkingsystem.constants.Fare.BIKE_RATE_PER_HOUR);
                        }
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }


}
