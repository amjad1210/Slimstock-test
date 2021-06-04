package com.branthill.techinicaltask.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Pet {

    long id;

    String name;

    Vet vet;

    AnimalType animal;

}
