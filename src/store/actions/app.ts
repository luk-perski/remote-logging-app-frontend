import { Dispatch } from 'redux';
import * as usersApi from '../../api/users';
import { USER_ID } from '../../utils/lockrKeys';

const Lockr = require("lockr");

export const updateSessionUser = (user: any)=> ({
    type: 'UPDATE_SESSION_USER',
    user,
});

export const getSessionUser = (sessionUser: JsonSchema.ModelsApiUser)=> {
    const userId = Lockr.get(USER_ID)
    return async (dispatch: Dispatch)=> {

        dispatch({
            type: 'SET_LOADING_SESSION_USER',
        });

        const user = await usersApi.getSessionUser(userId);

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