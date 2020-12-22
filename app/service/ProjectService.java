package service;

import models.api.v1.ApiProject;
import models.db.remote.logging.Project;
import models.db.user.User;
import repository.ProjectRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectService {

    @Inject
    ProjectRepository projectRepository;

    @Inject
    UserRepository userRepository;

    public Project add(ApiProject apiProject) {
        Project project = getProjectFromApi(apiProject);
        project.setCratedDate(new Date());
        return projectRepository.add(project);
    }

    public Project update(ApiProject apiProject) {
        Project project = getProjectFromApi(apiProject);
        if (apiProject.getEndDate() != null) {
            project.setEndDate(apiProject.getEndDate());
        }
        return projectRepository.update(project);
    }

    public List<ApiProject> getAll() {
        List<Project> projects = projectRepository.getAll();
        return getApiProjectsFromProjects(projects);
    }

    public Project getById(Long id) {
        return projectRepository.getById(id);
    }

    public List<Project> getByManagerId(Long id) {
        return projectRepository.getByManagerId(id);
    }

    private Project getProjectFromApi(ApiProject apiProject) {
        User manager = userRepository.getById(apiProject.getManagerId());
        return Project.builder()
                .name(apiProject.getName())
                .description(apiProject.getDescription())
                .startDate(apiProject.getStartDate())
                .isActive(apiProject.getIsActive())
                .manager(manager)
                .build();
    }

    private ApiProject getApiProjectFromProject(Project project) {
        return ApiProject.builder()
                .id(project.getId())
                .cratedDate(project.getCratedDate())
                .endDate(project.getEndDate())
                .name(project.getName())
                .description(project.getName())
                .isActive(project.getIsActive())
                .managerId(project.getManager().getID())
                .managerName(project.getManager().getDisplayName())
                .build();
    }

    private List<ApiProject> getApiProjectsFromProjects(List<Project> projects) {
        List<ApiProject> result = new ArrayList<>();
        projects.forEach(project -> {
            result.add(getApiProjectFromProject(project));
        });
        return result;
    }
}
