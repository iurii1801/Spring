package com.example.bibliotekaSport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TeamDto {
    private Long id;

    @NotBlank(message = "Team name must not be blank")
    private String name;

    @NotNull(message = "Coach ID is required")
    private Long coachId;

    @NotNull(message = "League ID is required")
    private Long leagueId;

    @NotNull(message = "Player IDs are required")
    private List<Long> playerIds;

    // Расширенные поля для ответа (GET)
    private CoachDto coach;
    private LeagueDto league;
    private List<PlayerDto> players;
    private List<MatchDto> matches;
}
