import { Dispatch } from 'redux';
import * as usersApi from '../../api/users';
import { languages } from '../../language-strings';

export const userRoleFieldByLanguage = {
    [languages.ENGLISH]: 'name_en',
    [languages.PORTUGUESE]: 'name_pt',
}

export const setLanguage = (language = languages.ENGLISH)=> ({
    type: 'CHANGE_LANGUAGE',
    language,
});

export const updateSessionUser = (user: any)=> ({
    type: 'UPDATE_SESSION_USER',
    user,
});

export const getSessionUser = (token: string)=> {
    return async (dispatch: Dispatch)=> {
        if(!token) {
            return;
        }

        dispatch({
            type: 'SET_LOADING_SESSION_USER',
        });

        const user = await usersApi.getSessionUser();

        dispatch({
            type: 'SET_SESSION_USER',
            user,
        });
    }
};

export const removeSessionUser = ()=> ({
    type: 'REMOVE_SESSION_USER',
});

export const changeSessionUserActiveRole = (roleId: number)=> {
    return async (dispatch: Dispatch)=> {
        dispatch({
            type: 'CHANGING_SESSION_USER_ROLE',
        });

        const user = await usersApi.changeCurrentUserActiveRole(roleId);

        dispatch(updateSessionUser(user));
    }
};

export const setToken = (token: string)=> {
    return async (dispatch: Dispatch)=> {
        dispatch({
            type: 'SET_TOKEN',
            token: token,
        });

        const user = await usersApi.getSessionUser();

        dispatch(updateSessionUser(user));
    }
};

export const environments = {
    PRODUCTION: 'production',
    STAGING: 'test',
    DEVELOPMENT: 'development'
}
