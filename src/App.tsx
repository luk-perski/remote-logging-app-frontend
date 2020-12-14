import React, { useEffect } from 'react';
import {
    Router,
    Switch,
    Route,
} from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import cookie from 'js-cookie';
import { TopBar } from './components/TopBar';
import { Home } from './pages/Home';
import { Login } from './pages/Login';
import { history } from './utils/history';
import { Logout } from './pages/Logout';
import { UserMenu } from './components/UserMenu';
import { getSessionUser } from './store/actions/app';
import { RootState } from './store/reducers';
import { useMediaQuery, useTheme } from '@material-ui/core';

function App() {
    const dispatch = useDispatch();
    const theme = useTheme();
    const state = useSelector((state: RootState)=> state);
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

    useEffect(()=> {
        if(token && !loadedSessionUser && !loadingSessionUser) {
            dispatch(getSessionUser(token));
        }
    }, [token, dispatch, loadingSessionUser, loadedSessionUser]);

    return (
        <div className="App iul-font bg-iscte-gray-alt">
            <Router history={history}>
                <div className="flex h-screen flex-col">
                    <TopBar />
                    <div className={`relative flex flex-1 ${matches? '' : 'mx-4'}`}>
                        {sessionUser && matches && (
                            <div>
                                <UserMenu />
                            </div>
                        )}
                        <Switch>
                            <Route exact path="/">
                                <Home />
                            </Route>
                            <Route exact path="/login">
                                <Login />
                            </Route>
                            <Route exact path="/logout">
                                <Logout />
                            </Route>
                        </Switch>
            <p>Dupa Kasia</p>
                    </div>
                </div>
            </Router>
        </div>
    );
}

export default App;
