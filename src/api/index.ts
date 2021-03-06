import axios from 'axios';
import { history } from '../utils/history';
import { API_URL } from '../config';

export const apiInstance = axios.create({
    baseURL: API_URL,
});

apiInstance.interceptors.response.use((response)=> response, (error)=> {
    if(error.response?.status === 401) {
        history.push('/login');
    } else if(error.response?.status === 403) {
        history.push('/user/change-role?forbiddenError');
    }

    return Promise.reject(error);
});
