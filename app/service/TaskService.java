package service;

import models.api.v1.ApiTask;
import models.db.remote.logging.Category;
import models.db.remote.logging.Task;
import repository.CategoryRepository;
import repository.ProjectRepository;
import repository.TaskRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.List;

import static utils.api.v1.ModelsUtils.*;

public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final CategoryRepository categoryRepository;

    @Inject
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.categoryRepository = categoryRepository;
    }

    private Task addTimeToTask(Task task, Long time) {
        Long newTime = task.getTimeSpent() != null ? task.getTimeSpent() + time : time;
        task.setTimeSpent(newTime);
        return taskRepository.update(task);
    }

    public ApiTask add(ApiTask apiTask) {
        Task task = getTaskFromApi(apiTask, userRepository, projectRepository, categoryRepository);
        taskRepository.add(task);
        return getApiTaskFromModel(task);
    }

    //todo add setting category, project, assignee
    public Boolean update(ApiTask apiTask) {
        taskRepository.update(getTaskFromApi(apiTask, userRepository, projectRepository, categoryRepository));
        return true;
    }

    public Task logWork(Long taskId, Long time) {
        Task task = taskRepository.getById(taskId);
        return addTimeToTask(task, time);
    }

    public List<ApiTask> getAll() {
        List<Task> taskList = taskRepository.getAll();
        return getApiTasks(taskList);
    }

    public ApiTask getById(Long id) {
        Task task = taskRepository.getById(id);
        return getApiTaskFromModel(task);
    }

    public List<ApiTask> getUserTasks(Long userId) {
        List<Task> tasks = taskRepository.getByAssigneeId(userId);
        return getApiTasks(tasks);
    }

    public List<ApiTask> getByProjectId(Long projectId) {
        List<Task> tasks = taskRepository.getByProjectId(projectId);
        return getApiTasks(tasks);
    }

    public List<ApiTask> getByCategoryId(Long categoryId) {
        List<Task> tasks = taskRepository.getByCategoryId(categoryId);
        return getApiTasks(tasks);
    }

    public boolean addCategoryToTask(Long taskId, Long categoryId) {
        Task task = taskRepository.getById(taskId);
        Category category = categoryRepository.getById(categoryId);
        task.setCategory(category);
        taskRepository.update(task);
        return true;
    }


}
