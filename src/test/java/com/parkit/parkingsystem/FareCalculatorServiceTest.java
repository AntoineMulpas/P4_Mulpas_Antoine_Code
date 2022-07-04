package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTest {

    private FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    private final double DISCOUNT = 0.95;
    private final double HALF_AN_HOUR = 30;
    private Fare fare;

    @Mock
    private TicketDAO ticketDAO;

    @BeforeEach
    private void setUp() {
        fareCalculatorService = new FareCalculatorService(ticketDAO);
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = Date.from(Instant.now().plusSeconds(120L));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.BIKE_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareIfTicketGetOutTimeIsBeforeGetInTime() {

        assertThrows(IllegalArgumentException.class, () -> {
            Date inTime = new Date();
            inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );
            Date outTime = new Date();
            outTime.setTime(System.currentTimeMillis() - (  45 * 70 * 1000) );
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            fareCalculatorService.calculateFare(ticket);
        });
    }

    @Test
    public void calculateFareBikeForLesThanHalfAnHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 28 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( 0 , ticket.getPrice());
    }

    @Test
    public void calculateFareCarForLesThanHalfAnHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 28 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( 0 , ticket.getPrice());
    }

    @Test
    public void calculateFareCarForLesThanHalfAnHourParkingTimeRecurrentUser(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 28 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( 0 , ticket.getPrice() * DISCOUNT);
    }

    @Test
    public void calculateFareBikeForLesThanHalfAnHourParkingTimeRecurrentUser(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 28 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( 0 , ticket.getPrice() * DISCOUNT);
    }

    @Test
    public void calculateFareCarForLessThanAnHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 1000 * 45);
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( 0 , ticket.getPrice() * DISCOUNT);
    }



    @Test
    public void calculateFareCarForMoreThanHalfAnHourAndRecurrentUser() {
        when(ticketDAO.isDiscount(any(Ticket.class))).thenReturn(true);
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 1000 * 60 * 45);
        Date outTime = new Date();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        fareCalculatorService.calculateFare(ticket);
        double priceToAssert = ((45 * 1.5) / 60) * 0.95;
        assertEquals(priceToAssert, ticket.getPrice());
    }

    @Test
    public void calculateFareCarForMoreThanAnHourAndRecurrentUser() {
        when(ticketDAO.isDiscount(any(Ticket.class))).thenReturn(true);
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 1000 * 60 * 70);
        Date outTime = new Date();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        fareCalculatorService.calculateFare(ticket);
        double priceToAssert = (1 * 1.5) * 0.95;
        assertEquals(priceToAssert, ticket.getPrice());
    }

    @Test
    public void calculateFareBikeForMoreThanHalfAnHourAndRecurrentUser() {
        when(ticketDAO.isDiscount(any(Ticket.class))).thenReturn(true);
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 1000 * 60 * 45);
        Date outTime = new Date();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, false));
        fareCalculatorService.calculateFare(ticket);
        double priceToAssert = ((45 * 1.0) / 60) * 0.95;
        assertEquals(priceToAssert, ticket.getPrice());
    }

    @Test
    public void calculateFareBikeForMoreAnHourAndRecurrentUser() {
        when(ticketDAO.isDiscount(any(Ticket.class))).thenReturn(true);
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 1000 * 60 * 70);
        Date outTime = new Date();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, false));
        fareCalculatorService.calculateFare(ticket);
        double priceToAssert = 0.95;
        assertEquals(priceToAssert, ticket.getPrice());
    }

}
