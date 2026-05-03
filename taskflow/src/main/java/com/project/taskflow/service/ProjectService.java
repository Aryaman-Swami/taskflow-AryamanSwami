package com.project.taskflow.service;

import com.project.taskflow.dto.ProjectRequest;
import com.project.taskflow.dto.ProjectResponse;
import com.project.taskflow.dto.TaskResponse;
import com.project.taskflow.entity.Project;
import com.project.taskflow.entity.Task;
import com.project.taskflow.entity.User;
import com.project.taskflow.mapper.ProjectMapper;
import com.project.taskflow.mapper.TaskMapper;
import com.project.taskflow.repository.ProjectRepository;
import com.project.taskflow.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsForUser(User user) {
        List<Project> projects = projectRepository.findByOwnerOrHasTaskAssigned(user.getId());
        return projects.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(UUID projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Check if user has access to this project
        if (!project.getOwner().equals(user) && !hasTaskInProject(project, user)) {
            throw new RuntimeException("Access denied to this project");
        }

        return convertToResponse(project);
    }

    @Transactional
    public ProjectResponse createProject(ProjectRequest request, User owner) {
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(owner)
                .build();

        project = projectRepository.save(project);
        return convertToResponse(project);
    }

    @Transactional
    public ProjectResponse updateProject(UUID projectId, ProjectRequest request, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Only owner can update project
        if (!project.getOwner().equals(user)) {
            throw new RuntimeException("Only project owner can update the project");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        project = projectRepository.save(project);
        return convertToResponse(project);
    }

    @Transactional
    public void deleteProject(UUID projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Only owner can delete project
        if (!project.getOwner().equals(user)) {
            throw new RuntimeException("Only project owner can delete the project");
        }

        projectRepository.delete(project);
    }

    private ProjectResponse convertToResponse(Project project) {
        ProjectResponse response = projectMapper.toResponse(project);
        
        List<Task> tasks = taskRepository.findByProject(project);
        List<TaskResponse> taskResponses = tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
        
        response.setTasks(taskResponses);
        return response;
    }

    private boolean hasTaskInProject(Project project, User user) {
        return taskRepository.findByProjectAndAssignee(project, user).size() > 0;
    }
}