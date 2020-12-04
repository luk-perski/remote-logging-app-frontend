package repository;

import io.ebean.Finder;
import models.db.remote.logging.Project;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ProjectRepository implements IProjectRepository {

    Finder<Long, Project> finder = new Finder<>(Project.class);

    @Override
    public List<Project> getAll() {
        return null;
    }

    public Project add(Project project) {
        project.save();
        project.refresh();
        return project;
    }

    @Override
    public Project update(Project project) {
        project.update();
        project.refresh();
        return project;
    }

    @Override
    public Project getById(Long projectId) {
        return (projectId == null) ? null : finder.byId(projectId);
    }
}
