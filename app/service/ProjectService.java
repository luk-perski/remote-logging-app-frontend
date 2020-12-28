package service;

import models.api.v1.ApiProject;
import models.db.remote.logging.Project;
import repository.ProjectRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static utils.api.v1.ModelsUtils.*;

public class ProjectService {

    @Inject
    ProjectRepository projectRepository;

    @Inject
    UserRepository userRepository;

    public Project add(ApiProject apiProject) {
        Project project = getProjectFromApi(apiProject, userRepository);
        project.setCratedDate(new Date());
        return projectRepository.add(project);
    }

    public Project update(ApiProject apiProject) {
        Project project = getProjectFromApi(apiProject, userRepository);
        if (apiProject.getEndDate() != null) {
            project.setEndDate(apiProject.getEndDate());
        }
        return projectRepository.update(project);
    }

    public List<ApiProject> getAll() {
        List<Project> projects = projectRepository.getAll();
        return getApiProjectsFromProjects(projects);
    }

    public ApiProject getById(Long id) {
        Project project = projectRepository.getById(id);
        return getApiProjectFromProject(project);
    }

    public List<ApiProject> getByManagerId(Long id) {
        List<Project> projects = projectRepository.getByManagerId(id);
        return getApiProjectsFromProjects(projects);
    }
}
