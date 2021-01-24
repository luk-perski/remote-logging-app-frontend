import { apiInstance } from '.';

export const getProjects = async ()=>{
    const response = await apiInstance.get('/v1/remote-logging/projects');

    return response.data;
}