import { Dispatch } from 'redux';
import * as tasksApi from '../../api/tasks';
import * as logWorkApi from '../../api/log-work';
import * as usersApi from '../../api/users';
import { USER_ID } from '../../utils/lockrKeys';

const Lockr = require("lockr");

export const getTasks = () => {
    return async (dispatch: Dispatch) => {
        dispatch(setLoadingTasks());

        const tasks = await tasksApi.getTasks();

        dispatch(setUsers(tasks));
    }
};

export const getTask = (taskId: number) => {
    return async (dispatch: Dispatch) => {
        dispatch(setLoadingTask())

        const task = await tasksApi.getTask(taskId);

        dispatch(setTaskRequest(task));

        dispatch(setTaskStatus(task.taskStatus));
    }
}

export const assignUser = (taskId: number, userId: number | null) => {
    return async (dispatch: Dispatch) => {
        const task = await tasksApi.assignUser(taskId, userId);

        dispatch(setTaskRequest(task));
    }
}

export const startProgress = (taskId: number, userId: number) => {
    return async (dispatch: Dispatch) => {
        const task = await tasksApi.startProgress(taskId, userId);

        dispatch(setTaskRequest(task));
    }
}

export const suspendTask = (taskId: number, userId: number) => {
    return async (dispatch: Dispatch) => {
        const task = await tasksApi.suspend(taskId, userId);

        dispatch(setTaskRequest(task));
    }
}

export const addLogWork = (taskId: number, userId: number, days: number, hours: number, minutes: number, comment?: string | null) => {
    return async (dispatch: Dispatch) => {
        const timeSpend = (days * 86400000) + (hours * 3600000) + (minutes * 60000);
        const logWork: JsonSchema.ModelApiLogWork = {
            taskId: taskId,
            timeSpend: timeSpend,
            userId: userId,
            comment: comment
        };
        const result = await logWorkApi.addLogWork(logWork);

        dispatch(setTaskRequest(result.task));

        dispatch(setDays("0"))
        dispatch(setHours("0"))
        dispatch(setMinutes("0"))
        dispatch(setLogWorkComment(""))
    }
}

export const addTask = (
    task: JsonSchema.ModelApiTask,
    projects: JsonSchema.ModelApiProject[] | null,
    categories: JsonSchema.ModelApiCategory[] | null,
) => {
    const userId = Lockr.get(USER_ID)

    if (!task.projectId && projects) {
        task.projectId = projects[0]?.id
    }

    if (!task.creatorId && categories) {
        task.category = categories[0]
    }

    task.assigneeId = task.assigneeId !== -1 ? task.assigneeId : null

    return async (dispatch: Dispatch) => {
        task.creatorId = userId
        const result = await tasksApi.addTask(task);
        if (result.status === 201) {
            dispatch(setReturnToTasks(true))
            dispatch(setReturnToTasks(false))
            dispatch(addTaskToList(result.data))
            dispatch(setTaskToAdd({
                estimate: 86400000,
                creatorId: userId,
            }));
        }
    }
}

export const handleSetDialogField = (field: string, value: string) => {
    switch (field) {
        case 'days':
            return async (dispatch: Dispatch) => {
                dispatch(setDays(value));
            }
        case 'hours':
            return async (dispatch: Dispatch) => {
                dispatch(setHours(value));
            }
        case 'minutes':
            return async (dispatch: Dispatch) => {
                dispatch(setMinutes(value));
            }
        case 'userToAssignId':
            return async (dispatch: Dispatch) => {
                dispatch(setUserToAssignId(value));
            }
        case 'logWorkComment':
            return async (dispatch: Dispatch) => {
                dispatch(setLogWorkComment(value));
            }
    }
}

export const handleSetTaskField = (field: string, value: string, task: JsonSchema.ModelApiTask) => {
    switch (field) {
        case "description":
            task.description = value;
            break;
        case "priority":
            task.priority = value;
            break;
        case "name":
            task.name = value;
            break;
        case "assignee":
            task.assigneeId = parseInt(value);
            break;
        case "project":
            task.projectId = parseInt(value);
            break;
        case "category":
            const category = { id: parseInt(value) }
            task.category = category;
    }

    return async (dispatch: Dispatch) => {
        dispatch(setTaskToAdd(task));
    }
}

export const getUsers = () => {
    return async (dispatch: Dispatch) => {
        const users = await usersApi.getAll();

        dispatch(setUsersList(users));
    }
}

export const addTaskToList = (task: JsonSchema.ModelApiTask) => ({
    type: 'ADD_TASK_TO_LIST',
    task
})



export const setDays = (value: string) => ({
    type: 'SET_LOG_DAYS',
    value,
});

export const setHours = (value: string) => ({
    type: 'SET_LOG_HOURS',
    value,
});

export const setMinutes = (value: string) => ({
    type: 'SET_LOG_MINUTES',
    value,
});

export const setUserToAssignId = (value: string) => ({
    type: 'SET_USER_TO_ASSIGN_ID',
    value,
});

export const setOpenLogDialog = (value: boolean) => ({
    type: 'OPEN_LOG_DIALOG',
    value
})

export const setOpenAssignDialog = (value: boolean) => ({
    type: 'OPEN_ASSIGN_DIALOG',
    value
})

export const setUsers = (tasks: JsonSchema.ModelApiTask[]) => ({
    type: 'SET_TASKS',
    tasks,
});

export const setLoadingTasks = () => ({
    type: 'LOADING_TASKS'
})

export const setTaskRequest = (task: JsonSchema.ModelApiTask) => ({
    type: 'SET_TASK_REQUEST',
    task
})

export const setLoadingTask = () => ({
    type: 'LOADING_TASK_REQUEST'
})

export const setTaskStatus = (status: string) => ({
    type: 'SET_TASK_STATUS',
    status
})

export const setLogWorkComment = (value: string) => ({
    type: 'SET_LOG_COMMENT',
    value
})

export const setTaskToAdd = (task: JsonSchema.ModelApiTask) => ({
    type: 'SET_TASK_TO_ADD',
    task
})

export const setReturnToTasks = (value: boolean) => ({
    type: 'RETURN_TO_TASKS',
    value
})

export const setUsersList = (users: JsonSchema.ModelsApiUser[]) => ({
    type: 'SET_USERS',
    users
})