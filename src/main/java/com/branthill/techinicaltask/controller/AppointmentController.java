package com.branthill.techinicaltask.controller;

import com.branthill.techinicaltask.dto.Appointment;
import com.branthill.techinicaltask.dto.response.ApiResponse;
import com.branthill.techinicaltask.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/create")
    public ApiResponse createAppointment(
            @RequestParam long petId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime end
    ) {
        long id = appointmentService.create(petId, Timestamp.valueOf(start), Timestamp.valueOf(end));
        return new ApiResponse(id > 0, id);
    }

    @GetMapping("/read")
    public Appointment readAppointment(@RequestParam long appointmentId) {
        return appointmentService.read(appointmentId);
    }

    @PostMapping("/update")
    public ApiResponse updateAppointment(
            @RequestParam long appointmentId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime end
    ) {
        int result = appointmentService.update(appointmentId, Timestamp.valueOf(start), Timestamp.valueOf(end));
        return new ApiResponse(result == 1);
    }

    @DeleteMapping("/delete")
    public ApiResponse deleteAppointment(@RequestParam long appointmentId) {
        int result = appointmentService.delete(appointmentId);
        return new ApiResponse(result == 1);
    }

}
