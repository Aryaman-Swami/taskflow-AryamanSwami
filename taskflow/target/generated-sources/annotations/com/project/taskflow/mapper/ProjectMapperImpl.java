package com.project.taskflow.mapper;

import com.project.taskflow.dto.ProjectResponse;
import com.project.taskflow.dto.UserResponse;
import com.project.taskflow.entity.Project;
import com.project.taskflow.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-14T06:02:05+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public ProjectResponse toResponse(Project project) {
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
}
