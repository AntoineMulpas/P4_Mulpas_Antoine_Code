package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TicketDAOTest {

    public static DataBaseConfig dataBaseConfig = new DataBaseConfig();
    public static final String DELETE_TICKET = "delete from ticket as t where t.VEHICLE_REG_NUMBER = ?";
    private static String vehicleRegistrationNumber = "##";
    private static ParkingSpot parkingSpot;
    private static TicketDAO underTest;
    private static Ticket ticket;

    @BeforeAll
    static void setUp() {
        underTest = new TicketDAO();
        parkingSpot = new ParkingSpot(
                2,
                ParkingType.CAR,
                true
        );
        Date inTime = Date.from(Instant.now().minusMillis(1000 * 60));
        ticket = new Ticket(1, parkingSpot, "##", 2.3, inTime);
    }


    @AfterAll
    static void afterAll() throws SQLException, ClassNotFoundException {
        Connection con = dataBaseConfig.getConnection();
        PreparedStatement ps = con.prepareStatement(DELETE_TICKET);
        try {
            ps.setString(1, vehicleRegistrationNumber);
            ps.executeUpdate();
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex){

        } finally {
            dataBaseConfig.closeConnection(con);
            ps.close();
            con.close();
        }
    }




    @Test
    @Order(1)
    void saveTicket()  {
        try {
            assertFalse(underTest.saveTicket(ticket));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    void getTicket() throws SQLException, ClassNotFoundException {
        assertTrue(underTest.getTicket("##").getPrice() == 2.3);
    }

    @Test
    @Order(3)
    void updateTicket() {
        ticket.setOutTime(Date.from(Instant.now()));
        ticket.setPrice(10.0);
        try {
            assertTrue(underTest.updateTicket(ticket));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}