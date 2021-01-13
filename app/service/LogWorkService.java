package service;

import models.api.v1.ApiLogWork;
import models.db.remote.logging.LogWork;
import models.db.remote.logging.Task;
import repository.LogWorkRepository;
import repository.TaskRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static utils.api.v1.ModelsUtils.*;


public class LogWorkService {
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final LogWorkRepository logWorkRepository;

    @Inject
    public LogWorkService(TaskRepository taskRepository, UserRepository userRepository, LogWorkRepository logWorkRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.logWorkRepository = logWorkRepository;
    }

    public ApiLogWork add(ApiLogWork apiLogWork) {
        LogWork logWork = getLogWorkModelFromApiLogWork(apiLogWork, userRepository, taskRepository);
        logWork.setCratedDate(new Date());
        Task task = taskRepository.getById(apiLogWork.getTaskId());
        addTimeToTask(task, apiLogWork.getTimeSpend());
        taskRepository.update(task);
        return getApiWorkLogFromModel(logWorkRepository.add(logWork));
    }

    public List<ApiLogWork> getAll() {
        List<LogWork> logWorks = logWorkRepository.getAll();
        return getApiWorkLogsListFromModels(logWorks, userRepository, taskRepository);
    }

    private void addTimeToTask(Task task, Long time) {
        Long newTime = task.getTimeSpent() != null ? task.getTimeSpent() + time : time;
        task.setTimeSpent(newTime);
    }
}
