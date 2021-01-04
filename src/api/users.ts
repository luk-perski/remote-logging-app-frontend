import { apiInstance } from '.';

export const getSessionUser = async ()=> {
    const response = await apiInstance.get('/v1/remote-logging/user');
    console.log(response.data.user);
    return response.data.user;
}

export const changeCurrentUserActiveRole = async (roleId: number)=> {
    return null;
}

export const signIn = async (userName: string, localPwd: string) => {
    const data = {
        userName: userName,
        localPwd: localPwd}
      console.log(data)
    const response = await apiInstance.post('/v1/remote-logging/user/signIn', data);
    console.log(response.data)
    return [response.status, response.data];
}