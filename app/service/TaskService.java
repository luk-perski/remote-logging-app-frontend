package service;

import com.google.protobuf.Api;
import enums.TaskStatus;
import models.api.v1.ApiTask;
import models.db.remote.logging.Category;
import models.db.remote.logging.Task;
import models.db.user.User;
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


    public ApiTask add(ApiTask apiTask) {
        Task task = getTaskFromApi(apiTask, userRepository, projectRepository, categoryRepository);
        task.setTaskStatus(TaskStatus.NEW.ordinal());
        taskRepository.add(task);
        return getApiTaskFromModel(task);
    }

    //todo
    public Boolean update(ApiTask apiTask) {
        Task task = taskRepository.getById(apiTask.getId());
        task.setDescription(apiTask.getDescription());
        taskRepository.update(task);
        return true;
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

    public ApiTask assignTaskToUser(Long taskId, Long userId) {
        User user = userRepository.getById(userId);
        Task task = taskRepository.getById(taskId);
        task.setAssignee(user);
        return getApiTaskFromModel(taskRepository.update(task));
    }

    //todo change to one method with case to change status
    public ApiTask startProgress(Long taskId, Long userId) {
        Task task = taskRepository.getById(taskId);
        if (task.getAssignee() != null && !userId.equals(task.getAssignee().getID())) {
            //todo throw exception
        }
        task.setTaskStatus(TaskStatus.IN_PROGRESS.ordinal());
        return getApiTaskFromModel(taskRepository.update(task));
    }

    public ApiTask suspend(Long taskId, Long userId) {
        Task task = taskRepository.getById(taskId);
        if (task.getAssignee() != null && !userId.equals(task.getAssignee().getID())) {
            //todo throw exception
        }
        if (task.getTaskStatus() != TaskStatus.IN_PROGRESS.ordinal()) {
            //todo throw exception
        }
        task.setTaskStatus(TaskStatus.SUSPEND.ordinal());
        return getApiTaskFromModel(taskRepository.update(task));
    }

}
