import { apiInstance } from '.';

export const getSessionUser = async ()=> {
    const response = await apiInstance.get('/v1/remote-logging/user');
    console.log(response.data.user);
    return response.data.user;
}

export const changeCurrentUserActiveRole = async (roleId: number)=> {
    return null;
}