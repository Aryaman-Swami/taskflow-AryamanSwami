package com.project.taskflow.repository;

import com.project.taskflow.entity.Project;
import com.project.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByOwner(User owner);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN Task t ON t.project = p WHERE p.owner.id = :user OR t.assignee.id = :user")
List<Project> findByOwnerOrHasTaskAssigned(@Param("user") UUID user);
}