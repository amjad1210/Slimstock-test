package com.branthill.techinicaltask.service;

import com.branthill.techinicaltask.dto.AnimalType;
import com.branthill.techinicaltask.dto.Pet;
import com.branthill.techinicaltask.dto.Vet;
import com.branthill.techinicaltask.exception.RestApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Service
public class PetService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PetService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean exists(long id) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM PETS WHERE id=?",
                new Object[]{
                        id
                },
                Integer.class) > 0;
    }

    public Vet getVet(String name) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM VETS WHERE name=?",
                    new Object[]{
                            name
                    },
                    (rs, rowNum) ->
                            new Vet(
                                    rs.getLong("id"),
                                    rs.getString("name")
                            ));
        } catch (EmptyResultDataAccessException e) {
            throw new RestApiException("Invalid vet name");
        }
    }

    public AnimalType getAnimalType(String name) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM ANIMAL_TYPES WHERE name=?",
                    new Object[]{
                            name
                    },
                    (rs, rowNum) ->
                            new AnimalType(
                                    rs.getLong("id"),
                                    rs.getString("name")
                            ));
        } catch (EmptyResultDataAccessException e) {
            throw new RestApiException("Invalid animal type");
        }
    }

    public long add(String name, String vetName, String type) {
        Vet vet = getVet(vetName);
        AnimalType animalType = getAnimalType(type);

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement("INSERT INTO PETS (name, vet_id, animal_type_id) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setLong(2, vet.getId());
            statement.setLong(3, animalType.getId());
            return statement;
        }, holder);
        return holder.getKey().longValue();
    }

    public Pet find(String name, String vetName, String type) {
        Vet vet = getVet(vetName);
        AnimalType animalType = getAnimalType(type);

        try {
            return jdbcTemplate.queryForObject("SELECT * FROM PETS WHERE name=? AND animal_type_id=?",
                    new Object[]{
                            name, animalType.getId()
                    },
                    (rs, rowNum) ->
                            new Pet(
                                    rs.getLong("id"),
                                    rs.getString("name"),
                                    vet,
                                    animalType
                            ));
        } catch (EmptyResultDataAccessException e) {
            throw new RestApiException("Invalid pet name");
        }
    }

    public int update(long id, String name, String vetName, String type) {
        Vet vet = getVet(vetName);
        AnimalType animalType = getAnimalType(type);

        return jdbcTemplate.update("UPDATE PETS SET name=?, vet_id=?, animal_type_id=? WHERE id=?",
                new Object[]{
                        name, vet.getId(), animalType.getId(), id
                });
    }

    public int delete(long id) {
        return jdbcTemplate.update("DELETE FROM PETS WHERE id=?", new Object[]{
                id
        });
    }

}
