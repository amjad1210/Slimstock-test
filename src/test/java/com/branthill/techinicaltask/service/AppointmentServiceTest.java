package com.branthill.techinicaltask.service;

import com.branthill.techinicaltask.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Sql({"classpath:schema.sql", "classpath:data.sql"})
class AppointmentServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService(jdbcTemplate, new PetService(jdbcTemplate));
    }

    @Test
    void create() {
        Timestamp start = new Timestamp(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(start.getTime());
        cal.add(Calendar.HOUR, 1);
        Timestamp end = new Timestamp(cal.getTime().getTime());

        //Valid
        assertTrue(appointmentService.create(1, start, end) > 0);
        assertTrue(appointmentService.create(2, start, end) > 0);

        //Invalid
        Exception exception1 = assertThrows(RestApiException.class, () -> appointmentService.create(55, start, end));
        assertEquals(exception1.getMessage(), "Invalid pet id");

        Exception exception2 = assertThrows(RestApiException.class, () -> appointmentService.create(2, end, start));
        assertEquals(exception2.getMessage(), "End timestamp must be greater than start");
    }

    @Test
    void read() {
        //Valid
        assertNotNull(appointmentService.read(1));

        //Invalid
        Exception exception1 = assertThrows(RestApiException.class, () -> appointmentService.read(4));
        assertEquals(exception1.getMessage(), "Invalid appointment id");
    }

    @Test
    void update() {
        Timestamp start = new Timestamp(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(start.getTime());
        cal.add(Calendar.HOUR, 1);
        Timestamp end = new Timestamp(cal.getTime().getTime());

        //Valid
        assertTrue(appointmentService.update(1, start, end) > 0);
        assertTrue(appointmentService.update(2, start, end) > 0);

        //Invalid
        Exception exception1 = assertThrows(RestApiException.class, () -> appointmentService.update(2, end, start));
        assertEquals(exception1.getMessage(), "End timestamp must be greater than start");
    }

    @Test
    void delete() {
        //Valid
        assertTrue(appointmentService.delete(1) > 0);

        //Invalid
        assertEquals(appointmentService.delete(1), 0);
    }

}