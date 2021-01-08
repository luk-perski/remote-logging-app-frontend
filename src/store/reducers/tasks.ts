export interface TasksRootState {
    loadingTasks: boolean,
    tasksList: JsonSchema.ModelApiTask[] | null,
    selectedTaskRequest: JsonSchema.ModelApiTask | null,
    loadingTaskRequest: boolean,
}

export const tasks = (state: TasksRootState = {
    loadingTasks: false,
    tasksList: null,
    selectedTaskRequest: null,
    loadingTaskRequest: false
}, action: Record<string, any>) => {
    switch (action.type) {
        case 'LOADING_TASKS':
            return { ...state, loadingTasks: true };
        case 'LOADING_TASK_REQUEST':
            return { ...state, loadingTaskRequest: true };
        case 'SET_TASKS':
            return { state, tasksList: action.tasks, loadingTasks: false };
        case 'SET_TASK_REQUEST':
            return {state, selectedTaskRequest: action.task, loadingTaskRequest: false};
        default:
            return state;
    }
}
