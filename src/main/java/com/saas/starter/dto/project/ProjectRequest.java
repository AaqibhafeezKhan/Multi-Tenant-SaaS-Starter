package com.saas.starter.dto.project;

import com.saas.starter.entity.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Project.ProjectStatus status;
}
