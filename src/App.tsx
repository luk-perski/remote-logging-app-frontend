import React from 'react';
import {
    Router,
    Switch,
    Route,
} from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import cookie from 'js-cookie';
import { TopBar } from './components/TopBar';
import { Login } from './pages/Login';
import { history } from './utils/history';
import { Logout } from './pages/Logout';
import { RootState } from './store/reducers';
import { useMediaQuery, useTheme } from '@material-ui/core';
import { AppTabs } from './components/AppTabs';
import { Tasks } from './pages/Tasks';
import { useEffect } from 'react';
import { getSessionUser } from './store/actions/app';

function App() {
    const dispatch = useDispatch();
    const theme = useTheme();
    const state = useSelector((state: RootState) => state);
    const token = cookie.get('token');
    const matches = useMediaQuery(theme.breakpoints.up('md'));
    const {
        loadingSessionUser,
        loadedSessionUser,
        sessionUser
    }: {
        loadingSessionUser: boolean,
        loadedSessionUser: boolean,
        sessionUser: any
    } = state.app;

    console.log(sessionUser, 'sessionUser');

    useEffect(() => {
        if (!sessionUser && !loadedSessionUser && !loadingSessionUser) {
            history.push('/login');
        }
    }, [loadingSessionUser, loadedSessionUser]);

    return (
        <div className="App iul-font bg-iscte-gray-alt">
            <div className="flex h-screen flex-col">
                <Router history={history}>
                    <div className="flex h-screen flex-col">
                        <TopBar />
                        <div className={`relative flex flex-1 ${matches ? '' : 'mx-4'}`}>
                            {matches && (
                                <AppTabs />
                            )}
                            <Switch>
                                <Route exact path="/">
                                </Route>
                                <Route exact path="/login">
                                    <Login />
                                </Route>
                                <Route exact path="/logout">
                                    <Logout />
                                </Route>
                                <Route exact path={['/', '/calendar']}>
                                </Route>
                                <Route exact path="/tasks">
                                    <Tasks />
                                </Route>
                            </Switch>
                        </div>
                    </div>
                </Router>
            </div>
        </div>
    );
}

export default App;
