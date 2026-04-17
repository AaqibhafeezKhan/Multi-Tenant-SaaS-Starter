package com.saas.starter.mapper;

import com.saas.starter.dto.project.ProjectRequest;
import com.saas.starter.dto.project.ProjectResponse;
import com.saas.starter.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toEntity(ProjectRequest request);
    ProjectResponse toResponse(Project project);
    void updateEntityFromRequest(ProjectRequest request, @MappingTarget Project project);
}
