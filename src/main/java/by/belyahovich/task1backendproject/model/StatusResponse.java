package by.belyahovich.task1backendproject.model;

import lombok.Data;


public record StatusResponse(Long idUser,
                             boolean previousActivity,
                             boolean actualActivity) {}
