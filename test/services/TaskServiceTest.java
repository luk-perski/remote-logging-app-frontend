package services;

import models.db.remote.logging.Task;
import org.junit.Test;
import repository.CategoryRepository;
import repository.ProjectRepository;
import repository.TaskRepository;
import repository.UserRepository;
import service.TaskService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TaskServiceTest {


    @Test
    public void testIsWorkLogged() {
        Long initTime = 300L;
        Long timeToAdd = 200L;

        TaskRepository taskRepository = mock(TaskRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        ProjectRepository projectRepository = mock(ProjectRepository.class);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);

        Task task1 = Task.builder().timeSpent(initTime).build();
        Task task2 = Task.builder().timeSpent(initTime + timeToAdd).build();

        when(taskRepository.getById(any(Long.class))).thenReturn(task1);
        when(taskRepository.update(any(Task.class))).thenReturn(task2);

        TaskService taskService = new TaskService(taskRepository, userRepository, projectRepository, categoryRepository);
        assertEquals(taskService.logWork(1L, timeToAdd).getTimeSpent(), new Long(initTime + timeToAdd));
    }
}
