import { apiInstance } from '.';

export const getTasks = async () => {
    const response = await apiInstance.get('/v1/remote-logging/tasks');

    return response.data;
}

export const getTask = async (taskId: number) => {
    const response = await apiInstance.get(`/v1/remote-logging/tasks/${taskId}`);

    return response.data;
}

export const assignUser = async (taskId: number, userId: number | null) => {
    const response = await apiInstance.patch(`/v1/remote-logging/tasks/assign-user?taskId=${taskId}&userId=${userId}`)

    return response.data;
}

export const startProgress = async (taskId: number, userId: number) => {
    const response = await apiInstance.patch(`/v1/remote-logging/tasks/start-progress?taskId=${taskId}&userId=${userId}`)

    return response.data;
}

export const suspend = async (taskId: number, userId: number) => {
    const response = await apiInstance.patch(`/v1/remote-logging/tasks/suspend?taskId=${taskId}&userId=${userId}`)

    return response.data;
}

export const addTask = async (task: JsonSchema.ModelApiTask)=>{
    const response = await apiInstance.post('/v1/remote-logging/tasks', task)
    
    return response;
}