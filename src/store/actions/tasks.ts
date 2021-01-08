import { Dispatch } from 'redux';
import * as tasksApi from '../../api/tasks';
import { RootState } from "../reducers";

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


export const getTasks = () => {
    return async (dispatch: Dispatch) => {
        dispatch(setLoadingTasks());

        const tasks = await tasksApi.getTasks();

        dispatch(setTasks(tasks));
    }
};

export const getTask = (state: RootState, taskId: number) => {
    return async (dispatch: Dispatch) => {
        dispatch(setLoadingTask())
        
        const task = await tasksApi.getTask(taskId);

        dispatch(setTaskRequest(task));
    }
}