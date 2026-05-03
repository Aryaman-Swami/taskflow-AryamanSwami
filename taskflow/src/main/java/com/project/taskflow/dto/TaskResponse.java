package com.project.taskflow.dto;

import com.project.taskflow.enums.TaskPriority;
import com.project.taskflow.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private ProjectResponse project;
    private UserResponse assignee;
    private LocalDate dueDate;
    private Instant createdAt;
    private Instant updatedAt;
}