export interface TasksRootState {
    loadingTasks: boolean;
    tasksList: JsonSchema.ModelApiTask[] | null;
    selectedTaskRequest: JsonSchema.ModelApiTask;
    taskToAdd: JsonSchema.ModelApiTask;
    loadingTaskRequest: boolean;
    logWorkToAdd?: JsonSchema.ModelApiLogWork | null;
    logWorkDays: number;
    logWorkHours: number;
    logWorkMinutes: number;
    loadingAddLogWork: boolean;
    openLogDialog: boolean;
    openAssignDialog: boolean;
    userToAssignId: number | null;
    logWorkComment: string | null;
    isReturnToTasks: boolean;
    users: JsonSchema.ModelsApiUser[] | null
}

export const tasks = (state: TasksRootState = {
    loadingTasks: false,
    tasksList: null,
    selectedTaskRequest: {},
    taskToAdd: {
        estimate: 86400000,
    },
    loadingTaskRequest: false,
    logWorkToAdd: null,
    loadingAddLogWork: false,
    logWorkDays: 0,
    logWorkHours: 0,
    logWorkMinutes: 0,
    openLogDialog: false,
    openAssignDialog: false,
    userToAssignId: null,
    logWorkComment: null,
    isReturnToTasks: false,
    users: null
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
            return { ...state, logWorkComment: action.value }
        case 'SET_TASK_TO_ADD':
            return { ...state, taskToAdd: action.task }
        case 'RETURN_TO_TASKS':
            return { ...state, isReturnToTasks: action.value }
        case 'SET_USERS':
            return { ...state, users: action.users }
        case 'ADD_TASK_TO_LIST':
            return { ...state, tasksList: state.tasksList?.concat(action.task) }
        default:
            return state;
    }
}
