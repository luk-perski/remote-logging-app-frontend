package service;

import com.google.protobuf.Api;
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

    public ApiProject add(ApiProject apiProject) {
        Project project = getProjectFromApi(apiProject, userRepository);
        project.setCratedDate(new Date());
        return getApiProjectFromProject(projectRepository.add(project));
    }

    public ApiProject update(ApiProject apiProject) {
        Project project = getProjectFromApi(apiProject, userRepository);
        if (apiProject.getEndDate() != null) {
            project.setEndDate(apiProject.getEndDate());
        }
        return getApiProjectFromProject(projectRepository.update(project));
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
