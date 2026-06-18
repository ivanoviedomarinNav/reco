package com.ucr.reco.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationDTO {
    @NotBlank(message = "El correo del usuario no puede estar en blanco")
    private String userEmail;

    @NotNull(message = "El ID del espacio es obligatorio")
    private Integer spaceId;

    @NotBlank(message = "La fecha de inicio es obligatoria")
    private String startDate;

    private String endDate;

    private String status;

    public ReservationDTO() {
    }

    public ReservationDTO(String userEmail, Integer spaceId, String startDate, String endDate, String status) {
        this.userEmail = userEmail;
        this.spaceId = spaceId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
