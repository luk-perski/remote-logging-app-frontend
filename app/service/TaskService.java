package service;

import models.db.remote.logging.Category;
import models.db.remote.logging.Project;
import models.db.remote.logging.Task;
import models.db.user.User;
import repository.CategoryRepository;
import repository.ProjectRepository;
import repository.TaskRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class TaskService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    ProjectRepository projectRepository;

    @Inject
    CategoryRepository categoryRepository;

    private void addTimeToTask(Task task, Long time) {
        Long newTime = task.getTimeSpent() != null ? task.getTimeSpent() + time : time;
        task.setTimeSpent(newTime);
        taskRepository.update(task);
    }

    public Task add(Task task, Long creatorId, Long projectId, Long assigneeId, Long categoryId) {
        User creator = userRepository.getById(creatorId);
        User assignee = userRepository.getById(assigneeId);
        Project project = projectRepository.getById(projectId);
        Category category = categoryRepository.getById(categoryId);
        task.setCreator(creator);
        task.setAssignee(assigneeId != -1 ? assignee : null);
        task.setProject(project);
        task.setCategory(category);
        task.setCratedDate(new Date());
        return taskRepository.add(task);
    }

    public Task update(Task task) {
        return taskRepository.update(task);
    }

    public boolean logWork(Long taskId, Long time) {
        Task task = taskRepository.getById(taskId);
        addTimeToTask(task, time);
        return true;
    }

    public List<Task> getAll() {
        return taskRepository.getAll();
    }

    public Task getById(Long id) {
        return taskRepository.getById(id);
    }
}
