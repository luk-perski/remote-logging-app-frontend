package repository;

import io.ebean.Finder;
import models.db.remote.logging.Task;

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
    public List<Task> getByAssigneeId(Long userId, int maxRows, int page_index) {
        return (userId == null) ? null :
                finder.query()
                        .where()
                        .eq("assignee.id", userId).findList();
    }

    @Override
    public List<Task> getByProjectId(Long projectId, int maxRows, int page_index) {
        return (projectId == null) ? null :
                finder.query()
                        .where()
                        .eq("project.id", projectId).findList();
    }
}
