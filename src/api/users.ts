import { apiInstance } from '.';

export const getSessionUser = async (userId: number) => {
    const response = await apiInstance.get(`/v1/remote-logging/user/${userId}`);

    return response.data.user;
}

export const changeCurrentUserActiveRole = async (roleId: number) => {
    return null;
}

export const signIn = async (userName: string, localPwd: string) => {
    const data = {
        userName: userName,
        localPwd: localPwd
    }
    const response = await apiInstance.post('/v1/remote-logging/user/signIn', data);

    return [response.status, response.data];
}

export const getAll = async () => {
    const response = await apiInstance.get('/v1/remote-logging/user');
    
    return response.data;
}