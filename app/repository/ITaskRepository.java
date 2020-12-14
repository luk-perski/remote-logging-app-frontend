package repository;

import models.db.remote.logging.Task;

import java.util.List;

public interface ITaskRepository {
    List<Task> getAll();

    Task add(Task task);

    Task update(Task task);

    Task getById(Long id);


    //todo inspect why paged list not works
    List<Task> getByAssigneeId(Long userId);

    List<Task> getByProjectId(Long projectId);

    List<Task> getByCategoryId(Long categoryId);
}
