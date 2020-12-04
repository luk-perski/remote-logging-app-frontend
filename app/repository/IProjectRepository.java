package repository;

import models.db.remote.logging.Project;

import java.util.List;

public interface IProjectRepository {
    List<Project> getAll();

    Project add(Project project);

    Project update(Project project);

    Project getById(Long projectId);
}