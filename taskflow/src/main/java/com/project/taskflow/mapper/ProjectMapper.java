package com.project.taskflow.mapper;

import com.project.taskflow.dto.ProjectResponse;
import com.project.taskflow.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "tasks", ignore = true)
    ProjectResponse toResponse(Project project);
}