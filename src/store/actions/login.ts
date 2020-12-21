import { Dispatch } from 'redux';
import { LoginRootState } from '../reducers/login';


export const handleChange = (prop: keyof LoginRootState) => (event: React.ChangeEvent<HTMLInputElement>) => {
    //todo switch with prop
    return async (dispatch: Dispatch) => {
        dispatch(setUserName(event.target.value));
    }
};


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


export const handleClickShowPassword = (showPassword: boolean) => (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    return async (dispatch: Dispatch) => {
        dispatch(setShowPassword(showPassword));
    }
};
