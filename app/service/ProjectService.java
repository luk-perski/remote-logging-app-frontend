package service;

import models.db.remote.logging.Project;
import models.db.user.User;
import repository.ProjectRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class ProjectService {

    @Inject
    ProjectRepository projectRepository;

    @Inject
    UserRepository userRepository;

    public Project add(Project project, Long managerId) {
        User manager = userRepository.getById(managerId);
        project.setManager(manager);
        project.setCratedDate(new Date());
        return projectRepository.add(project);
    }

    public Project update(Project project) {
        return projectRepository.update(project);
    }

    public List<Project> getAll(){
        return projectRepository.getAll();
    }
}
