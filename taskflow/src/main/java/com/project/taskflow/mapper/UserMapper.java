package com.project.taskflow.mapper;

import com.project.taskflow.dto.UserResponse;
import com.project.taskflow.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}