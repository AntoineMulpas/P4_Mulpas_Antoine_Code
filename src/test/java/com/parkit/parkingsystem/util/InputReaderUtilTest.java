package com.parkit.parkingsystem.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputReaderUtilTest {


    @AfterEach
    void tearDown() {
        InputStream sysInBackup = System.in;
        System.setIn(sysInBackup);
    }



    @Test
    public void readSelectionCatchError() {
        ByteArrayInputStream in = new ByteArrayInputStream("1B".getBytes());
        System.setIn(in);
        InputReaderUtil util = new InputReaderUtil();
        assertEquals(-1, util.readSelection());
    }



    @Test
    void readVehicleRegistrationNumberThrowsException() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("".getBytes());
        System.setIn(in);
        InputReaderUtil util = new InputReaderUtil();
        assertThrows(Exception.class, () -> util.readVehicleRegistrationNumber());
    }


}