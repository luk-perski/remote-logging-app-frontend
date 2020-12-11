package repository;

import models.db.remote.logging.Task;

import java.util.List;

public interface ITaskRepository {
    List<Task> getAll();

    Task add(Task task);

    Task update(Task task);

    Task getById(Long id);

    List<Task> getByAssigneeId(Long userId, int maxRows, int page_index);

    List<Task> getByProjectId(Long projectId, int maxRows, int page_index);
}
