package com.ucr.reco.controller;

import com.ucr.reco.model.Reservation;
import com.ucr.reco.model.dto.ReservationDTO;
import com.ucr.reco.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @GetMapping("/all")
    public ResponseEntity<List<?>> getAll() {
        List<Reservation> reservations = service.findAll();
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Reservation reservation = service.getById(id);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody ReservationDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        if (service.add(dto) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear reservación: usuario o espacio no encontrado");
        }
        return ResponseEntity.ok("Reservación creada exitosamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody ReservationDTO dto) {
        dto.setSpaceId(id);
        if (service.update(dto) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Reservación actualizada exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (service.delete(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Reservación eliminada exitosamente");
    }

    @PatchMapping("/change/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Integer id, @RequestBody ReservationDTO dto) {
        if (service.changeStatus(id, dto.getStatus()) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Estado actualizado exitosamente");
    }
}
