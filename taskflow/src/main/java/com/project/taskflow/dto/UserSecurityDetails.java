package com.project.taskflow.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSecurityDetails {
  private String userId;
}
