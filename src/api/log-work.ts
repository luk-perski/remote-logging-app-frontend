import { apiInstance } from '.';

export const getLogWorks = async ()=>{
    const response = await apiInstance.get('/v1/remote-logging/log-works');

    return response.data;
}

export const addLogWork = async (logWork: JsonSchema.ModelApiLogWork)=>{
    const response = await apiInstance.post('/v1/remote-logging/log-works', logWork)
    
    return response.data;
}

