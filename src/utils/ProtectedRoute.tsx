import React from 'react';
import cookie from 'js-cookie';
import { useSelector } from 'react-redux';
import { Redirect, Route, RouteProps } from 'react-router-dom';
import { RootState } from '../store/reducers';

export const ProtectedRoute = (props: RouteProps)=> {
    const state = useSelector((state: RootState)=> state);
    const token = cookie.get('token');
    const { loadingSessionUser, loadedSessionUser } = state.app;

    if(!token) {
        return <Redirect push to="/login" />
    }

    if(loadingSessionUser || !loadedSessionUser) {
        return <></>;
    }

    const { children, ...otherProps } = props;

    return (
        <Route {...otherProps} >
            {children}
        </Route>
    )
}
