export interface AppRootState {
    loadingSessionUser: boolean,
    loadedSessionUser: boolean,
    sessionUser: any,
}


export const app = (state: AppRootState = {
    loadingSessionUser: false,
    loadedSessionUser: false,
    sessionUser: null,
}, action: Record<string, any>)=> {
    switch(action.type) {
        case 'SET_LOADING_SESSION_USER':
            return { ...state, loadingSessionUser: true };
        case 'SET_SESSION_USER':
            return { ...state, sessionUser: action.user, loadingSessionUser: false, loadedSessionUser: true };
        case 'REMOVE_SESSION_USER':
            return {...state, sessionUser: null };
        case 'CHANGING_SESSION_USER_ROLE':
            return { ...state, changingSessionUserRole: true };
        case 'UPDATE_SESSION_USER':
            return { ...state, changingSessionUserRole: false, sessionUser: action.user };
        default:
            return state;
    }
}
