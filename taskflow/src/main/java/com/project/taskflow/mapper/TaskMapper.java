package com.project.taskflow.mapper;

import com.project.taskflow.dto.TaskResponse;
import com.project.taskflow.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskResponse toResponse(Task task);
};