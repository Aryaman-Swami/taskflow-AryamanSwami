package com.project.taskflow.controller;

import com.project.taskflow.dto.ProjectRequest;
import com.project.taskflow.dto.ProjectResponse;
import com.project.taskflow.entity.User;
import com.project.taskflow.service.ProjectService;
import com.project.taskflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(
            @AuthenticationPrincipal UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(projectService.getProjectsForUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(projectService.getProjectById(id, user));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectRequest request,
            @AuthenticationPrincipal UUID userId) {
        User user = userService.getUserById(userId);
        ProjectResponse project = projectService.createProject(request, user);
        return ResponseEntity.status(200).body(project);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable UUID id,
            @Valid @RequestBody ProjectRequest request,
            @AuthenticationPrincipal UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(projectService.updateProject(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {
        User user = userService.getUserById(userId);
        projectService.deleteProject(id, user);
        return ResponseEntity.noContent().build();
    }
}