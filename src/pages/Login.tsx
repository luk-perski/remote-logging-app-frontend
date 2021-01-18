import React from 'react';
import queryString from 'query-string';
import { useLocation } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../store/reducers';
import { Input, InputAdornment } from '@material-ui/core';
import IconUserName from '@material-ui/icons/AccountCircle';
import { FormControl } from '@material-ui/core';
import { InputLabel } from '@material-ui/core';
import IconPassword from '@material-ui/icons/Lock';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import { Button } from '@material-ui/core';
import { IconButton } from '@material-ui/core';
import { handleSetField, handleSetShowPassword, signIn } from '../store/actions/login';

export const Login = () => {
    const location = useLocation();
    const dispatch = useDispatch();
    const state = useSelector((state: RootState) => state);

    const login = state.login
    const params = queryString.parse(location.search);
    const username = login.username
    const password = login.password
    const showPassword = login.showPassword


    const handleChange = (field: string) => (event: React.ChangeEvent<HTMLInputElement>) => {
        dispatch(handleSetField(field, event.target.value));
    };

    const handleMouseDownPassword = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
    };


    const handleClickShowPassword = (showPassword: boolean) => (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        dispatch(handleSetShowPassword(showPassword));
    };

    const handleLoginBtn = () => {
        dispatch(signIn(login.username, login.password))
    }

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
                                    {password ? < Visibility /> : <VisibilityOff />}
                                </IconButton>
                            </InputAdornment>
                        }
                    />
                </FormControl></div>

            <div className="flex items-center">
                <Button
                    variant="contained"
                    color="primary"
                    onClickCapture={handleLoginBtn}>
                    Login
                </Button>
            </div>
        </div>
    )
}
