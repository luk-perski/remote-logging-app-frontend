import { Dispatch } from 'redux';
import * as tasksApi from '../../api/tasks';
import { RootState } from "../reducers";

export const kurwa = (jaJebie: string) => {
    return async (dispatch: Dispatch) => {
        dispatch(setMinutes(jaJebie));
    }
}


export const getTasks = () => {
    return async (dispatch: Dispatch) => {
        dispatch(setLoadingTasks());

        const tasks = await tasksApi.getTasks();

        dispatch(setTasks(tasks));
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

export const assign = (taskId: number, userId: number) => {
    return async (dispatch: Dispatch) => {
        const task = await tasksApi.assign(taskId, userId);

        dispatch(setTaskRequest(task));
    }
}

export const startProgress = (taskId: number, userId: number) => {
    return async (dispatch: Dispatch) => {
        const task = await tasksApi.startProgress(taskId, userId);

        dispatch(setTaskRequest(task));
    }
}

export const suspend = (taskId: number, userId: number) => {
    return async (dispatch: Dispatch) => {
        const task = await tasksApi.suspend(taskId, userId);

        dispatch(setTaskRequest(task));
    }
}

export const handleSetDialogField = (field: string, value: string) => {
    console.log("actions here")
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
    }
}

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

export const setTasks = (tasks: JsonSchema.ModelApiTask[]) => ({
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