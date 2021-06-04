package com.branthill.techinicaltask.service;

import com.branthill.techinicaltask.dto.Appointment;
import com.branthill.techinicaltask.exception.RestApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

@Service
public class AppointmentService {

    private final JdbcTemplate jdbcTemplate;
    private final PetService petService;

    @Autowired
    public AppointmentService(JdbcTemplate jdbcTemplate, PetService petService) {
        this.jdbcTemplate = jdbcTemplate;
        this.petService = petService;
    }

    private boolean isValidTimeFrame(Timestamp start, Timestamp end) {
        return end.after(start);
    }

    public long create(long petId, Timestamp start, Timestamp end) {
        if (!petService.exists(petId)) {
            throw new RestApiException("Invalid pet id");
        }

        if (!isValidTimeFrame(start, end)) {
            throw new RestApiException("End timestamp must be greater than start");
        }

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement("INSERT INTO APPOINTMENTS (start, end, pet_id) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            statement.setLong(3, petId);
            return statement;
        }, holder);
        return holder.getKey().longValue();
    }

    public Appointment read(long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM APPOINTMENTS WHERE id=?",
                    new Object[]{
                            id
                    },
                    (rs, rowNum) ->
                            new Appointment(
                                    rs.getLong("id"),
                                    rs.getTimestamp("start"),
                                    rs.getTimestamp("end")
                            ));
        } catch (EmptyResultDataAccessException e) {
            throw new RestApiException("Invalid appointment id");
        }
    }

    public int update(long id, Timestamp start, Timestamp end) {
        if (!isValidTimeFrame(start, end)) {
            throw new RestApiException("End timestamp must be greater than start");
        }

        return jdbcTemplate.update("UPDATE APPOINTMENTS SET start=?, end=? WHERE id=?",
                new Object[]{
                        start, end, id
                });
    }

    public int delete(long id) {
        return jdbcTemplate.update("DELETE FROM APPOINTMENTS WHERE id=?", new Object[]{
                id
        });
    }

}
