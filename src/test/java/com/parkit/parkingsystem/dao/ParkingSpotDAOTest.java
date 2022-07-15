package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ParkingSpotDAOTest {

    private ParkingSpotDAO underTest;


    @BeforeEach
    void setUp() {
        underTest = new ParkingSpotDAO();
    }


    @Test
    void getNextAvailableSlot() throws SQLException, ClassNotFoundException {
        assertTrue(underTest.getNextAvailableSlot(ParkingType.CAR) != 0);
    }


    @Test
    void updateParking() throws SQLException, ClassNotFoundException {
        ParkingSpot parkingSpot = new ParkingSpot(
                2,
                ParkingType.CAR,
                true
        );
        assertTrue(underTest.updateParking(parkingSpot));
    }

}