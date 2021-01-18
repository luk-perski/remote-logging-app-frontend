import { apiInstance } from '.';

export const getAll = async () => {
    const response = await apiInstance.get('/v1/remote-logging/category');

    return response.data;
}