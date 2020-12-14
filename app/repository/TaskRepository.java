package repository;

import io.ebean.Finder;
import models.db.remote.logging.Task;
import utils.repository.FinderUtils;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TaskRepository implements ITaskRepository {

    Finder<Long, Task> finder = new Finder<>(Task.class);

    @Override
    public List<Task> getAll() {
        return finder.all();
    }

    @Override
    public Task add(Task task) {
        task.save();
        task.refresh();
        return task;
    }

    @Override
    public Task update(Task task) {
        task.update();
        task.refresh();
        return task;
    }

    @Override
    public Task getById(Long id) {
        return finder.byId(id);
    }

    //todo inspect why paged list not works
    @Override
    public List<Task> getByAssigneeId(Long userId) {
        return FinderUtils.getObjects(finder, Task.class, userId, "assignee.id");
    }

    @Override
    public List<Task> getByProjectId(Long projectId) {
        return FinderUtils.getObjects(finder, Task.class, projectId, "project.id");
    }

    @Override
    public List<Task> getByCategoryId(Long categoryId) {
        return FinderUtils.getObjects(finder, Task.class, categoryId, "category.id");
    }

}
