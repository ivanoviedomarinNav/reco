package com.ucr.reco.service;

import com.ucr.reco.model.Reservation;
import com.ucr.reco.model.Space;
import com.ucr.reco.model.Status;
import com.ucr.reco.model.User;
import com.ucr.reco.model.dto.ReservationDTO;
import com.ucr.reco.repository.ReservationJpaRepository;
import com.ucr.reco.repository.SpaceJpaRepository;
import com.ucr.reco.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationJpaRepository repository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private SpaceJpaRepository spaceRepository;

    public List<Reservation> findAll() {
        return repository.findAll();
    }

    public Reservation getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Reservation add(ReservationDTO dto) {
        if (dto.getUserEmail() == null || dto.getSpaceId() == null || dto.getStartDate() == null) {
            return null;
        }
        User user = userRepository.getByEmail(dto.getUserEmail());
        if (user == null) {
            return null;
        }
        Space space = spaceRepository.findById(dto.getSpaceId()).orElse(null);
        if (space == null) {
            return null;
        }
        Reservation r = new Reservation();
        r.setUser(user);
        r.setSpace(space);
        r.setStartDate(LocalDateTime.parse(dto.getStartDate()));
        if (dto.getEndDate() != null && !dto.getEndDate().isBlank()) {
            r.setEndDate(LocalDate.parse(dto.getEndDate()));
        }
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            r.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
        } else {
            r.setStatus(Status.PENDING);
        }
        return repository.save(r);
    }

    public Reservation update(ReservationDTO dto) {
        if (dto.getSpaceId() == null) {
            return null;
        }
        Reservation existing = repository.findById(dto.getSpaceId()).orElse(null);
        if (existing == null) {
            return null;
        }
        if (dto.getUserEmail() != null) {
            User user = userRepository.getByEmail(dto.getUserEmail());
            if (user != null) {
                existing.setUser(user);
            }
        }
        if (dto.getSpaceId() != null) {
            Space space = spaceRepository.findById(dto.getSpaceId()).orElse(null);
            if (space != null) {
                existing.setSpace(space);
            }
        }
        if (dto.getStartDate() != null && !dto.getStartDate().isBlank()) {
            existing.setStartDate(LocalDateTime.parse(dto.getStartDate()));
        }
        if (dto.getEndDate() != null && !dto.getEndDate().isBlank()) {
            existing.setEndDate(LocalDate.parse(dto.getEndDate()));
        }
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            existing.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
        }
        return repository.save(existing);
    }

    public Reservation delete(Integer id) {
        Reservation existing = repository.findById(id).orElse(null);
        if (existing != null) {
            repository.deleteById(id);
            return existing;
        }
        return null;
    }

    public Reservation changeStatus(Integer id, String status) {
        Reservation existing = repository.findById(id).orElse(null);
        if (existing != null) {
            existing.setStatus(Status.valueOf(status.toUpperCase()));
            return repository.save(existing);
        }
        return null;
    }
}
