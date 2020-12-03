package repository;

import models.db.remote.logging.Task;

import java.util.List;

public interface ITaskRepository {
    List<Task> getAll();

    Task add(Task task);

    Task update(Task task);
}
