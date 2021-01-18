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
import { Projects } from './pages/Projects';
import { useEffect } from 'react';
import { USER_ID } from './utils/lockrKeys';
import { pages } from './utils/pages';

function App() {
    const Lockr = require("lockr");
    const theme = useTheme();
    const state = useSelector((state: RootState) => state);
    const matches = useMediaQuery(theme.breakpoints.up('md'));
    const login = state.login
    const userId = Lockr.get(USER_ID)

    console.log(login)

    useEffect(() => {
        if (userId == null) {
            history.push('/login');
            console.log('User == null')
        }
    });

    return (
        <div className="App iul-font bg-iscte-gray-alt">
            <div className="flex h-screen flex-col">
                <Router history={history}>
                    <div className="flex h-screen flex-col">
                        <TopBar />
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
                                            <Route exact path= {pages.tasks.url()}>
                                                <pages.tasks.Component />
                                            </Route>
                                            <Route exact path= {pages.taskDetails.url()}>
                                                <pages.taskDetails.Component />
                                            </Route>
                                            <Route exact path= {pages.addTask.url()}>
                                                <pages.addTask.Component />
                                            </Route>
                                            <Route exact path= {pages.projects.url()}>
                                                < Projects/>
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
