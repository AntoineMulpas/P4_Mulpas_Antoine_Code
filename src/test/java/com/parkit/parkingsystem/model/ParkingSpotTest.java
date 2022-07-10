package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotTest {

    @Test
    void setId() {
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setId(1);
        assertEquals(1, parkingSpot.getId());
    }

    @Test
    void setParkingType() {
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setParkingType(ParkingType.CAR);
        assertEquals(ParkingType.CAR, parkingSpot.getParkingType());
    }

    @Test
    void equals() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        assertTrue(parkingSpot.equals(parkingSpot));
    }


    @Test
    void equalsReturnsFalse() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ParkingSpot parkingSpotTwo = new ParkingSpot(2, ParkingType.BIKE, false);
        assertFalse(parkingSpot.equals(parkingSpotTwo));
    }

    @Test
    void equalsReturnsFalseWhenNull() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        assertFalse(parkingSpot.equals(null));
    }

    @Test
    void hasCode() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

        int hash = 1;
        assertEquals(hash, parkingSpot.hashCode());
    }

}