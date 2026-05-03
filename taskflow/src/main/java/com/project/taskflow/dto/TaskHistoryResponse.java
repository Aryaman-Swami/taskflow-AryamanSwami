package com.project.taskflow.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskHistoryResponse {
    private Long id;
    private UUID taskId;
    private JsonNode taskData;
    private UUID updatedBy;
    private Instant updatedAt;
    private String changeType;
}