export interface TasksRootState {
    loadingTasks: boolean,
    tasksList: JsonSchema.ModelApiTask[] | null,
}

export const tasks = (state: TasksRootState = {
    loadingTasks: false,
    tasksList: null,
}, action: Record<string, any>) => { 
    switch(action.type){
        case 'LOADING_TASKS':
            return {...state, loadingTasks: true};
        case 'SET_TASKS':
            return{state, tasksList: action.tasks, loadingTasks: false};
        default:
            return state;
    }
}
