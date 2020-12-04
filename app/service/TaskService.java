package service;

import models.db.remote.logging.Project;
import models.db.remote.logging.Task;
import models.db.user.User;
import repository.ProjectRepository;
import repository.TaskRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.Date;

public class TaskService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    ProjectRepository projectRepository;

    private static void addTimeToTask(Task task, Long time) {
        Long newTime = task.getTimeSpent() + time;
        task.setTimeSpent(newTime);
    }

    public Task add(Task task, Long creatorId, Long projectId) {
        User creator = userRepository.getById(creatorId);
        Project project = projectRepository.getById(projectId);
        task.setCreator(creator);
        task.setProject(project);
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
}
