package com.saas.starter.dto.project;

import com.saas.starter.entity.Project;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ProjectResponse {
    private UUID id;
    private String name;
    private String description;
    private Project.ProjectStatus status;
    private UUID ownerId;
    private Instant createdAt;
    private Instant updatedAt;
}
