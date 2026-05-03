package com.project.taskflow.service;

import com.project.taskflow.dto.TaskRequest;
import com.project.taskflow.dto.TaskResponse;
import com.project.taskflow.entity.Project;
import com.project.taskflow.entity.Task;
import com.project.taskflow.entity.User;
import com.project.taskflow.enums.TaskStatus;
import com.project.taskflow.mapper.TaskMapper;
import com.project.taskflow.repository.ProjectRepository;
import com.project.taskflow.repository.TaskRepository;
import com.project.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksForProject(UUID projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Check if user has access to this project
        if (!project.getOwner().equals(user) && !hasTaskInProject(project, user)) {
            throw new RuntimeException("Access denied to this project");
        }

        List<Task> tasks = taskRepository.findByProject(project);
        return tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksForProjectWithFilters(
            UUID projectId, 
            User user, 
            TaskStatus status, 
            UUID assigneeId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Check if user has access to this project
        if (!project.getOwner().equals(user) && !hasTaskInProject(project, user)) {
            throw new RuntimeException("Access denied to this project");
        }

        List<Task> tasks = taskRepository.findByProjectWithFilters(project, status, assigneeId);
        return tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Check if user has access to this task
        if (!hasAccessToTask(task, user)) {
            throw new RuntimeException("Access denied to this task");
        }

        return taskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponse createTask(UUID projectId, TaskRequest request, User creator) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Check if user has access to this project
        if (!project.getOwner().equals(creator) && !hasTaskInProject(project, creator)) {
            throw new RuntimeException("Access denied to this project");
        }

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO)
                .priority(request.getPriority() != null ? request.getPriority() : com.project.taskflow.enums.TaskPriority.MEDIUM)
                .project(project)
                .assignee(assignee)
                .dueDate(request.getDueDate())
                .build();

        task = taskRepository.save(task);
                
        return taskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(UUID taskId, TaskRequest request, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Check if user can update this task
        if (!canUpdateTask(task, user)) {
            throw new RuntimeException("You do not have permission to update this task");
        }

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignee(assignee);
        } else if (request.getAssigneeId() == null && task.getAssignee() != null) {
            // If assigneeId is explicitly null, unassign
            task.setAssignee(null);
        }

        task = taskRepository.save(task);
        return taskMapper.toResponse(task);
    }

    @Transactional
    public void deleteTask(UUID taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Check if user can delete this task
        if (!canDeleteTask(task, user)) {
            throw new RuntimeException("You do not have permission to delete this task");
        }

        taskRepository.delete(task);
    }

    private boolean hasAccessToTask(Task task, User user) {
        Project project = task.getProject();
        return project.getOwner().equals(user) || 
               (task.getAssignee() != null && task.getAssignee().equals(user));
    }

    private boolean canUpdateTask(Task task, User user) {
        Project project = task.getProject();
        return project.getOwner().equals(user);
    }

    private boolean canDeleteTask(Task task, User user) {
        Project project = task.getProject();
        return project.getOwner().equals(user);
    }

    private boolean hasTaskInProject(Project project, User user) {
        return taskRepository.findByProjectAndAssignee(project, user).size() > 0;
    }
}