export interface LoginRootState {
    username: string;
    password: string;
    showPassword: boolean;
}

export const login = (state: LoginRootState = {
    username: '',
    password: '',
    showPassword: false
}, action: Record<string, any>) => {
    switch (action.type) {
        case 'SET_USER_NAME':
            return {...state, username: "Hello"};
        case 'SHOW_PASSWORD':
        return {...state, showPassword: action.showPassword}
        default:
            return state;
    }
}