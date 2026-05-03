package com.project.taskflow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.taskflow.dto.TaskRequest;
import com.project.taskflow.dto.TaskResponse;
import com.project.taskflow.dto.UserSecurityDetails;
import com.project.taskflow.entity.User;
import com.project.taskflow.enums.TaskStatus;
import com.project.taskflow.service.TaskService;
import com.project.taskflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @PathVariable UUID projectId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) UUID assignee,
            @AuthenticationPrincipal UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(taskService.getTasksForProjectWithFilters(projectId, user, status, assignee));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable UUID projectId,
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UUID userId) {
        User user = userService.getUserById(userId);
        TaskResponse task = taskService.createTask(projectId, request, user);
        return ResponseEntity.status(200).body(task);
    }
}