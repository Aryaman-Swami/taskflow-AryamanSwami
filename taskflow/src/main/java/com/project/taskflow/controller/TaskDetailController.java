package com.project.taskflow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.taskflow.dto.TaskRequest;
import com.project.taskflow.dto.TaskResponse;
import com.project.taskflow.dto.UserSecurityDetails;
import com.project.taskflow.entity.User;
import com.project.taskflow.service.TaskService;
import com.project.taskflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskDetailController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(taskService.getTaskById(id, user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(taskService.updateTask(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {
        User user = userService.getUserById(userId);
        taskService.deleteTask(id, user);
        return ResponseEntity.noContent().build();
    }
}