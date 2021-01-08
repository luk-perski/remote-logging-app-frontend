import { apiInstance } from '.';

export const getTasks = async ()=>{
    const response = await apiInstance.get('/v1/remote-logging/task');

    return response.data;
}

export const getTask= async (taskId: number)=>{
    const response = await apiInstance.get(`/v1/remote-logging/task/${taskId}`);

    return response.data;
}