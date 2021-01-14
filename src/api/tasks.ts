import { apiInstance } from '.';

export const getTasks = async () => {
    const response = await apiInstance.get('/v1/remote-logging/task');

    return response.data;
}

export const getTask = async (taskId: number) => {
    const response = await apiInstance.get(`/v1/remote-logging/task/${taskId}`);

    return response.data;
}

export const assign = async (taskId: number, userId: number) => {
    const response = await apiInstance.patch(`/v1/remote-logging/task/assign?taskId=${taskId}&userId=${userId}`)

    return response.data;
}

export const startProgress = async (taskId: number, userId: number) => {
    const response = await apiInstance.patch(`/v1/remote-logging/task/startProgress?taskId=${taskId}&userId=${userId}`)

    return response.data;
}

export const suspend = async (taskId: number, userId: number) => {
    const response = await apiInstance.patch(`/v1/remote-logging/task/suspend?taskId=${taskId}&userId=${userId}`)

    return response.data;
}