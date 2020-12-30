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
    console.log("login reducer here")
    switch (action.type) {
        case 'SET_USER_NAME':
            return {...state, username: action.value};
            case 'SET_PASSWORD':
                return {...state, password: action.value};
        case 'SHOW_PASSWORD':
        return {...state, showPassword: action.showPassword}
        default:
            return state;
    }
}