import { Dispatch } from 'redux';
import { LoginRootState } from '../reducers/login';
import * as userApi from "../../api/users";
import { history } from '../../utils/history';
import { ApiOkResponse } from '@nestjs/swagger';
import { TypedUseSelectorHook } from 'react-redux';
import { USER_ID } from '../../utils/lockrKeys';

const Lockr = require("lockr");


export const handleSetField = (field: string, value: string) => {
    switch (field) {
        case 'username':
            return async (dispatch: Dispatch) => {
                dispatch(setUserName(value));
            }
        case 'password':
            return async (dispatch: Dispatch) => {
                dispatch(setPassword(value));
            }
    }
};

export const handleSetShowPassword = (showPassowrd: boolean) => (
    async (dispatch: Dispatch) => {
        dispatch(setShowPassword(showPassowrd))
    }
);



export const signIn = (userName: string, localPwd: string) => {
    return async (dispatch: Dispatch) => {
        const [status, user] = await userApi.signIn(userName, localPwd);
        console.log(status);
        if (status == 200 && user != null) {
            history.push('/index');
            Lockr.set(USER_ID, user.id);
            dispatch(setUser(user))
        }
        //todo dispatch and save userData
    }
}


export const setUserName = (value: string) => ({
    type: 'SET_USER_NAME',
    value,
});

export const setPassword = (value: string) => ({
    type: 'SET_PASSWORD',
    value,
});

export const setShowPassword = (showPassword: boolean) => ({
    type: 'SHOW_PASSWORD',
    showPassword
})

export const setUser = (user: JsonSchema.ModelsApiUser) => ({
    type: 'SET_USER',
    user
})