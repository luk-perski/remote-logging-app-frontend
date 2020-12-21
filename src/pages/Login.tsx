
import React from 'react';
import queryString from 'query-string';
import { Redirect, useHistory, useLocation } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircle, faLock } from "@fortawesome/free-solid-svg-icons";
import { languages } from '../language-strings';
import { API_URL } from '../config';
import { RootState } from '../store/reducers';
import { Input, InputAdornment } from '@material-ui/core';
import { TextField } from '@material-ui/core';
import { Grid } from '@material-ui/core';
import IconUserName from '@material-ui/icons/AccountCircle';
import { FormControl } from '@material-ui/core';
import { InputLabel } from '@material-ui/core';
import IconPassword from '@material-ui/icons/Lock';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import { Button } from '@material-ui/core';
import { IconButton } from '@material-ui/core';
import { LoginRootState } from '../store/reducers/login';
import { handleChange, handleClickShowPassword } from '../store/actions/login';

export const Login = () => {
    const history = useHistory();
    const location = useLocation();
    const dispatch = useDispatch();
    const state = useSelector((state: RootState) => state);
    const app = state.app;
    const params = queryString.parse(location.search);
    const username = state.login.username
    const password = state.login.password
    const showPassword = state.login.showPassword

    // const env = app.environment;
    const lang = app.language;


    // const handleClickShowPassword = () => {
    //     setValues({ ...values, showPassword: !values.showPassword });
    // };

    const handleMouseDownPassword = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
    };

    return (
        <div className="mx-auto">
            <div>
                <FormControl className="m-3">
                    <InputLabel>User name</InputLabel>
                    <Input
                        id="input-username"
                        value={username}
                        onChange={handleChange('username')}
                        startAdornment={
                            <InputAdornment position="start">
                                <IconUserName />
                            </InputAdornment>
                        }
                    />
                </FormControl>
                <FormControl className="m-3">
                    <InputLabel>Password</InputLabel>
                    <Input
                        id="input-password"
                        type={showPassword ? 'text' : 'password'}
                        value={password}
                        onChange={handleChange("password")}
                        startAdornment={
                            <InputAdornment position="start">
                                < IconPassword />
                            </InputAdornment>
                        }
                        endAdornment={
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="toggle password visibility"
                                    onClick={handleClickShowPassword(!showPassword)}
                                    onMouseDown={handleMouseDownPassword}
                                >
                                    {password ? < Visibility/> : <VisibilityOff />}
                                </IconButton>
                            </InputAdornment>
                        }
                    />
                </FormControl></div>

            <div>
                <Button variant="contained" color="primary">
                    Login
                </Button>
            </div>
        </div>
    )
}
