package com.branthill.techinicaltask.service;

import com.branthill.techinicaltask.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Sql({"classpath:schema.sql", "classpath:data.sql"})
class PetServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PetService petService;

    @BeforeEach
    void setUp() {
        petService = new PetService(jdbcTemplate);
    }

    @Test
    void exists() {
        //Valid
        assertTrue(petService.exists(1));
        assertTrue(petService.exists(2));

        //Invalid
        assertFalse(petService.exists(3));
        assertFalse(petService.exists(3));
    }

    @Test
    void getVet() {
        //Valid
        assertNotNull(petService.getVet("Sarah"));
        assertNotNull(petService.getVet("John"));

        //Invalid
        Exception exception = assertThrows(RestApiException.class, () -> petService.getVet("Bob"));
        assertEquals(exception.getMessage(), "Invalid vet name");
    }

    @Test
    void getAnimalType() {
        //Valid
        assertNotNull(petService.getAnimalType("Cat"));
        assertNotNull(petService.getAnimalType("Dog"));

        //Invalid
        Exception exception = assertThrows(RestApiException.class, () -> petService.getAnimalType("Cow"));
        assertEquals(exception.getMessage(), "Invalid animal type");
    }

    @Test
    void add() {
        //Valid
        assertTrue(petService.add("Steel", "Sarah", "Cat") > 0);
        assertTrue(petService.add("Iron", "John", "Dog") > 0);

        //Invalid
        Exception exception1 = assertThrows(RestApiException.class, () -> petService.add("Pip", "Sam", "Cat"));
        assertEquals(exception1.getMessage(), "Invalid vet name");

        Exception exception2 = assertThrows(RestApiException.class, () -> petService.add("Cherry", "Sarah", "Cat3"));
        assertEquals(exception2.getMessage(), "Invalid animal type");
    }

    @Test
    void find() {
        //Valid
        assertNotNull(petService.find("Muffin", "John", "Dog"));
        assertNotNull(petService.find("Apple", "Sarah", "Cat"));

        //Invalid
        Exception exception1 = assertThrows(RestApiException.class, () -> petService.find("Pip", "Sam", "Cat"));
        assertEquals(exception1.getMessage(), "Invalid vet name");

        Exception exception2 = assertThrows(RestApiException.class, () -> petService.find("Cherry", "Sarah", "Cat3"));
        assertEquals(exception2.getMessage(), "Invalid animal type");
    }

    @Test
    void update() {
        //Valid
        assertTrue(petService.update(1, "Muffin", "Sarah", "Dog") > 0);
        assertTrue(petService.update(2, "Apple", "John", "Cat") > 0);

        //Invalid
        Exception exception1 = assertThrows(RestApiException.class, () -> petService.update(1, "Pip", "Sam", "Cat"));
        assertEquals(exception1.getMessage(), "Invalid vet name");

        Exception exception2 = assertThrows(RestApiException.class, () -> petService.update(2, "Cherry", "Sarah", "Cat3"));
        assertEquals(exception2.getMessage(), "Invalid animal type");
    }

    @Test
    void delete() {
        //Valid
        assertTrue(petService.delete(1) > 0);

        //Invalid
        assertEquals(petService.delete(1), 0);
    }

}