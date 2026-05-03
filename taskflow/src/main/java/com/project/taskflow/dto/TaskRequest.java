package com.project.taskflow.dto;

import com.project.taskflow.enums.TaskPriority;
import com.project.taskflow.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private UUID assigneeId;

    private LocalDate dueDate;
}