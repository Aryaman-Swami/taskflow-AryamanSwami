package com.project.taskflow.repository;

import com.project.taskflow.entity.Project;
import com.project.taskflow.entity.Task;
import com.project.taskflow.entity.User;
import com.project.taskflow.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByProject(Project project);

    Page<Task> findByProject(Project project, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.project = :project AND (:status IS NULL OR t.status = :status) AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)")
    Page<Task> findByProjectWithFilters(@Param("project") Project project,
                                        @Param("status") TaskStatus status,
                                        @Param("assigneeId") UUID assigneeId,
                                        Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.project = :project AND (:status IS NULL OR t.status = :status) AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)")
    List<Task> findByProjectWithFilters(@Param("project") Project project,
                                        @Param("status") TaskStatus status,
                                        @Param("assigneeId") UUID assigneeId);

    List<Task> findByAssignee(User assignee);

    List<Task> findByProjectAndAssignee(Project project, User assignee);

    @Query("SELECT t FROM Task t WHERE t.project.owner = :user OR t.assignee = :user")
    List<Task> findByProjectOwnerOrAssignee(@Param("user") User user);

    @Query("SELECT t FROM Task t WHERE t.project.owner = :user OR t.assignee = :user")
    Page<Task> findByProjectOwnerOrAssignee(@Param("user") User user, Pageable pageable);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project = :project AND t.status = :status")
    long countByProjectAndStatus(@Param("project") Project project, @Param("status") TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project = :project AND t.assignee = :assignee")
    long countByProjectAndAssignee(@Param("project") Project project, @Param("assignee") User assignee);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project = :project AND t.assignee = :assignee AND t.status = :status")
    long countByProjectAssigneeAndStatus(@Param("project") Project project, 
                                         @Param("assignee") User assignee, 
                                         @Param("status") TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.dueDate < :date AND t.status != 'DONE'")
    List<Task> findOverdueTasks(@Param("date") LocalDate date);

    @Query("SELECT t FROM Task t WHERE t.project = :project AND t.dueDate < :date AND t.status != 'DONE'")
    List<Task> findOverdueTasksByProject(@Param("project") Project project, @Param("date") LocalDate date);
}