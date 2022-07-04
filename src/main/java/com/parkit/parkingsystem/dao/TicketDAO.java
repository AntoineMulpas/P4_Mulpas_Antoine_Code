package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();




    public boolean saveTicket(Ticket ticket) throws SQLException, ClassNotFoundException {
        Connection con = dataBaseConfig.getConnection();
        PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
        try {
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            ps.setInt(1,ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null)?null: (new Timestamp(ticket.getOutTime().getTime())) );
            return ps.execute();
        } catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        } finally {
            dataBaseConfig.closeConnection(con);
            ps.close();
            con.close();
        }
        return false;
    }

    public Ticket getTicket(String vehicleRegNumber) throws SQLException, ClassNotFoundException {
        Connection con = dataBaseConfig.getConnection();
        Ticket ticket = null;
        PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
        try {
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1,vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)),false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        } finally {
            dataBaseConfig.closeConnection(con);
            ps.close();
            con.close();
        }
        return ticket;
    }

    public boolean updateTicket(Ticket ticket) throws SQLException, ClassNotFoundException {
        Connection con = dataBaseConfig.getConnection();
        PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
        try {
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3,ticket.getId());
            ps.execute();
            return true;
        }catch (Exception ex){
            logger.error("Error saving ticket info",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
            ps.close();
            con.close();
        }
        return false;
    }

    public boolean discountTicket(String vehicleRegNum) throws SQLException, ClassNotFoundException {
        Connection con = dataBaseConfig.getConnection();
        PreparedStatement ps = con.prepareStatement(DBConstants.COUNT_TICKET);
        try {
            ps.setString(1, vehicleRegNum);
            ResultSet rs = ps.executeQuery();
            int count = 0;
            while(rs.next()) {
                if (rs.getString(3).equals(vehicleRegNum)) {
                    count++;
                }
            }
            return count > 1;
        } catch (Exception e) {
            logger.error("Error counting the ticket.", e);
        } finally {
            dataBaseConfig.closeConnection(con);
            ps.close();
            con.close();
        }
        return false;
    }

    public boolean isDiscount(Ticket ticket) {
        try {
            return discountTicket(ticket.getVehicleRegNumber());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
