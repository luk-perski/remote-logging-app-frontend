import { Dispatch } from 'redux';
import { LoginRootState } from '../reducers/login';


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