import React from 'react';
import {
    Router,
    Switch,
    Route,
} from 'react-router-dom';
import { useSelector } from 'react-redux';
import { Login } from './pages/Login';
import { history } from './utils/history';
import { Logout } from './pages/Logout';
import { RootState } from './store/reducers';
import { Toolbar, useMediaQuery, useTheme } from '@material-ui/core';
import { AppTabs } from './components/AppTabs';
import { useEffect } from 'react';
import { USER_ID } from './utils/lockrKeys';
import { pages } from './utils/pages';

function App() {
    const Lockr = require("lockr");
    const theme = useTheme();
    const state = useSelector((state: RootState) => state);
    const matches = useMediaQuery(theme.breakpoints.up('md'));
    const login = state.login
    const userId = Lockr.get(USER_ID) //todo change it after backend will be ready for using token

    console.log(login)

    useEffect(() => {
        if (userId == null) {
            history.push('/login');
        }
    });

    return (
        <div className="App iul-font bg-iscte-gray-alt">
            <div className="flex h-screen flex-col">
                <Router history={history}>
                    <div className="flex h-screen flex-col">
                        <Toolbar>Remote Loging App</Toolbar>
                        <div className={`relative flex flex-1 ${matches ? '' : 'mx-4'}`}>
                            {userId == null ? (
                                <Login />
                            ) : (
                                    <>
                                        {matches && (
                                            <AppTabs />
                                        )}
                                        <Switch>
                                            <Route exact path="/">
                                            </Route>
                                            <Route exact path={pages.login.url()}>
                                                <Logout />
                                            </Route>
                                            <Route exact path={['/', '/calendar']}>
                                            </Route>
                                            <Route exact path={pages.tasks.url()}>
                                                <pages.tasks.Component />
                                            </Route>
                                            <Route exact path={pages.addTask.url()}>
                                                <pages.addTask.Component />
                                            </Route>
                                            <Route exact path={pages.taskDetails.url()}>
                                                <pages.taskDetails.Component />
                                            </Route>
                                            <Route exact path={pages.projects.url()}>
                                                < pages.projects.Component />
                                            </Route>
                                        </Switch>
                                    </>
                                )}
                        </div>
                    </div>
                </Router>
            </div>
        </div>
    );
}

export default App;
