package com.project.taskflow.mapper;

import com.project.taskflow.dto.ProjectResponse;
import com.project.taskflow.dto.TaskResponse;
import com.project.taskflow.dto.UserResponse;
import com.project.taskflow.entity.Project;
import com.project.taskflow.entity.Task;
import com.project.taskflow.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-14T06:02:04+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskResponse toResponse(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskResponse.TaskResponseBuilder taskResponse = TaskResponse.builder();

        taskResponse.assignee( userToUserResponse( task.getAssignee() ) );
        taskResponse.createdAt( task.getCreatedAt() );
        taskResponse.description( task.getDescription() );
        taskResponse.dueDate( task.getDueDate() );
        taskResponse.id( task.getId() );
        taskResponse.priority( task.getPriority() );
        taskResponse.project( projectToProjectResponse( task.getProject() ) );
        taskResponse.status( task.getStatus() );
        taskResponse.title( task.getTitle() );
        taskResponse.updatedAt( task.getUpdatedAt() );

        return taskResponse.build();
    }

    protected UserResponse userToUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.createdAt( user.getCreatedAt() );
        userResponse.email( user.getEmail() );
        userResponse.id( user.getId() );
        userResponse.name( user.getName() );

        return userResponse.build();
    }

    protected ProjectResponse projectToProjectResponse(Project project) {
        if ( project == null ) {
            return null;
        }

        ProjectResponse.ProjectResponseBuilder projectResponse = ProjectResponse.builder();

        projectResponse.createdAt( project.getCreatedAt() );
        projectResponse.description( project.getDescription() );
        projectResponse.id( project.getId() );
        projectResponse.name( project.getName() );
        projectResponse.owner( userToUserResponse( project.getOwner() ) );

        return projectResponse.build();
    }
}
