package utils.api.v1;

import enums.Priority;
import enums.TaskStatus;
import enums.TaskType;
import models.api.v1.*;
import models.db.remote.logging.*;
import models.db.user.User;
import repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//todo remove and add https://mapstruct.org/ library
public class ModelsUtils {
    public static List<ApiCategory> getApiCategories(List<Category> categories) {
        List<ApiCategory> result = new ArrayList<>();
        categories.forEach(category -> result.add(getApiCategory(category)));
        return result;
    }

    public static ApiCategory getApiCategory(Category category) {
        return ApiCategory.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Project getProjectFromApi(ApiProject apiProject, UserRepository userRepository) {
        User manager = userRepository.getById(apiProject.getManagerId());
        return Project.builder()
                .name(apiProject.getName())
                .description(apiProject.getDescription())
                .startDate(apiProject.getStartDate())
                .isActive(apiProject.getIsActive())
                .manager(manager)
                .build();
    }

    public static ApiProject getApiProjectFromProject(Project project) {
        return ApiProject.builder()
                .id(project.getId())
                .cratedDate(project.getCratedDate())
                .endDate(project.getEndDate())
                .name(project.getName())
                .description(project.getName())
                .isActive(project.getIsActive())
                .managerId(project.getManager().getID())
                .managerName(project.getManager().getDisplayName())
                .build();
    }

    public static List<ApiProject> getApiProjectsFromProjects(List<Project> projects) {
        List<ApiProject> result = new ArrayList<>();
        projects.forEach(project -> {
            result.add(getApiProjectFromProject(project));
        });
        return result;
    }

    public static Task getTaskFromApi(ApiTask apiTask, UserRepository userRepository, ProjectRepository projectRepository, CategoryRepository categoryRepository) {
        User creator = userRepository.getById(apiTask.getCreatorId());
        User assignee = userRepository.getById(apiTask.getAssigneeId());
        Project project = projectRepository.getById(apiTask.getProjectId());
        Category category = apiTask.getCategory() != null ? categoryRepository.getById(apiTask.getCategory().getId()) : null;
        return Task.builder()
                .id(apiTask.getId())
                .name(apiTask.getName())
                .description(apiTask.getDescription())
                .assignee(assignee)
                .category(category)
                .estimate(apiTask.getEstimate())
                .priority(apiTask.getPriority() != null ? apiTask.getPriority().ordinal() : -1)
                .runStart(apiTask.getRunStart())
                .project(project)
                .creator(creator)
                .cratedDate(new Date())
                .taskType(apiTask.getTaskType() != null ? apiTask.getTaskType().ordinal() : -1)
                .taskStatus(apiTask.getTaskStatus() != null ? apiTask.getTaskStatus().ordinal() : -1)
                .build();
    }

    public static ApiTask getApiTaskFromModel(Task task) {
        return ApiTask.builder()
                .id(task.getId())
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getID() : null)
                .assigneeName(task.getAssignee() != null ? task.getAssignee().getDisplayName() : null)
                .cratedDate(task.getCratedDate())
                .name(task.getName())
                .priority(Priority.orderOf(task.getPriority()).orElse(null))
                .description(task.getDescription())
                .creatorId(task.getCreator().getID())
                .creatorName(task.getCreator().getDisplayName())
                .taskType(TaskType.orderOf(task.getTaskType()).orElse(null))
                .taskStatus(TaskStatus.orderOf(task.getTaskStatus()).orElse(null))
                .category(ApiCategory.builder().id(task.getCategory().getId()).name(task.getCategory().getName()).build())
                .estimate(task.getEstimate())
                .projectName(task.getProject().getName())
                .projectId(task.getProject().getId())
                .resolvedDate(task.getResolvedDate())
                .cratedDate(task.getCratedDate())
                .runStart(task.getRunStart())
                .runEnd(task.getRunEnd())
                .timeSpent(task.getTimeSpent())
                .build();
    }

    public static List<ApiTask> getApiTasks(List<Task> tasks) {
        List<ApiTask> result = new ArrayList<>();
        tasks.forEach(task -> result.add(getApiTaskFromModel(task)));
        return result;
    }

    public static Team getTeamFromApiModel(ApiTeam apiTeam, UserRepository userRepository) {
        User manager = userRepository.getById(apiTeam.getManager().getId());
        return Team.builder()
                .manager(manager)
                .name(apiTeam.getName())
                .createdDate(new Date())
                .build();
    }

    public static ApiTeam getApiTeamFromModel(Team team) {
        return ApiTeam.builder()
                .id(team.getId())
                .name(team.getName())
                .manager(getApiUserFromModel(team.getManager()))
                .build();
    }

    public static List<ApiTeam> getApiTeamListFromModels(List<Team> teams) {
        List<ApiTeam> result = new ArrayList<>();
        teams.forEach(team -> result.add(getApiTeamFromModel(team)));
        return result;
    }

    public static ApiUser getApiUserFromModel(User user) {
        return ApiUser.builder()
                .id(user.getID())
                .email(user.getEmail())
                .name(user.getName())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .roles(user.getRoles())
                .teamId(user.getTeam() != null ? user.getTeam().getId() : null)
                .build();
    }

    public static User getUserModelFromApiUser(ApiUser apiUser, TeamRepository teamRepository) {
        Team team = teamRepository.getById(apiUser.getTeamId());
        return User.builder()
                .username(apiUser.getUsername())
                .display_name(apiUser.getDisplayName())
                .email(apiUser.getEmail())
                .name(apiUser.getName())
                .id(apiUser.getId())
                .local_pwd(apiUser.getLocalPwd())
                .roles(apiUser.getRoles())
                .team(team)
                .build();
    }

    public static List<ApiUser> getApiUserListFromModels(List<User> users) {
        List<ApiUser> result = new ArrayList<>();
        users.forEach(user -> result.add(getApiUserFromModel(user)));
        return result;
    }

    public static ApiLogWork getApiWorkLogFromModel(LogWork logWork) {
        return ApiLogWork.builder()
                .id(logWork.getId())
                .comment(logWork.getComment())
                .taskId(logWork.getTask().getId())
                .taskName(logWork.getTask().getName())
                .timeSpend(logWork.getTimeSpend())
                .userDisplayName(logWork.getUser().getDisplayName())
                .userId(logWork.getUser().getID())
                .build();
    }

    public static LogWork getLogWorkModelFromApiLogWork(ApiLogWork apiLogWork, UserRepository userRepository, TaskRepository taskRepository) {
        User user = userRepository.getById(apiLogWork.getUserId());
        Task task = taskRepository.getById(apiLogWork.getTaskId());
        return LogWork.builder()
                .id(apiLogWork.getId())
                .timeSpend(apiLogWork.getTimeSpend())
                .comment(apiLogWork.getComment())
                .task(task)
                .user(user)
                .build();
    }

    public static List<ApiLogWork> getApiWorkLogsListFromModels(List<LogWork> logWorks, UserRepository userRepository, TaskRepository taskRepository) {
        List<ApiLogWork> result = new ArrayList<>();
        logWorks.forEach(logWork -> result.add(getApiWorkLogFromModel(logWork)));
        return result;
    }
}
