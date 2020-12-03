package repository;

import io.ebean.Finder;
import models.db.remote.logging.Task;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TaskRepository implements ITaskRepository {

    Finder<Integer, Task> finder = new Finder<Integer, Task>(Task.class);

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
}
