export interface TasksRootState {
    loadingTasks: boolean;
    tasksList: JsonSchema.ModelApiTask[] | null;
    selectedTaskRequest: JsonSchema.ModelApiTask | null;
    loadingTaskRequest: boolean;
    logWorkToAdd?: JsonSchema.ModelApiLogWork | null;
    logWorkDays: number;
    logWorkHours: number;
    logWorkMinutes: number;
    loadingAddLogWork: boolean;
    openLogDialog: boolean;
    openAssignDialog: boolean;
    userToAssignId: number;
    logWorkComment: string;
}

export const tasks = (state: TasksRootState = {
    loadingTasks: false,
    tasksList: null,
    selectedTaskRequest: null,
    loadingTaskRequest: false,
    logWorkToAdd: null,
    loadingAddLogWork: false,
    logWorkDays: 0,
    logWorkHours: 0,
    logWorkMinutes: 0,
    openLogDialog: false,
    openAssignDialog: false,
    userToAssignId: -1,
    logWorkComment: "",
}, action: Record<string, any>) => {
    switch (action.type) {
        case 'LOADING_TASKS':
            return { ...state, loadingTasks: true };
        case 'LOADING_TASK_REQUEST':
            return { ...state, loadingTaskRequest: true };
        case 'SET_TASKS':
            return { ...state, tasksList: action.tasks, loadingTasks: false };
        case 'SET_TASK_REQUEST':
            return { ...state, selectedTaskRequest: action.task, loadingTaskRequest: false };
        case 'LOADING_ADD_LOG':
            return { ...state, loadingAddLogWork: action.value };
        case 'SET_LOG_DAYS':
            return { ...state, logWorkDays: action.value }
        case 'SET_LOG_HOURS':
            return { ...state, logWorkHours: action.value }
        case 'SET_LOG_MINUTES':
            return { ...state, logWorkMinutes: action.value }
        case 'SET_USER_TO_ASSIGN_ID':
            return { ...state, userToAssignId: action.value }
        case 'OPEN_LOG_DIALOG':
            return { ...state, openLogDialog: action.value }
        case 'OPEN_ASSIGN_DIALOG':
            return { ...state, openAssignDialog: action.value }
        case 'SET_TASK_STATUS':
            return { ...state, taskStatus: action.status }
        case 'SET_LOG_COMMENT':
            return {...state, logWorkComment: action.value}
        default:
            return state;
    }
}
