package com.saas.starter.service;

import com.saas.starter.dto.project.ProjectRequest;
import com.saas.starter.dto.project.ProjectResponse;
import com.saas.starter.entity.Project;
import com.saas.starter.entity.User;
import com.saas.starter.mapper.ProjectMapper;
import com.saas.starter.multitenancy.TenantContext;
import com.saas.starter.repository.ProjectRepository;
import com.saas.starter.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(UUID id) {
        return projectRepository.findById(id)
                .map(projectMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }

    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByUsernameAndTenantId(username, TenantContext.getTenantId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Project project = projectMapper.toEntity(request);
        project.setOwnerId(owner.getId());
        project.setTenantId(TenantContext.getTenantId());
        
        Project saved = projectRepository.save(project);
        return projectMapper.toResponse(saved);
    }

    @Transactional
    public ProjectResponse updateProject(UUID id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
                
        projectMapper.updateEntityFromRequest(request, project);
        Project updated = projectRepository.save(project);
        return projectMapper.toResponse(updated);
    }

    @Transactional
    public void deleteProject(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project not found");
        }
        projectRepository.deleteById(id);
    }
}
