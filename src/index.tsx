import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { createMuiTheme, StylesProvider, MuiThemeProvider } from '@material-ui/core';
import { store } from './store';
import App from './App';
import * as serviceWorker from './serviceWorker';
import './styles/main.css';

const theme = createMuiTheme({
    typography: {
        fontFamily: [
            'iul_font',
            'arial',
            'helvetica',
            'sans-serif'
        ].join(','),
    },
    palette: {
        primary: {
            main: '#0D28C2' 
        },
        error: {
            main: '#b41916',
            light: '#f1b5b4'
        }
    }
});

ReactDOM.render(
    <React.StrictMode>
        <MuiThemeProvider theme={theme}>
            <StylesProvider injectFirst>
                    <Provider store={store}>
                        <App />
                    </Provider>
            </StylesProvider>
        </MuiThemeProvider>
    </React.StrictMode>,
    document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.register({});
