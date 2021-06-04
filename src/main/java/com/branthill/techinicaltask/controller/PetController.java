package com.branthill.techinicaltask.controller;

import com.branthill.techinicaltask.dto.Pet;
import com.branthill.techinicaltask.dto.response.ApiResponse;
import com.branthill.techinicaltask.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pet")
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/add")
    public ApiResponse addPet(@RequestParam String name, @RequestParam String vetName, @RequestParam String type) {
        long id = petService.add(name, vetName, type);
        return new ApiResponse(id > 0, id);
    }

    @GetMapping("/find")
    public Pet findPet(@RequestParam String name, @RequestParam String vetName, @RequestParam String type) {
        return petService.find(name, vetName, type);
    }

    @PostMapping("/update")
    public ApiResponse updatePet(@RequestParam long id, @RequestParam String name, @RequestParam String vetName, @RequestParam String type) {
        int result = petService.update(id, name, vetName, type);
        return new ApiResponse(result == 1);
    }

    @DeleteMapping("/delete")
    public ApiResponse deletePet(@RequestParam long id) {
        int result = petService.delete(id);
        return new ApiResponse(result == 1);
    }

}
