package com.parkit.parkingsystem.constants;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareTest {


    @Test
    void testBikeRatePerHour() {
        assertEquals(1.0, Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    void testCareRatePerHour() {
        assertEquals(1.5, Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    void testFare() {
        Fare fare = new Fare();
    }


}