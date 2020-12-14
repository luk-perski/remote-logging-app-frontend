import React from 'react';
import cookie from 'js-cookie';
import { useDispatch } from 'react-redux';
import { Redirect } from 'react-router-dom';
import { removeSessionUser } from '../store/actions/app';

export const Logout = ()=> {
    const dispatch = useDispatch();
    cookie.remove('token');

    dispatch(removeSessionUser());

    return (
        <Redirect to="/login" />
    )
}
